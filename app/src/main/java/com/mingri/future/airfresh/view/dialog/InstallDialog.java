package com.mingri.future.airfresh.view.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.util.ApkManager;

/**
 * Created by Administrator on 2017/9/21.
 */

public class InstallDialog extends BaseDialog {
    int percent=0;
    TextView tv_percent;
     private Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             percent++;
             tv_percent.setText(percent+"%");
             if(percent<99)handler.sendEmptyMessageDelayed(0,25);
         }
     };

    public InstallDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        View view= setView(R.layout.dialog_install);
        tv_percent= (TextView) view.findViewById(R.id.install_percent);
        setWindowLayout( 256, 208);

//        setCancelable(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.sendEmptyMessageDelayed(0,20);
    }
}
