package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/6/27.
 */
public class UVCFragment extends BaseFragment {
    @InjectView(R.id.btn_uvc)
    Button btnUvc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_uvc, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        updateUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    private void updateUI() {
        if (!MachineStatusForMrFrture.Switch_UVC) {
            btnUvc.setText(getString(R.string.open));
            btnUvc.setBackgroundResource(R.drawable.caid_btn_black);
        }else{
            btnUvc.setText(getString(R.string.close));
            btnUvc.setBackgroundResource(R.drawable.caid_btn_blue);
        }
    }

    @OnClick(R.id.btn_uvc)
    public void onViewClicked() {

//        if( MachineStatusForMrFrture.Mode == 0 ){
//            Toast.makeText(getActivity(),getString(R.string.uvc_frag_tip),Toast.LENGTH_SHORT).show();
//            return;
//        }

        MachineStatusForMrFrture.Switch_UVC = !MachineStatusForMrFrture.Switch_UVC;
        CommonUtils.setOrder(Constants.ANDROID_SEND_SWITCH_UVC, MachineStatusForMrFrture.Switch_UVC ? 1 : 0);
        int cmd[] = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_SWITCH_UVC);
        EventBus.getDefault().post(new SendDataToMachine(cmd));
        LogUtils.d("resend msg cmd 1 " +  MachineStatusForMrFrture.Switch_UVC );
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
