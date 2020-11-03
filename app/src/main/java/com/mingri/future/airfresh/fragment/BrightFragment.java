package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.view.ArcProgressbar;
import com.mingri.future.airfresh.view.BrightControlView;

import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;

/**
 * Created by Administrator on 2017/7/11.
 */
public class BrightFragment extends BaseFragment {
    private ArcProgressbar apLight;
    private TextView tvLight;
    private ScreenBrightnessTool  screenBrightnessTool;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frg_bright,null);
        initData();
        initView(view);

        return view;
    }

    private void initData() {
        screenBrightnessTool= ScreenBrightnessTool.Builder(activity);
    }


    private void initView(View view) {
        apLight= (ArcProgressbar) view.findViewById(R.id.bright_bar);
        tvLight = (TextView) view.findViewById(R.id.tv_light);
        apLight.setAngleListener(new ArcProgressbar.BrightAngleListener() {
            @Override
            public void curAngle(int angle) {
                LogUtils.d("angle is " + angle);
                screenBrightnessTool.setBrightness1(angle);
                tvLight.setText("" + (5*( angle - 1 ))/4 );
                SPUtils.put(getActivity(),"light" , angle);
            }
        });

        int angle = (int) SPUtils.get(getActivity(),"light", 40);
        apLight.setAngle(angle);
        tvLight.setText("" + (5*( angle - 1 ))/4 );
    }

}
