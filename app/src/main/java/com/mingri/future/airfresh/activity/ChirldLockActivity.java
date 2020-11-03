package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragOne;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragOneAdapter;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragThree;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragThreeAdapter;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragTwo;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragTwoAdapter;
import com.mingri.future.airfresh.view.MainPageData.VerticalViewPager;
import com.mingri.future.airfresh.view.SlideUnlockView;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;

/**
 * Created by Administrator on 2017/6/21.
 */
public class ChirldLockActivity extends Activity {
    private StringBuffer buffer = new StringBuffer();
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chirld_lock);
        tvError = (TextView) findViewById(R.id.tv_error);
        tvError.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_e:
                buffer.append("1");
                break;
            case R.id.tv_f:
                buffer.append("2");
                break;
            case R.id.tv_l:
                buffer.append("3");
                break;
            case R.id.tv_a:
                buffer.append("4");
                break;
            case R.id.tv_b:
                buffer.append("5");
                break;
            case R.id.tv_c:
                buffer.append("6");
                break;
            case R.id.tv_d:
                buffer.append("7");
                break;
            case R.id.tv_g:
                buffer.append("8");
                break;
            case R.id.tv_h:
                buffer.append("9");
                break;
            case R.id.tv_i:
                buffer.append("a");
                break;
            case R.id.tv_j:
                buffer.append("b");
                break;
            case R.id.tv_k:
                buffer.append("c");
                break;
        }

        detectSecret();
    }

    private void detectSecret() {

        if (buffer.length() == 4) {
            if (buffer.toString().equals("1234")) {
                buffer.delete(0, buffer.length());
                finish();
            } else {
                tvError.setVisibility(View.VISIBLE);
                buffer.delete(0, buffer.length());
                tvError.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvError.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        } else if (buffer.length() > 4) {
            tvError.setVisibility(View.VISIBLE);
            buffer.delete(0, buffer.length());
            tvError.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvError.setVisibility(View.INVISIBLE);
                }
            }, 3000);
        }
    }
}
