package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class ResetFactoryDialog extends BaseDialog{

    private TextView tvOk;
    private TextView tvCancel;
    private ResetDialogCallback  mCallback;

    public ResetFactoryDialog(Context context, ResetDialogCallback rc) {
        super(context);
        mCallback = rc;
    }

    @Override
    protected void initView() {
        View  view=setView(R.layout.dialog_reset_factory);
        tvOk = (TextView) view.findViewById(R.id.tv_ok);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onOk();
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onCancel();
                dismiss();
            }
        });
        setCanceledOnTouchOutside(false);
        setWindowLayout(283,287);
    }

    public interface ResetDialogCallback{
        public void onOk();
        public void onCancel();
    }
}
