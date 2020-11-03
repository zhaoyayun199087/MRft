package mingrifuture.gizlib.code.remote;

import java.net.InetAddress;

public interface TCPMessageListener {

	public void onClose(InetAddress address);

	public void onWriteData(InetAddress address, byte[] data);

	public void onWriteDataFail(InetAddress address, byte[] data);
	
	public void onReceiveData(InetAddress address, byte[] data);
}
