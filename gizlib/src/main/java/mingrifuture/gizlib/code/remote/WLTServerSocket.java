package mingrifuture.gizlib.code.remote;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import mingrifuture.gizlib.code.provider.BizCmdConverter;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.NetUtils;
import com.wlt.http.client.GatewayClientException;
import com.wlt.http.client.RemoteMessageArrivedListener;
import com.wlt.http.client.TCPWLTServer;
import com.wlt.http.client.WLTMqttClient;
import com.wlt.http.client.WLTService;
import com.wlt.http.client.dto.CreateDeviceREQ;
import com.wlt.http.client.dto.DeviceProvisionREQ;
import com.wlt.http.client.http.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 连接机智云的socket，主要用于控制远程在线客户端
 * @author pengl
 *
 */
public class WLTServerSocket implements RemoteMessageArrivedListener{
	public static final String TAG = "WLTServerSocket";
	public static final String mingrifuture_CONFIG = "mingrifuture.properties";

	/** 当前本机IP **/
	private String ip = null;
	private String mac = null;
	
	private UDPWLTServerEx mUDPWLTServerEx;
	private TCPWLTServer mTcpwltServer;
	private WLTMqttClient mWltMqttClient;
	private WLTService mWltService;
	private Handler handler;

	private volatile boolean isWLTMqttClientRunning;
	private volatile String did = null;
	private Context context;
	
	private WifiManager wifiManager;

	public WLTServerSocket(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		try {
			did = readDid();
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		} catch (Exception e) {
			FileUtils.printExceptionToFile(e, "read did  config failed1:");
		}
	}
	
	public void startTcpService() {
		try {
			if (mWltService == null) {
				mWltService = new WLTService("zh_CN");
				mWltService.setConnectionTimeoutSeconds(30);
			}
			
			if (mWltMqttClient == null && !isWLTMqttClientRunning) {
				new Thread(new WLTMqttClientRunnable()).start();
			}
			
			if (mWltMqttClient != null && !mWltMqttClient.isConnected() && wifiManager.isWifiEnabled()) {
				FileUtils.writeLogToFile("reconnConnect...", new byte[] {});
				mWltMqttClient.startReconnect();			
				Thread.sleep(2000);
				FileUtils.writeLogToFile("reconnConnected?..."+mWltMqttClient.isConnected(), new byte[] {});
				//mc.subscribeTopic();
				//writeReceivedFromDevToFile("subscribeTopic success...", new byte[] {});
			}
			
			if (mWltMqttClient != null && mWltMqttClient.isConnected() && wifiManager.isWifiEnabled()) {
				mWltMqttClient.subscribeTopic();
				FileUtils.writeLogToFile("subscribeTopic only success...", new byte[] {});
			}
			
		} catch (Exception e) {
			FileUtils.printExceptionToFile(e, "connect to JZY failed failed:");
		}
		
		
	}
	
	/**
	 * IP地址是否可用
	 * @return
	 */
	public boolean networkIsAble() {
		if (ip == null) {
			ip = NetUtils.getIP(context);
			if (TextUtils.isEmpty(ip)) {
				// return false;
			}
		}
		if (mac == null) {
			mac = NetUtils.getMac(context);
			if(mac != null){
				mac = mac.replace(":", "");
				if (mac.length() > 4) {
					mac = "accf" + mac.substring(4, mac.length());
				}
			}
		}
//		mac =  "accfdff6f7e5";
		return true;
	}
	
	
	public void startUdpService() throws Exception {
		try {
			//Log.i("test", "startUdp");
			if (did != null) {
				if (null == mUDPWLTServerEx) {
					mUDPWLTServerEx = new UDPWLTServerEx(UDPWLTServerEx.HOST, UDPWLTServerEx.UDP_PORT, mac, did);
					mUDPWLTServerEx.start();
					FileUtils.writeLogToFile("started udp server -->startUdpService:", new byte[] {});
				}
			}
		} catch (Exception e) {
			// handler.sendEmptyMessageDelayed(HomeBackService.UDP_EXCEPTION,
			// 3000);
			FileUtils.printExceptionToFile(e, "started udp server failed:");
			throw e;

		}
	}
	
	public void releaseConnection(){
		if (mWltService != null) {
			try {
				mWltService.destory();
				mWltService = null;
			} catch (Exception e) {
				FileUtils.printExceptionToFile(e, "stopConnection failed:");
			}
		}

		try {
			if (mWltMqttClient != null && isWLTMqttClientRunning) {
				mWltMqttClient.shutDown();
				mWltMqttClient = null;
			}
			if (mUDPWLTServerEx != null) {
				mUDPWLTServerEx.shutdown();
				mUDPWLTServerEx = null;
			}
			if (mTcpwltServer != null) {
				mTcpwltServer.shutdown();
				mTcpwltServer = null;
			}
		} catch (Exception e) {
			FileUtils.printExceptionToFile(e, "stopConnection failed:");
		}
	}
	
	/**
	 * 收到云端传输过来的消息
	 */
	@Override
	public void receiveMsg(String arg0, byte[] arg1) {
		LogUtils.d("on receive date from app server " + arg0 + " " + CommonUtils.decodeBytesToHexString(arg1));
		 BizCmdConverterForMrFuture.receivedMsgFromGizConvert(arg1, handler);
	}
	
	public WLTMqttClient getWltMqttClient() {
		return mWltMqttClient;
	}
	

	/**
	 * 得到设备id
	 * @return
	 * @throws Exception
	 */
	private String readDid() throws Exception {
		// 读文件
		Properties properties = new Properties();
		try {
			File file = new File(Environment.getExternalStorageDirectory(), mingrifuture_CONFIG);
			if (!file.exists()) {
				file.createNewFile();
				return null;
			} else {
				FileInputStream s = new FileInputStream(file);
				properties.load(s);
			}
		} catch (Exception e) {
			throw e;
		}
		
		if ((String) properties.get("did") != null) {
			return ((String) properties.get("did"));
		}

		return null;
	}
	
	private void setDid(String did) throws Exception {
		// 写文件
		try
		{
			File file = new File(Environment.getExternalStorageDirectory(), mingrifuture_CONFIG);
			FileOutputStream s = new FileOutputStream(file, false);
			Properties properties = new Properties();
			properties.setProperty("did", did + "");

			properties.store(s, null);
		} catch (Exception e)
		{
			throw e;
		}
	}
	
	private class WLTMqttClientRunnable implements Runnable {
		@Override
		public void run() {
			try {
				if (null == mWltService) {
					mWltService = new WLTService("zh_CN");
					mWltService.setConnectionTimeoutSeconds(30);
				}

				String didFromConfig = readDid();
				if (didFromConfig == null && did == null) {
					CreateDeviceREQ req = new CreateDeviceREQ();
					req.setMac(mac);
					req.setPasscode("gokit1");
					req.setProductKey("02ebbd8a73de4a58b9679abe63372ef9");
					req.setType("normal");
					Map<String, String> createDevResult = mWltService.createDevice(req, HttpRequest.METHOD_POST);

					FileUtils.writeLogToFile("get did...:" + createDevResult.get("did"), new byte[] {});
					did = createDevResult.get("did");
					if (did != null) {
						setDid(did);
					}
				}

				if (did != null) {
					DeviceProvisionREQ provisionReq = new DeviceProvisionREQ();
					provisionReq.setDid(did);
					Map<String, String> provisionResult = null;

					try {
						provisionResult = mWltService.provision(provisionReq, HttpRequest.METHOD_GET);

						FileUtils.writeLogToFile("starting JGY server -->hostStr:" + provisionResult.get("host")
								+ "--" + provisionResult.get("port"), new byte[] {});
					} catch (GatewayClientException e) {
						FileUtils.printExceptionToFile(e, "provision failed1:" + did);
					}
					String hostStr = "tcp://" + provisionResult.get("host") + ":" + provisionResult.get("port");
					mWltMqttClient = new WLTMqttClient(hostStr, did, "gokit1", WLTServerSocket.this);
					isWLTMqttClientRunning = true;
				}

				FileUtils.writeLogToFile("MAC :" +mac
						+ "--DID" + did, new byte[] {});
			} catch (Exception e) {
				isWLTMqttClientRunning = false;
				FileUtils.printExceptionToFile(e, "starting JGY server failed1:");
			}
		}

	}
	
	
}
