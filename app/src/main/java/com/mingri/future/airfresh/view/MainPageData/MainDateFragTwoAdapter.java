package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by Administrator on 2017/6/26.
 * 室外数据适配器
 */
public class MainDateFragTwoAdapter extends FragmentPagerAdapter {
    private MainDateFragTwo mainDateFragTwo[] = new MainDateFragTwo[1000];
    private MainDateFragTwo.OutParaViewClick mSetWifi;
    public MainDateFragTwoAdapter(FragmentManager fm, MainDateFragTwo.OutParaViewClick sw) {
        super(fm);
        mSetWifi = sw;
    }

    @Override
    public Fragment getItem(int position) {
        if( mainDateFragTwo[position] == null  ){
            mainDateFragTwo[position] = MainDateFragTwo.newInstance(position%4 + 1,mSetWifi);
        }
        return mainDateFragTwo[position] ;

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1000;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position = position %4;
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
                return "PAGE 3";
            case 3:
                return "PAGE 3";
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
                for(int i=0; i< mainDateFragTwo.length; i++){
                    if( mainDateFragTwo[i] != null ){
                        mainDateFragTwo[i].updateDate();
                    }
                }
            }
        },100);
    }
}