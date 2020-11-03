package com.mingri.future.airfresh.service;

import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.mingri.future.airfresh.activity.BlackActivity;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.SendDateToGiz;
import com.mingri.future.airfresh.bean.Task;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.util.TaskQueue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.AppCmd;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.provider.GizCloudCallback;
import mingrifuture.gizlib.code.provider.GizCloudManager;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;


/**
 * 亮度控制服务
 */
public class LightControlService extends Service {

    private boolean bChangeLight;
    private int iLignt;
    private float iDiffLignt;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Timer mLightTimer;

    private TimerTask mLightTimerTask;
    private int iLightChangeCount = 10;
    private ScreenBrightnessTool screenBrightnessTool;
    private final static int DECRESE_LIGHT = 0;
    private final static int INCRESE_LIGHT = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case DECRESE_LIGHT:
                    iLightChangeCount--;

                    LogUtils.d("set bright " + ( ( (int)(iDiffLignt * iLightChangeCount))) + " ChangeLight " +bChangeLight);
                    if( !bChangeLight ){
                        iLightChangeCount = 40;
                    }
                    screenBrightnessTool.setBrightness1( ( (int)(iDiffLignt * iLightChangeCount)));
                    if (iLightChangeCount <= 0) {
                        mLightTimer.cancel();
                        Intent intent = new Intent(LightControlService.this, BlackActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        mHandler.sendEmptyMessageDelayed(DECRESE_LIGHT, 50);
                    }
                    break;
                case INCRESE_LIGHT:

                    break;
            }
            return  true;
        }

    });
    /**
     * 每次只执行一个任务的线程池
     */
    private ExecutorService mSingleThreadExecutor = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimerTask = new TimerTimerTask();
        mTimer.schedule(mTimerTask, 1000, 2000);

        mLightTimer = new Timer();
        screenBrightnessTool = ScreenBrightnessTool.Builder(this);
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class TimerTimerTask extends TimerTask {
        @Override
        public void run() {
            if(MachineStatusForMrFrture.isExiting){
                return;
            }
            LogUtils.d("icount is " + MachineStatusForMrFrture.iCount);
            if (MachineStatusForMrFrture.Mode != 2) {
                if (MachineStatusForMrFrture.iCount >= 14 && MachineStatusForMrFrture.iCount < 30) {
                    int light = (int) SPUtils.get(LightControlService.this, "light", 40);
                    if (light > 20) {
                        LogUtils.d("set bright1 20");
                        screenBrightnessTool.setBrightness1(20);
                    }

                } else if (MachineStatusForMrFrture.iCount == 30) {
                    LogUtils.d("set bright2 1");
                    screenBrightnessTool.setBrightness1(0);
                    Intent intent = new Intent(LightControlService.this, BlackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (MachineStatusForMrFrture.iCount > 30) {
                    LogUtils.d("set bright2 1");
                    screenBrightnessTool.setBrightness1(0);
                } else {

                    int light = (int) SPUtils.get(LightControlService.this, "light", 40);
                    screenBrightnessTool.setBrightness1(light);
                }
            } else {
                if (MachineStatusForMrFrture.iCount == 1) {
//                    iLignt-=10;
//                    if( iLignt < 10 ) {
//                        Intent intent = new Intent(LightControlService.this, BlackActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        iLignt = 0;
//                    }
//                    LogUtils.d("set bright " + iLignt);
//                    screenBrightnessTool.setBrightness1(iLignt);
                    startDecreseLight();
                } else if (MachineStatusForMrFrture.iCount == 0) {
                    iLignt = (int) SPUtils.get(LightControlService.this, "light", 40);
                    LogUtils.d("set bright3 " + iLignt);
                    bChangeLight = false;
                    screenBrightnessTool.setBrightness1(iLignt);
                }
            }
            MachineStatusForMrFrture.iCount++;
        }
    }

    private void startDecreseLight() {
        bChangeLight = true;
        iLightChangeCount = 40;
        iLignt = (int) SPUtils.get(LightControlService.this, "light", 40);
        iDiffLignt = (float)iLignt / (float)iLightChangeCount;
//        mSingleThreadExecutor.execute(new LightDecreseRunable());

        mHandler.sendEmptyMessage(DECRESE_LIGHT);
    }

    private void startIncreaseLight() {
        iLightChangeCount = 50;
        iLignt = (int) SPUtils.get(LightControlService.this, "light", 40);
        iDiffLignt = (float)iLignt / (float)iLightChangeCount;
//        mLightTimer.schedule(mLightTimerTask, 0, 500);
        mSingleThreadExecutor.execute(new LightIncreaseRunable());
    }

    public class LightDecreseRunable implements Runnable {

        @Override
        public void run() {
            while (iLightChangeCount > 0 && bChangeLight) {
                iLightChangeCount--;

                LogUtils.d("set bright " + ( ( (int)(iDiffLignt * iLightChangeCount))) + " ChangeLight " +bChangeLight);
                screenBrightnessTool.setBrightness1( ( (int)(iDiffLignt * iLightChangeCount)));
                if (iLightChangeCount <= 0) {
                    mLightTimer.cancel();
                    Intent intent = new Intent(LightControlService.this, BlackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class LightIncreaseRunable implements Runnable {

        @Override
        public void run() {
            while (iLightChangeCount > 0) {
                iLightChangeCount--;

                LogUtils.d("set bright4 " + iDiffLignt * iLightChangeCount + " idiff " + iDiffLignt + " ilightcount " + iLightChangeCount);
                screenBrightnessTool.setBrightness1((int)(iDiffLignt * iLightChangeCount));
                if (iLightChangeCount <= 0) {
                    mLightTimer.cancel();
                    Intent intent = new Intent(LightControlService.this, BlackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

};



