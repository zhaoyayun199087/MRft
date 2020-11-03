package com.mingri.future.airfresh.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mingri.future.airfresh.util.FileUtil;
import com.pgyersdk.crash.PgyCrashManager;

import mingrifuture.gizlib.code.util.FileUtils;
import mingrifuture.gizlib.code.util.ScreenBrightnessTool;

/**
 * Created by pengl on 2016/1/6.
 */
public class InitApplication extends Application {

	private static InitApplication sInstance;
	public static InitApplication getInstance() {
		return sInstance;
	}

	public static  boolean isJumpZeroView=true;//是否跳转到耗材为零的界面
	public static  int JumpZeroIndex=2;  //跳转到具体界面
	public static  boolean isChuxiao=true,isGaoxiao=true,isZhongxiao=true,isHuoxing=true,isUVC=true;
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		//奔溃信息本地保存
		CrashHandler.getInstance().init(getApplicationContext());
		PgyCrashManager.register(this);
		FileUtils.writeLogToFile1("start at " + System.currentTimeMillis(), new byte[]{});
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
