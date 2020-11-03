package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.application.InitApplication;
import com.mingri.future.airfresh.bean.ReceiverHcZero;
import com.mingri.future.airfresh.view.RoundProgressBar;

import org.greenrobot.eventbus.EventBus;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class GaoxiaoLwAlertDialog extends BaseDialog{

    private TextView tvOk, tvPercent;
    private VocAletCallback  mCallback;
    private RoundProgressBar rpb;

    public GaoxiaoLwAlertDialog(Context context, VocAletCallback rc) {
        super(context);
        InitApplication.isGaoxiao=false;
        mCallback = rc;
    }

    @Override
    protected void initView() {
        View  view=setView(R.layout.dialog_gaoxiao_lw_alert);
        tvPercent =(TextView) view.findViewById(R.id.tv_percent);
        rpb = (RoundProgressBar) view.findViewById(R.id.rpb);
        tvOk = (TextView) view.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitApplication.isGaoxiao=true;
                EventBus.getDefault().post(new ReceiverHcZero(2));
                mCallback.onOk();
                dismiss();
            }
        });
        setCanceledOnTouchOutside(false);
        setWindowLayout(279,288);
    }

    public interface VocAletCallback{
        public void onOk();
    }

    public void setPercent(int percent){
        tvPercent.setText("" + percent);
        rpb.setProgress(-percent);
    }
}
