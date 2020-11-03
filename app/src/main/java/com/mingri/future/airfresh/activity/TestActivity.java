package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.PopupEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.service.GizSendRecvService;
import com.mingri.future.airfresh.service.SerialReceSendService;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.util.UpdateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.Utils;

public class TestActivity extends Activity {
    @InjectView(R.id.sw_switch)
    Switch swSwitch;
    @InjectView(R.id.sw_plastic)
    Switch swPlastic;
    @InjectView(R.id.sw_chirld)
    Switch swChirld;
    @InjectView(R.id.sw_uvc)
    Switch swUvc;
    @InjectView(R.id.sw_ptc)
    Switch swPtc;
    @InjectView(R.id.sw_electric)
    Switch swElectric;
    @InjectView(R.id.sw_surk)
    Switch swSurk;
    @InjectView(R.id.btn_wind)
    Button btnWind;
    @InjectView(R.id.btn_mode)
    Button btnMode;
    @InjectView(R.id.btn_surk)
    Button btnSurk;
    @InjectView(R.id.btn_read)
    Button btnRead;
    @InjectView(R.id.ll1)
    LinearLayout ll1;
    @InjectView(R.id.btn_surk_dec)
    ImageButton btnSurkDec;
    @InjectView(R.id.tv_surk)
    TextView tvSurk;
    @InjectView(R.id.btn_surk_add)
    ImageButton btnSurkAdd;
    @InjectView(R.id.btn_Fresh_dec)
    ImageButton btnFreshDec;
    @InjectView(R.id.tv_fresh)
    TextView tvFresh;
    @InjectView(R.id.btn_Fresh_add)
    ImageButton btnFreshAdd;
    @InjectView(R.id.btn_mode_dec)
    ImageButton btnModeDec;
    @InjectView(R.id.tv_mode)
    TextView tvMode;
    @InjectView(R.id.btn_mode_add)
    ImageButton btnModeAdd;
    @InjectView(R.id.btn_uvc_dec)
    ImageButton btnUvcDec;
    @InjectView(R.id.tv_uvc)
    TextView tvUvc;
    @InjectView(R.id.btn_uvc_add)
    ImageButton btnUvcAdd;
    @InjectView(R.id.btn_dec_voc)
    ImageButton btnDecVoc;
    @InjectView(R.id.tv_voc)
    TextView tvVoc;
    @InjectView(R.id.btn_voc_add)
    ImageButton btnVocAdd;
    @InjectView(R.id.btn_humidity_dec)
    ImageButton btnHumidityDec;
    @InjectView(R.id.tv_humidity)
    TextView tvHumidity;
    @InjectView(R.id.btn_humidity_add)
    ImageButton btnHumidityAdd;
    @InjectView(R.id.btn_temp_dec)
    ImageButton btnTempDec;
    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.btn_temp_add)
    ImageButton btnTempAdd;
    @InjectView(R.id.btn_lv1_dec)
    ImageButton btnLv1Dec;
    @InjectView(R.id.tv_lv1)
    TextView tvLv1;
    @InjectView(R.id.btn_lv1_add)
    ImageButton btnLv1Add;
    @InjectView(R.id.btn_lv2_dec)
    ImageButton btnLv2Dec;
    @InjectView(R.id.tv_lv2)
    TextView tvLv2;
    @InjectView(R.id.btn_lv2_add)
    ImageButton btnLv2Add;
    @InjectView(R.id.btn_lv3_dec)
    ImageButton btnLv3Dec;
    @InjectView(R.id.tv_lv3)
    TextView tvLv3;
    @InjectView(R.id.btn_lv3_add)
    ImageButton btnLv3Add;
    @InjectView(R.id.btn_lv4_dec)
    ImageButton btnLv4Dec;
    @InjectView(R.id.tv_lv4)
    TextView tvLv4;
    @InjectView(R.id.btn_lv4_add)
    ImageButton btnLv4Add;
    @InjectView(R.id.tv_pm)
    TextView tvPm;
    @InjectView(R.id.tv_hcho)
    TextView tvHcho;
    @InjectView(R.id.tv_co)
    TextView tvCo;
    @InjectView(R.id.iv_asert_air)
    ImageView ivAsertAir;
    @InjectView(R.id.iv_asert_lv1)
    ImageView ivAsertLv1;
    @InjectView(R.id.iv_asert_lv2)
    ImageView ivAsertLv2;
    @InjectView(R.id.iv_asert_lv3)
    ImageView ivAsertLv3;
    @InjectView(R.id.iv_asert_lv4)
    ImageView ivAsertLv4;
    @InjectView(R.id.iv_asert_uvc)
    ImageView ivAsertUvc;
    @InjectView(R.id.iv_asert_hcho)
    ImageView ivAsertHcho;
    @InjectView(R.id.iv_asert_co)
    ImageView ivAsertCo;
    @InjectView(R.id.iv_asert_ch)
    ImageView ivAsertCh;
    @InjectView(R.id.iv_asert_voc)
    ImageView ivAsertVoc;
    @InjectView(R.id.iv_fault_engin)
    ImageView ivFaultEngin;
    @InjectView(R.id.iv_falut_co)
    ImageView ivFalutCo;
    @InjectView(R.id.iv_falut_pm)
    ImageView ivFalutPm;
    @InjectView(R.id.iv_falut_voc)
    ImageView ivFalutVoc;
    @InjectView(R.id.iv_falut_hcho)
    ImageView ivFalutHcho;
    @InjectView(R.id.iv_falut_ch)
    ImageView ivFalutCh;
    @InjectView(R.id.iv_falut_temp)
    ImageView ivFalutTemp;
    @InjectView(R.id.iv_falut_humidity)
    ImageView ivFalutHumidity;
    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.tv_ch4)
    TextView tvCh4;
    @InjectView(R.id.btn_shutdown_add)
    ImageButton btnShutAdd;
    @InjectView(R.id.btn_shutdown_dec)
    ImageButton btnShutDec;
    @InjectView(R.id.tv_shutdown)
    TextView tvShutDown;

    private long setSpeedmodeTime;
    private int lastMode;
    private boolean bNeedUpdate = true;
    private int iTimeCount = 0;
    private Thread mMonitorUserOprate = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                //如果30秒用户没操作，那么更新
                if (!bNeedUpdate) {
                    iTimeCount++;
                    if (iTimeCount > 5) {
                        iTimeCount = 0;
                        bNeedUpdate = true;
                    }
                } else {
                    iTimeCount = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);


        initSwitch();
        initDate();
        initFault();

        initMachineSetting();

        UpdateUtils.pgyCheckUpdate(this, false);
        tvVersion.setText(Utils.getVersion(this));
        lastMode = MachineStatusForMrFrture.Mode;
        mMonitorUserOprate.start();

        getScreenInfo();
        Button exit = (Button) findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getScreenInfo() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        float density = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        LogUtils.d("screen info density " + density + " dpi " + densityDPI + " x " + getWindowManager().getDefaultDisplay().getWidth() + " y  " + getWindowManager().getDefaultDisplay().getHeight());
    }

    /*
        设置需要更新从设备发过来的新风排风档位
         */
    private void setNeedUpdate() {
        bNeedUpdate = true;
        iTimeCount = 0;
    }

    /*
    设置不需要更新从设备发过来的新风排风档位
    */
    private void setNotNeedUpdate() {
        bNeedUpdate = false;
        iTimeCount = 0;
    }

    /**
     * 开机初始化设置
     * 智能模式，五档
     */
    private void initMachineSetting() {
        MachineStatusForMrFrture.bCh4Popup = false;
        MachineStatusForMrFrture.bCo2Popup = false;
        MachineStatusForMrFrture.bHchoPopup = false;
        MachineStatusForMrFrture.bSmartControl = true;

        MachineStatusForMrFrture.Mode = 0;
        MachineStatusForMrFrture.Wind_Velocity = 4;
        MachineStatusForMrFrture.Switch_UVC = true;
        int[] date = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(date));
    }

    /**
     * 运行智能模式
     */
    private void runSmartMode() {
        if (String.valueOf(MachineStatusForMrFrture.PM25_Indoor) != null) {
            if (MachineStatusForMrFrture.PM25_Indoor >= 0 && MachineStatusForMrFrture.PM25_Indoor <= 15)
                MachineStatusForMrFrture.Wind_Velocity = 2;
            if (MachineStatusForMrFrture.PM25_Indoor > 15 && MachineStatusForMrFrture.PM25_Indoor <= 50)
                MachineStatusForMrFrture.Wind_Velocity = 4;
            if (MachineStatusForMrFrture.PM25_Indoor > 50 && MachineStatusForMrFrture.PM25_Indoor <= 80)
                MachineStatusForMrFrture.Wind_Velocity = 6;
            if (MachineStatusForMrFrture.PM25_Indoor > 80)
                MachineStatusForMrFrture.Wind_Velocity = 8;
        } else
            Toast.makeText(TestActivity.this, "PM2.5数据为空", Toast.LENGTH_SHORT).show();
        MachineStatusForMrFrture.Switch_UVC = true;
        MachineStatusForMrFrture.Mode = 0;
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行极速模式
     */
    private void runSpeedMode() {
        MachineStatusForMrFrture.Wind_Velocity = 7;
        LogUtils.d("speed mode " + lastMode);
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
            LogUtils.d("speed mode1 " + MachineStatusForMrFrture.Switch_UVC);
        }
        MachineStatusForMrFrture.Mode = 1;
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
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    private void initFault() {
        if (MachineStatusForMrFrture.Alert_Air_Quality) {
            ivAsertAir.setVisibility(View.VISIBLE);
        } else {
            ivAsertAir.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_Filter_Life1) {
            ivAsertLv1.setVisibility(View.VISIBLE);
        } else {
            ivAsertLv1.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_Filter_Life2) {
            ivAsertLv2.setVisibility(View.VISIBLE);
        } else {
            ivAsertLv2.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_Filter_Life3) {
            ivAsertLv3.setVisibility(View.VISIBLE);
        } else {
            ivAsertLv3.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_Filter_Life4) {
            ivAsertLv4.setVisibility(View.VISIBLE);
        } else {
            ivAsertLv4.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_UVC_Life) {
            ivAsertUvc.setVisibility(View.VISIBLE);
        } else {
            ivAsertUvc.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_HCHO) {
            ivAsertHcho.setVisibility(View.VISIBLE);
        } else {
            ivAsertHcho.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_CO2) {
            ivAsertCo.setVisibility(View.VISIBLE);
        } else {
            ivAsertCo.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_CH4) {
            ivAsertCh.setVisibility(View.VISIBLE);
        } else {
            ivAsertCh.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Alert_VOC) {
            ivAsertVoc.setVisibility(View.VISIBLE);
        } else {
            ivAsertVoc.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.Fault_Motor) {
            ivFaultEngin.setVisibility(View.VISIBLE);
        } else {
            ivFaultEngin.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_CO2) {
            ivFalutCo.setVisibility(View.VISIBLE);
        } else {
            ivFalutCo.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_PM25) {
            ivFalutPm.setVisibility(View.VISIBLE);
        } else {
            ivFalutPm.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_VOC) {
            ivFalutVoc.setVisibility(View.VISIBLE);
        } else {
            ivFalutVoc.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_HCHO) {
            ivFalutHcho.setVisibility(View.VISIBLE);
        } else {
            ivFalutHcho.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_CH4) {
            ivFalutCh.setVisibility(View.VISIBLE);
        } else {
            ivFalutCh.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_tmp) {
            ivFalutTemp.setVisibility(View.VISIBLE);
        } else {
            ivFalutTemp.setVisibility(View.INVISIBLE);
        }
        if (MachineStatusForMrFrture.fault_humidity) {
            ivFalutHumidity.setVisibility(View.VISIBLE);
        } else {
            ivFalutHumidity.setVisibility(View.INVISIBLE);
        }
    }

    private void initDate() {
        tvSurk.setText("" + MachineStatusForMrFrture.Surge_tank);
        tvFresh.setText("" + (MachineStatusForMrFrture.Wind_Velocity + 1));
        if (MachineStatusForMrFrture.Mode == 0) {
            tvMode.setText("智能");
        } else if (MachineStatusForMrFrture.Mode == 1) {
            tvMode.setText("极速");
        } else if (MachineStatusForMrFrture.Mode == 2) {
            tvMode.setText("睡眠");
        } else if (MachineStatusForMrFrture.Mode == 3) {
            tvMode.setText("舒适");
        } else if (MachineStatusForMrFrture.Mode == 4) {
            tvMode.setText("自定义");
        }
        tvUvc.setText("" + MachineStatusForMrFrture.UVC_Life);
        tvVoc.setText("" + ((float) MachineStatusForMrFrture.VOC_Quality / 100f));
        tvTemp.setText("" + (MachineStatusForMrFrture.Temperature_Quality - 20));
        tvHumidity.setText("" + MachineStatusForMrFrture.humidity);
        tvLv1.setText("" + MachineStatusForMrFrture.Filter_Life1);
        tvLv2.setText("" + MachineStatusForMrFrture.Filter_Life2);
        tvLv3.setText("" + MachineStatusForMrFrture.Filter_Life3);
        tvLv4.setText("" + MachineStatusForMrFrture.Filter_Life4);
        tvHcho.setText("" + ((float) MachineStatusForMrFrture.HCHO_Quality / 100f));
        tvCo.setText("" + MachineStatusForMrFrture.CO2_value);
        tvPm.setText("" + MachineStatusForMrFrture.PM25_Indoor);
        tvCh4.setText("" + MachineStatusForMrFrture.read_int1);
        String sSettime = (String) SPUtils.get(TestActivity.this, "setshuttime", "0");
        long settime = Long.parseLong(sSettime);
        int shuttime = (int) SPUtils.get(TestActivity.this, "shuttime", 0);
        long current = System.currentTimeMillis();
        long shut = settime + shuttime * 1000 * 60;
        long time = (shut - current) / (1000 * 60);

        if (time < 0)
            time = 0;
        tvShutDown.setText("" + time);
    }

    private void initSwitch() {
        swChirld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineStatusForMrFrture.Child_Security_Lock = !MachineStatusForMrFrture.Child_Security_Lock;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_CHIRLD, MachineStatusForMrFrture.Child_Security_Lock ? 1 : 0);
                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_CHIRLD);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
        swElectric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MachineStatusForMrFrture.Switch_Electrostatic = !MachineStatusForMrFrture.Switch_Electrostatic;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_ELECTROSTATIC, MachineStatusForMrFrture.Switch_Electrostatic ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_ELECTROSTATIC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
        swSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineStatusForMrFrture.Switch = !MachineStatusForMrFrture.Switch;
                if (MachineStatusForMrFrture.Switch) {
                    MachineStatusForMrFrture.startTime = System.currentTimeMillis();
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
                initMachineSetting();
            }
        });
        swPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MachineStatusForMrFrture.Switch_Plasma1 = !MachineStatusForMrFrture.Switch_Plasma1;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_PLASMAL1, MachineStatusForMrFrture.Switch_Plasma1 ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_PLASMAL1);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
        swUvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineStatusForMrFrture.Switch_UVC = !MachineStatusForMrFrture.Switch_UVC;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_UVC, MachineStatusForMrFrture.Switch_UVC ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_UVC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
        swPtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineStatusForMrFrture.Switch_PTC = !MachineStatusForMrFrture.Switch_PTC;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_PTC, MachineStatusForMrFrture.Switch_PTC ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_PTC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
        swSurk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineStatusForMrFrture.Switch_Valve = !MachineStatusForMrFrture.Switch_Valve;
                CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_FRESH, MachineStatusForMrFrture.Switch_Valve ? 1 : 0);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_FRESH);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        });
    }


    @OnClick({R.id.btn_shutdown_add, R.id.btn_shutdown_dec, R.id.btn_wind, R.id.btn_mode, R.id.btn_surk, R.id.btn_read, R.id.btn_surk_dec, R.id.btn_surk_add, R.id.btn_Fresh_dec, R.id.btn_Fresh_add, R.id.btn_mode_dec, R.id.btn_mode_add, R.id.btn_uvc_dec, R.id.btn_uvc_add, R.id.btn_dec_voc, R.id.btn_voc_add, R.id.btn_humidity_dec, R.id.btn_humidity_add, R.id.btn_temp_dec, R.id.btn_temp_add, R.id.btn_lv1_dec, R.id.btn_lv1_add, R.id.btn_lv2_dec, R.id.btn_lv2_add, R.id.btn_lv3_dec, R.id.btn_lv3_add, R.id.btn_lv4_dec, R.id.btn_lv4_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_shutdown_add:
                long current = System.currentTimeMillis();
                int shuttime = (int) SPUtils.get(TestActivity.this, "shuttime", 0);
                shuttime++;
                SPUtils.put(TestActivity.this, "setshuttime", "" + current);
                SPUtils.put(TestActivity.this, "shuttime", shuttime);
                tvShutDown.setText("" + shuttime);
                break;
            case R.id.btn_shutdown_dec:
                long current1 = System.currentTimeMillis();
                int shuttime1 = (int) SPUtils.get(TestActivity.this, "shuttime", 0);
                shuttime1--;
                if (shuttime1 < 0) {
                    shuttime1 = 0;
                }
                SPUtils.put(TestActivity.this, "setshuttime", "" + current1);
                SPUtils.put(TestActivity.this, "shuttime", shuttime1);
                tvShutDown.setText("" + shuttime1);
                break;
            case R.id.btn_wind:
                MachineStatusForMrFrture.Wind_Velocity++;
                if (MachineStatusForMrFrture.Wind_Velocity > 9) {
                    MachineStatusForMrFrture.Wind_Velocity = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);

                int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
                EventBus.getDefault().post(new SendDataToMachine(d));
                break;
            case R.id.btn_mode:
                MachineStatusForMrFrture.Mode++;
                if (MachineStatusForMrFrture.Mode > 4) {
                    MachineStatusForMrFrture.Mode = 0;
                }
                if (MachineStatusForMrFrture.Mode == 0) {
                    tvMode.setText("智能");
                    runSmartMode();
                } else if (MachineStatusForMrFrture.Mode == 1) {
                    tvMode.setText("极速");
                    MachineStatusForMrFrture.setSpeedmodeTime = System.currentTimeMillis();
                    runSpeedMode();
                } else if (MachineStatusForMrFrture.Mode == 2) {
                    tvMode.setText("睡眠");
                    runSleepMode();
                } else if (MachineStatusForMrFrture.Mode == 3) {
                    tvMode.setText("舒适");
                    runConfortMode();
                } else if (MachineStatusForMrFrture.Mode == 4) {
                    tvMode.setText("自定义");
                    runCustomMode();
                }
                break;
            case R.id.btn_surk:
                MachineStatusForMrFrture.Surge_tank++;
                if (MachineStatusForMrFrture.Surge_tank > 5) {
                    MachineStatusForMrFrture.Surge_tank = 0;
                }
                int[] d2 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SURGE_TANK);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d2));
                EventBus.getDefault().post(new SendDataToMachine(d2));
                tvSurk.setText("" + MachineStatusForMrFrture.Surge_tank);
                break;
            case R.id.btn_read:
                int[] d3 = CreateCmdToMachineFactory.createReadStatusCmd();
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d3));
                EventBus.getDefault().post(new SendDataToMachine(d3));
                break;
            case R.id.btn_surk_dec:
                MachineStatusForMrFrture.bSmartControl = false;
                MachineStatusForMrFrture.Surge_tank--;
                if (MachineStatusForMrFrture.Surge_tank < 0) {
                    MachineStatusForMrFrture.Surge_tank = 5;
                }
                if (MachineStatusForMrFrture.Surge_tank == 0) {
                    MachineStatusForMrFrture.Switch_Valve = false;
                } else {
                    MachineStatusForMrFrture.Switch_Valve = true;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_SURGE_TANK, MachineStatusForMrFrture.Surge_tank);

                int[] surk = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SURGE_TANK | Constants.ANDROID_SEND_SWITCH_FRESH);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(surk));
                EventBus.getDefault().post(new SendDataToMachine(surk));
                tvSurk.setText("" + MachineStatusForMrFrture.Surge_tank);
                break;
            case R.id.btn_surk_add:
                MachineStatusForMrFrture.bSmartControl = false;
                MachineStatusForMrFrture.Surge_tank++;
                if (MachineStatusForMrFrture.Surge_tank > 5) {
                    MachineStatusForMrFrture.Surge_tank = 0;
                }
                if (MachineStatusForMrFrture.Surge_tank == 0) {
                    MachineStatusForMrFrture.Switch_Valve = false;
                } else {
                    MachineStatusForMrFrture.Switch_Valve = true;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_SURGE_TANK, MachineStatusForMrFrture.Surge_tank);

                int[] surk1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SURGE_TANK | Constants.ANDROID_SEND_SWITCH_FRESH);

                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(surk1));
                EventBus.getDefault().post(new SendDataToMachine(surk1));
//                tvSurk.setText("" + MachineStatusForMrFrture.Surge_tank);

                break;
            case R.id.btn_Fresh_dec:
                if (MachineStatusForMrFrture.Mode != 4) {
                    Toast.makeText(this, "非自定义模式无法修改", Toast.LENGTH_SHORT).show();
                    return;
                }
                MachineStatusForMrFrture.Wind_Velocity--;
                if (MachineStatusForMrFrture.Wind_Velocity < 0) {
                    MachineStatusForMrFrture.Wind_Velocity = 9;
                }
                SPUtils.put(this, "wind level", (int) MachineStatusForMrFrture.Wind_Velocity);
                CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);

                int[] wind = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(wind));
                EventBus.getDefault().post(new SendDataToMachine(wind));
//                tvFresh.setText("" + MachineStatusForMrFrture.Wind_Velocity);

                break;
            case R.id.btn_Fresh_add:
                if (MachineStatusForMrFrture.Mode != 4) {
                    Toast.makeText(this, "非自定义模式无法修改", Toast.LENGTH_SHORT).show();
                    return;
                }
                MachineStatusForMrFrture.Wind_Velocity++;
                if (MachineStatusForMrFrture.Wind_Velocity > 9) {
                    MachineStatusForMrFrture.Wind_Velocity = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);

                SPUtils.put(this, "wind level", (int) MachineStatusForMrFrture.Wind_Velocity);
                int[] wind1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(wind1));
                EventBus.getDefault().post(new SendDataToMachine(wind1));
//                tvFresh.setText("" + MachineStatusForMrFrture.Wind_Velocity);

                break;
            case R.id.btn_mode_dec:

                MachineStatusForMrFrture.Mode--;
                if (MachineStatusForMrFrture.Mode < 0) {
                    MachineStatusForMrFrture.Mode = 4;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_MODE, MachineStatusForMrFrture.Mode);

                if (MachineStatusForMrFrture.Mode == 0) {

                    runSmartMode();
                } else if (MachineStatusForMrFrture.Mode == 1) {
                    MachineStatusForMrFrture.setSpeedmodeTime = System.currentTimeMillis();
                    runSpeedMode();
                } else if (MachineStatusForMrFrture.Mode == 2) {

                    runSleepMode();
                } else if (MachineStatusForMrFrture.Mode == 3) {

                    runConfortMode();
                } else if (MachineStatusForMrFrture.Mode == 4) {

                    runCustomMode();
                }
                lastMode = MachineStatusForMrFrture.Mode;
                if (MachineStatusForMrFrture.Mode == 0) {
                    tvMode.setText("智能");
                } else if (MachineStatusForMrFrture.Mode == 1) {
                    tvMode.setText("极速");
                } else if (MachineStatusForMrFrture.Mode == 2) {
                    tvMode.setText("睡眠");
                } else if (MachineStatusForMrFrture.Mode == 3) {
                    tvMode.setText("舒适");
                } else if (MachineStatusForMrFrture.Mode == 4) {
                    tvMode.setText("自定义");
                }

                break;
            case R.id.btn_mode_add:

                MachineStatusForMrFrture.Mode++;
                if (MachineStatusForMrFrture.Mode > 4) {
                    MachineStatusForMrFrture.Mode = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_MODE, MachineStatusForMrFrture.Mode);

                if (MachineStatusForMrFrture.Mode == 0) {
                    runSmartMode();
                } else if (MachineStatusForMrFrture.Mode == 1) {
                    MachineStatusForMrFrture.setSpeedmodeTime = System.currentTimeMillis();
                    runSpeedMode();
                } else if (MachineStatusForMrFrture.Mode == 2) {
                    runSleepMode();
                } else if (MachineStatusForMrFrture.Mode == 3) {

                    runConfortMode();
                } else if (MachineStatusForMrFrture.Mode == 4) {

                    runCustomMode();
                }
                lastMode = MachineStatusForMrFrture.Mode;
                if (MachineStatusForMrFrture.Mode == 0) {
                    tvMode.setText("智能");
                } else if (MachineStatusForMrFrture.Mode == 1) {
                    tvMode.setText("极速");
                } else if (MachineStatusForMrFrture.Mode == 2) {
                    tvMode.setText("睡眠");
                } else if (MachineStatusForMrFrture.Mode == 3) {
                    tvMode.setText("舒适");
                } else if (MachineStatusForMrFrture.Mode == 4) {
                    tvMode.setText("自定义");
                }

                break;
            case R.id.btn_uvc_dec:
                MachineStatusForMrFrture.UVC_Life = 0;
                int[] uvc = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_UVC);
                CommonUtils.setOrder(Constants.ANDROID_SEND_UVC, MachineStatusForMrFrture.UVC_Life);

                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(uvc));
                EventBus.getDefault().post(new SendDataToMachine(uvc));
                tvUvc.setText("" + MachineStatusForMrFrture.UVC_Life);

                break;
            case R.id.btn_uvc_add:
                MachineStatusForMrFrture.UVC_Life += 10;
                if (MachineStatusForMrFrture.UVC_Life > 100) {
                    MachineStatusForMrFrture.UVC_Life = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_UVC, MachineStatusForMrFrture.UVC_Life);

                int[] uvc1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_UVC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(uvc1));
                EventBus.getDefault().post(new SendDataToMachine(uvc1));
                tvUvc.setText("" + MachineStatusForMrFrture.UVC_Life);

                break;
            case R.id.btn_dec_voc:
                MachineStatusForMrFrture.VOC_Quality -= 10;
                if (MachineStatusForMrFrture.VOC_Quality < 0) {
                    MachineStatusForMrFrture.VOC_Quality = 100;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_VOC, MachineStatusForMrFrture.VOC_Quality);

                int[] voc = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_VOC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(voc));
                EventBus.getDefault().post(new SendDataToMachine(voc));
                tvVoc.setText("" + MachineStatusForMrFrture.VOC_Quality);

                break;
            case R.id.btn_voc_add:
                MachineStatusForMrFrture.VOC_Quality += 10;
                if (MachineStatusForMrFrture.VOC_Quality > 100) {
                    MachineStatusForMrFrture.VOC_Quality = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_VOC, MachineStatusForMrFrture.VOC_Quality);

                int[] voc1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_VOC);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(voc1));
                EventBus.getDefault().post(new SendDataToMachine(voc1));
                tvVoc.setText("" + MachineStatusForMrFrture.VOC_Quality);

                break;
            case R.id.btn_humidity_dec:
                MachineStatusForMrFrture.humidity -= 10;
                if (MachineStatusForMrFrture.humidity < 0) {
                    MachineStatusForMrFrture.humidity = 100;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_SET_HUMIDITY, MachineStatusForMrFrture.humidity);

                int[] humidity = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SET_HUMIDITY);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(humidity));
                EventBus.getDefault().post(new SendDataToMachine(humidity));
                tvHumidity.setText("" + MachineStatusForMrFrture.humidity);

                break;
            case R.id.btn_humidity_add:
                MachineStatusForMrFrture.humidity += 10;
                if (MachineStatusForMrFrture.humidity > 100) {
                    MachineStatusForMrFrture.humidity = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_SET_HUMIDITY, MachineStatusForMrFrture.humidity);

                int[] humidity1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SET_HUMIDITY);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(humidity1));
                EventBus.getDefault().post(new SendDataToMachine(humidity1));
                tvHumidity.setText("" + MachineStatusForMrFrture.humidity);

                break;
            case R.id.btn_temp_dec:
                MachineStatusForMrFrture.Temperature_Quality -= 1;
                if (MachineStatusForMrFrture.Temperature_Quality < 0) {
                    MachineStatusForMrFrture.Temperature_Quality = 100;
                }
                int[] tem1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_TEMP);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(tem1));
                EventBus.getDefault().post(new SendDataToMachine(tem1));
                tvTemp.setText("" + MachineStatusForMrFrture.Temperature_Quality);

                break;
            case R.id.btn_temp_add:
                MachineStatusForMrFrture.Temperature_Quality += 1;
                if (MachineStatusForMrFrture.Temperature_Quality > 100) {
                    MachineStatusForMrFrture.Temperature_Quality = 0;
                }
                int[] tem = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_TEMP);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(tem));
                EventBus.getDefault().post(new SendDataToMachine(tem));
                tvTemp.setText("" + MachineStatusForMrFrture.Temperature_Quality);

                break;
            case R.id.btn_lv1_dec:
                MachineStatusForMrFrture.Filter_Life1 = 0;
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_CHU, MachineStatusForMrFrture.Filter_Life1);

                int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_CHU);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter11));
                EventBus.getDefault().post(new SendDataToMachine(filter11));
                tvLv1.setText("" + MachineStatusForMrFrture.Filter_Life1);

                break;
            case R.id.btn_lv1_add:
                MachineStatusForMrFrture.Filter_Life1 += 10;
                if (MachineStatusForMrFrture.Filter_Life1 > 100) {
                    MachineStatusForMrFrture.Filter_Life1 = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_CHU, MachineStatusForMrFrture.Filter_Life1);

                int[] filter1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_CHU);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter1));
                EventBus.getDefault().post(new SendDataToMachine(filter1));
                tvLv1.setText("" + MachineStatusForMrFrture.Filter_Life1);

                break;
            case R.id.btn_lv2_dec:
                MachineStatusForMrFrture.Filter_Life2 = 0;
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_ZHONG, MachineStatusForMrFrture.Filter_Life2);

                int[] filter2 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_ZHONG);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter2));
                EventBus.getDefault().post(new SendDataToMachine(filter2));
                tvLv2.setText("" + MachineStatusForMrFrture.Filter_Life2);

                break;
            case R.id.btn_lv2_add:
                MachineStatusForMrFrture.Filter_Life2 += 10;
                if (MachineStatusForMrFrture.Filter_Life2 > 100) {
                    MachineStatusForMrFrture.Filter_Life2 = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_ZHONG, MachineStatusForMrFrture.Filter_Life2);

                int[] filter21 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_ZHONG);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter21));
                EventBus.getDefault().post(new SendDataToMachine(filter21));
                tvLv2.setText("" + MachineStatusForMrFrture.Filter_Life2);

                break;
            case R.id.btn_lv3_dec:
                MachineStatusForMrFrture.Filter_Life3 = 0;
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_HUOXING, MachineStatusForMrFrture.Filter_Life3);

                int[] filter31 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_HUOXING);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter31));
                EventBus.getDefault().post(new SendDataToMachine(filter31));
                tvLv3.setText("" + MachineStatusForMrFrture.Filter_Life3);

                break;
            case R.id.btn_lv3_add:
                MachineStatusForMrFrture.Filter_Life3 += 10;
                if (MachineStatusForMrFrture.Filter_Life3 > 100) {
                    MachineStatusForMrFrture.Filter_Life3 = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_HUOXING, MachineStatusForMrFrture.Filter_Life3);

                int[] filter3 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_HUOXING);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(filter3));
                EventBus.getDefault().post(new SendDataToMachine(filter3));
                tvLv3.setText("" + MachineStatusForMrFrture.Filter_Life3);

                break;
            case R.id.btn_lv4_dec:
                MachineStatusForMrFrture.Filter_Life4 = 0;
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO, MachineStatusForMrFrture.Filter_Life4);

                int[] fiter4 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(fiter4));
                EventBus.getDefault().post(new SendDataToMachine(fiter4));
                tvLv4.setText("" + MachineStatusForMrFrture.Filter_Life4);

                break;
            case R.id.btn_lv4_add:
                MachineStatusForMrFrture.Filter_Life4 += 10;
                if (MachineStatusForMrFrture.Filter_Life4 > 100) {
                    MachineStatusForMrFrture.Filter_Life4 = 0;
                }
                CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO, MachineStatusForMrFrture.Filter_Life4);

                int[] fiter41 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(fiter41));
                EventBus.getDefault().post(new SendDataToMachine(fiter41));
                tvLv4.setText("" + MachineStatusForMrFrture.Filter_Life4);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        swSwitch.setChecked(MachineStatusForMrFrture.Switch);
        swElectric.setChecked(MachineStatusForMrFrture.Switch_Electrostatic);
        swChirld.setChecked(MachineStatusForMrFrture.Child_Security_Lock);
        swPtc.setChecked(MachineStatusForMrFrture.Switch_PTC);
        swUvc.setChecked(MachineStatusForMrFrture.Switch_UVC);
        swPlastic.setChecked(MachineStatusForMrFrture.Switch_Plasma1);
        swSurk.setChecked(MachineStatusForMrFrture.Switch_Valve);
        initDate();
        initFault();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(PopupEvent data) {
        int type = data.getType();
        if (type == 1) {
            Toast.makeText(TestActivity.this, "二氧化碳报警", Toast.LENGTH_SHORT).show();
        }
        if (type == 2) {
            Toast.makeText(TestActivity.this, "甲烷报警", Toast.LENGTH_SHORT).show();
        }
        if (type == 3) {
            Toast.makeText(TestActivity.this, "甲醛报警", Toast.LENGTH_SHORT).show();
        }
    }


}
