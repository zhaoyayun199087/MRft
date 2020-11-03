package com.mingri.future.airfresh.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.adapter.WifiListAdapter;
import com.mingri.future.airfresh.bean.WifItemInfo;
import com.mingri.future.airfresh.bean.WifiChangeEvent;
import com.mingri.future.airfresh.view.dialog.ResetDialog;
import com.mingri.future.airfresh.view.dialog.WifiConnectDialog;
import com.mingri.future.airfresh.view.dialog.WifiDisConnectDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.Utils;
import mingrifuture.gizlib.code.wifi.WifiAdmin;
import mingrifuture.gizlib.code.wifi.WifiCipherType;
import mingrifuture.gizlib.code.wifi.WifiScanTimerTask;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WifiFragment extends BaseFragment {
    @InjectView(R.id.wifi_list_lv)
    ListView wifiListLv;
    /**
     * wifi管理类
     */
    private WifiAdmin mWifiAdmin;
    private WifiListAdapter mWifiAdapter;
    private WifiManager mWifiManager;
    private List<ScanResult> list;
    private WifiInfo wifiInfo;
    private Timer mScanTimer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_wifi, null);
        ButterKnife.inject(this, v);

        mWifiAdmin = new WifiAdmin(activity.getApplicationContext());
        mWifiManager = mWifiAdmin.getWifiManager();

        mWifiAdmin.openWifi();
        wifiListLv = (ListView) v.findViewById(R.id.wifi_list_lv);
        list = new ArrayList<ScanResult>();
        mWifiAdapter = new WifiListAdapter(activity, list);
        wifiListLv.setAdapter(mWifiAdapter);

        wifiListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ScanResult result = list.get(position);
                 WifItemInfo tag = new WifItemInfo();
                tag.setCip(getSecurity(result.capabilities));
                tag.setSsid(result.SSID);

                String connectedSsid = getConnectWifiSsid();
                //如果已经连接上了，点击则弹出重置wifi对话框
                if(connectedSsid != null ){
                    String curSSid = "\"" + result.SSID + "\"";
                    if (connectedSsid.equals(curSSid)) {
                        WifiDisConnectDialog wd = new WifiDisConnectDialog(getActivity(), tag.getSsid());
                        wd.setWifiAdmin(mWifiAdmin);
                        wd.show();
                        return;
                    }
                }

                // 无密码
                if (tag.getCip() == WifiCipherType.WIFICIPHER_NOPASS) {
                    Toast.makeText(getActivity(), R.string.wifi_connecting, Toast.LENGTH_SHORT).show();
                    mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(tag.getSsid()
                            .toString().trim(), "", 1));
                    return;
                }
                //有密码的wifi
                LogUtils.d("ssid is1 " + tag.getSsid());
                WifiConnectDialog wd = new WifiConnectDialog(getActivity(), tag.getSsid(),
                        tag.getCip(), "dd");
                wd.setWifiAdmin(mWifiAdmin);
                wd.show();
            }
        });
        wifiListLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        mScanTimer = new Timer();
        mScanTimer.schedule(new WifiScanTimerTask(activity, mHandler), 0, 8000);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private static final int REQUEST_SELECT_CITY = 100;

    public static final int STATE_START_SCAN = 200;

    public static final int STATE_END_SCAN = 201;

    public static final int UPDATE_WIFI_LIST = 202;

    private final int UPDATE_TIME_PERIOD = 30000;

    private final int MESSAGE_UPDATE_TIME = 100;

    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_UPDATE_TIME:
                    break;
                case STATE_START_SCAN:
                    break;

                case STATE_END_SCAN:
                    LogUtils.d("update wifi list " + isAdded() + " " + isVisible()
                            + " " + isResumed() + " " + isHidden() + " "
                            + isDetached() + " " + isResumed() + " ");
                    //fragment 当前显示状态，则更新UI
                    if (isAdded() && isResumed()) {
                        //更新wifi列表
                        UpdateWIfiHandler.post(UpdateWIfiTask);
                        boolean bConn =  isWifiConnected(getActivity());
                        int level = getConnectWifiRssi();
                        EventBus.getDefault().post(new WifiChangeEvent(level,bConn));
                    }else{
                        //fragment处于后台，不更新UI，发送event，更新状态栏图标
                        boolean bConn =  isWifiConnected(getActivity());
                        int level = getConnectWifiRssi();
                        EventBus.getDefault().post(new WifiChangeEvent(level,bConn));
                    }
                    break;
                case UPDATE_WIFI_LIST:
                    if (isAdded() && isResumed()) {
                        mWifiAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private Handler UpdateWIfiHandler = new Handler();

    private Runnable UpdateWIfiTask = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            getWifiInfo();
            getScanResult();
            mWifiAdapter.setList(list);
            LogUtils.d("update wifi list1 " + isWifiConnected(activity) + "connect wifi: " + getConnectWifiSsid() );
            if (isWifiConnected(activity)) {
                mWifiAdapter.setConnectedSSid(getConnectWifiSsid());
            } else {
                mWifiAdapter.setConnectedSSid(null);
            }
            mHandler.sendEmptyMessage(UPDATE_WIFI_LIST);
        }
    };

    /**
     * 加载信号列表
     */
    private void getWifiInfo() {
        wifiInfo = mWifiManager.getConnectionInfo();
    }

    private void getScanResult() {
        List<ScanResult> list = mWifiManager.getScanResults();
        if (list != null) {
            if (wifiInfo != null) {
                Comparator comp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        ScanResult p1 = (ScanResult) o1;
                        ScanResult p2 = (ScanResult) o2;
                        if (p1.level < p2.level)
                            return 1;
                        else if (p1.level == p2.level)
                            return 0;
                        else if (p1.level > p2.level)
                            return -1;
                        return 0;
                    }
                };
                Collections.sort(list, comp);

                String ssid = wifiInfo.getSSID().replace("\"", "");
                for (ScanResult result : list) {
                    String sid = result.SSID;
                    if (ssid.equalsIgnoreCase(sid)) {
                        // 移除sid，放到第一个位置
                        list.remove(result);
                        list.add(0, result);
                        break;
                    }
                }
            }
            // 防止搜索不到设备
            if (list.size() != 0) {
                this.list = list;
            } else {
                mWifiManager.startScan();
            }
            Log.e("error", mWifiManager.isWifiEnabled() + " "
                    + mWifiManager.getConnectionInfo().toString());
            return;
        }
        if (this.list == null) {
            this.list = new ArrayList<ScanResult>();
        }
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiNetworkInfo;
    private boolean isWifiConnected(Context context)
    {
        connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private String getConnectWifiSsid() {
        if( wifiInfo == null )
            getWifiInfo();
        return wifiInfo.getSSID();
    }

    private int getConnectWifiRssi() {
        if( wifiInfo == null )
            getWifiInfo();
        return wifiInfo.getRssi();
    }

    public int getSecurity(String capabilities) {
        if (capabilities.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (capabilities.contains("WPA")) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else if (capabilities.equals("[ESS]")) {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
        return WifiCipherType.WIFICIPHER_INVALID;
    }
}
