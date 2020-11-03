package mingrifuture.gizlib.code.remote;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.LogUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPWLTServerEx extends Thread {
    public static String HOST = "255.255.255.255";
    public static final int UDP_PORT = 12414;
    private volatile int isRun = 0;
    private DatagramSocket socket;
    private InetAddress group;
    private String mac;
    private String did;

    public void shutdown() {
        this.socket.disconnect();
        this.socket.close();
    }

    public UDPWLTServerEx(String host, int port, String mac, String did) throws Exception {
        this.group = InetAddress.getByName(HOST);
        this.socket = new DatagramSocket(12414);
        this.socket.setBroadcast(true);
        this.socket.setSoTimeout(5000);
        this.mac = mac;
        this.did = did;
    }

    public void startup() throws Exception {
        this.group = InetAddress.getByName(HOST);
        this.socket = new DatagramSocket(12414);
        this.socket.setBroadcast(true);
        this.socket.setSoTimeout(5000);
        this.start();
    }

    public boolean isRunning() {
        return this.isRun == 1;
    }

    public void run() {
        try {
            LogUtils.d("started udp server");
            this.doStartUDPServer();
        } catch (Exception var2) {
            LogUtils.d("starting failed", var2);
        }

    }

    private void doStartUDPServer() throws Exception {
        byte[] buf = new byte[1024];

        while(true) {
            this.isRun = 1;

            try {
                DatagramPacket e = new DatagramPacket(buf, buf.length);
                try {
                    this.socket.receive(e);
                } catch (SocketTimeoutException var4) {
//                    var4.printStackTrace();
                }

                if(e.getData().length != 0 && e.getAddress() != null) {
                    byte[] message = new byte[e.getLength()];
                    System.arraycopy(buf, 0, message, 0, e.getLength());
                    System.out.println(e.getAddress());
                    System.out.println(e.getPort());
                    this.sendBack(this.group, e.getPort(), message);
                }
            } catch (Exception var5) {
                LogUtils.d("udp server stoped " +  var5);
                this.isRun = 0;
                this.shutdown();
                throw var5;
            }
        }
    }

    public void sendBack(InetAddress callbackAddr, int port, byte[] message) throws Exception {
        byte[] callbackData = this.getCallBackData(message);
        DatagramPacket msgPacket = new DatagramPacket(callbackData, callbackData.length, callbackAddr, port);
        if(this.socket != null) {
            try {
                this.socket.send(msgPacket);
            } catch (IOException var7) {
                LogUtils.d("udp msg send " + var7);
            }

        }
    }

    public byte[] getCallBackData(byte[] revMsg) throws Exception {
        byte[] ORDER2 = new byte[]{WLTUtilsEx.intToByte(WLTUtilsEx.intToByte(11)[3] + WLTUtilsEx.intToByte(this.did.getBytes().length)[3] + WLTUtilsEx.intToByte(this.mac.getBytes().length)[3] + WLTUtilsEx.intToByte(WLTUtilsEx.product_key.getBytes().length)[3] + WLTUtilsEx.intToByte(WLTUtilsEx.product_key.getBytes().length)[3])[3]};
        byte[] date = WLTUtilsEx.uniteBytes(new byte[][]{WLTUtilsEx.ORDER1, ORDER2, WLTUtilsEx.ORDER3, WLTUtilsEx.ORDER4, WLTUtilsEx.ORDER5, this.did.getBytes(), WLTUtilsEx.ORDER7, this.mac.getBytes(), WLTUtilsEx.ORDER9, WLTUtilsEx.ORDER10, WLTUtilsEx.ORDER11, WLTUtilsEx.ORDER12});
        LogUtils.d("parseDataFromAPP find cmd req " + CommonUtils.decodeBytesToHexString(date));
        return date;
    }
}
