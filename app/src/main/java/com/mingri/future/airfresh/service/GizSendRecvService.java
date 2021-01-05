package com.mingri.future.airfresh.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.activity.ShutDownActivity;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.OpenMachineEvent;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.SendDateToGiz;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.UnLockChilrdLock;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.AppCmd;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.provider.GizCloudCallback;
import mingrifuture.gizlib.code.provider.GizCloudManager;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;


/**
 * 机智云收发服务
 * Created by andyz on 2017/3/14.
 */
public class GizSendRecvService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private GizCloudManager gizCloudManager;
    private long setPowerTime = System.currentTimeMillis();

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        gizCloudManager = new GizCloudManager(this, new GizCloudCallback() {
            @Override
            public void OnChangeDate(AppCmd obj) {
                LogUtils.d("app send cmd " + obj.toString() + "  " + obj.getCmd() + "  " + (obj.getCmd() == Constants.ANDROID_SEND_MODE));
                if (obj.getCmd() == 0) {
                    LogUtils.d("app send cmd 0");
                    return;
                } else if ((obj.getCmd() == Constants.ANDROID_SEND_CLOSETIME)) {
                    int time = (int) obj.getDate();
                    LogUtils.d("");
                    time = time / 60;
//                    if( time  != 0 ) {
                    long current = System.currentTimeMillis();
                    SPUtils.put(GizSendRecvService.this, "setshuttime", "" + current);
                    SPUtils.put(GizSendRecvService.this, "shuttime", time);
                    SPUtils.put(GizSendRecvService.this, "shutswitch", 2);
                    MachineStatusForMrFrture.Switch_Clock = true;
//                    }else{
//                        long current = System.currentTimeMillis();
//                        SPUtils.put(GizSendRecvService.this, "setshuttime", "" + current);
//                        SPUtils.put(GizSendRecvService.this, "shuttime", 0);
//                        SPUtils.put(GizSendRecvService.this, "shutswitch", 0);
//                        MachineStatusForMrFrture.Switch_Clock = false;
//                    }
                } else if ((obj.getCmd() == Constants.ANDROID_SEND_SWITCH_CHIRLD)) {
                    if ((Boolean) obj.getDate()) {
                        EventBus.getDefault().post(new ShowMainPage(1));
                        EventBus.getDefault().post(new ChirldLock(true));
                        MachineStatusForMrFrture.Child_Security_Lock = true;
                    } else {
                        EventBus.getDefault().post(new UnLockChilrdLock());
                        MachineStatusForMrFrture.Child_Security_Lock = false;
                    }
                } else if ((obj.getCmd() == Constants.ANDROID_SEND_MODE)) {
                    //设置模式
                    Object date = obj.getDate();
                    SPUtils.put(GizSendRecvService.this, "mode", date.toString());

                    switch (Integer.parseInt(date.toString())) {
                        case 0:

                            runSmartMode();
                            break;
                        case 1:
                            LogUtils.d("app send cmd 2");
                            runSpeedMode();
                            break;
                        case 2:
                            runSleepMode();
                            break;
                        case 3:
                            runConfortMode();
                            break;
                        case 4:
                            runCustomMode();
                            break;

                    }
                } else if ((obj.getCmd() == Constants.ANDROID_SEND_WIND_LEVEL)) {
                    //设置新风量
                    Object date = obj.getDate();
                    int windLevel = Integer.parseInt(date.toString());
                    MachineStatusForMrFrture.Wind_Velocity = (byte) windLevel;
                    MachineStatusForMrFrture.Mode = 4;
                    CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);
                    int[] wind1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL | Constants.ANDROID_SEND_MODE);
                    LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(wind1));
                    EventBus.getDefault().post(new SendDataToMachine(wind1));
                } else if ((obj.getCmd() == Constants.ANDROID_SEND_POWER)) {
                    //设置新风量
                    Object date = obj.getDate();
                    boolean power = Boolean.parseBoolean(date.toString());
                    LogUtils.d("power is " + power);
                    if (power) {
                        long current = System.currentTimeMillis();
                        LogUtils.d("power is open machine " + (current - setPowerTime));
                        if (current - setPowerTime < 6 * 1000) {
                            return;
                        }
                        EventBus.getDefault().post(new OpenMachineEvent());
                    } else {
                        LogUtils.d("power is close machine ");
                        setPowerTime = System.currentTimeMillis();
                        shutDown();
                        FileUtils.writeLogToFile1("app set power off", new byte[]{});
                        Intent dialogIntent = new Intent(getBaseContext(), ShutDownActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(dialogIntent);
                    }
                } else {
                    int[] d = CreateCmdToMachineFactory.createControlCmd(obj.getCmd());
                    EventBus.getDefault().post(new SendDataToMachine(d));
                }
            }
        });
        gizCloudManager.initGizCloud();
        LogUtils.d("gizservice start ");
    }

    @Override
    public void onDestroy() {
        //注销EventBus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(SendDateToGiz data) {
        try {
//            MachineStatusForMrFrture.o3_outdoor = 1;
//            MachineStatusForMrFrture.pm10_outdoor = 2;
//            MachineStatusForMrFrture.pm25_outdoor = 3;
//            MachineStatusForMrFrture.co_outdoor = 4;
//            MachineStatusForMrFrture.aqi_outdoor = 5;
//            MachineStatusForMrFrture.humidity_outdoor = 6;
//            MachineStatusForMrFrture.no2_outdoor = 7;
//            MachineStatusForMrFrture.so2_outdoor = 8;
//            MachineStatusForMrFrture.temp_outdoor = 9;
            gizCloudManager.sendDateToGizCloud(BizCmdConverterForMrFuture.deviceMsgToGizConverter()  /*CommonUtils.chatOrders("0000000327000091040000040000000000000000000000000000000000000000000000000000000000000000")*/);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int lastMode = -1;

    /**
     * 运行智能模式
     */
    private void runSmartMode() {
        if (String.valueOf(MachineStatusForMrFrture.PM25_Indoor) != null) {
            if( MachineStatusForMrFrture.PM25_Indoor >= 75 ){
                MachineStatusForMrFrture.Wind_Velocity = 7;
                MachineStatusForMrFrture.Surge_tank = 4;
            }else if( MachineStatusForMrFrture.CO2_value >= 1500 ){
                MachineStatusForMrFrture.Wind_Velocity = 7;
                MachineStatusForMrFrture.Surge_tank = 4;
            }else if( MachineStatusForMrFrture.PM25_Indoor >= 25 ){
                MachineStatusForMrFrture.Wind_Velocity = 5;
                MachineStatusForMrFrture.Surge_tank = 3;
            }else if( MachineStatusForMrFrture.CO2_value >= 1000 ){
                MachineStatusForMrFrture.Wind_Velocity = 5;
                MachineStatusForMrFrture.Surge_tank = 3;
            }else{
                MachineStatusForMrFrture.Wind_Velocity = 3;
                MachineStatusForMrFrture.Surge_tank = 3;
            }
        } else
            Toast.makeText(GizSendRecvService.this, "PM2.5数据为空", Toast.LENGTH_SHORT).show();
        if (lastMode != 0)
            MachineStatusForMrFrture.Switch_UVC = true;
        MachineStatusForMrFrture.Mode = 0;
        lastMode = MachineStatusForMrFrture.Mode;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行极速模式
     */
    private void runSpeedMode() {
        MachineStatusForMrFrture.Wind_Velocity = 7;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
            LogUtils.d("speed mode1 " + MachineStatusForMrFrture.Switch_UVC);
        }
        MachineStatusForMrFrture.setSpeedmodeTime = System.currentTimeMillis();
        MachineStatusForMrFrture.Mode = 1;
        lastMode = MachineStatusForMrFrture.Mode;
        CommonUtils.setOrder(Constants.ANDROID_SEND_MODE, MachineStatusForMrFrture.Mode);

        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行睡眠模式
     */
    private void runSleepMode() {
        MachineStatusForMrFrture.Wind_Velocity = 0;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 2;
        lastMode = MachineStatusForMrFrture.Mode;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行舒适模式
     */
    private void runConfortMode() {
        MachineStatusForMrFrture.Wind_Velocity = 2;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 3;
        lastMode = MachineStatusForMrFrture.Mode;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行自定义模式
     */
    private void runCustomMode() {
        int level = (Integer) SPUtils.get(this, "wind level", 0);
        MachineStatusForMrFrture.Wind_Velocity = (byte) level;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 4;
        lastMode = MachineStatusForMrFrture.Mode;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    //关闭设备
    private void shutDown() {
        MachineStatusForMrFrture.Switch = false;
        LogUtils.d("power  5 is " + MachineStatusForMrFrture.Switch);
        if (MachineStatusForMrFrture.Switch) {
            MachineStatusForMrFrture.startTime = System.currentTimeMillis();
        }
        CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch ? 1 : 0);

        int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

};



