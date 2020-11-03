package com.mingri.future.airfresh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.view.ClockView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;

/**
 * Created by Administrator on 2017/7/6.
 */
public class ClockFragment extends BaseFragment {

    private ClockView cvClock;
    private ImageButton btnClock;
    private int iClockStatus = 0;
    private int iClock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_clock, null);
        EventBus.getDefault().register(this);
        cvClock = (ClockView) v.findViewById(R.id.cv_clock);
        btnClock = (ImageButton) v.findViewById(R.id.btn_clock);

        // 0 关闭  1.设置了时间但未打开  2.打开了时间
        iClockStatus = (Integer) SPUtils.get(getActivity(), "shutswitch", 0);
        if (iClockStatus == 0) {
            btnClock.setVisibility(View.GONE);
        } else if (iClockStatus == 1) {
            btnClock.setVisibility(View.VISIBLE);
            btnClock.setImageResource(R.mipmap.clock_begin);
        } else {
            btnClock.setVisibility(View.VISIBLE);
            btnClock.setImageResource(R.mipmap.clock_reset);
        }
        sendSerialData(new ReceDataFromMachine(new byte[]{}));

        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iClockStatus == 2) {
                    iClockStatus = 0;
                    btnClock.setVisibility(View.GONE);
                    cvClock.setClock(0);
                    long current = System.currentTimeMillis();
                    SPUtils.put(activity, "setshuttime", "" + current);
                    SPUtils.put(activity, "shuttime", 0);
                    SPUtils.put(getActivity(), "shutswitch", 0);
                    Log.i("textShow", "btnClock2");
                    MachineStatusForMrFrture.Switch_Clock = false;
                    cvClock.setClock((int) (0));
                    cvClock.setClockValue(0);
                }

                if (iClockStatus == 1) {
                    btnClock.setImageResource(R.mipmap.clock_reset);
                    SPUtils.put(getActivity(), "shutswitch", 2);
                    long current = System.currentTimeMillis();
                    SPUtils.put(activity, "setshuttime", "" + current);
                    SPUtils.put(activity, "shuttime", iClock);
                    Log.i("textShow", "btnClock1");

                    MachineStatusForMrFrture.Switch_Clock = true;
                    iClockStatus = 2;
                }

                if (iClockStatus == 0) {
                    SPUtils.put(getActivity(), "shutswitch", 1);
                    MachineStatusForMrFrture.Switch_Clock = false;
                    Log.i("textShow", "btnClock0");
                    iClockStatus = 1;
                    btnClock.setImageResource(R.mipmap.clock_begin);
                }
            }
        });

        cvClock.setCallBack(new ClockView.Callback() {
            @Override
            public void setClock(int clock) {
                if (clock == 0) {
                    iClockStatus = 0;
                    btnClock.setVisibility(View.GONE);
                    cvClock.setClock(0);
                    long current = System.currentTimeMillis();
                    SPUtils.put(activity, "setshuttime", "" + current);
                    SPUtils.put(activity, "shuttime", 0);
                    SPUtils.put(getActivity(), "shutswitch", 0);
                    MachineStatusForMrFrture.Switch_Clock = false;

                } else {
                    btnClock.setVisibility(View.VISIBLE);
                    iClock = clock;
                    long current = System.currentTimeMillis();
                    Log.i("textShow", "current = " + current);
                    SPUtils.put(activity, "setshuttime", "" + current);
                    SPUtils.put(activity, "shuttime", iClock);
                    btnClock.setImageResource(R.mipmap.clock_begin);
                    SPUtils.put(getActivity(), "shutswitch", 2);
                    iClockStatus = 2;
                    MachineStatusForMrFrture.Switch_Clock = true;
                }
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private boolean isVisible = true;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = !hidden;
        sendSerialData(new ReceDataFromMachine(new byte[]{}));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        if (isVisible) {
            iClockStatus = (Integer) SPUtils.get(getActivity(), "shutswitch", 0);
            String sSettime = (String) SPUtils.get(activity, "setshuttime", "0");
            long settime = Long.parseLong(sSettime);
            int shuttime = (int) SPUtils.get(activity, "shuttime", 0);
            long current = System.currentTimeMillis();
            long shut = settime + shuttime * 1000 * 60 * 60;
            long time = (shut - current) / (1000);
            Log.i("textShow", "time1 = " + time);
            LogUtils.d("left time " + time);
            if (time > 100000 || time < 0) {        //后面修改|| time < 0
                return;
            }
            //设备向客户端发送相应的倒计时关机时间
            MachineStatusForMrFrture.Timing_Off = (int) (time / 60);
            Log.i("textShow", "Timing_Off = " + MachineStatusForMrFrture.Timing_Off);
            if (iClockStatus == 2) {
                if (time <= 4) {
                    cvClock.setClock((int) (0));
                    cvClock.setClockValue(0);
                } else {
                    cvClock.setClock((int) (time / 3600 + 1));
                    cvClock.setClockValue(time);
                }
            }
            if (iClockStatus == 0) {
                cvClock.setClock((int) (0));
                cvClock.setClockValue(0);
            }
            if (time == 0) {
                btnClock.setVisibility(View.GONE);
            }
        }
    }
}
