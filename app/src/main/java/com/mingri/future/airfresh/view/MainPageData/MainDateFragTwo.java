package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.SPUtils;

/**
 * Created by Administrator on 2017/6/26.
 * 室外数据
 */
public class MainDateFragTwo extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static OutParaViewClick mSetWifi;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainDateFragTwo newInstance(int sectionNumber, OutParaViewClick sw) {
        MainDateFragTwo fragment = new MainDateFragTwo();
        mSetWifi = sw;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainDateFragTwo() {

    }

    private TextView name;
    private TextView date;
    private TextView company;
    private TextView info;
    private ImageButton ib;
    String city = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int type = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = inflater.inflate(R.layout.fragment_mainpage_two, container, false);
        name = (TextView) rootView.findViewById(R.id.tv_name);
        date = (TextView) rootView.findViewById(R.id.tv_date);
        company = (TextView) rootView.findViewById(R.id.tv_company);
        info = (TextView) rootView.findViewById(R.id.tv_info);
        ib = (ImageButton) rootView.findViewById(R.id.ib_config_net);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetWifi.setWifi();
            }
        });
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetWifi.setWifi();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetWifi.dateClick();
            }
        });

        Typeface mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYB.TTF");
        date.setTypeface(mTypeface);
        company.setTypeface(mTypeface);

        city = (String) SPUtils.get(getActivity(), "city_name","");
        if( city.length() > 1 ){
            city = city.substring(0, city.length() -1);
        }
        if (type == 1) {
            company.setVisibility(View.GONE);
            name.setText(city + "AQI");
            if (MachineStatusForMrFrture.aqi_outdoor > 999) {
                MachineStatusForMrFrture.aqi_outdoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.aqi_outdoor);
            date.setText(str);
            if (MachineStatusForMrFrture.aqi_outdoor < 36) {
                info.setText(getResources().getString(R.string.main_page_air_quality_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 36 && MachineStatusForMrFrture.aqi_outdoor < 76) {
                info.setText(getResources().getString(R.string.main_page_air_quality_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 76 && MachineStatusForMrFrture.aqi_outdoor < 116) {
                info.setText(getResources().getString(R.string.main_page_air_quality_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 116 && MachineStatusForMrFrture.aqi_outdoor < 151) {
                info.setText(getResources().getString(R.string.main_page_air_quality_d));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 151 && MachineStatusForMrFrture.aqi_outdoor < 301) {
                info.setText(getResources().getString(R.string.main_page_air_quality_e));
                date.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                info.setText(getResources().getString(R.string.main_page_air_quality_f));
                date.setTextColor(getResources().getColor(R.color.color_938986));
            }
        } else if (type == 2) {
            company.setVisibility(View.GONE);
            name.setText(city + "PM25");
            if (MachineStatusForMrFrture.pm25_outdoor > 999) {
                MachineStatusForMrFrture.pm25_outdoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.pm25_outdoor);
            date.setText(str);
            if (MachineStatusForMrFrture.pm25_outdoor < 36) {
                info.setText(getResources().getString(R.string.main_page_air_quality_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 36 && MachineStatusForMrFrture.pm25_outdoor < 76) {
                info.setText(getResources().getString(R.string.main_page_air_quality_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 76 && MachineStatusForMrFrture.pm25_outdoor < 116) {
                info.setText(getResources().getString(R.string.main_page_air_quality_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 116 && MachineStatusForMrFrture.pm25_outdoor < 151) {
                info.setText(getResources().getString(R.string.main_page_air_quality_d));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 151 && MachineStatusForMrFrture.pm25_outdoor < 301) {
                info.setText(getResources().getString(R.string.main_page_air_quality_e));
                date.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                info.setText(getResources().getString(R.string.main_page_air_quality_f));
                date.setTextColor(getResources().getColor(R.color.color_938986));
            }
        } else if (type == 3) {
            company.setText("℃");
            name.setText(city + "温度");
            if (MachineStatusForMrFrture.temp_outdoor < -35) {
                MachineStatusForMrFrture.temp_outdoor = -35;
            }
            if (MachineStatusForMrFrture.temp_outdoor > 50) {
                MachineStatusForMrFrture.temp_outdoor = 50;
            }
            if (MachineStatusForMrFrture.temp_outdoor < 0) {
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_ffffff));
                date.setTextColor(getResources().getColor(R.color.color_ffffff));
            } else if (MachineStatusForMrFrture.temp_outdoor >= 0 && MachineStatusForMrFrture.temp_outdoor < 15) {
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else  if (MachineStatusForMrFrture.temp_outdoor >= 15 && MachineStatusForMrFrture.temp_outdoor < 25){
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }else  if (MachineStatusForMrFrture.temp_outdoor >= 25 && MachineStatusForMrFrture.temp_outdoor < 35){
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_f7a77c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            }else{
                info.setText(getResources().getString(R.string.main_page_temp));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            }
            String str = String.format("%02d", MachineStatusForMrFrture.temp_outdoor);
            date.setText(str);
        } else {
            company.setText("%");
            name.setText(city + "湿度");
            if (MachineStatusForMrFrture.humidity_outdoor < 20) {
                MachineStatusForMrFrture.humidity_outdoor = 20;
            }
            if (MachineStatusForMrFrture.humidity_outdoor > 100) {
                MachineStatusForMrFrture.humidity_outdoor = 100;
            }
            String str = String.format("%02d", MachineStatusForMrFrture.humidity_outdoor);
            if (MachineStatusForMrFrture.humidity_outdoor < 40) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.humidity_outdoor >= 40 && MachineStatusForMrFrture.humidity_outdoor < 71) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }
            date.setText(str);
        }
        if (!MachineStatusForMrFrture.bOutDateEnable) {
            ib.setVisibility(View.VISIBLE);
            date.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            info.setText(getString(R.string.main_page_config_net));
        } else {
            ib.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            company.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    public void updateDate() {
        city = (String) SPUtils.get(getActivity(), "city_name","");
        if( city.length() > 1 ){
            city = city.substring(0, city.length() -1);
        }
        int type = getArguments().getInt(ARG_SECTION_NUMBER);
        if (!MachineStatusForMrFrture.bOutDateEnable) {
            ib.setVisibility(View.VISIBLE);
            date.setVisibility(View.GONE);
            company.setVisibility(View.GONE);
            return
                    ;
        } else {
            ib.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            company.setVisibility(View.VISIBLE);
        }
        if (type == 1) {
            company.setVisibility(View.GONE);
            name.setText(city + "AQI");
            if (MachineStatusForMrFrture.aqi_outdoor > 999) {
                MachineStatusForMrFrture.aqi_outdoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.aqi_outdoor);
            date.setText(str);
            if (MachineStatusForMrFrture.aqi_outdoor < 36) {
                info.setText(getResources().getString(R.string.main_page_air_quality_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 36 && MachineStatusForMrFrture.aqi_outdoor < 76) {
                info.setText(getResources().getString(R.string.main_page_air_quality_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 76 && MachineStatusForMrFrture.aqi_outdoor < 116) {
                info.setText(getResources().getString(R.string.main_page_air_quality_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 116 && MachineStatusForMrFrture.aqi_outdoor < 151) {
                info.setText(getResources().getString(R.string.main_page_air_quality_d));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.aqi_outdoor >= 151 && MachineStatusForMrFrture.aqi_outdoor < 301) {
                info.setText(getResources().getString(R.string.main_page_air_quality_e));
                date.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                info.setText(getResources().getString(R.string.main_page_air_quality_f));
                date.setTextColor(getResources().getColor(R.color.color_938986));
            }
        } else if (type == 2) {
            company.setVisibility(View.GONE);
            name.setText(city + "PM25");
            if (MachineStatusForMrFrture.pm25_outdoor > 999) {
                MachineStatusForMrFrture.pm25_outdoor = 999;
            }
            String str = String.format("%03d", MachineStatusForMrFrture.pm25_outdoor);
            date.setText(str);
            if (MachineStatusForMrFrture.pm25_outdoor < 36) {
                info.setText(getResources().getString(R.string.main_page_air_quality_a));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 36 && MachineStatusForMrFrture.pm25_outdoor < 76) {
                info.setText(getResources().getString(R.string.main_page_air_quality_b));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 76 && MachineStatusForMrFrture.pm25_outdoor < 116) {
                info.setText(getResources().getString(R.string.main_page_air_quality_c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 116 && MachineStatusForMrFrture.pm25_outdoor < 151) {
                info.setText(getResources().getString(R.string.main_page_air_quality_d));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.pm25_outdoor >= 151 && MachineStatusForMrFrture.pm25_outdoor < 301) {
                info.setText(getResources().getString(R.string.main_page_air_quality_e));
                date.setTextColor(getResources().getColor(R.color.color_a97dec));
            } else {
                info.setText(getResources().getString(R.string.main_page_air_quality_f));
                date.setTextColor(getResources().getColor(R.color.color_938986));
            }
        } else if (type == 3) {
            company.setText("℃");
            name.setText(city + "温度");
            if (MachineStatusForMrFrture.temp_outdoor < -15) {
                MachineStatusForMrFrture.temp_outdoor = -15;
            }
            if (MachineStatusForMrFrture.temp_outdoor > 70) {
                MachineStatusForMrFrture.temp_outdoor = 70;
            }
            if (MachineStatusForMrFrture.temp_outdoor < 20) {
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_ffffff));
                date.setTextColor(getResources().getColor(R.color.color_ffffff));
            } else if (MachineStatusForMrFrture.temp_outdoor >= 20 && MachineStatusForMrFrture.temp_outdoor < 35) {
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else  if (MachineStatusForMrFrture.temp_outdoor >= 35 && MachineStatusForMrFrture.temp_outdoor < 45){
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }else  if (MachineStatusForMrFrture.temp_outdoor >= 45 && MachineStatusForMrFrture.temp_outdoor < 55){
                info.setText(getResources().getString(R.string.main_page_temp));

                company.setTextColor(getResources().getColor(R.color.color_f7a77c));
                date.setTextColor(getResources().getColor(R.color.color_f7a77c));
            }else{
                info.setText(getResources().getString(R.string.main_page_temp));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            }
            String str = String.format("%02d", (MachineStatusForMrFrture.temp_outdoor));
            date.setText(str);
        } else {
            company.setText("%");
            name.setText(city + "湿度");
            if (MachineStatusForMrFrture.humidity_outdoor < 20) {
                MachineStatusForMrFrture.humidity_outdoor = 20;
            }
            if (MachineStatusForMrFrture.humidity_outdoor > 100) {
                MachineStatusForMrFrture.humidity_outdoor = 100;
            }
            String str = String.format("%02d", MachineStatusForMrFrture.humidity_outdoor);
            if (MachineStatusForMrFrture.humidity_outdoor < 40) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_fa6f6f));
                date.setTextColor(getResources().getColor(R.color.color_fa6f6f));
            } else if (MachineStatusForMrFrture.humidity_outdoor >= 40 && MachineStatusForMrFrture.humidity_outdoor < 71) {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_8bea78));
                date.setTextColor(getResources().getColor(R.color.color_8bea78));
            } else {
                info.setText(getResources().getString(R.string.main_page_relative_humidity));
                company.setTextColor(getResources().getColor(R.color.color_77cef4));
                date.setTextColor(getResources().getColor(R.color.color_77cef4));
            }
            date.setText(str);
        }
    }

    public interface OutParaViewClick {
        public void setWifi();

        public void dateClick();

    }

}