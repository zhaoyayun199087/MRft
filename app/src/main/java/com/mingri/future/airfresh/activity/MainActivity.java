package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.adapter.MainFragmentAdapter;

import com.mingri.future.airfresh.application.InitApplication;
import com.mingri.future.airfresh.bean.CheckOtaEvent;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.PopupEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.ReceOutDataFromNet;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.SendDateToGiz;

import com.mingri.future.airfresh.bean.SendInitDateEvent;
import com.mingri.future.airfresh.bean.SendUpdateDataToMachine;
import com.mingri.future.airfresh.bean.ShowFilterLife;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.ShowNetWork;
import com.mingri.future.airfresh.bean.UnLockChilrdLock;
import com.mingri.future.airfresh.bean.WifiChangeEvent;
import com.mingri.future.airfresh.fragment.FunctionFragment;
import com.mingri.future.airfresh.fragment.MainFragment;
import com.mingri.future.airfresh.fragment.UVCFragment;
import com.mingri.future.airfresh.network.APIService;
import com.mingri.future.airfresh.network.BaseObersveImp;
import com.mingri.future.airfresh.network.RetrofitFactory;
import com.mingri.future.airfresh.network.RetrofitFactoryForCity;
import com.mingri.future.airfresh.service.GizSendRecvService;
import com.mingri.future.airfresh.service.LightControlService;
import com.mingri.future.airfresh.service.SerialReceSendService;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.util.FileUtil;
import com.mingri.future.airfresh.util.UpdateUtils;
import com.mingri.future.airfresh.view.dialog.CH4AlertDialog;
import com.mingri.future.airfresh.view.dialog.CO2AlertDialog;
import com.mingri.future.airfresh.view.dialog.ChuxiaoLwAlertDialog;
import com.mingri.future.airfresh.view.dialog.GaoxiaoLwAlertDialog;
import com.mingri.future.airfresh.view.dialog.HCHOAlertDialog;
import com.mingri.future.airfresh.view.dialog.HuoxingLwAlertDialog;
import com.mingri.future.airfresh.view.dialog.UvcLwAlertDialog;
import com.mingri.future.airfresh.view.dialog.VocAlertDialog;
import com.mingri.future.airfresh.view.dialog.ZhongxiaoLwAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.AppCmd;
import mingrifuture.gizlib.code.provider.BizCmdConverter;
import mingrifuture.gizlib.code.provider.BizCmdConverterForMrFuture;
import mingrifuture.gizlib.code.provider.GizCloudCallback;
import mingrifuture.gizlib.code.provider.GizCloudManager;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.NetUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;
import mingrifuture.gizlib.code.wifi.WifiAdmin;

public class MainActivity extends FragmentActivity {

    @InjectView(R.id.iv_wifi)
    ImageView ivWifi;
    @InjectView(R.id.iv_clock)
    ImageView ivClock;
    @InjectView(R.id.iv_chirld_lock)
    ImageView ivChirldLock;
    @InjectView(R.id.iv_ptc)
    ImageView ivPtc;
    @InjectView(R.id.iv_uvc)
    ImageView ivUvc;
    @InjectView(R.id.iv_anion)
    ImageView ivAnion;
    @InjectView(R.id.iv_fresh_gear)
    ImageView ivFreshGear;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.vp_main)
    ViewPager vpMain;
    @InjectView(R.id.btn_lock)
    Button btnLock;
    @InjectView(R.id.v_back)
    View vBack;
    private GizCloudManager gizCloudManager;
    private boolean bChirldLock = false;
    private boolean dataStatus = true;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private MainFragmentAdapter mMainFragmentAdapter;
    private FragmentManager mFragmentManager;
    private IntentFilter wifiIntentFilter;    // wifi监听器
    private ScreenBrightnessTool screenBrightnessTool;

    TimerForUVC timerForUVC;    //UVC灯周期性打开
    TimerTask mTimerTask;
    private boolean timeShowUVC = true;    //UVC正显示

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                bChirldLock = false;
                EventBus.getDefault().post(new ChirldLock(false));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        LogUtils.d("dispatch event");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (bChirldLock) {
                startActivityForResult(new Intent(MainActivity.this, ChirldLockActivityEx.class), 1);
                return false;
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ignored) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bBackgound = false;
    }

    private boolean bBackgound = false;

    @Override
    protected void onPause() {
        super.onPause();
        bBackgound = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        System.exit(0);
        timerForUVC.cancel();
        timeShowUVC = true;
        mTimerTask.cancel();
        SPUtils.put(MainActivity.this, "shutswitch", 0);
        dataStatus = false;
    }

    private WifiAdmin wifiAdmin;
    private WifiManager mWifiManager;
    private Handler mHandler = new Handler();

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.d("mainactivity onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        LogUtils.d("mainactivity oncreate");
        initFragment();
        initViewPager();
        screenBrightnessTool = ScreenBrightnessTool.Builder(MainActivity.this);
        Timer mTimer = new Timer();
        mTimerTask = new TimerTimerTask();
        mTimer.schedule(mTimerTask, 100, 1000);

        timerForUVC = new TimerForUVC();
        mTimer.schedule(timerForUVC, 2 * 60 * 1000, 2 * 60 * 60 * 1000);

        findViewById(R.id.btn_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Intent i = new Intent(this, GizSendRecvService.class);
        ComponentName c = startService(i);
        Intent i1 = new Intent(this, SerialReceSendService.class);
        ComponentName c1 = startService(i1);

        LogUtils.d("mainactivity start service  " + c1.getClassName());
        MachineStatusForMrFrture.startTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetUtils.checkOta(MainActivity.this);
                LogUtils.d("check ota result " + result);
            }
        }).start();
        //TODO 测试固件升级
//        Log.e("TAG","进入升级");
//        Intent t=new Intent(this,TestUpdataActivity.class);
//        startActivity(t);
        MachineStatusForMrFrture.soft_version = (int) SPUtils.get(this, "soft_version", 0);
        MachineStatusForMrFrture.hard_version = (int) SPUtils.get(this, "hard_version", 0);
        int iClockStatus = (Integer) SPUtils.get(this, "shutswitch", 0);
        if (iClockStatus == 0) {
        } else if (iClockStatus == 1) {
        } else {
            MachineStatusForMrFrture.Switch_Clock = true;
        }
        wifiAdmin = new WifiAdmin(getApplicationContext());
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        LogUtils.d("connect saved wifi ssid is enable "
                + mWifiManager.isWifiEnabled());

        if (mWifiManager.isWifiEnabled()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connectSavedWifi();
                }
            }).start();
        }

        String mode = (String) SPUtils.get(this, "mode", "0");
        MachineStatusForMrFrture.Mode = Byte.parseByte(mode);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!MachineStatusForMrFrture.bRecvSerialDate) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("p2ower is " + MachineStatusForMrFrture.Switch);

                        if (!MachineStatusForMrFrture.Switch) {
                            if (isFirst) {
                                LogUtils.d("start welcomeactivity");
                                MachineStatusForMrFrture.iCount = 100;
                                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                isFirst = false;
                            }
                        } else {
                            isFirst = false;
                            LogUtils.d("set black 1");
                            vBack.setVisibility(View.GONE);
                            int angle = (int) SPUtils.get(MainActivity.this, "light", 40);
                            screenBrightnessTool.setBrightness1(angle);
                            screenBrightnessTool.setBrightness1(angle);
                            screenBrightnessTool.setBrightness1(angle);
                        }
                    }
                });
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i2 = new Intent(MainActivity.this, LightControlService.class);
                        ComponentName c2 = startService(i2);
                        LogUtils.d("set black 2");
                        vBack.setVisibility(View.GONE);
                    }
                }, 1500);
            }
        }).start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    vBack.setVisibility(View.GONE);
                    int angle = (int) SPUtils.get(MainActivity.this, "light", 40);
                    screenBrightnessTool.setBrightness1(angle);
                    screenBrightnessTool.setBrightness1(angle);
                    screenBrightnessTool.setBrightness1(angle);
                    Intent i2 = new Intent(MainActivity.this, LightControlService.class);
                    ComponentName c2 = startService(i2);
                }
            }
        }, 5000);
    }

    private boolean isFirst = true;

    /**
     * 连接保存过的wifi
     */
    private void connectSavedWifi() {
        String ssid = (String) SPUtils.get(this, "ssid", "");
        String password = (String) SPUtils.get(this, "pwd", "");
        int type = (int) SPUtils.get(this, "wifiType", 0);
        LogUtils.d("connect saved wifi ssid is " + ssid);
        LogUtils.d("connect saved wifi ssid weather active "
                + NetUtils.isWiFiActive(this));
        if (!NetUtils.isWiFiActive(this) && !ssid.equals("")) {
//            List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
            List<ScanResult> wifis = mWifiManager.getScanResults();
            LogUtils.d("connect saved wifi 2");
            for (ScanResult item : wifis) {
                LogUtils.d("connect saved wifi item " + item.SSID + " saveed ssid " + ssid);

                if (item.SSID.equals(ssid)) {
                    LogUtils.d("connect saved wifi item begin connect");
                    wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(ssid.trim(), password.toString().trim(), type));
                    break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        LogUtils.d("key is " + keyCode + " event " + event.toString());

        if (event.getRepeatCount() == 25) {
            startActivity(new Intent(this, LockTest.class));
        }

        //新装机器，在主页按住按键5秒钟之后，才算打开新风阀门的控制功能
        if (event.getRepeatCount() == 60) {
            SPUtils.put(MainActivity.this, "is_install", true);
        }

        //如果在未关机状态下，并且按下去的时间低于两秒，那么亮屏
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getRepeatCount() < 25) {
                if (MachineStatusForMrFrture.Switch) {
                    MachineStatusForMrFrture.iCount = 0;
                }
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

    /**
     * 开机初始化设置
     * 智能模式，五档
     */
    private void initMachineSetting() {
        MachineStatusForMrFrture.bVOCPopup = false;
        MachineStatusForMrFrture.bCh4Popup = false;
        MachineStatusForMrFrture.bCo2Popup = false;
        MachineStatusForMrFrture.bHchoPopup = false;
        MachineStatusForMrFrture.bSmartControl = true;

        MachineStatusForMrFrture.Switch = true;
        MachineStatusForMrFrture.Mode = 0;
        MachineStatusForMrFrture.Wind_Velocity = 2;
        MachineStatusForMrFrture.Switch_UVC = true;

        if ((Boolean) SPUtils.get(MainActivity.this, "is install", false)) {
            MachineStatusForMrFrture.Switch_Valve = true;
            MachineStatusForMrFrture.Surge_tank = 2;
            int[] date = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL | Constants.ANDROID_SEND_SWITCH_FRESH | Constants.ANDROID_SEND_SURGE_TANK);
            EventBus.getDefault().post(new SendDataToMachine(date));
        } else {
            int[] date = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL);
            EventBus.getDefault().post(new SendDataToMachine(date));
        }

//        EventBus.getDefault().post(new PopupEvent(7));
//        EventBus.getDefault().post(new PopupEvent(8));
    }

    private void initViewPager() {
        mFragmentManager = getSupportFragmentManager();
        mMainFragmentAdapter = new MainFragmentAdapter(mFragmentManager, mFragmentList);
        vpMain.setAdapter(mMainFragmentAdapter);
        vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("onpageselected " + position);
//                if (position == 1) {
//                    rlTitle.setBackgroundResource((R.color.all_background_color));
//                } else {
//                    rlTitle.setBackgroundResource((R.color.main_bg));
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        MainFragment mf = new MainFragment();
        FunctionFragment ff = new FunctionFragment();
        mFragmentList.add(mf);
        mFragmentList.add(ff);
    }


    private boolean bReadCmdFlag = false;

    private class TimerTimerTask extends TimerTask {
        @Override
        public void run() {
            //未连接设备时调试使用
//            if( true ){
//                EventBus.getDefault().post(new SendDateToGiz());
//                return;
//            }


            //进入升级模式，停止一切收发数据
            if (MachineStatusForMrFrture.bUpdating) {
                return;
            }

            //判断弹窗及智能是否进入智能控制
            judgePopup();

            if (MachineStatusForMrFrture.Mode == 1) {
                long current = System.currentTimeMillis();
                if (current - MachineStatusForMrFrture.setSpeedmodeTime > 70 * 60 * 1000) {
                    runSmartMode();
                    return;
                }
            }

            if (MachineStatusForMrFrture.Mode == 0) {
                bReadCmdFlag = !bReadCmdFlag;
                if (bReadCmdFlag) {
                    long current = System.currentTimeMillis();
                    if (current - MachineStatusForMrFrture.startTime > 10 * 1000) {
                        runSmartMode();
                    }
                } else {

                    /**********2017-11-8 增加 暂时不要智能控制*************/
                    MachineStatusForMrFrture.bSmartControl = false;
                    /**********2017-11-8 增加 暂时不要智能控制*************/

                    if (MachineStatusForMrFrture.bSmartControl) {
                        runSmartControl();
                    } else {
                        LogUtils.d("runReadCmd");
                        EventBus.getDefault().post(new SendDataToMachine(null));
                    }
                }
            } else {
                LogUtils.d("runReadCmd");
                EventBus.getDefault().post(new SendDataToMachine(null));

            }
//            try {
//                gizCloudManager.sendDateToGizCloud(BizCmdConverterForMrFuture.deviceMsgToGizConverter()  /*CommonUtils.chatOrders("0000000327000091040000040000000000000000000000000000000000000000000000000000000000000000")*/);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //判断是否需要自动关机
            judgeShutDown();

            getNetWeather();
        }

    }

    /**
     * 从网络上获取室外天气数据
     *
     */
    private int iCount = 0;
    private void getNetWeather() {

        if( !MachineStatusForMrFrture.bUpdateOutDate ){
            iCount++;
            if( iCount > 10 ){
                iCount = 0;
                MachineStatusForMrFrture.bUpdateOutDate = true;
            }
        }

        if( MachineStatusForMrFrture.bUpdateOutDate ){
            MachineStatusForMrFrture.bUpdateOutDate = false;
            getCityId();
        }
    }

    private void getCityId() {
        String city = (String) SPUtils.get(this, "city_name", "");
        Map<String, String> req = new HashMap<>();
        req.put("location", city);
        req.put("key", "1958a289a0a64f5fae20b3909eaf8ab6");
        JSONObject jsonObject = new JSONObject(req);
        RetrofitFactoryForCity.getInstance(this)
                .createService(APIService.class)
                .getCityId(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObersveImp(new BaseObersveImp.NetCallback() {

                    @Override
                    public void Onfailed(Throwable e) {

                    }

                    @Override
                    public void onSucces(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if(code == 200){
                                JSONArray jsonElements = jsonObject.getJSONArray("location");
                                if( jsonElements != null  && jsonElements.length() > 0){
                                    getWeather(jsonElements.getJSONObject(0).getString("id"));
                                    getAqiDate(jsonElements.getJSONObject(0).getString("id"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));

    }

    private void getWeather(String id) {

        Map<String, String> req = new HashMap<>();
        req.put("location", id);
        req.put("key", "1958a289a0a64f5fae20b3909eaf8ab6");
        JSONObject jsonObject = new JSONObject(req);
        RetrofitFactory.getInstance(this)
                .createService(APIService.class)
                .getWeatherDate(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObersveImp(new BaseObersveImp.NetCallback() {
                    @Override
                    public void Onfailed(Throwable e) {

                    }

                    @Override
                    public void onSucces(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if(code == 200){
                                JSONObject jsonElements = jsonObject.getJSONObject("now");
                                if( jsonElements != null ){
                                    MachineStatusForMrFrture.humidity_outdoor = (byte) jsonElements.getInt("humidity");
                                    MachineStatusForMrFrture.temp_outdoor = (byte) jsonElements.getInt("temp");
                                    LogUtils.d("temp_outdoor outdoor is " + MachineStatusForMrFrture.temp_outdoor + "  " + jsonElements.getInt("temp") + "  " + (byte) jsonElements.getInt("temp"));

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }));

    }

    private void getAqiDate(String id) {

        Map<String, String> req = new HashMap<>();
        req.put("location", id);
        req.put("key", "1958a289a0a64f5fae20b3909eaf8ab6");
        JSONObject jsonObject = new JSONObject(req);
        RetrofitFactory.getInstance(this)
                .createService(APIService.class)
                .getAirQulity(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new BaseObersveImp(new BaseObersveImp.NetCallback() {
                    @Override
                    public void Onfailed(Throwable e) {

                    }

                    @Override
                    public void onSucces(JSONObject jsonObject) {
                        try {
                            int code = jsonObject.getInt("code");
                            if(code == 200){
                                JSONObject jsonElements = jsonObject.getJSONObject("now");
                                if( jsonElements != null ){
                                    MachineStatusForMrFrture.bOutDateEnable = true;
                                    EventBus.getDefault().post(new ReceOutDataFromNet());
                                    MachineStatusForMrFrture.aqi_outdoor = jsonElements.getInt("aqi");
                                    MachineStatusForMrFrture.pm25_outdoor = jsonElements.getInt("pm2p5");
                                    MachineStatusForMrFrture.pm10_outdoor = jsonElements.getInt("pm10");
                                    MachineStatusForMrFrture.o3_outdoor = jsonElements.getInt("o3");
                                    MachineStatusForMrFrture.co_outdoor = (byte) jsonElements.getInt("co") ;
                                    MachineStatusForMrFrture.no2_outdoor = jsonElements.getInt("no2");
                                    MachineStatusForMrFrture.so2_outdoor =  jsonElements.getInt("so2");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));

    }


    private void judgeShutDown() {
        if ((Integer) SPUtils.get(MainActivity.this, "shutswitch", 0) != 2) {
            return;
        }
        String sSettime = (String) SPUtils.get(MainActivity.this, "setshuttime", "0");
        long settime = Long.parseLong(sSettime);
        int shuttime = (int) SPUtils.get(MainActivity.this, "shuttime", 0);
        long current = System.currentTimeMillis();
        long shut = settime + shuttime * 1000 * 60 * 60;

        FileUtils.writeLogToFile1("shut time is " + shut + " current time is " + current + "  " + ((shut - current) / 1000), new byte[]{});
        LogUtils.d("shut time is " + shut + " current time is " + current + "  " + ((shut - current) / 1000));
        if ((shut - current) < 5 * 1000 && (shut - current) > 0) {
            MachineStatusForMrFrture.Switch = false;
            LogUtils.d("power  2 is " + MachineStatusForMrFrture.Switch);
            MachineStatusForMrFrture.Switch_Clock = false;
            CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch ? 1 : 0);
            int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
            LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
            EventBus.getDefault().post(new SendDataToMachine(d));
            FileUtils.writeLogToFile1("shut down time is coming", new byte[]{});
            startActivity(new Intent(MainActivity.this, ShutDownActivity.class));
        }
    }


    private int lastChuxiaoLv = -1, lastZhongxiaoLv = -1, lastGaoxiaoLv = -1, lastHuoxingLv = -1, lastUvcLife = -1;

    private void judgePopup() {

        if (!MachineStatusForMrFrture.bRecvSerialDate) {
            LogUtils.d("not receve serial date");
            return;
        }

        LogUtils.d("alert co2");
        if (!MachineStatusForMrFrture.Alert_CO2) {
            MachineStatusForMrFrture.bCo2Popup = false;
        } else {
            if (MachineStatusForMrFrture.Mode == 0) {
                if (!MachineStatusForMrFrture.bCo2Popup) {
                    MachineStatusForMrFrture.bCo2Popup = true;
                    MachineStatusForMrFrture.bSmartControl = true;
                    EventBus.getDefault().post(new PopupEvent(1));
                }
            }
        }
        if (!MachineStatusForMrFrture.Alert_HCHO) {
            MachineStatusForMrFrture.bHchoPopup = false;
        } else {
            if (MachineStatusForMrFrture.Mode == 0) {
                if (!MachineStatusForMrFrture.bHchoPopup) {
                    MachineStatusForMrFrture.bHchoPopup = true;
                    MachineStatusForMrFrture.bSmartControl = true;
                    EventBus.getDefault().post(new PopupEvent(3));
                }
            }
        }
        if (!MachineStatusForMrFrture.Alert_CH4) {
            MachineStatusForMrFrture.bCh4Popup = false;
        } else {
            if (MachineStatusForMrFrture.Mode == 0) {
                if (!MachineStatusForMrFrture.bCh4Popup) {
                    MachineStatusForMrFrture.bCh4Popup = true;
                    MachineStatusForMrFrture.bSmartControl = true;
                    EventBus.getDefault().post(new PopupEvent(2));
                }
            }
        }
        if (!MachineStatusForMrFrture.Alert_VOC) {
            MachineStatusForMrFrture.bVOCPopup = false;
        } else {
            if (!MachineStatusForMrFrture.bVOCPopup) {
                MachineStatusForMrFrture.bVOCPopup = true;
                EventBus.getDefault().post(new PopupEvent(4));
            }
        }

        if (MachineStatusForMrFrture.Filter_Life1 > 10) {
            MachineStatusForMrFrture.bChuDiag = false;
        } else {
            if (!MachineStatusForMrFrture.bChuDiag) {
                EventBus.getDefault().post(new PopupEvent(5));
                MachineStatusForMrFrture.bChuDiag = true;
                InitApplication.JumpZeroIndex = 4;
            }
        }
        if (MachineStatusForMrFrture.Filter_Life2 > 10) {
            MachineStatusForMrFrture.bZhongDiag = false;
        } else {
            if (!MachineStatusForMrFrture.bZhongDiag) {
                EventBus.getDefault().post(new PopupEvent(6));
                MachineStatusForMrFrture.bZhongDiag = true;
                InitApplication.JumpZeroIndex = 0;
            }
        }
        if (MachineStatusForMrFrture.Filter_Life3 > 10) {
            MachineStatusForMrFrture.bHuoDiag = false;
        } else {
            if (!MachineStatusForMrFrture.bHuoDiag) {
                EventBus.getDefault().post(new PopupEvent(7));
                MachineStatusForMrFrture.bHuoDiag = true;
                InitApplication.JumpZeroIndex = 1;
            }
        }
        if (MachineStatusForMrFrture.Filter_Life4 > 10) {
            MachineStatusForMrFrture.bGaoDiag = false;
        } else {
            if (!MachineStatusForMrFrture.bGaoDiag) {
                EventBus.getDefault().post(new PopupEvent(8));
                MachineStatusForMrFrture.bGaoDiag = true;
                InitApplication.JumpZeroIndex = 2;
            }
        }
        if (MachineStatusForMrFrture.UVC_Life > 10) {
            MachineStatusForMrFrture.bUvcDiag = false;
        } else {
            if (!MachineStatusForMrFrture.bUvcDiag) {
                EventBus.getDefault().post(new PopupEvent(9));
                MachineStatusForMrFrture.bUvcDiag = true;
                InitApplication.JumpZeroIndex = 3;
            }
        }
    }

    /**
     * 运行智能模式
     */
    private void runSmartMode() {
        LogUtils.d("runSmartMode");
        if (dataStatus) {
            if (MachineStatusForMrFrture.PM25_Indoor >= 0 && MachineStatusForMrFrture.PM25_Indoor <= 15)
                MachineStatusForMrFrture.Wind_Velocity = 2;
            if (MachineStatusForMrFrture.PM25_Indoor > 15 && MachineStatusForMrFrture.PM25_Indoor <= 50)
                MachineStatusForMrFrture.Wind_Velocity = 4;
            if (MachineStatusForMrFrture.PM25_Indoor > 50 && MachineStatusForMrFrture.PM25_Indoor <= 80)
                MachineStatusForMrFrture.Wind_Velocity = 6;
            if (MachineStatusForMrFrture.PM25_Indoor > 80)
                MachineStatusForMrFrture.Wind_Velocity = 8;
            dataStatus = false;
        } else {
            if (String.valueOf(MachineStatusForMrFrture.PM25_Indoor) != null) {
                if (MachineStatusForMrFrture.PM25_Indoor >= 0 && MachineStatusForMrFrture.PM25_Indoor <= 10)
                    MachineStatusForMrFrture.Wind_Velocity = 2;
                if (MachineStatusForMrFrture.PM25_Indoor > 10 && MachineStatusForMrFrture.PM25_Indoor <= 35)
                    MachineStatusForMrFrture.Wind_Velocity = 4;
                if (MachineStatusForMrFrture.PM25_Indoor > 35 && MachineStatusForMrFrture.PM25_Indoor <= 60)
                    MachineStatusForMrFrture.Wind_Velocity = 6;
                if (MachineStatusForMrFrture.PM25_Indoor > 60)
                    MachineStatusForMrFrture.Wind_Velocity = 8;
            } else
                Toast.makeText(MainActivity.this, "PM2.5数据为空", Toast.LENGTH_SHORT).show();
        }
        MachineStatusForMrFrture.Mode = 0;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL);  // | Constants.ANDROID_SEND_SWITCH_UVC
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行智能控制
     */
    private void runSmartControl() {

        if (!(Boolean) SPUtils.get(MainActivity.this, "is install", false)) {
            return;
        }

        LogUtils.d("runSmartControl");
        //开机后两分钟之内不进行智能控制，调压仓0档，风阀关，两分钟后智能控制才介入
        long current = System.currentTimeMillis();
        if (current - MachineStatusForMrFrture.startTime > 2 * 60 * 1000) {
            if (MachineStatusForMrFrture.HCHO_Quality > 200 || MachineStatusForMrFrture.Alert_CH4 || MachineStatusForMrFrture.CO2_value > 1000) {
                MachineStatusForMrFrture.Switch_Valve = true;
            } else {
                MachineStatusForMrFrture.Switch_Valve = false;
            }
            if (MachineStatusForMrFrture.HCHO_Quality > 200) {
                MachineStatusForMrFrture.Surge_tank = 0;
            } else {
                MachineStatusForMrFrture.Surge_tank = 5;
            }
        } else {
            MachineStatusForMrFrture.Switch_Valve = false;
            MachineStatusForMrFrture.Surge_tank = 0;
        }
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_FRESH | Constants.ANDROID_SEND_SURGE_TANK);
        EventBus.getDefault().post(new SendDataToMachine(d));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final ShowMainPage data) {
        vpMain.arrowScroll(View.FOCUS_LEFT);
        vpMain.arrowScroll(View.FOCUS_LEFT);

        if (data.getType() == 1) {
            bChirldLock = true;
        } else {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final ShowNetWork data) {
        vpMain.arrowScroll(View.FOCUS_RIGHT);
        vpMain.arrowScroll(View.FOCUS_RIGHT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final ShowFilterLife data) {
        vpMain.arrowScroll(View.FOCUS_RIGHT);
        vpMain.arrowScroll(View.FOCUS_RIGHT);
    }

    private CO2AlertDialog co2Diag;
    private CH4AlertDialog ch4Diag;
    private HCHOAlertDialog hchoDiag;
    private VocAlertDialog vocDiag;
    private ChuxiaoLwAlertDialog chuDiag;
    private ZhongxiaoLwAlertDialog zhongDiag;
    private HuoxingLwAlertDialog huoDiag;
    private UvcLwAlertDialog uvcDiag;
    private GaoxiaoLwAlertDialog gaoDiag;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(PopupEvent data) {


        if (bBackgound) {
            return;
        }

        if (!CommonUtils.isTopActivity(MainActivity.this, "com.mingri.future.airfresh.activity.MainActivity")) {
            return;
        }

        /**
         * 1  co2
         * 2  ch4
         * 3  hcho
         * 4  voc
         * 5 初效滤网
         * 6 中效滤网
         * 7 活性滤网
         * 8 高效滤网
         * 9 uvc寿命
         */
        if (data.getType() == 1) {
            if (co2Diag == null || !co2Diag.isShowing()) {
                co2Diag = new CO2AlertDialog(MainActivity.this, new CO2AlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                    }
                });
                co2Diag.show();
            }
        } else if (data.getType() == 2) {
            if (ch4Diag == null || !ch4Diag.isShowing()) {
                ch4Diag = new CH4AlertDialog(MainActivity.this, new CH4AlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                    }
                });
                ch4Diag.show();
            }
        } else if (data.getType() == 3) {
            if (hchoDiag == null || !hchoDiag.isShowing()) {
                hchoDiag = new HCHOAlertDialog(MainActivity.this, new HCHOAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                hchoDiag.show();
            }
        } else if (data.getType() == 4) {
            if (vocDiag == null || !vocDiag.isShowing()) {
                vocDiag = new VocAlertDialog(MainActivity.this, new VocAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                    }
                });
                vocDiag.show();
            }
        } else if (data.getType() == 5) {
            if (chuDiag == null || !chuDiag.isShowing()) {
                chuDiag = new ChuxiaoLwAlertDialog(MainActivity.this, new ChuxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                chuDiag.setPercent(MachineStatusForMrFrture.Filter_Life1);
                chuDiag.show();
            }
        } else if (data.getType() == 6) {
            if (zhongDiag == null || !zhongDiag.isShowing()) {
                zhongDiag = new ZhongxiaoLwAlertDialog(MainActivity.this, new ZhongxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                zhongDiag.setPercent(MachineStatusForMrFrture.Filter_Life2);
                zhongDiag.show();
            }
        } else if (data.getType() == 7) {

            if (huoDiag == null || !huoDiag.isShowing()) {
                huoDiag = new HuoxingLwAlertDialog(MainActivity.this, new HuoxingLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                huoDiag.setPercent(MachineStatusForMrFrture.Filter_Life3);
                huoDiag.show();
            }
        } else if (data.getType() == 8) {
            if (gaoDiag == null || !gaoDiag.isShowing()) {
                gaoDiag = new GaoxiaoLwAlertDialog(MainActivity.this, new GaoxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                gaoDiag.setPercent(MachineStatusForMrFrture.Filter_Life4);
                gaoDiag.show();
            }
        } else if (data.getType() == 9) {
            if (uvcDiag == null || !uvcDiag.isShowing()) {
                uvcDiag = new UvcLwAlertDialog(MainActivity.this, new UvcLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());

                    }
                });
                uvcDiag.setPercent(MachineStatusForMrFrture.UVC_Life);
                uvcDiag.show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(WifiChangeEvent data) {
        int level = data.getLevel();
        boolean conn = data.isConn();
        LogUtils.d("wifi change " + level + " " + conn);
        if (!conn) {
            ivWifi.setImageResource(R.mipmap.sta_icon_wifi0);
        } else {
            if (level > -50) {
                ivWifi.setImageResource(R.mipmap.sta_icon_wifi4);
            } else if (level > -60) {
                ivWifi.setImageResource(R.mipmap.sta_icon_wifi3);
            } else if (level > -80) {
                ivWifi.setImageResource(R.mipmap.sta_icon_wifi2);
            } else if (level > -90) {
                ivWifi.setImageResource(R.mipmap.sta_icon_wifi1);
            } else {
                ivWifi.setImageResource(R.mipmap.sta_icon_wifi0);
            }
            EventBus.getDefault().post(new CheckOtaEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        setStatusBar();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final UnLockChilrdLock data) {
        bChirldLock = false;
        EventBus.getDefault().post(new ChirldLock(false));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(CheckOtaEvent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = NetUtils.checkOta(MainActivity.this);
                LogUtils.d("check ota result " + result);
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(SendInitDateEvent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initMachineSetting();
            }
        }).start();
    }

    private void setStatusBar() {
        LogUtils.d("Switch_Plasma1 " + MachineStatusForMrFrture.Switch_Plasma1);
        if (MachineStatusForMrFrture.Switch_Valve) {
            ivFreshGear.setVisibility(View.VISIBLE);
        } else {
            ivFreshGear.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Switch_Plasma1) {
            ivAnion.setVisibility(View.VISIBLE);
        } else {
            ivAnion.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Switch_UVC) {
            ivUvc.setVisibility(View.VISIBLE);
        } else {
            ivUvc.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Switch_PTC) {
            ivPtc.setVisibility(View.VISIBLE);
        } else {
            ivPtc.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Child_Security_Lock) {
            ivChirldLock.setVisibility(View.VISIBLE);
        } else {
            ivChirldLock.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Switch_Clock) {
            ivClock.setVisibility(View.VISIBLE);
        } else {
            ivClock.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.Switch_Wifi) {
            ivWifi.setVisibility(View.VISIBLE);
        } else {
            ivWifi.setVisibility(View.GONE);
        }
    }

    //UVC灯打开后过2h关闭，关闭过2h再自动打开
    private class TimerForUVC extends TimerTask {

        @Override
        public void run() {
            if (timeShowUVC) {
                MachineStatusForMrFrture.Switch_UVC = true;
                timeShowUVC = false;
            } else {
                MachineStatusForMrFrture.Switch_UVC = false;
                timeShowUVC = true;
            }
        }
    }

    // 每隔一段时间检测数据是否正常，数据不正常时，重开设备
    private class DataNormal extends TimerTask {

        @Override
        public void run() {
//            if (MachineStatusForMrFrture.PM25_Indoor){
//
//            }
        }
    }
}
