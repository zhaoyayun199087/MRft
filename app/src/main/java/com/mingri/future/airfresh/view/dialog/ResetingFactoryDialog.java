package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;

/**
 * 耗材重置Dialog
 * Created by Administrator on 2017/7/7.
 */
public class ResetingFactoryDialog extends BaseDialog{

    private int percent = 0;
    private TextView tvPercent;
    private ResetDialogCallback  mCallback;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            percent++;
            tvPercent.setText("" + percent + "%");
            if( percent >= 100 ){
                mCallback.onOk();
                mHandler.removeCallbacksAndMessages(null);
            }else {
                mHandler.sendEmptyMessageDelayed(0, 60);
            }
            return false;
        }
    });

    public ResetingFactoryDialog(Context context, ResetDialogCallback rc) {
        super(context);
        mCallback = rc;
        mHandler.sendEmptyMessage(0);
        MachineStatusForMrFrture.isExiting = true;
    }

    @Override
    protected void initView() {
        View  view=setView(R.layout.dialog_reseting_factory);
        tvPercent = (TextView) view.findViewById(R.id.tv_percent);
        setCanceledOnTouchOutside(false);
        setWindowLayout(283,287);
    }

    public interface ResetDialogCallback{
        public void onOk();
    }
}
