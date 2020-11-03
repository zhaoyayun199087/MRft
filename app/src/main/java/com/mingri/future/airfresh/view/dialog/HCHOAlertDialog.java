package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class HCHOAlertDialog extends BaseDialog{

    private TextView tvOk;
    private VocAletCallback  mCallback;

    public HCHOAlertDialog(Context context, VocAletCallback rc) {
        super(context);
        mCallback = rc;
    }

    @Override
    protected void initView() {
        View  view=setView(R.layout.dialog_hcho_alert);
        tvOk = (TextView) view.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
