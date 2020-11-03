package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;

public class ShutDownActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shutdown);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                long current = System.currentTimeMillis();
                SPUtils.put(ShutDownActivity.this, "setshuttime", "" + current);
                SPUtils.put(ShutDownActivity.this, "shuttime", 0);
                SPUtils.put(ShutDownActivity.this, "shutswitch", 0);
                MachineStatusForMrFrture.Switch_Clock = false;
                MachineStatusForMrFrture.iCount = 100;
                Intent intent1=new Intent(ShutDownActivity.this,BlackActivityEx.class);
                startActivity(intent1);
                ShutDownActivity.this.finish();
            }
        };
//        timer.schedule(timerTask,1000*3);
        timer.schedule(timerTask, (long) (5000));

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


//
//        if( event.getRepeatCount() == 25 ){
//            openMachine();
//            finish();
//        }
        //屏蔽音量键 return true
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) // 音量增大键响应撤销
        {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)// 音量减小键响应重做
        {
            return true;
        } else return super.onKeyUp(keyCode, event);
    }

    private void openMachine(){
        MachineStatusForMrFrture.Switch = true;
        if( MachineStatusForMrFrture.Switch ){
            MachineStatusForMrFrture.startTime = System.currentTimeMillis();
        }
        CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch?1:0);

        int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
        LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
        EventBus.getDefault().post(new SendDataToMachine(d));
    }
}
