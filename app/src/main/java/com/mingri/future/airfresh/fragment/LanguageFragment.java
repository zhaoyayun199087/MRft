package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/7/18.
 */
public class LanguageFragment extends  BaseFragment {
    private RadioGroup group;
    private RadioButton china,eng,korea;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frg_language,null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        group= (RadioGroup) v.findViewById(R.id.rd_group);
        china= (RadioButton) v.findViewById(R.id.rd_china);
        eng= (RadioButton) v.findViewById(R.id.rd_eng);
        korea= (RadioButton) v.findViewById(R.id.rd_korea);

        china.setChecked(true);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd_china:

                        break;
                    case R.id.rd_eng:

                        break;
                    case R.id.rd_korea:

                        break;
                }
            }
        });
    }
}
