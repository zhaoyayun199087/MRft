package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;

/**
 * Created by Administrator on 2017/6/27.
 */
public class FlzFragment extends BaseFragment {

    @InjectView(R.id.btn_anion)
    Button btnAnion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_flz, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        updateUI();
        return view;
    }

    private void updateUI() {
        if(!MachineStatusForMrFrture.Switch_Plasma1){
            btnAnion.setText(getString(R.string.open));
            btnAnion.setBackgroundResource(R.drawable.caid_btn_black);
        }else{
            btnAnion.setText(getString(R.string.close));
            btnAnion.setBackgroundResource(R.drawable.caid_btn_blue);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_anion)
    public void onViewClicked() {
        MachineStatusForMrFrture.Switch_Plasma1 = !MachineStatusForMrFrture.Switch_Plasma1;
        CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_PLASMAL1,MachineStatusForMrFrture.Switch_Plasma1?1:0);
        int cmd[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_PLASMAL1);
        EventBus.getDefault().post(new SendDataToMachine(cmd));
        updateUI();
        iCount = 0;
    }

    private int iCount = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        if( iCount < 5 ){
            iCount ++;
        }else{
            updateUI();
        }
    }
}
