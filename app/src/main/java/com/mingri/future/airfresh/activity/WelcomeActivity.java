package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.OpenMachineEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendInitDateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;

import mingrifuture.gizlib.code.provider.AppCmd;
import mingrifuture.gizlib.code.provider.GizCloudCallback;
import mingrifuture.gizlib.code.provider.GizCloudManager;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.CommonUtils;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;

public class WelcomeActivity extends Activity {
    private ScreenBrightnessTool mBrightnessTool;
    private RelativeLayout rlRoot;
    private final static int DECRESE_LIGHT = 0;
    private final static int INCRESE_LIGHT = 1;
    private VideoView videoView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("welcome oncreate");
        setContentView(R.layout.activity_lock_ex);
        EventBus.getDefault().register(this);
        rlRoot = (RelativeLayout)findViewById(R.id.rl_root);
        mBrightnessTool = ScreenBrightnessTool.Builder(this);
        mBrightnessTool.setBrightness(0);
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        LogUtils.d("key is1 " + keyCode + " event " + event.toString());

        if (event.getRepeatCount() == 25) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int light = (int) SPUtils.get(WelcomeActivity.this, "light", 40);
                    mBrightnessTool.setBrightness1(light);
                    MachineStatusForMrFrture.iCount = 0;
                    Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.start);
                    videoView.setVideoURI(mUri);
                    videoView.start();
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                        }
                    });
                }
            },10);


            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openMachine();
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            },6000);
        }


        //屏蔽音量键 return true
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) // 音量增大键响应撤销
        {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)// 音量减小键响应重做
        {
            return true;
        } else return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        LogUtils.d("welcome ondestoy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void openMachine(){
        MachineStatusForMrFrture.iCount = 0;
        MachineStatusForMrFrture.Switch = true;
        if( MachineStatusForMrFrture.Switch ){
            MachineStatusForMrFrture.startTime = System.currentTimeMillis();
        }
        EventBus.getDefault().post(new ReceDataFromMachine(new byte[]{}));
        EventBus.getDefault().post(new SendInitDateEvent());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(OpenMachineEvent data) {

        MachineStatusForMrFrture.iCount = 0;
        int light = (int) SPUtils.get(this, "light", 40);
        mBrightnessTool.setBrightness1(light);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.start);
//				videoView.setVisibility(View.VISIBLE);

                videoView.setVideoURI(mUri);
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                    }
                });
            }
        },10);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMachine();
                finish();
            }
        },6000);
    }
}
