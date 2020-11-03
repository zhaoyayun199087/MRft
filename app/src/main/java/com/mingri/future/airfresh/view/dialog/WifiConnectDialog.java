package com.mingri.future.airfresh.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;

import org.greenrobot.eventbus.EventBus;

import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.PxUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.wifi.WifiAdmin;
import mingrifuture.gizlib.code.wifi.WifiCipherType;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class WifiConnectDialog extends Dialog {
    private WifiAdmin wifiAdmin;
    private Context context;
    private EditText etPwd;
    private ImageButton ibVisible;
    private TextView tvSsid;
    private Button btnOK, btnCancel;
    private String ssid, type;
    private int typeid;
    public WifiConnectDialog(Context context, String ssid,int typeid, String type) {
        super(context,R.style.ddddialog1);
        this.ssid = ssid;
        this.typeid = typeid;
        this.type = type;
        this.context = context;
        initView();
    }

    protected void initView() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_connect_wifi, null);
        etPwd = (EditText) view.findViewById(R.id.et_pwd);
        ibVisible = (ImageButton) view.findViewById(R.id.ib_visible);
        tvSsid = (TextView) view.findViewById(R.id.tv_ssid);
        btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        LogUtils.d("ssid is " + ssid);
        tvSsid.setText(ssid);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = String.valueOf(etPwd.getText());
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), R.string.wifi_pwd_nw_null,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(getContext(), R.string.wifi_pwd_nw8,
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                SPUtils.put(context, "ssid", ssid.toString().trim());
                SPUtils.put(context, "pwd", password.toString().trim());
                SPUtils.put(context, "wifiType", typeid == (WifiCipherType.WIFICIPHER_WPA) ? 3 : 2);

                LogUtils.d("connect to " +ssid.toString().trim() + "  " + password.toString().trim() );
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(ssid.toString()
                                .trim(), password.toString().trim(),
                        typeid == (WifiCipherType.WIFICIPHER_WPA) ? 3 : 2));
                Toast.makeText(getContext(), R.string.wifi_connecting,
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ibVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("ddddd " + etPwd.getInputType());
                if( etPwd.getText().toString() != null || etPwd.getText().toString().length() > 0 ){
                    if( etPwd.getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT )) {
                        etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD |InputType.TYPE_CLASS_TEXT );
                    }else {
                        etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |InputType.TYPE_CLASS_TEXT );
                    }
                }
            }
        });
        setCanceledOnTouchOutside(false);
        setContentView(view);
        setWindowLayout(329,179);
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
