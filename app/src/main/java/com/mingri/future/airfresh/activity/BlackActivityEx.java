package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.OpenMachineEvent;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.bean.SendInitDateEvent;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;


public class BlackActivityEx extends Activity {
	private ScreenBrightnessTool mBrightnessTool;
	private RelativeLayout rlRoot;
	private final static int DECRESE_LIGHT = 0;
	private final static int INCRESE_LIGHT = 1;
	private VideoView videoView;

	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_ex);
		rlRoot = (RelativeLayout)findViewById(R.id.rl_root);
		mBrightnessTool = ScreenBrightnessTool.Builder(this);


		mBrightnessTool.setBrightness1(0);

		EventBus.getDefault().register(this);
		videoView = (VideoView) findViewById(R.id.videoView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		LogUtils.d("key is1 " + keyCode + " event " + event.toString());

		if (event.getRepeatCount() == 25) {

			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					MachineStatusForMrFrture.iCount = 0;
					int light = (int) SPUtils.get(BlackActivityEx.this, "light", 40);
					mBrightnessTool.setBrightness1(light);
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
					MachineStatusForMrFrture.iCount = 0;
					int light = (int) SPUtils.get(BlackActivityEx.this, "light", 40);
					mBrightnessTool.setBrightness1(light);
					openMachine();
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
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private void openMachine(){
		MachineStatusForMrFrture.Switch = true;
		if( MachineStatusForMrFrture.Switch ){
			MachineStatusForMrFrture.startTime = System.currentTimeMillis();
		}
//		CommonUtils.setOrder(Constants.ANDROID_SEND_POWER, MachineStatusForMrFrture.Switch?1:0);
//
//		int[] d = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_POWER);
//		LogUtils.d("cmd is " + CommonUtils.decodeBytesToHexString(d));
//		EventBus.getDefault().post(new SendDataToMachine(d));
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
