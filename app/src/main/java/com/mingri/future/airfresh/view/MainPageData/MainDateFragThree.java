package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.activity.TestUpdataActivity;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;

/**
 * Created by Administrator on 2017/6/26.
 * 运行模式
 */
public class MainDateFragThree extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ModeClick mModeClick;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainDateFragThree newInstance(int sectionNumber, ModeClick mc) {
        MainDateFragThree fragment = new MainDateFragThree();
        mModeClick = mc;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainDateFragThree() {

    }

    private TextView info;
    private ImageButton ib;
    private TextView tvDate, tvCompany,tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int type = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_mainpage_three, container, false);

        info = (TextView) rootView.findViewById(R.id.tv_info);
        ib = (ImageButton) rootView.findViewById(R.id.ib_config_net);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        tvCompany = (TextView) rootView.findViewById(R.id.tv_company);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);

        updateUI();

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModeClick.callback();
            }
        });

        tvName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mModeClick.longClick(motionEvent);
                return false;
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModeClick.callback();
            }
        });
        return rootView;
    }

    //更改首页运行模式标志
    private void updateUI() {
        switch (MachineStatusForMrFrture.Mode) {
            case 0:
                ib.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.GONE);
                tvCompany.setVisibility(View.GONE);
                ib.setImageResource(R.mipmap.icon_zhin);
                info.setText(getResources().getString(R.string.main_page_mode_smart));
                break;

            case 1:
                ib.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.GONE);
                tvCompany.setVisibility(View.GONE);
                ib.setImageResource(R.mipmap.icon_jis);
                info.setText(getResources().getString(R.string.main_page_mode_speed));
                break;

            case 2:
                ib.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.GONE);
                tvCompany.setVisibility(View.GONE);
                ib.setImageResource(R.mipmap.icon_shuim);
                info.setText(getResources().getString(R.string.main_page_mode_sleep));
                break;

            case 3:
                ib.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.GONE);
                tvCompany.setVisibility(View.GONE);
                ib.setImageResource(R.mipmap.icon_shus);
                info.setText(getResources().getString(R.string.main_page_mode_comfortable));
                break;

            case 4:
                ib.setVisibility(View.GONE);
                tvDate.setVisibility(View.VISIBLE);
//                tvCompany.setVisibility(View.VISIBLE);
                tvDate.setText("" + (MachineStatusForMrFrture.Wind_Velocity + 1) + getResources().getString(R.string.level));
                info.setText(getResources().getString(R.string.main_page_mode_custom));
                break;
        }
    }

    public void updateDate() {
        updateUI();
    }


    public interface ModeClick {
        public void callback();
        public void longClick(MotionEvent motionEvent);
    }
}