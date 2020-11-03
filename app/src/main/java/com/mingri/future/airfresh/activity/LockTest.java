package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.view.SlideUnlockView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;

/**
 * Created by Administrator on 2017/6/21.
 */
public class LockTest extends Activity {
    private SlideUnlockView sduv;
    private ImageButton ibCancel;
    private ExecutorService mSingleThreadExecutor  = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_test);
        sduv = (SlideUnlockView) findViewById(R.id.sv);
        ibCancel = (ImageButton) findViewById(R.id.ib_cancel);
        final ScreenBrightnessTool brightnessTool = ScreenBrightnessTool
                .Builder(this);
        final int baseLight = 150;
        sduv.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean lock)             {
                if( lock ) {
                    shutDown();
                    startActivity(new Intent(LockTest.this, ShutDownActivity.class));
                    finish();
                }
                else {
                    brightnessTool.setBrightness(baseLight);
                }
            }

            @Override
            public void OnProgress(float progress) {

                final int prog = (int)(255f * progress);
                LogUtils.d("onprogress " + prog );
                mSingleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        brightnessTool.setBrightness(255 - prog);
                    }
                });
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }
    private void shutDown(){
        MachineStatusForMrFrture.Switch = false;
        LogUtils.d("power  6 is " + MachineStatusForMrFrture.Switch);
        if( MachineStatusForMrFrture.Switch ){
            MachineStatusForMrFrture.startTime = System.currentTimeMillis();
        }
        CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch?1:0);

        int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }




}
