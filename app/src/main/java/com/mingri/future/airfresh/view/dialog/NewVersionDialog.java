package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/7/7.
 */
public class NewVersionDialog extends BaseDialog {

    private TextView tvNewVersion, tvVersionTip;
    private Button btnUpdate;
    private ImageButton ibExit;
    private updateCallback mUpdateCallback;
    private String sNewVersion,sVersionTip,sUrl;

    public NewVersionDialog(Context context, String newVersion, String VersionTip,String url, updateCallback uc) {
        super(context);
        this.sNewVersion = newVersion;
        this.sVersionTip = VersionTip;
        this.mUpdateCallback = uc;
        this.sUrl = url;
        tvNewVersion.setText("V" + sNewVersion);
        tvVersionTip.setText(sVersionTip);
    }

    @Override
    protected void initView() {
       View v= setView(R.layout.dialog_new_version);
        setWindowLayout(424,208);
        tvNewVersion = (TextView) v.findViewById(R.id.tv_newversoin);
        tvVersionTip = (TextView) v.findViewById(R.id.tv_version_tip);


        btnUpdate = (Button) v.findViewById(R.id.btn_update);
        ibExit = (ImageButton) v.findViewById(R.id.iv_exit);

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateCallback.update(sUrl);
                dismiss();
            }
        });
    }

    public interface updateCallback{
        public void update(String url);
    }
}
