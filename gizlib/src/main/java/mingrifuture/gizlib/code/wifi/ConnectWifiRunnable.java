package mingrifuture.gizlib.code.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;


/**
 * Created by pengl on 2016/1/17.
 */
public class ConnectWifiRunnable  implements Runnable {

    private WifiConfiguration wifiConfig;
    private WifiManager wifiManager;

    public ConnectWifiRunnable(WifiManager wifiManager, WifiConfiguration wifiConfig) {
        this.wifiManager = wifiManager;
        this.wifiConfig = wifiConfig;
    }

    @Override
    public void run() {
        // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        long time = System.currentTimeMillis();
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒检测……
                Thread.sleep(100);

                if (System.currentTimeMillis() - time > 1500) {
                    break;
                }

            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int netID = wifiManager.addNetwork(wifiConfig);
        boolean enabled = wifiManager.enableNetwork(netID, true);
        boolean connected = wifiManager.reconnect();
    }
}