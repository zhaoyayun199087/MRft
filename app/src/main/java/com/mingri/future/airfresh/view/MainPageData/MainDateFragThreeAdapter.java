package com.mingri.future.airfresh.view.MainPageData;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;

import com.mingri.future.airfresh.R;

import java.util.Locale;

/**
 * Created by Administrator on 2017/6/26.
 * 运行模式适配器
 */
public class MainDateFragThreeAdapter extends FragmentPagerAdapter {
    private MainDateFragThree.ModeClick mModeClick;
    private MainDateFragThree mainDateFragThree[] = new MainDateFragThree[5];
    public MainDateFragThreeAdapter(FragmentManager fm, MainDateFragThree.ModeClick modeClick) {
        super(fm);
        mModeClick = modeClick;
    }

    @Override
    public Fragment getItem(int position) {
        mainDateFragThree[position] = MainDateFragThree.newInstance(position + 1,mModeClick );
        return mainDateFragThree[position];
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
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
                for(int i=0; i< mainDateFragThree.length; i++){
                    if( mainDateFragThree[i] != null ){
                        mainDateFragThree[i].updateDate();
                    }
                }
            }
        },100);
    }
}