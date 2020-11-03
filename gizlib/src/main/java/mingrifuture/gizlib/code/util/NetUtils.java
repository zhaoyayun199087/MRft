package mingrifuture.gizlib.code.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import mingrifuture.gizlib.code.remote.WLTUtilsEx;

public class NetUtils {

    public final static int NETWORK_PHONE = 1;
    public final static int NO_NETWORK = 0;
    public final static int NETWORK_WIFI = 2;

    /**
     * 判断wifi是否可用
     *
     * @param inContext
     * @return true表示可用, false表示不可用
     */
    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI")
                            && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断当前网络是否能用，并判断网络类型 当前网络类型，0无网络，1手机网，2wifi网
     *
     * @param context
     */
    public static int isNetworkActive(Context context) {
        State wifiState;
        State mobileState;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        // wifi和手机网同时开启时手机优先使用wifi网络
        if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED == mobileState) {
            // 手机网络连接成功
            return NETWORK_PHONE;
        } else if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED != mobileState) {
            // 手机没有任何的网络
            return NO_NETWORK;
        } else {
            // 无线网络连接成功 (wifiState != null && State.CONNECTED == wifiState)
            return NETWORK_WIFI;
        }
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测wifi是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 手机网络下获取ip
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalIpAddress() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface
                .getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                    .hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()
                        && (inetAddress instanceof Inet4Address)) {
                    return inetAddress.getHostAddress().toString();
                }
            }
        }
        return null;
    }

    /**
     * wifi下获取ip地址
     *
     * @param context
     * @return
     */
    public static String getIP(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return null;
//			wifiManager.setWifiEnabled(true);
        }

        WifiInfo infor = wifiManager.getConnectionInfo();
        int ipaddress = infor.getIpAddress();
        String ip = intoIp(ipaddress);
        return ip;
    }

    /**
     * 转成16进制IP
     *
     * @param
     * @return
     */
    public static String intoIp(int ip) {
        return (ip & 0xFF) + "." + (ip >> 8 & 0xFF) + "." + (ip >> 16 & 0xFF)
                + "." + (ip >> 24 & 0xFF);
    }

    /**
     * 获取物理地址
     *
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取mac地址并保存mac地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        String mac = (String) SPUtils.get(context, "mac", null);
        if (TextUtils.isEmpty(mac)) {
            mac = getLocalMacAddress(context);
            if (TextUtils.isEmpty(mac)) {
                return null;
            } else {
                SPUtils.put(context, "mac", mac);
            }
        }
        return mac;
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 机智云检测固件版本
     */
    public static String checkOta(Context context) {
        String did = null;

        try {
            did = CommonUtils.readDid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = "http://api.gizwits.com/dev/ota/v4.1/update_and_check/" + did;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "did=" + did + "&passcode=gokit1&type=2&hard_version=" + CommonUtils.getBinHardVersion(context) + "&soft_version=" + CommonUtils.getBinVersion(context) + "&product_key=" + WLTUtilsEx.product_key + "&otaid_type=2";
//			String data = "did=" + did + "&passcode=gokit1&type=2&hard_version="+"00000000"+"&soft_version=" + "00000001" + "&product_key=" + WLTUtilsEx.product_key+"&otaid_type=2" ;

            //至少要设置的两个请求头
            LogUtils.d("check ota " + data);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
                return inputStream2String(is);
            } else {
                //请求失败
                return "" + responseCode + "  " + inputStream2String(connection.getErrorStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        if (in != null) {

            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            return out.toString();
        }else return "";
    }
}
