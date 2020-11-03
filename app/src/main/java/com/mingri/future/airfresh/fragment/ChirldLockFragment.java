package com.mingri.future.airfresh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.activity.ChirldLockActivity;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.ShowMainPage;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;

/**
 * Created by Administrator on 2017/6/27.
 */
public class ChirldLockFragment extends BaseFragment {
    @InjectView(R.id.btn_chirld_lock)
    Button btnChirldLock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chilrd_lock, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_chirld_lock)
    public void onViewClicked() {
        EventBus.getDefault().post(new ShowMainPage(1));
        EventBus.getDefault().post(new ChirldLock(true));
        MachineStatusForMrFrture.Child_Security_Lock = true;
//        Intent intent =  new Intent(Settings.ACTION_DISPLAY_SETTINGS);
//        startActivity(intent);
    }
}
