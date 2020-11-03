package mingrifuture.gizlib.code.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by pengl on 2016/1/18.
 */
public class ScreenBrightnessTool {

	/**
	 * Activty自动调节亮度模式
	 */
	public static final int ACTIVITY_BRIGHTNESS_AUTOMATIC = -1;
	/**
	 * 自动调节模式
	 */
	public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	/**
	 * 手动调节模式
	 */
	public static final int SCREEN_BRIGHTNESS_MODE_MANUAL = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
	/**
	 * 默认亮度
	 */
	public static final int SCREEN_BRIGHTNESS_DEFAULT = 75;
	/**
	 * 最大亮度
	 */
	public static final int MAX_BRIGHTNESS = 100;
	/**
	 * 最小亮度
	 */
	public static final int MIN_BRIGHTNESS = 0;

	private static final int mMaxBrighrness = 255;
	private static final int mMinBrighrness = 120;

	// 当前系统调节模式
	private boolean sysAutomaticMode;
	// 当前系统亮度值
	private int sysBrightness;

	private Context context;

	private ScreenBrightnessTool(Context context, int sysBrightness,
								 boolean sysAutomaticMode) {
		this.context = context;
		this.sysBrightness = sysBrightness;
		this.sysAutomaticMode = sysAutomaticMode;
	}

	/**
	 * 创建屏幕亮度工具
	 * 
	 * @param context
	 * @return
	 */
	public static ScreenBrightnessTool Builder(Context context) {
		int brightness;
		boolean automaticMode;
		try {
			// 获取当前系统亮度值
			brightness = Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
			// 获取当前系统调节模式
			automaticMode = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE) == SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Settings.SettingNotFoundException e) {
			return null;
		}

		return new ScreenBrightnessTool(context, brightness, automaticMode);
	}

	/**
	 * 返回当前系统亮度调节模式
	 * 
	 * @return
	 */
	public boolean getSystemAutomaticMode() {
		return sysAutomaticMode;
	}

	/**
	 * 返回当前系统亮度值
	 * 
	 * @return 0--255
	 */
	public int getSystemBrightness() {
		updateLightness();
		return sysBrightness;
	}

	/**
	 * 更新亮度值
	 */
	private void updateLightness() {
		try {
			sysBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Settings.SettingNotFoundException e) {

		}
	}

	/**
	 * 设置调节模式
	 * 
	 * @param mode
	 *            调节模式,1为自动，0为手动
	 */
	public void setMode(int mode) {
		if (mode != SCREEN_BRIGHTNESS_MODE_AUTOMATIC
				&& mode != SCREEN_BRIGHTNESS_MODE_MANUAL)
			return;

		sysAutomaticMode = mode == SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
	}

	/**
	 * 设置屏幕亮度
	 * 
	 * @param brightness
	 *            亮度值,值为0至255
	 */
	public void setBrightness(int brightness) {
		LogUtils.d("setbrightness " + brightness);
		ContentResolver resolver = context.getContentResolver();
		Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
				brightness);
		this.sysBrightness = brightness;
	}

	/**
	 * 设置屏幕亮度
	 * 
	 * @param brightness
	 *            亮度值,值为0至255
	 */
	public void setBrightness1(int brightness) {
//		brightness -= 1;
		float diff = 255 / 80;
		brightness = (int) (brightness * diff);
		LogUtils.d("setbrightness " + brightness);
		ContentResolver resolver = context.getContentResolver();
//		LogUtils.d("set light is " + brightness);
		Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
				brightness);
		this.sysBrightness = brightness;
	}

	/**
	 * 亮度预览
	 * 
	 * @param activity
	 *            预览activity
	 * @param brightness
	 *            亮度值（0.47~1）
	 */
	public static void brightnessPreview(Activity activity, float brightness) {
		Window window = activity.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightness;
		window.setAttributes(lp);
	}

	/**
	 * 亮度预览
	 * 
	 * @param activity
	 *            预览activity
	 * @param percent
	 *            百分比（0.0~1.00）
	 */
	public static void brightnessPreviewFromPercent(Activity activity,
			float percent) {
		float brightness = percent + (1.0f - percent)
				* (((float) mMinBrighrness) / mMaxBrighrness);
		brightnessPreview(activity, brightness);
	}
}
