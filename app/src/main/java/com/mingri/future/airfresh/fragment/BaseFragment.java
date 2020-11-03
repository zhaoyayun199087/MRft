package com.mingri.future.airfresh.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.mingri.future.airfresh.activity.MainActivity;

/**
 * Created by Administrator on 2017/6/27.
 */
public class BaseFragment extends Fragment {
    protected MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity= (MainActivity) context;
    }
}
