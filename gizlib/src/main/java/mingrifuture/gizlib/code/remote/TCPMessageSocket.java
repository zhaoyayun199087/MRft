package mingrifuture.gizlib.code.remote;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPMessageSocket extends Thread {
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private volatile boolean isOnline = true;
	private TCPMessageListener listener;
	private InetAddress address;
	/**
	 * 数据缓冲区大小
	 */
	private int length = 1024;

	public TCPMessageSocket(TCPMessageListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		if (socket == null) {
			close();
			listener.onClose(null);
			return;
		}

		address = socket.getInetAddress();

		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			isOnline = true;
		} catch (IOException e) {
			close();
			listener.onClose(address);
			return;
		}

		while (isOnline) {
			byte[] dataBuffer = new byte[length];
			try {
				int readLength = inputStream.read(dataBuffer, 0, length);
				if (readLength != -1) {
					OnReceiveData(dataBuffer, readLength);
				} else {
					Log.e("error", "read data is -1");
					throw new IOException();
				}
			} catch (IOException e) {
				break;
			}
		}
		close();
		listener.onClose(address);
	}

	public void writeBytes(byte[] data) {
		if (outputStream != null) {
			try {
				outputStream.write(data, 0, data.length);
				listener.onWriteData(address, data);
				return;
			} catch (IOException e) {
				listener.onWriteDataFail(address, data);
			}
		} else {
			listener.onWriteDataFail(address, data);
		}
	}

	private void OnReceiveData(byte[] dataBuffer, int readLength) {
		byte[] data = new byte[readLength];
		System.arraycopy(dataBuffer, 0, data, 0, readLength);
		listener.onReceiveData(address, data);
	}

	public void close() {
		isOnline = false;
		if (socket != null) {
			try {
				socket.close();
				socket = null;
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}

				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
			} catch (IOException e) {

			}
		}
	}

	/**
	 * 获取连接状态
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return isOnline;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
