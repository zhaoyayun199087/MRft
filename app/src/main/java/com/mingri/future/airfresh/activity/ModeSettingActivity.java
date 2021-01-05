/**
 * 模式选择界面
 */
package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.PopupEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.ShowFilterLife;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.WifiChangeEvent;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;


public class ModeSettingActivity extends Activity {
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
    @InjectView(R.id.ib_exit)
    ImageButton ibExit;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.tv_fresh)
    TextView tvFresh;
    @InjectView(R.id.tv_company)
    TextView tvCompany;
    @InjectView(R.id.ib_fresh_dec)
    ImageButton ibFreshDec;
    @InjectView(R.id.tv_fresh_info)
    TextView tvFreshInfo;
    @InjectView(R.id.ib_fresh_add)
    ImageButton ibFreshAdd;
    @InjectView(R.id.ib_mode_smart)
    ImageButton ibModeSmart;
    @InjectView(R.id.ib_mode_speed)
    ImageButton ibModeSpeed;
    @InjectView(R.id.ib_mode_confort)
    ImageButton ibModeConfort;
    @InjectView(R.id.ib_mode_sleep)
    ImageButton ibModeSleep;
    @InjectView(R.id.tv_custom)
    TextView tvCustom;
    @InjectView(R.id.tv_custom_company)
    TextView tvCustomCompany;
    @InjectView(R.id.ib_custom_dec)
    ImageButton ibCustomDec;
    @InjectView(R.id.ib_custom_add)
    ImageButton ibCustomAdd;
    @InjectView(R.id.rl_custom)
    RelativeLayout rlCustom;
    @InjectView(R.id.ll_content)
    LinearLayout llContent;
    @InjectView(R.id.iv_nofresh)
    ImageView ivNofresh;
    @InjectView(R.id.ll_cus)
    LinearLayout llCus;

    private boolean bExit = false;
    private boolean bNeedUpdate = true;
    private int iTimeCount = 0;
    private byte iSurkTank;
    private byte iWind;
    private boolean customState = true;

    Handler handler = new Handler();

    private int surTankOne[] = {35, 57, 69, 83, 102, 122, 143, 162, 183, 218};
    private int surTankTwo[] = {51, 70, 93, 117, 150, 170, 200, 223, 251, 304};
    private int surTankThree[] = {71, 94, 128, 160, 184, 215, 249, 289, 319, 382};
    private int surTankFour[] = {79, 115, 142, 183, 224, 254, 301, 335, 372, 440};
    private int surTankFive[] = {95, 133, 168, 200, 251, 289, 332, 379, 418, 550};


    private Thread mMonitorUserOprate = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!bExit) {
                //如果5秒用户没操作，那么更新
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
        setContentView(R.layout.activity_modesetting);
        ButterKnife.inject(this);

        MachineStatusForMrFrture.Surge_tank = 3;    //进入设置为3档

        EventBus.getDefault().register(this);
        iSurkTank = MachineStatusForMrFrture.Surge_tank;
        iWind = MachineStatusForMrFrture.Wind_Velocity;
        sendSerialData(new ReceDataFromMachine(new byte[]{}));
        lastMode = MachineStatusForMrFrture.Mode;
        if (iSurkTank == 0) {
            ibFreshDec.setEnabled(false);
            ibFreshDec.setImageResource(R.mipmap.xinf_btn_jian_d);
        }
        if (iSurkTank == 5) {
            ibFreshAdd.setEnabled(false);
            ibFreshAdd.setImageResource(R.mipmap.xinf_btn_jia_d);
        }

        mMonitorUserOprate.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d("key is " + keyCode + " event " + event.toString());
        if (event.getRepeatCount() == 25) {
            startActivity(new Intent(this, LockTest.class));
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
        ButterKnife.reset(this);
        bExit = true;
    }

    @OnClick({R.id.tv_fresh_info, R.id.rl_custom, R.id.ib_exit, R.id.ib_fresh_dec, R.id.ib_fresh_add, R.id.ib_mode_smart, R.id.ib_mode_speed, R.id.ib_mode_confort, R.id.ib_mode_sleep, R.id.ib_custom_dec, R.id.ib_custom_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fresh_info:
                setInstalled();
                break;
            case R.id.rl_custom:
                runCustomMode();
                setModeBg(4);
                setWind();
                tvCustom.setText("" + (MachineStatusForMrFrture.Wind_Velocity + 1) + getResources().getString(R.string.level));
                setNotNeedUpdate();
                break;
            case R.id.ib_exit:
                finish();
                break;
            case R.id.ib_fresh_dec:
//                if (!(Boolean) SPUtils.get(ModeSettingActivity.this, "is install", false)) {
//                    Toast.makeText(ModeSettingActivity.this, R.string.user_tips, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                MachineStatusForMrFrture.bSmartControl = false;
                iSurkTank--;
                if (iSurkTank == 0) {
                    ibFreshDec.setEnabled(false);
                    ibFreshDec.setImageResource(R.mipmap.xinf_btn_jian_d);
                }
                if (!ibFreshAdd.isEnabled()) {
                    ibFreshAdd.setEnabled(true);
                    ibFreshAdd.setImageResource(R.mipmap.xinf_btn_jia_n);
                }
                if (iSurkTank == 0) {
                    MachineStatusForMrFrture.Switch_Valve = false;
                } else {
                    MachineStatusForMrFrture.Switch_Valve = true;
                }
                MachineStatusForMrFrture.Surge_tank = iSurkTank;
                setWind();
                CommonUtils.setOrder(Constants.ANDROID_SEND_SURGE_TANK, MachineStatusForMrFrture.Surge_tank);

                int[] surk = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SURGE_TANK | Constants.ANDROID_SEND_SWITCH_FRESH);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(surk));
                EventBus.getDefault().post(new SendDataToMachine(surk));
                setNotNeedUpdate();
                if (iSurkTank != 0) {
                    ivNofresh.setVisibility(View.GONE);
                    tvFresh.setVisibility(View.VISIBLE);
                    tvCompany.setVisibility(View.VISIBLE);
                    tvFreshInfo.setText("" + MachineStatusForMrFrture.Surge_tank + getString(R.string.all_level));
                } else {
                    ivNofresh.setVisibility(View.VISIBLE);
                    tvFresh.setVisibility(View.GONE);
                    tvCompany.setVisibility(View.GONE);
                    tvFreshInfo.setText(getString(R.string.all_close));
                }
                break;
            case R.id.ib_fresh_add:
//                if (!(Boolean) SPUtils.get(ModeSettingActivity.this, "is install", false)) {
//                    Toast.makeText(ModeSettingActivity.this, R.string.user_tips, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                MachineStatusForMrFrture.bSmartControl = false;
                iSurkTank++;
                if (iSurkTank == 5) {
                    ibFreshAdd.setEnabled(false);
                    ibFreshAdd.setImageResource(R.mipmap.xinf_btn_jia_d);
                }
                if (!ibFreshDec.isEnabled()) {
                    ibFreshDec.setEnabled(true);
                    ibFreshDec.setImageResource(R.mipmap.xinf_btn_jian_n);
                }
                if (iSurkTank == 0) {
                    MachineStatusForMrFrture.Switch_Valve = false;
                } else {
                    MachineStatusForMrFrture.Switch_Valve = true;
                }
                MachineStatusForMrFrture.Surge_tank = iSurkTank;
                setWind();
                CommonUtils.setOrder(Constants.ANDROID_SEND_SURGE_TANK, MachineStatusForMrFrture.Surge_tank);

                int[] surk1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SURGE_TANK | Constants.ANDROID_SEND_SWITCH_FRESH);

                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(surk1));
                EventBus.getDefault().post(new SendDataToMachine(surk1));
                setNotNeedUpdate();
                if (iSurkTank != 0) {
                    ivNofresh.setVisibility(View.GONE);
                    tvFresh.setVisibility(View.VISIBLE);
                    tvCompany.setVisibility(View.VISIBLE);
                    tvFreshInfo.setText("" + MachineStatusForMrFrture.Surge_tank + getString(R.string.all_level));
                } else {
                    ivNofresh.setVisibility(View.VISIBLE);
                    tvFresh.setVisibility(View.GONE);
                    tvCompany.setVisibility(View.GONE);
                    tvFreshInfo.setText(getString(R.string.all_close));
                }
                break;
            case R.id.ib_mode_smart:
                runSmartMode();
                setModeBg(0);
                setWind();
                setNotNeedUpdate();
                break;
            case R.id.ib_mode_speed:
                runSpeedMode();
                setModeBg(1);
                setWind();
                setNotNeedUpdate();
                break;
            case R.id.ib_mode_confort:
                runConfortMode();
                setModeBg(3);
                setWind();
                setNotNeedUpdate();
                break;
            case R.id.ib_mode_sleep:
                runSleepMode();
                setModeBg(2);
                setWind();
                setNotNeedUpdate();
                break;
            case R.id.ib_custom_dec:
                setModeBg(4);
                MachineStatusForMrFrture.Mode = 4;
                iWind--;
                if (iWind < 0) {
                    iWind = 9;
                }
                if (lastMode != 4) {
                    iWind = 1;
                }
                MachineStatusForMrFrture.Wind_Velocity = iWind;
                CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);
                setWind();
                int[] wind = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL | Constants.ANDROID_SEND_MODE);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(wind));
                EventBus.getDefault().post(new SendDataToMachine(wind));
                setNotNeedUpdate();
                lastMode = 4;
                tvCustom.setText("" + ((iWind + 1)) + getResources().getString(R.string.level));
                break;
            case R.id.ib_custom_add:
                setModeBg(4);
                MachineStatusForMrFrture.Mode = 4;
                iWind++;
                if (iWind > 9) {
                    iWind = 0;
                }
                if (lastMode != 4) {
                    iWind = 1;
                }
                MachineStatusForMrFrture.Wind_Velocity = iWind;
                CommonUtils.setOrder(Constants.ANDROID_SEND_WIND_LEVEL, MachineStatusForMrFrture.Wind_Velocity);
                setWind();
                SPUtils.put(this, "wind level", (int) MachineStatusForMrFrture.Wind_Velocity);
                int[] wind1 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_WIND_LEVEL | Constants.ANDROID_SEND_MODE);
                LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(wind1));
                EventBus.getDefault().post(new SendDataToMachine(wind1));
                setNotNeedUpdate();
                tvCustom.setText("" + ((iWind + 1)) + getResources().getString(R.string.level));
                lastMode = 4;
                break;
        }
    }

    private void setWind() {
        if (MachineStatusForMrFrture.Surge_tank == 1)
            tvFresh.setText("" + surTankOne[MachineStatusForMrFrture.Wind_Velocity]);
        else if (MachineStatusForMrFrture.Surge_tank == 2)
            tvFresh.setText("" + surTankTwo[MachineStatusForMrFrture.Wind_Velocity]);
        else if (MachineStatusForMrFrture.Surge_tank == 3)
            tvFresh.setText("" + surTankThree[MachineStatusForMrFrture.Wind_Velocity]);
        else if (MachineStatusForMrFrture.Surge_tank == 4)
            tvFresh.setText("" + surTankFour[MachineStatusForMrFrture.Wind_Velocity]);
        else if (MachineStatusForMrFrture.Surge_tank == 5)
            tvFresh.setText("" + surTankFive[MachineStatusForMrFrture.Wind_Velocity]);
    }

    private boolean bBackgound = false;

    @Override
    protected void onResume() {
        super.onResume();
        bBackgound = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bBackgound = true;
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

        if (!CommonUtils.isTopActivity(ModeSettingActivity.this, "com.mingri.future.airfresh.activity.ModeSettingActivity")) {
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
                co2Diag = new CO2AlertDialog(ModeSettingActivity.this, new CO2AlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                        finish();
                    }
                });
                co2Diag.show();
            }
        } else if (data.getType() == 2) {
            if (ch4Diag == null || !ch4Diag.isShowing()) {
                ch4Diag = new CH4AlertDialog(ModeSettingActivity.this, new CH4AlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                        finish();
                    }
                });
                ch4Diag.show();
            }
        } else if (data.getType() == 3) {
            if (hchoDiag == null || !hchoDiag.isShowing()) {
                hchoDiag = new HCHOAlertDialog(ModeSettingActivity.this, new HCHOAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                        finish();
                    }
                });
                hchoDiag.show();
            }
        } else if (data.getType() == 4) {
            if (vocDiag == null || !vocDiag.isShowing()) {
                vocDiag = new VocAlertDialog(ModeSettingActivity.this, new VocAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowMainPage(0));
                        finish();
                    }
                });
                vocDiag.show();
            }
        } else if (data.getType() == 5) {
            if (chuDiag == null || !chuDiag.isShowing()) {
                chuDiag = new ChuxiaoLwAlertDialog(ModeSettingActivity.this, new ChuxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());
                        finish();
                    }
                });
                chuDiag.setPercent(MachineStatusForMrFrture.Filter_Life1);
                chuDiag.show();
            }
        } else if (data.getType() == 6) {
            if (zhongDiag == null || !zhongDiag.isShowing()) {
                zhongDiag = new ZhongxiaoLwAlertDialog(ModeSettingActivity.this, new ZhongxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());
                        finish();
                    }
                });
                zhongDiag.setPercent(MachineStatusForMrFrture.Filter_Life2);
                zhongDiag.show();
            }
        } else if (data.getType() == 7) {

            if (huoDiag == null || !huoDiag.isShowing()) {
                huoDiag = new HuoxingLwAlertDialog(ModeSettingActivity.this, new HuoxingLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());
                        finish();
                    }
                });
                huoDiag.setPercent(MachineStatusForMrFrture.Filter_Life3);
                huoDiag.show();
            }
        } else if (data.getType() == 8) {
            if (gaoDiag == null || !gaoDiag.isShowing()) {
                gaoDiag = new GaoxiaoLwAlertDialog(ModeSettingActivity.this, new GaoxiaoLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());
                        finish();
                    }
                });
                gaoDiag.setPercent(MachineStatusForMrFrture.Filter_Life4);
                gaoDiag.show();
            }
        } else if (data.getType() == 9) {
            if (uvcDiag == null || !uvcDiag.isShowing()) {
                uvcDiag = new UvcLwAlertDialog(ModeSettingActivity.this, new UvcLwAlertDialog.VocAletCallback() {
                    @Override
                    public void onOk() {
                        EventBus.getDefault().post(new ShowFilterLife());
                        finish();
                    }
                });
                uvcDiag.setPercent(MachineStatusForMrFrture.UVC_Life);
                uvcDiag.show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ChirldLock data) {
        finish();
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
        }
    }

    //实时获取数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        if (!bNeedUpdate) {
            LogUtils.d("not update date");
            return;
        }
        LogUtils.d("mode is " + MachineStatusForMrFrture.Mode);
        setModeBg(MachineStatusForMrFrture.Mode);
        iSurkTank = MachineStatusForMrFrture.Surge_tank;
        Log.i("textShow", "iSurkTank1 : " + iSurkTank);
        iWind = MachineStatusForMrFrture.Wind_Velocity;
        if (MachineStatusForMrFrture.Surge_tank != 0) {
            ivNofresh.setVisibility(View.GONE);
            tvFresh.setVisibility(View.VISIBLE);
            tvCompany.setVisibility(View.VISIBLE);
            tvFreshInfo.setText("" + MachineStatusForMrFrture.Surge_tank + getString(R.string.all_level));
        } else {
            ivNofresh.setVisibility(View.VISIBLE);
            tvFresh.setVisibility(View.GONE);
            tvCompany.setVisibility(View.GONE);
            tvFreshInfo.setText(getString(R.string.all_close));
        }
        tvCustom.setText("" + (MachineStatusForMrFrture.Wind_Velocity + 1) + getResources().getString(R.string.level));
        setStatusBar();
        setWind();
    }

    //实时获取状态并显示打开对应的功能
    private void setStatusBar() {
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

    //更改运行模式选择图标
    private void setModeBg(int mode) {
        setModeDefaultBg();
        switch (mode) {
            case 0:
                ibModeSmart.setImageResource(R.mipmap.moshi_btn_zhin_n);
                break;
            case 1:
                ibModeSpeed.setImageResource(R.mipmap.moshi_btn_jis_n);
                break;
            case 2:
                ibModeSleep.setImageResource(R.mipmap.moshi_btn_shuim_n);
                break;
            case 3:
                ibModeConfort.setImageResource(R.mipmap.moshi_btn_shus_n);
                break;
            case 4:
                rlCustom.setBackgroundResource(R.mipmap.moshi_bg_zdy_n);
                llCus.setVisibility(View.VISIBLE);
                ibCustomAdd.setVisibility(View.VISIBLE);
                ibCustomDec.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 设置几个模式默认背景
     */
    private void setModeDefaultBg() {
        ibModeSleep.setImageResource(R.mipmap.moshi_btn_shuim_d);
        ibModeSmart.setImageResource(R.mipmap.moshi_btn_zhin_d);
        ibModeSpeed.setImageResource(R.mipmap.moshi_btn_jis_d);
        ibModeConfort.setImageResource(R.mipmap.moshi_btn_shus_d);
        rlCustom.setBackgroundResource((R.mipmap.moshi_bg_zdy_d));
        llCus.setVisibility(View.GONE);
        ibCustomAdd.setVisibility(View.GONE);
        ibCustomDec.setVisibility(View.GONE);
    }

    private int lastMode = 0;

    /**
     * 运行智能模式,切换到智能模式
     * 2021-01-05 10:52:35
     * 开机默认运行在智能模式(ebm三档/新风三档)
     * 智能模式下，档位(ebm/新风)根据实测数据与设定参数对比来自动调节，由PM2.5和CO2共同参与自动控制，采取高风速优先的策略。
     * 延时10s执行
     *
     *  PM2.5和CO2取最大值运行, 如PM2.5>75, CO2<1000时, 以PM2.5为准.   如PM2.5<25, CO2>1500时, 以CO2为准
     */
    private void runSmartMode() {
        Log.i("textShow", "切换到智能模式 : " + MachineStatusForMrFrture.Switch_UVC);
        rlCustom.setEnabled(true);
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
            Toast.makeText(ModeSettingActivity.this, "PM2.5数据为空", Toast.LENGTH_SHORT).show();

        timeDown();
        MachineStatusForMrFrture.Mode = 0;
        SPUtils.put(ModeSettingActivity.this, "mode", MachineStatusForMrFrture.Mode);
        lastMode = MachineStatusForMrFrture.Mode;
        //发送给设备的指令
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行极速模式
     */
    private void runSpeedMode() {
        Log.i("textShow", "切换到极速模式  " + MachineStatusForMrFrture.Switch_UVC);
        rlCustom.setEnabled(true);
        MachineStatusForMrFrture.Wind_Velocity = 7;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
            LogUtils.d("speed mode1 " + MachineStatusForMrFrture.Switch_UVC);
        }
        MachineStatusForMrFrture.setSpeedmodeTime = System.currentTimeMillis();
        MachineStatusForMrFrture.Mode = 1;
        SPUtils.put(ModeSettingActivity.this, "mode", MachineStatusForMrFrture.Mode);
        lastMode = MachineStatusForMrFrture.Mode;
        CommonUtils.setOrder(Constants.ANDROID_SEND_MODE, MachineStatusForMrFrture.Mode);
        Log.i("textShow", "Switch_UVC2 = " + MachineStatusForMrFrture.Switch_UVC);
        //发送给设备的指令
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行睡眠模式
     */
    private void runSleepMode() {
        Log.i("textShow", "切换到睡眠模式  " + MachineStatusForMrFrture.Switch_UVC);
        rlCustom.setEnabled(true);
        MachineStatusForMrFrture.Wind_Velocity = 0;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 2;
        SPUtils.put(ModeSettingActivity.this, "mode", MachineStatusForMrFrture.Mode);
        lastMode = MachineStatusForMrFrture.Mode;
        Log.i("textShow", "Switch_UVC3 = " + MachineStatusForMrFrture.Switch_UVC);
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行舒适模式
     */
    private void runConfortMode() {
        Log.i("textShow", "切换到舒适模式  " + MachineStatusForMrFrture.Switch_UVC);
        rlCustom.setEnabled(true);
        MachineStatusForMrFrture.Wind_Velocity = 2;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 3;
        SPUtils.put(ModeSettingActivity.this, "mode", MachineStatusForMrFrture.Mode);
        lastMode = MachineStatusForMrFrture.Mode;
        Log.i("textShow", "Switch_UVC4 = " + MachineStatusForMrFrture.Switch_UVC);
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 运行自定义模式
     */
    private void runCustomMode() {
        Log.i("textShow", "切换到自定义模式  " + MachineStatusForMrFrture.Switch_UVC);
        rlCustom.setEnabled(false);
        int level = (Integer) SPUtils.get(this, "wind level", 0);
        MachineStatusForMrFrture.Wind_Velocity = 1;
        iWind = 1;
        if (lastMode == 0) {
            MachineStatusForMrFrture.Switch_UVC = false;
        }
        MachineStatusForMrFrture.Mode = 4;
        SPUtils.put(ModeSettingActivity.this, "mode", MachineStatusForMrFrture.Mode);
        lastMode = MachineStatusForMrFrture.Mode;
        Log.i("textShow", "Switch_UVC5 = " + MachineStatusForMrFrture.Switch_UVC);
        int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(d));
    }

    /**
     * 系统检查，若连续点击四次，每次点击间隔少于1s就启动PM2.5校验页面
     */
    private long mLastTimeMillis = 0;
    private int mClickNumber = 0;

    private void setInstalled() {
        LogUtils.d("click number " + mClickNumber);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastTimeMillis > 1000) {
            mClickNumber = 0;
            // displayToast(R.string.check_finish);
            // mHandler.sendEmptyMessageDelayed(MESSAGE_CHECK_SYSTEM,1000);
        } else {
            mClickNumber++;
            // mHandler.removeMessages(MESSAGE_CHECK_SYSTEM);
        }
        if (mClickNumber == 4) {
            // 连续点击4次
            SPUtils.put(ModeSettingActivity.this, "is install", true);
        }
        if (mClickNumber == 9) {
            // 连续点击4次
            SPUtils.put(ModeSettingActivity.this, "is install", false);
        }
        mLastTimeMillis = currentTimeMillis;
    }

    //倒计时两分钟
    public void timeDown() {
        CountDownTimer timer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Log.i("textShow", "打开UVC灯");
                //倒计时结束后开启UVC灯
                MachineStatusForMrFrture.Switch_UVC = true;
                MachineStatusForMrFrture.UVC_TIMES--;
                //发送给设备打开UVC灯的指令
                int d[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_UVC);
                EventBus.getDefault().post(new SendDataToMachine(d));
            }
        };
        timer.start();
    }
}
