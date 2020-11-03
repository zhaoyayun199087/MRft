package mingrifuture.gizlib.code.remote;

import android.content.Context;
import android.os.Handler;


import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.BizCmdConverter;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.LogUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 本地局域网的Tcp连接，用于控制本地网络的app连接
 * @author pengl
 *
 */
public class TCPServerSocket extends Thread implements TCPMessageListener {

	private final Handler handler;
	private Context context;
	private ServerSocket serverSocket;
	private boolean run = false;
	
	private String passcode = "gokit1";
	/**
	 * 同时支持管理30个客户端
	 */
	private HashMap<String, TCPMessageSocket> lists = new HashMap<String, TCPMessageSocket>();

	public TCPServerSocket(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;
	}

	@Override
	public void run() {
		run = true;
		try {
			serverSocket = new ServerSocket(Constants.TCP_PORT);
		} catch (IOException e) {
			run = false;
			handler.sendEmptyMessageDelayed(RemoteControlService.TCP_EXCEPTION, 3000);
			return;
		}

		while (true) {
			try {
				Socket socket = serverSocket.accept();
				TCPMessageSocket client = new TCPMessageSocket(this);
				client.setSocket(socket);
				client.start();

				String clientIp = socket.getInetAddress().toString();
				lists.put(clientIp, client);
			} catch (IOException e) {
				break;
			}
		}
		if (run) {
			handler.sendEmptyMessageDelayed(RemoteControlService.TCP_EXCEPTION, 3000);
		} else {
			//主动关闭
			handler.sendEmptyMessage(RemoteControlService.TCP_CLOSE);
		}
		close();

	}

	public void writeBytes(String message) {
		writeBytes(message.getBytes());
	}

	public void writeBytes(byte[] data) {
		Iterator iter = lists.entrySet().iterator();
		List<String> keys = new ArrayList<String>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			TCPMessageSocket val = (TCPMessageSocket) entry.getValue();
			if (val != null) {
				if (val.isOnline()) {
					val.writeBytes(data);
					continue;
				}

				val.close();
				val = null;
			}
			keys.add((String) entry.getKey());
		}

		// hash容器不能同时遍历又同时修改
		for (String object : keys) {
			lists.remove(object);
		}
	}

	public void close() {
		run = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {

			} finally {
				serverSocket = null;
			}
		}

		closeClients();
	}

	/**
	 * 关闭所有客户端连接
	 */
	private void closeClients() {
		Iterator iter = lists.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			TCPMessageSocket val = (TCPMessageSocket) entry.getValue();
			// 不在線就移除
			if (val != null)
				val.close();
			val = null;
		}

		// // hash容器不能同时遍历又同时修改
		// for (String object : keys) {
		// lists.remove(object);
		// }

		lists.clear();
	}

	public boolean isRun() {
		return run;
	}
	
	/** 移除连接 **/
	private void removeConnection(String ip) {
		TCPMessageSocket client = lists.get(ip);
		if (client == null) {
			return;
		}
		client.close();
		lists.remove(ip);
		client = null;
	}

	@Override
	public void onClose(InetAddress address) {
		if (address == null) {
			return;
		}
		removeConnection(address.toString());
	}

	@Override
	public void onWriteData(InetAddress address, byte[] data) {

	}

	@Override
	public void onWriteDataFail(InetAddress address, byte[] data) {
		removeConnection(address.toString());
	}

	/**
	 * 收到远程连接发过来的数据
	 */
	@Override
	public void onReceiveData(InetAddress address, byte[] data) {
		LogUtils.d("on receive date from app tcp" + address + " " + CommonUtils.decodeBytesToHexString(data));
		onReceiveAppData(data);
	}

	/**
	 * 从app收到数据
	 * @param
	 */
	private void onReceiveAppData(byte[] info) {
		byte[] data = info;
		if (data.length < 2) {
			return;
		}

		/*if (data[0] == Constants.ORDER_HEAD && data[1] == Constants.ORDER_APP_HEAD) {
			// 亮度调节数据
			EventBus.getDefault().post(new ReceiveAppControlDataEvent(data));
			return;
		}*/

		if (!parseDataFromAPP(data)) {
			return;
		}

		if (null != data) {
			new Thread(new BizCommandRunnable(data)).start();
		}
	}


	public class BizCommandRunnable implements Runnable {
		private byte[] data;

		public BizCommandRunnable(byte[] data) {
			this.data = data;
		}

		@Override
		public void run() {
			BizCmdConverterForMrFuture.receivedMsgFromGizConvert(data, handler);
		}
	}

	private boolean parseDataFromAPP(byte[] message) {
		try {
			int LOGIN_CMD = 8;
			int BIND_CMD = 6;
			int HEARTBEAT_CMD = 21;
			int READ_MSG_CMD = 19;
			LogUtils.d("parseDataFromAPP cmd " + CommonUtils.decodeBytesToHexString(message));

			byte[] msg = message;
			byte cmd_flag = msg[7];
			if (msg.length < 4 || msg[3] != 0x03) {
				//System.out.println("invalid data...");
				return false;
			}
			if (cmd_flag == LOGIN_CMD) {
				//System.out.println("Local APP Login...");
				LogUtils.d("parseDataFromAPP login cmd");
				int passcodeLength = msg[9];
				byte[] passcodebyte = new byte[passcodeLength];
				for (int i = 0; i < passcodeLength; i++) {
					passcodebyte[i] = msg[10 + i];
				}
				if (passcode.equals(new String(passcodebyte))) {
					byte [] date = WLTUtilsEx.uniteBytes(WLTUtilsEx.LOGIN_ORDER1, WLTUtilsEx.LOGIN_ORDER2,
							WLTUtilsEx.LOGIN_ORDER3, WLTUtilsEx.LOGIN_ORDER4, WLTUtilsEx.LOGIN_ORDER5_SUCCESS);
					LogUtils.d("parseDataFromAPP login cmd success req "+ CommonUtils.decodeBytesToHexString(date));
					writeBytes(date);
				} else {
					// login falied
					byte [] date = WLTUtilsEx.uniteBytes(WLTUtilsEx.LOGIN_ORDER1, WLTUtilsEx.LOGIN_ORDER2,
							WLTUtilsEx.LOGIN_ORDER3, WLTUtilsEx.LOGIN_ORDER4, WLTUtilsEx.LOGIN_ORDER5_FAILED);
					LogUtils.d("parseDataFromAPP login cmd failed req "+ CommonUtils.decodeBytesToHexString(date));
					writeBytes(date);
				}
				return false;

			} else if (cmd_flag == BIND_CMD) {
				LogUtils.d("parseDataFromAPP bind cmd");
				//System.out.println("Local APP BOUND(已经绑定)...");
				byte[] sendDate = WLTUtilsEx.uniteBytes(WLTUtilsEx.BIND_ORDER1, WLTUtilsEx.BIND_ORDER2,
						WLTUtilsEx.BIND_ORDER3, WLTUtilsEx.BIND_ORDER4, WLTUtilsEx.BIND_ORDER5, WLTUtilsEx.BIND_ORDER6);
				LogUtils.d("parseDataFromAPP bind cmd req is " + CommonUtils.decodeBytesToHexString(sendDate));
				writeBytes(sendDate);
				return false;
			} else if (cmd_flag == HEARTBEAT_CMD) {
				// System.out.println("HEART BEATING...");
				LogUtils.d("parseDataFromAPP heart cmd");
				byte[] sendDate = WLTUtilsEx.uniteBytes(WLTUtilsEx.HEARTBEAT_ORDER1, WLTUtilsEx.HEARTBEAT_ORDER2,
						WLTUtilsEx.HEARTBEAT_ORDER3, WLTUtilsEx.HEARTBEAT_ORDER4);
				LogUtils.d("parseDataFromAPP heart cmd req	 " + CommonUtils.decodeBytesToHexString(sendDate));
				writeBytes(sendDate);
				return false;
			} else if (cmd_flag == READ_MSG_CMD) {
				// System.out.println("HEART BEATING...");
				LogUtils.d("parseDataFromAPP send info cmd");
				WLTUtilsEx.READINFO_ORDER8 = CommonUtils.getBinVersion(context).getBytes();
				byte[] sendDate = WLTUtilsEx.uniteBytes(WLTUtilsEx.READINFO_ORDER1,WLTUtilsEx.READINFO_ORDER2,WLTUtilsEx.READINFO_ORDER3,WLTUtilsEx.READINFO_ORDER4,WLTUtilsEx.READINFO_ORDER5,WLTUtilsEx.READINFO_ORDER6,WLTUtilsEx.READINFO_ORDER7,WLTUtilsEx.READINFO_ORDER8,WLTUtilsEx.READINFO_ORDER9,WLTUtilsEx.READINFO_ORDER10,WLTUtilsEx.READINFO_ORDER11,WLTUtilsEx.READINFO_ORDER12,WLTUtilsEx.READINFO_ORDER13);
				LogUtils.d("parseDataFromAPP send info req	 " + CommonUtils.decodeBytesToHexString(sendDate));
				writeBytes(sendDate);
				return false;
			}else {
				//System.out.println("BIZ CMD COMMING(业务指令)...");
				LogUtils.d("parseDataFromAPP yewu cmd");

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	


}
