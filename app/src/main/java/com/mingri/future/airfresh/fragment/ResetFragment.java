package com.mingri.future.airfresh.fragment;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.application.AppManager;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.ApkManager;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.view.dialog.AlreadyBestOldVersionDialog;
import com.mingri.future.airfresh.view.dialog.ResetFactoryDialog;
import com.mingri.future.airfresh.view.dialog.ResetingFactoryDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.Utils;

/**
 * Created by Administrator on 2017/6/27.
 */
public class ResetFragment extends BaseFragment {

    @InjectView(R.id.btn_anion)
    Button btnAnion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_reset, null);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void updateUI() {

    }


    @OnClick(R.id.btn_anion)
    public void onViewClicked() {
        LogUtils.d("current version is " + Utils.getVersionCode(getActivity()));
        if( Utils.getVersionCode(getActivity()) <= 42 ){
            AlreadyBestOldVersionDialog ad = new AlreadyBestOldVersionDialog(getActivity());
            ad.show();
            return;
        }
        ResetFactoryDialog rd = new ResetFactoryDialog(getActivity(), new ResetFactoryDialog.ResetDialogCallback() {
            @Override
            public void onOk() {
                ResetingFactoryDialog rd = new ResetingFactoryDialog(getActivity(), new ResetingFactoryDialog.ResetDialogCallback() {
                    @Override
                    public void onOk() {

                        SPUtils.clear(getActivity());
                        initMachineSetting();
                        //TODO 静默卸载

                        ApkManager.uninstallSlient("com.mingri.future.airfresh");
                    }
                });
                rd.show();
            }

            @Override
            public void onCancel() {

            }
        });
        rd.show();
    }

    /**
     * 开机初始化设置
     * 智能模式，五档
     */
    private void initMachineSetting() {
        MachineStatusForMrFrture.bSmartControl = true;

        MachineStatusForMrFrture.Switch = true;
        MachineStatusForMrFrture.Mode = 0;
        MachineStatusForMrFrture.Wind_Velocity = 4;
        MachineStatusForMrFrture.Switch_UVC = true;
        MachineStatusForMrFrture.Filter_Life1 = 100;
        MachineStatusForMrFrture.Filter_Life2 = 100;
        MachineStatusForMrFrture.Filter_Life3 = 100;
        MachineStatusForMrFrture.Filter_Life4 = 100;
        MachineStatusForMrFrture.UVC_Life = 0;
        int[] date = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_UVC |Constants.ANDROID_SEND_LEFTTIME_HUOXING |Constants.ANDROID_SEND_LEFTTIME_GAOXIAO |Constants.ANDROID_SEND_LEFTTIME_ZHONG |Constants.ANDROID_SEND_LEFTTIME_CHU |Constants.ANDROID_SEND_POWER | Constants.ANDROID_SEND_SWITCH_UVC | Constants.ANDROID_SEND_MODE | Constants.ANDROID_SEND_WIND_LEVEL);
        EventBus.getDefault().post(new SendDataToMachine(date));
    }

}
