package mingrifuture.gizlib.code.provider;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import mingrifuture.gizlib.code.wifi.WifiAdmin;
import mingrifuture.gizlib.code.wifi.WifiApAdmin;

/**
 * Created by andyz on 2017/6/1.
 */
public class SmartConfig {

    public static  void connectTest(Context context, SmartConfigCallback callback) {
        WifiAdmin wifiAdmin;
        WifiManager mWifiManager;
        WifiApAdmin wifiAp;
        WifiAdmin mWifiAdmin;
        byte[] buf = new byte[1000];
        DatagramSocket socket;
        boolean isRun = true;
        String result;
        String[]  wifimsg = new String[]{};
        wifiAdmin = new WifiAdmin(context);
        wifiAp = new WifiApAdmin(context);
        wifiAp.startWifiAp("mingrifutureHotSpot", "12345678");

        mWifiAdmin = new WifiAdmin(context);


        callback.OnStep("Create ap success");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            InetAddress serverAddr = InetAddress.getByName("192.168.43.1");
            socket = new DatagramSocket(8888,
                    serverAddr);

            callback.OnStep("In listening");
            while (isRun) {
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                if (result != null) {
                    callback.OnStep("Receve connect info : " + result);
                    wifimsg = result.split(",");
                    if (wifimsg.length == 3) {
                        isRun = false;
                        socket.close();
                    }else{
                        callback.OnStep("parament error");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ;callback.OnStep("create socke error");
        }
        callback.OnStep("Closing hotspot" );
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wifiAp.closeWifiHotspot();
        callback.OnStep("Opening wifi" );
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWifiAdmin.openWifi();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.OnStep("Connecting to wifi ssid: " + wifimsg[0] + " pwd:  " + wifimsg[1]  + " type: " + wifimsg[2] );
        mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(wifimsg[0],  wifimsg[1], Integer.parseInt(wifimsg[2])));
        callback.OnStep("success connect to " + wifimsg[0]);

    }
}
