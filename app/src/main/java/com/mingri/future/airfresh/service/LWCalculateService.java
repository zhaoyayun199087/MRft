package com.mingri.future.airfresh.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.mingri.future.airfresh.activity.BlackActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;


/**
 * 滤网寿命计算服务
 * 初效: 4330小时    中效: 6480小时    高效: 10800小时    活性炭: 10800小时
 * 滤网寿命均在设备开机后才开始计时,  关机停止倒计时.  以秒为单位.
 * UVC杀菌灯的寿命为开关寿命9000次. (UVC在智能模式下会自动开启,   其他模式下可手动开启.        开启UVC功能后,  每两小时运行十分钟,  也就是说每两小时开关一次.   UVC关闭状态下,  寿命不减少)
 * 注意:  智能模式下,  UVC自动开启后,  也可手动关闭的哦
 */
public class LWCalculateService extends Service {

    private Timer mTimer;
    private TimerTask mTimerTask;
    private long cxTime = 0, zxTime = 0, gxTime = 0, hxTime = 0, uvcTimes = 0;
    private final long CXTIME = 4330 * 60 * 60l;
    private final long ZXTIME = 6480 * 60 * 60l;
    private final long GXTIME = 10800 * 60 * 60l;
    private final long HXTIME = 10800 * 60 * 60l;
    private final long UVCTIME = 9000;
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
        getTimeDate();
        mTimer.schedule(mTimerTask, 1000, 10000);
    }

    private void getTimeDate() {
        cxTime = (long) SPUtils.get(this, "cx_time", CXTIME);
        zxTime = (long) SPUtils.get(this, "zx_time", ZXTIME);
        gxTime = (long) SPUtils.get(this, "gx_time", GXTIME);
        hxTime = (long) SPUtils.get(this, "hx_time", HXTIME);
        uvcTimes = (long) SPUtils.get(this, "uvc_time", UVCTIME);
        MachineStatusForMrFrture.UVC_TIMES = uvcTimes;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimerTask.cancel();
    }

    private class TimerTimerTask extends TimerTask {
        @Override
        public void run() {

            MachineStatusForMrFrture.Filter_Life1 = (byte)(cxTime * 100l / CXTIME) ;
            MachineStatusForMrFrture.Filter_Life2 = (byte)(zxTime * 100l / ZXTIME) ;
            MachineStatusForMrFrture.Filter_Life3 = (byte)(gxTime * 100l / GXTIME) ;
            MachineStatusForMrFrture.Filter_Life4 = (byte)(hxTime * 100l / HXTIME) ;
            MachineStatusForMrFrture.UVC_Life = (byte)(MachineStatusForMrFrture.UVC_TIMES / UVCTIME);

            cxTime -= 10 * 60;
            zxTime -= 10 * 60;
            gxTime -= 10 * 60;
            hxTime -= 10 * 60;
            uvcTimes = MachineStatusForMrFrture.UVC_TIMES;

            LogUtils.d("filter is cx " + MachineStatusForMrFrture.Filter_Life1 + " zx " + MachineStatusForMrFrture.Filter_Life2
                    + " gx " + MachineStatusForMrFrture.Filter_Life3
                    + " hx " + MachineStatusForMrFrture.Filter_Life4
                    + " uvc " + MachineStatusForMrFrture.UVC_Life
                    + " uvc times " + MachineStatusForMrFrture.UVC_TIMES);

            SPUtils.put(LWCalculateService.this, "cx_time", cxTime);
            SPUtils.put(LWCalculateService.this, "zx_time", zxTime);
            SPUtils.put(LWCalculateService.this, "gx_time", gxTime);
            SPUtils.put(LWCalculateService.this, "hx_time", hxTime);
            SPUtils.put(LWCalculateService.this, "uvc_time", uvcTimes);
        }
    }
}



