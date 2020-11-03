package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.OpenMachineEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;


public class BlackActivity extends Activity {
    private ScreenBrightnessTool mBrightnessTool;
    private RelativeLayout rlRoot;
    private final static int DECRESE_LIGHT = 0;
    private final static int INCRESE_LIGHT = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case DECRESE_LIGHT:

                    break;
                case INCRESE_LIGHT:

                    break;
            }
            return true;
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        EventBus.getDefault().register(this);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mBrightnessTool = ScreenBrightnessTool.Builder(this);

        if (true) {
            mHandler.sendEmptyMessage(DECRESE_LIGHT);
        } else
            mBrightnessTool.setBrightness(0);
        LogUtils.d("BlackActivity oncreate");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        LogUtils.d("dispatch event");
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.d("LockActivity", "onTouchEvent");
            finish();
            MachineStatusForMrFrture.iCount = 0;
            int light = (int) SPUtils.get(this, "light", 40);
            mBrightnessTool.setBrightness1(light);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d("key is " + keyCode + " event " + event.toString());

        //如果在未关机状态下，并且按下去的时间低于两秒，那么亮屏
        if (event.getRepeatCount() < 25) {
            if (MachineStatusForMrFrture.Switch) {
                finish();
                MachineStatusForMrFrture.iCount = 0;
                int light = (int) SPUtils.get(this, "light", 40);
                mBrightnessTool.setBrightness1(light);
            }
        }

        //屏蔽音量键 return true
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) // 音量增大键响应撤销
        {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)// 音量减小键响应重做
        {
            return true;
        } else return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(OpenMachineEvent data) {
        finish();
    }
}
