package mingrifuture.gizlib.code.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class Utils {
	private static Toast toast;

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void showToast(Context context,
								 String content) {
		if (toast == null) {
			toast = Toast.makeText(context,
					content,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}
	public static boolean Ping(String str) {
		boolean resault = false;
		Process p;
		try {
			// ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 100
			// 以秒为单位指定超时间隔，是指超时时间为100秒
			LogUtils.d("ping " + str);
			p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
			int status = p.waitFor();

			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}

			if (status == 0) {
				resault = true;
			} else {
				resault = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return resault;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = "V" + info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "v1.0";
		}
	}

	/**
	 * 系统语言是否为中文
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	/**
	 * 获取唯一标识码
	 * 
	 * @param context
	 * @return
	 */
	public static String getCode(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

}
