/**
 * 模式选择界面
 */
package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.PopupEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.ShowFilterLife;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.WifiChangeEvent;
import com.mingri.future.airfresh.util.CommonUtils;
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

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;


public class DetailParaActivity extends Activity {
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
    @InjectView(R.id.tv_title_out_para)
    TextView tvTitleOutPara;
    @InjectView(R.id.iv_out_para_index)
    ImageView ivOutParaIndex;
    @InjectView(R.id.ib_exit)
    ImageButton ibExit;
    @InjectView(R.id.tv_title_in_para)
    TextView tvTitleInPara;
    @InjectView(R.id.iv_in_para_index)
    ImageView ivInParaIndex;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.tv_aqi)
    TextView tvAqi;
    @InjectView(R.id.tv_aqi_value)
    TextView tvAqiValue;
    @InjectView(R.id.pb_aqi)
    ProgressBar pbAqi;
    @InjectView(R.id.tv_out_pm25)
    TextView tvOutPm25;
    @InjectView(R.id.tv_out_pm25_value)
    TextView tvOutPm25Value;
    @InjectView(R.id.pb_out_pm25)
    ProgressBar pbOutPm25;
    @InjectView(R.id.tv_out_tmp)
    TextView tvOutTmp;
    @InjectView(R.id.ll_out_temp)
    LinearLayout llOutTemp;
    @InjectView(R.id.pb_out_temp)
    ProgressBar pbOutTemp;
    @InjectView(R.id.pb_out_humidity)
    ProgressBar pbOutHumidity;
    @InjectView(R.id.tv_out_humidity)
    TextView tvOutHumidity;
    @InjectView(R.id.ll_out_humidity)
    LinearLayout llOutHumidity;
    @InjectView(R.id.pb_pm10)
    ProgressBar pbPm10;
    @InjectView(R.id.tv_pm10)
    TextView tvPm10;
    @InjectView(R.id.ll_pm10)
    LinearLayout llPm10;
    @InjectView(R.id.pb_o3)
    ProgressBar pbO3;
    @InjectView(R.id.tv_o3)
    TextView tvO3;
    @InjectView(R.id.ll_o3)
    LinearLayout llO3;
    @InjectView(R.id.ll_outpara)
    LinearLayout llOutpara;
    @InjectView(R.id.tv_pm_quality)
    TextView tvPmQuality;
    @InjectView(R.id.tv_pm_value)
    TextView tvPmValue;
    @InjectView(R.id.pb_pm25)
    ProgressBar pbPm25;
    @InjectView(R.id.tv_co_quality)
    TextView tvCoQuality;
    @InjectView(R.id.tv_co_value)
    TextView tvCoValue;
    @InjectView(R.id.pb_co)
    ProgressBar pbCo;
    @InjectView(R.id.tv_tmp)
    TextView tvTmp;
    @InjectView(R.id.ll_temp)
    LinearLayout llTemp;
    @InjectView(R.id.pb_temp)
    ProgressBar pbTemp;
    @InjectView(R.id.pb_humidity)
    ProgressBar pbHumidity;
    @InjectView(R.id.tv_humidity)
    TextView tvHumidity;
    @InjectView(R.id.ll_humidity)
    LinearLayout llHumidity;
    @InjectView(R.id.pb_hcho)
    ProgressBar pbHcho;
    @InjectView(R.id.tv_formaldehyde)
    TextView tvFormaldehyde;
    @InjectView(R.id.ll_hcho)
    LinearLayout llHcho;
    @InjectView(R.id.pb_voc)
    ProgressBar pbVoc;
    @InjectView(R.id.tv_voc)
    TextView tvVoc;
    @InjectView(R.id.ll_voc)
    LinearLayout llVoc;
    @InjectView(R.id.ll_inpara)
    LinearLayout llInpara;
    @InjectView(R.id.iv_aqi)
    ImageView ivAqi;
    @InjectView(R.id.iv_out_pm25)
    ImageView ivOutPm25;
    @InjectView(R.id.iv_out_temp)
    ImageView ivOutTemp;
    @InjectView(R.id.iv_out_humidity)
    ImageView ivOutHumidity;
    @InjectView(R.id.iv_pm10)
    ImageView ivPm10;
    @InjectView(R.id.iv_o3)
    ImageView ivO3;
    @InjectView(R.id.iv_pm25)
    ImageView ivPm25;
    @InjectView(R.id.iv_co)
    ImageView ivCo;
    @InjectView(R.id.iv_temp)
    ImageView ivTemp;
    @InjectView(R.id.iv_humidity)
    ImageView ivHumidity;
    @InjectView(R.id.iv_hcho)
    ImageView ivHcho;
    @InjectView(R.id.iv_voc)
    ImageView ivVoc;
    @InjectView(R.id.tv_temp_company)
    TextView tvTempCompany;
    @InjectView(R.id.tv_humidity_company)
    TextView tvHumidityCompany;
    @InjectView(R.id.tv_formaldehyde_company)
    TextView tvFormaldehydeCompany;
    @InjectView(R.id.tv_voc_company)
    TextView tvVocCompany;
    GestureDetector mDetector;
    protected static final float FLIP_DISTANCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_para_detail);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        int index = getIntent().getIntExtra("index", 1);
        rlTitle.setBackgroundResource(R.color.detail_bg);
        setfontType();
        showPara(index);
        sendSerialData(new ReceDataFromMachine(new byte[]{}));

        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            /**
             *
             * e1 The first down motion event that started the fling. e2 The
             * move motion event that triggered the current onFling.
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向左滑...");
                    showPara(2);
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向右滑...");
                    showPara(1);
                    return true;
                }
                if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向上滑...");
                    return true;
                }
                if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向下滑...");
                    return true;
                }

                Log.d("TAG", e2.getX() + " " + e2.getY());

                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    private void setfontType() {
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), "AGENCYB.TTF");
        tvOutPm25Value.setTypeface(mTypeface);
        tvCoValue.setTypeface(mTypeface);
        tvAqiValue.setTypeface(mTypeface);
        tvPmValue.setTypeface(mTypeface);
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
    }

    //展示室内外参数
    //1 室内  2 室外
    private void showPara(int index) {
        if (index == 1) {
            if (llInpara.getVisibility() == View.VISIBLE) {
                return;
            }
            llInpara.setVisibility(View.VISIBLE);
            llInpara.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
            llOutpara.setVisibility(View.GONE);
            llOutpara.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_out));
            ivInParaIndex.setVisibility(View.VISIBLE);
            ivOutParaIndex.setVisibility(View.INVISIBLE);
        } else {
            if (llOutpara.getVisibility() == View.VISIBLE) {
                return;
            }
            llInpara.setVisibility(View.GONE);
            llInpara.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
            llOutpara.setVisibility(View.VISIBLE);
            llOutpara.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_in));
            ivInParaIndex.setVisibility(View.INVISIBLE);
            ivOutParaIndex.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ib_exit, R.id.tv_title_in_para, R.id.tv_title_out_para})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_exit:
                finish();
                break;
            case R.id.tv_title_in_para:
                showPara(1);
                break;
            case R.id.tv_title_out_para:
                showPara(2);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ChirldLock data) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {

        long current = System.currentTimeMillis();
        //室外数据
        if (MachineStatusForMrFrture.bOutDateEnable) {
            pbOutTemp.setVisibility(View.GONE);
            pbOutHumidity.setVisibility(View.GONE);
            pbOutPm25.setVisibility(View.GONE);
            pbAqi.setVisibility(View.GONE);
            pbPm10.setVisibility(View.GONE);
            pbO3.setVisibility(View.GONE);
            tvAqiValue.setVisibility(View.VISIBLE);
            tvOutPm25Value.setVisibility(View.VISIBLE);
            llOutTemp.setVisibility(View.VISIBLE);
            llOutHumidity.setVisibility(View.VISIBLE);
            llPm10.setVisibility(View.VISIBLE);
            llO3.setVisibility(View.VISIBLE);

            if (MachineStatusForMrFrture.aqi_outdoor > 999) {
                MachineStatusForMrFrture.aqi_outdoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.aqi_outdoor);
            tvAqiValue.setText(str);
            if (MachineStatusForMrFrture.aqi_outdoor < 36) {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_a));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 36 && MachineStatusForMrFrture.aqi_outdoor < 76) {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_b));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 76 && MachineStatusForMrFrture.aqi_outdoor < 116) {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_c));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 116 && MachineStatusForMrFrture.aqi_outdoor < 151) {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_d));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 151 && MachineStatusForMrFrture.aqi_outdoor < 301) {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_e));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                tvAqi.setText(getResources().getString(R.string.main_page_air_quality_f));
                tvAqiValue.setTextColor(getResources().getColor(R.color.color_938986));
            }
            if (MachineStatusForMrFrture.pm25_outdoor > 999) {
                MachineStatusForMrFrture.pm25_outdoor = 999;
            }
            String str1 = String.format("%03d", MachineStatusForMrFrture.pm25_outdoor);
            tvOutPm25Value.setText(str1);
            if (MachineStatusForMrFrture.pm25_outdoor < 36) {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_a));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 36 && MachineStatusForMrFrture.pm25_outdoor < 76) {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_b));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 76 && MachineStatusForMrFrture.pm25_outdoor < 116) {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_c));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 116 && MachineStatusForMrFrture.pm25_outdoor < 151) {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_d));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 151 && MachineStatusForMrFrture.pm25_outdoor < 301) {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_e));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                tvOutPm25.setText(getResources().getString(R.string.main_page_air_quality_f));
                tvOutPm25Value.setTextColor(getResources().getColor(R.color.color_938986));
            }
            tvOutTmp.setText("" + (MachineStatusForMrFrture.temp_outdoor - 20));
            tvOutHumidity.setText("" + MachineStatusForMrFrture.humidity_outdoor);
            tvPm10.setText("" + MachineStatusForMrFrture.pm10_outdoor);
            tvO3.setText("" + MachineStatusForMrFrture.o3_outdoor);
        }

        //室内数据
        if (MachineStatusForMrFrture.bRecvSerialDate) {

            pbTemp.setVisibility(View.GONE);
            pbHumidity.setVisibility(View.GONE);
            llTemp.setVisibility(View.VISIBLE);
            llHumidity.setVisibility(View.VISIBLE);

            if (MachineStatusForMrFrture.PM25_Indoor > 999) {
                MachineStatusForMrFrture.PM25_Indoor = 999;
            }
            LogUtils.d("pm 25 is " + MachineStatusForMrFrture.PM25_Indoor + " jiaquan   " + MachineStatusForMrFrture.HCHO_Quality + " voc " + MachineStatusForMrFrture.VOC_Quality);
            String str = String.format("%03d", MachineStatusForMrFrture.PM25_Indoor);
            tvPmValue.setText(str);
            if (MachineStatusForMrFrture.PM25_Indoor < 36) {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_a));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 36 && MachineStatusForMrFrture.PM25_Indoor < 76) {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_b));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 76 && MachineStatusForMrFrture.PM25_Indoor < 116) {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_c));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 116 && MachineStatusForMrFrture.PM25_Indoor < 151) {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_d));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 151 && MachineStatusForMrFrture.PM25_Indoor < 301) {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_e));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                tvPmQuality.setText(getResources().getString(R.string.main_page_air_quality_f));
                tvPmValue.setTextColor(getResources().getColor(R.color.color_938986));
            }
            String str1 = String.format("%04d", MachineStatusForMrFrture.CO2_value);
            tvCoValue.setText(str1);
            if (MachineStatusForMrFrture.CO2_value < 1001) {
                tvCoQuality.setText(getResources().getString(R.string.main_page_co_a));
                tvCoValue.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.CO2_value >= 1001 && MachineStatusForMrFrture.CO2_value < 1501) {
                tvCoQuality.setText(getResources().getString(R.string.main_page_co_b));
                tvCoValue.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else {
                tvCoQuality.setText(getResources().getString(R.string.main_page_co_c));
                tvCoValue.setTextColor(getResources().getColor(R.color.color_f7a77c));
            }
            tvTmp.setText("" + (MachineStatusForMrFrture.Temperature_Quality - 20));
            tvHumidity.setText("" + MachineStatusForMrFrture.humidity);

            float hcho = (float) MachineStatusForMrFrture.HCHO_Quality / 100f;
//            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//            String p = decimalFormat.format(hcho);//format 返回的是字符串
            tvFormaldehyde.setText(math(String.valueOf(hcho)));
            tvVoc.setText("" + MachineStatusForMrFrture.VOC_Quality);
            if (MachineStatusForMrFrture.VOC_Quality < 600) {
                tvVoc.setText(getResources().getString(R.string.voc_level_a));
            } else if (MachineStatusForMrFrture.VOC_Quality >= 600 && MachineStatusForMrFrture.VOC_Quality < 800) {
                tvVoc.setText(getResources().getString(R.string.voc_level_b));
            } else {
                tvVoc.setText(getResources().getString(R.string.voc_level_c));
            }

            //传感器故障
            setFaultUI();

            if (current - MachineStatusForMrFrture.startTime > 15 * 1000) {
                if (pbPm25.getVisibility() == View.VISIBLE) {
                    pbPm25.setVisibility(View.GONE);
                }
                if (tvPmValue.getVisibility() == View.GONE) {
                    tvPmValue.setVisibility(View.VISIBLE);
                }

                if (MachineStatusForMrFrture.fault_PM25) {
                    tvPmValue.setVisibility(View.GONE);
                    ivPm25.setVisibility(View.VISIBLE);
                }
            } else {
                LogUtils.d("tvpm visible " + tvPmValue.getVisibility());
                if (pbPm25.getVisibility() == View.GONE) {
                    pbPm25.setVisibility(View.VISIBLE);
                }
                if (tvPmValue.getVisibility() == View.VISIBLE) {
                    tvPmValue.setVisibility(View.GONE);
                }
                if (ivPm25.getVisibility() == View.VISIBLE) {
                    ivPm25.setVisibility(View.GONE);
                }
            }


            if (current - MachineStatusForMrFrture.startTime > 2 * 60 * 1000) {
                if (pbCo.getVisibility() == View.VISIBLE) {
                    pbCo.setVisibility(View.GONE);
                }
                if (tvCoValue.getVisibility() == View.GONE) {
                    tvCoValue.setVisibility(View.VISIBLE);
                }
                if (MachineStatusForMrFrture.fault_CO2) {
                    tvCoValue.setVisibility(View.GONE);
                    ivCo.setVisibility(View.VISIBLE);
                } else {
                    if (ivCo.getVisibility() == View.VISIBLE) {
                        ivCo.setVisibility(View.GONE);
                    }
                }

                if (pbHcho.getVisibility() == View.VISIBLE) {
                    pbHcho.setVisibility(View.GONE);
                }
                if (llHcho.getVisibility() == View.GONE) {
                    llHcho.setVisibility(View.VISIBLE);
                }
                if (MachineStatusForMrFrture.fault_HCHO) {
                    tvFormaldehyde.setVisibility(View.GONE);
                    tvFormaldehydeCompany.setVisibility(View.GONE);
                    ivHcho.setVisibility(View.VISIBLE);
                } else {
                    if (ivHcho.getVisibility() == View.VISIBLE) {
                        ivHcho.setVisibility(View.GONE);
                    }
                }

                if (pbVoc.getVisibility() == View.VISIBLE) {
                    pbVoc.setVisibility(View.GONE);
                }
                if (llVoc.getVisibility() == View.GONE) {
                    llVoc.setVisibility(View.VISIBLE);
                }
                if (MachineStatusForMrFrture.fault_VOC) {
                    tvVoc.setVisibility(View.GONE);
                    tvVocCompany.setVisibility(View.GONE);
                    ivVoc.setVisibility(View.VISIBLE);
                } else {
                    if (ivVoc.getVisibility() == View.VISIBLE) {
                        ivVoc.setVisibility(View.GONE);
                    }
                }
            } else {
                if (pbCo.getVisibility() == View.GONE) {
                    pbCo.setVisibility(View.VISIBLE);
                }
                if (tvCoValue.getVisibility() == View.VISIBLE) {
                    tvCoValue.setVisibility(View.GONE);
                }
                if (ivCo.getVisibility() == View.VISIBLE) {
                    ivCo.setVisibility(View.GONE);
                }

                if (pbHcho.getVisibility() == View.GONE) {
                    pbHcho.setVisibility(View.VISIBLE);
                }
                if (llHcho.getVisibility() == View.VISIBLE) {
                    llHcho.setVisibility(View.GONE);
                }
                if (ivHcho.getVisibility() == View.VISIBLE) {
                    ivHcho.setVisibility(View.GONE);
                }

                if (pbVoc.getVisibility() == View.GONE) {
                    pbVoc.setVisibility(View.VISIBLE);
                }
                if (llVoc.getVisibility() == View.VISIBLE) {
                    llVoc.setVisibility(View.GONE);
                }
                if (ivVoc.getVisibility() == View.VISIBLE) {
                    ivVoc.setVisibility(View.GONE);
                }
            }

            //状态栏
            setStatusBar();

        }
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
        if (!CommonUtils.isTopActivity(DetailParaActivity.this, "com.mingri.future.airfresh.activity.DetailParaActivity")) {
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
                co2Diag = new CO2AlertDialog(DetailParaActivity.this, new CO2AlertDialog.VocAletCallback() {
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
                ch4Diag = new CH4AlertDialog(DetailParaActivity.this, new CH4AlertDialog.VocAletCallback() {
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
                hchoDiag = new HCHOAlertDialog(DetailParaActivity.this, new HCHOAlertDialog.VocAletCallback() {
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
                vocDiag = new VocAlertDialog(DetailParaActivity.this, new VocAlertDialog.VocAletCallback() {
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
                chuDiag = new ChuxiaoLwAlertDialog(DetailParaActivity.this, new ChuxiaoLwAlertDialog.VocAletCallback() {
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
                zhongDiag = new ZhongxiaoLwAlertDialog(DetailParaActivity.this, new ZhongxiaoLwAlertDialog.VocAletCallback() {
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
                huoDiag = new HuoxingLwAlertDialog(DetailParaActivity.this, new HuoxingLwAlertDialog.VocAletCallback() {
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
                gaoDiag = new GaoxiaoLwAlertDialog(DetailParaActivity.this, new GaoxiaoLwAlertDialog.VocAletCallback() {
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
                uvcDiag = new UvcLwAlertDialog(DetailParaActivity.this, new UvcLwAlertDialog.VocAletCallback() {
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

    private void setFaultUI() {


        if (MachineStatusForMrFrture.fault_tmp) {
            tvTmp.setVisibility(View.GONE);
            tvTempCompany.setVisibility(View.GONE);
            ivTemp.setVisibility(View.VISIBLE);
        } else {
            tvTmp.setVisibility(View.VISIBLE);
            tvTempCompany.setVisibility(View.VISIBLE);
            ivTemp.setVisibility(View.GONE);
        }
        if (MachineStatusForMrFrture.fault_humidity) {
            tvHumidity.setVisibility(View.GONE);
            tvHumidityCompany.setVisibility(View.GONE);

            ivHumidity.setVisibility(View.VISIBLE);
        } else {
            tvHumidity.setVisibility(View.VISIBLE);
            tvHumidityCompany.setVisibility(View.VISIBLE);

            ivHumidity.setVisibility(View.GONE);
        }


    }

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

    private String math(String str) {
        if (TextUtils.isEmpty(str))
            return "0.00";
        int lenth = str.indexOf(".");
        if (lenth > 0) {
            if (str.substring(lenth+1, str.length()).length() >= 2)
                return str.substring(0, lenth + 3);
            if (str.substring(lenth+1, str.length()).length() == 1)
                return str += "0";
        } else
            return str += ".00";
        return "";
    }
}
