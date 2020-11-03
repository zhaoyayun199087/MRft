package com.mingri.future.airfresh.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.Utils;

/**
 * Created by pengl on 2016/1/10.
 */
public class UpdateUtils {

	public final static int UPDATE_TYPE_AUTO = 1;
	public final static int UPDATE_TYPE_FORCE = 2;
	public final static int UPDATE_TYPE_SLIENT = 3;

	/**
	 * 蒲公英更新
	 * 
	 * @param context
	 */
	public static void pgyCheckUpdate(final Context context, final Boolean b) {
		PgyUpdateManager.register((Activity) context,
				new UpdateManagerListener() {

					@SuppressLint("NewApi")
					@Override
					public void onUpdateAvailable(final String result) {
						LogUtils.d("pgy result is " + result);
						if (true) {

							// 将新版本信息封装到AppBean中
							final AppBean appBean = getAppBeanFromString(result);
							int farCode = Integer.parseInt(appBean.getVersionCode());
							if( farCode <= Utils.getVersionCode(context) ){
								return;
							}
							new AlertDialog.Builder(context)
									.setTitle("有新版本")
									.setMessage(appBean.getReleaseNote())
									.setPositiveButton("稍后更新",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
												}
											})
									.setNegativeButton("现在更新",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													startDownloadTask(
															(Activity) context,
															appBean.getDownloadURL());
												}
											})
									.setOnDismissListener(
											new DialogInterface.OnDismissListener() {
												@Override
												public void onDismiss(
														DialogInterface dialog) {
												}
											}).show();
						}
					}

					@Override
					public void onNoUpdateAvailable() {
						LogUtils.d("更新检查失败");
						// Toast.makeText(
						// context,
						// context.getResources().getString(
						// R.string.not_update_tip),
						// Toast.LENGTH_SHORT).show();
						if(b){
							Toast.makeText(
									context,"更新检查失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * 蒲公英更新
	 *
	 * @param context
	 */
	public static void pgyCheckUpdate(final Context context, final pgyUpdateCallback cb) {
		PgyUpdateManager.register((Activity) context,
				new UpdateManagerListener() {

					@SuppressLint("NewApi")
					@Override
					public void onUpdateAvailable(final String result) {
						LogUtils.d("pgy result is " + result);
						if (true) {

							// 将新版本信息封装到AppBean中
							final AppBean appBean = getAppBeanFromString(result);
							cb.onSuccess(appBean);
						}
					}

					@Override
					public void onNoUpdateAvailable() {
						cb.onFailed();
					}
				});
	}

	public interface pgyUpdateCallback{
		public void onSuccess(AppBean appBean);
		public void onFailed();
	}
}
