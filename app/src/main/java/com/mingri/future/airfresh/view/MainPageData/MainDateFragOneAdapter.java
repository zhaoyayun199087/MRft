package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/6/26.
 * 室内数据显示适配器
 */
public class MainDateFragOneAdapter extends FragmentPagerAdapter {

    private MainDateFragOne mainDateFragOne[] = new MainDateFragOne[1000];
    private MainDateFragOne.FragOneClick mFragOneClick;
    public MainDateFragOneAdapter(FragmentManager fm, MainDateFragOne.FragOneClick fc) {
        super(fm);
        mFragOneClick = fc;
    }

    @Override
    public Fragment getItem(int position) {
        LogUtils.d("main frag one get iten " + position);
        if( mainDateFragOne[position ] == null  ){
            mainDateFragOne[position ] = MainDateFragOne.newInstance(position %4+ 1,mFragOneClick );
        }
        return mainDateFragOne[position];
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1000;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position = position % 4;
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
                return "PAGE 3";
            case 3:
                return "PAGE 4";
            case 4:
                return "PAGE 5";
        }
        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i< mainDateFragOne.length; i++){
                    if( mainDateFragOne[i] != null ){
                        mainDateFragOne[i].updateDate();
                    }
                }
            }
        },100);
    }
}