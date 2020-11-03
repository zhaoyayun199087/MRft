package mingrifuture.gizlib.code.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import mingrifuture.gizlib.code.config.WifiStateChangedEvent;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by pengl on 2016/1/13.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogUtils.d("receive data action:" + action);
		// if (action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
		// 收到广播
		int nowtatus = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
				WifiManager.WIFI_STATE_ENABLED);
		/*
		 * int preStatus =
		 * intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE,
		 * WifiManager.WIFI_STATE_DISABLED);
		 */


		// }
	}
}
