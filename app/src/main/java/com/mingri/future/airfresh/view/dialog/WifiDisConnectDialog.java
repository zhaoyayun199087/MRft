package com.mingri.future.airfresh.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.util.FileUtil;

import mingrifuture.gizlib.code.remote.WLTServerSocket;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.PxUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.wifi.WifiAdmin;
import mingrifuture.gizlib.code.wifi.WifiCipherType;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class WifiDisConnectDialog extends Dialog {
    private WifiAdmin wifiAdmin;
    private Context context;
    private TextView tvSsid;
    private RelativeLayout rlReset;
    private String ssid;
    public WifiDisConnectDialog(Context context, String ssid) {
        super(context,R.style.ddddialog1);
        this.ssid = ssid;
        this.context = context;
        initView();
    }

    protected void initView() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_disconnect_wifi, null);
        tvSsid = (TextView) view.findViewById(R.id.tv_ssid);
        rlReset = (RelativeLayout) view.findViewById(R.id.rl_reset);
        tvSsid.setText(ssid);
        rlReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());     //断开wifi网络
                SPUtils.remove(context, "ssid");        //移除wifi ssid
                SPUtils.remove(context, "pwd");         //移除wifi pwd
                //向云端发送一条重置wifi的指令
                FileUtils.deleteFile(Environment.getExternalStorageDirectory() + "/" + WLTServerSocket.mingrifuture_CONFIG);
                dismiss();
            }
        });
        setCanceledOnTouchOutside(true);
        setContentView(view);
        setWindowLayout(279,104);
    }

    /**
     * 设置Dialog大小  注：需在setContentView后设置才有效果
     * @param width
     * @param height
     */
    protected  void setWindowLayout(int width,int height){
        Window window = getWindow();
        WindowManager.LayoutParams param=window.getAttributes();
        param.width= PxUtils.dpToPx(width, context);
        param.height=PxUtils.dpToPx(height,context);
        window.setAttributes(param);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setWifiAdmin(WifiAdmin wifiAdmin) {
        this.wifiAdmin = wifiAdmin;
    }
}
