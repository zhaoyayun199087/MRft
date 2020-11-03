package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/6/26.
 * 室內数据
 */
public class MainDateFragOne extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static FragOneClick mFragOneClick;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainDateFragOne newInstance(int sectionNumber, FragOneClick fc) {
        MainDateFragOne fragment = new MainDateFragOne();
        mFragOneClick = fc;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainDateFragOne() {

    }


    private TextView name;
    private TextView date;
    private TextView company;
    private TextView info;
    private ProgressBar pb;
    private ImageView ivFault;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        type = getArguments().getInt(ARG_SECTION_NUMBER);
        LogUtils.d("main date frag create " + type);
        View rootView = inflater.inflate(R.layout.fragment_mainpage_one, container, false);
        name = (TextView) rootView.findViewById(R.id.tv_name);
        date = (TextView) rootView.findViewById(R.id.tv_date);
        company = (TextView) rootView.findViewById(R.id.tv_company);
        info = (TextView) rootView.findViewById(R.id.tv_info);
        pb = (ProgressBar) rootView.findViewById(R.id.pb_loading);
        ivFault = (ImageView) rootView.findViewById(R.id.iv_falut);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragOneClick.dateClick();
            }
        });

        ivFault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragOneClick.dateClick();
            }
        });

        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragOneClick.dateClick();
            }
        });

        Typeface mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYB.TTF");
        date.setTypeface(mTypeface);
        company.setTypeface(mTypeface);


        if (type == 1) {
            company.setVisibility(View.GONE);
            name.setText(getResources().getString(R.string.main_page_inner_pm));
            if (MachineStatusForMrFrture.PM25_Indoor > 999) {
                MachineStatusForMrFrture.PM25_Indoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.PM25_Indoor);
            date.setText(str);
            if (MachineStatusForMrFrture.PM25_Indoor < 36) {
                info.setText(getResources().getString(R.string.main_page_air_quality_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 36 && MachineStatusForMrFrture.PM25_Indoor < 76) {
                info.setText(getResources().getString(R.string.main_page_air_quality_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 76 && MachineStatusForMrFrture.PM25_Indoor < 116) {
                info.setText(getResources().getString(R.string.main_page_air_quality_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 116 && MachineStatusForMrFrture.PM25_Indoor < 151) {
                info.setText(getResources().getString(R.string.main_page_air_quality_d));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.PM25_Indoor >= 151 && MachineStatusForMrFrture.PM25_Indoor < 301) {
                info.setText(getResources().getString(R.string.main_page_air_quality_e));
                date.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                info.setText(getResources().getString(R.string.main_page_air_quality_f));
                date.setTextColor(getResources().getColor(R.color.color_938986));
            }
            //故障
            if (MachineStatusForMrFrture.fault_PM25) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            }

            //开机15秒后才显示数据
            if (System.currentTimeMillis() - MachineStatusForMrFrture.startTime <= 15 * 1000) {
                pb.setVisibility(View.VISIBLE);
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.GONE);
                info.setText("...");
            }

        } else if (type == 2) {
            company.setVisibility(View.GONE);
            name.setText(getResources().getString(R.string.main_page_inner_co2));
            String str = String.format("%04d", MachineStatusForMrFrture.CO2_value);
            date.setText(str);
            if (MachineStatusForMrFrture.CO2_value < 1001) {
                info.setText(getResources().getString(R.string.main_page_co_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.CO2_value >= 1001 && MachineStatusForMrFrture.CO2_value < 1501) {
                info.setText(getResources().getString(R.string.main_page_co_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else {
                info.setText(getResources().getString(R.string.main_page_co_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            }
            if (MachineStatusForMrFrture.fault_CO2) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            }

            //开机2分钟后才显示数据
            if (System.currentTimeMillis() - MachineStatusForMrFrture.startTime <= 2 * 60 * 1000) {
                pb.setVisibility(View.VISIBLE);
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.GONE);
                info.setText("...");
            }

        } else if (type == 3) {
            company.setText("℃");
            name.setText(getResources().getString(R.string.main_page_inner_tmp));

            if (MachineStatusForMrFrture.Temperature_Quality < -15) {
                MachineStatusForMrFrture.Temperature_Quality = -15;
            }
            if (MachineStatusForMrFrture.Temperature_Quality > 70) {
                MachineStatusForMrFrture.Temperature_Quality = 70;
            }
            String str = String.format("%02d", (MachineStatusForMrFrture.Temperature_Quality - 20));
            date.setText(str);
            info.setText(getResources().getString(R.string.main_page_temp));

            if (MachineStatusForMrFrture.Temperature_Quality < 20) {
                company.setTextColor(getResources().getColor(R.color.color_ffffff));
                date.setTextColor(getResources().getColor(R.color.color_ffffff));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 20 && MachineStatusForMrFrture.Temperature_Quality < 35) {
                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 35 && MachineStatusForMrFrture.Temperature_Quality < 45) {
                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 45 && MachineStatusForMrFrture.Temperature_Quality < 55) {
                company.setTextColor(getResources().getColor(R.color.color_f7a77c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else {
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            }
            if (MachineStatusForMrFrture.fault_tmp) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            }
        } else {
            company.setText("%");
            name.setText(getResources().getString(R.string.main_page_inner_humidity));
            if (MachineStatusForMrFrture.humidity < 20) {
                MachineStatusForMrFrture.humidity = 20;
            }
            if (MachineStatusForMrFrture.humidity > 100) {
                MachineStatusForMrFrture.humidity = 100;
            }
            String str = String.format("%02d", MachineStatusForMrFrture.humidity);
            if (MachineStatusForMrFrture.humidity < 40) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.humidity >= 40 && MachineStatusForMrFrture.humidity < 71) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }
            date.setText(str);

            if (MachineStatusForMrFrture.fault_humidity) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            }
        }
        if (!MachineStatusForMrFrture.bRecvSerialDate) {
            pb.setVisibility(View.VISIBLE);
            date.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            ivFault.setVisibility(View.GONE);
            info.setText("...");
        } else {
            ivFault.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            company.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void updateUI() {
        type = getArguments().getInt(ARG_SECTION_NUMBER);

        LogUtils.d("type is " + type + "  " + (System.currentTimeMillis() - MachineStatusForMrFrture.startTime));
        if (!MachineStatusForMrFrture.bRecvSerialDate) {
            pb.setVisibility(View.VISIBLE);
            date.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            return;
        } else {
            pb.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            company.setVisibility(View.VISIBLE);
        }

        if (type == 1) {
            //开机15秒后才显示数据
            if (System.currentTimeMillis() - MachineStatusForMrFrture.startTime <= 15 * 1000) {
                pb.setVisibility(View.VISIBLE);
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.GONE);
                info.setText("...");
            } else {
                pb.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                ivFault.setVisibility(View.VISIBLE);
                company.setVisibility(View.GONE);
                name.setText(getResources().getString(R.string.main_page_inner_pm));
                if (MachineStatusForMrFrture.PM25_Indoor > 999) {
                    MachineStatusForMrFrture.PM25_Indoor = 999;
                }
                String str = String.format("%03d", MachineStatusForMrFrture.PM25_Indoor);
                date.setText(str);
                if (MachineStatusForMrFrture.PM25_Indoor < 36) {
                    info.setText(getResources().getString(R.string.main_page_air_quality_a));
                    date.setTextColor(getResources().getColor(R.color.color_8bea78));
                } else if (MachineStatusForMrFrture.PM25_Indoor >= 36 && MachineStatusForMrFrture.PM25_Indoor < 76) {
                    info.setText(getResources().getString(R.string.main_page_air_quality_b));
                    date.setTextColor(getResources().getColor(R.color.color_77cef4));
                } else if (MachineStatusForMrFrture.PM25_Indoor >= 76 && MachineStatusForMrFrture.PM25_Indoor < 116) {
                    info.setText(getResources().getString(R.string.main_page_air_quality_c));
                    date.setTextColor(getResources().getColor(R.color.color_f7a77c));
                } else if (MachineStatusForMrFrture.PM25_Indoor >= 116 && MachineStatusForMrFrture.PM25_Indoor < 151) {
                    info.setText(getResources().getString(R.string.main_page_air_quality_d));
                    date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                } else if (MachineStatusForMrFrture.PM25_Indoor >= 151 && MachineStatusForMrFrture.PM25_Indoor < 301) {
                    info.setText(getResources().getString(R.string.main_page_air_quality_e));
                    date.setTextColor(getResources().getColor(R.color.color_a97dec));
                } else {
                    info.setText(getResources().getString(R.string.main_page_air_quality_f));
                    date.setTextColor(getResources().getColor(R.color.color_938986));
                }
                if (MachineStatusForMrFrture.fault_PM25) {
                    date.setVisibility(View.GONE);
                    company.setVisibility(View.GONE);
                    ivFault.setVisibility(View.VISIBLE);
                    info.setText(getString(R.string.all_fault));
                } else {
                    ivFault.setVisibility(View.GONE);
                }
            }
        } else if (type == 2) {

            //开机2分钟后才显示数据
            if (System.currentTimeMillis() - MachineStatusForMrFrture.startTime <= 2 * 60 * 1000) {
                pb.setVisibility(View.VISIBLE);
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.GONE);
                info.setText("...");
            } else {
                pb.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                ivFault.setVisibility(View.VISIBLE);
                company.setVisibility(View.GONE);
                name.setText(getResources().getString(R.string.main_page_inner_co2));
                String str = String.format("%04d", MachineStatusForMrFrture.CO2_value);
                date.setText(str);
                if (MachineStatusForMrFrture.CO2_value < 1001) {
                    info.setText(getResources().getString(R.string.main_page_co_a));
                    date.setTextColor(getResources().getColor(R.color.color_8bea78));
                } else if (MachineStatusForMrFrture.CO2_value >= 1001 && MachineStatusForMrFrture.CO2_value < 1501) {
                    info.setText(getResources().getString(R.string.main_page_co_b));
                    date.setTextColor(getResources().getColor(R.color.color_77cef4));
                } else {
                    info.setText(getResources().getString(R.string.main_page_co_c));
                    date.setTextColor(getResources().getColor(R.color.color_f7a77c));
                }
                if (MachineStatusForMrFrture.fault_CO2) {
                    date.setVisibility(View.GONE);
                    company.setVisibility(View.GONE);
                    ivFault.setVisibility(View.VISIBLE);
                    info.setText(getString(R.string.all_fault));
                } else {
                    ivFault.setVisibility(View.GONE);
                }
            }
        } else if (type == 3) {
            company.setText("℃");
            name.setText(getResources().getString(R.string.main_page_inner_tmp));

            if (MachineStatusForMrFrture.Temperature_Quality < -15) {
                MachineStatusForMrFrture.Temperature_Quality = -15;
            }
            if (MachineStatusForMrFrture.Temperature_Quality > 70) {
                MachineStatusForMrFrture.Temperature_Quality = 70;
            }
            String str = String.format("%02d", (MachineStatusForMrFrture.Temperature_Quality - 20));
            info.setText(getResources().getString(R.string.main_page_temp));
            date.setText(str);
            if (MachineStatusForMrFrture.Temperature_Quality < 20) {
                company.setTextColor(getResources().getColor(R.color.color_ffffff));
                date.setTextColor(getResources().getColor(R.color.color_ffffff));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 20 && MachineStatusForMrFrture.Temperature_Quality < 35) {
                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 35 && MachineStatusForMrFrture.Temperature_Quality < 45) {
                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.Temperature_Quality >= 45 && MachineStatusForMrFrture.Temperature_Quality < 55) {
                company.setTextColor(getResources().getColor(R.color.color_f7a77c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else {
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            }
            if (MachineStatusForMrFrture.fault_tmp) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            } else {
                ivFault.setVisibility(View.GONE);
            }
        } else {
            company.setText("%");
            name.setText(getResources().getString(R.string.main_page_inner_humidity));
            if (MachineStatusForMrFrture.humidity < 20) {
                MachineStatusForMrFrture.humidity = 20;
            }
            if (MachineStatusForMrFrture.humidity > 100) {
                MachineStatusForMrFrture.humidity = 100;
            }
            String str = String.format("%02d", MachineStatusForMrFrture.humidity);
            if (MachineStatusForMrFrture.humidity < 40) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));

                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.humidity >= 40 && MachineStatusForMrFrture.humidity < 71) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_8bea78));

                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_77cef4));

                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }
            date.setText(str);
            if (MachineStatusForMrFrture.fault_humidity) {
                date.setVisibility(View.GONE);
                company.setVisibility(View.GONE);
                ivFault.setVisibility(View.VISIBLE);
                info.setText(getString(R.string.all_fault));
            } else {
                ivFault.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新数据
     */
    public void updateDate() {
        LogUtils.d("update date ");
        updateUI();
    }

    public interface FragOneClick {
        public void dateClick();
    }
}