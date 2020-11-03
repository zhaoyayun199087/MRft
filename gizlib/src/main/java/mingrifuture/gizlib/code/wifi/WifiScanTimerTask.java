package mingrifuture.gizlib.code.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

import java.util.TimerTask;

import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;

/**
 * Created by pengl on 2016/1/17.
 */

public class WifiScanTimerTask extends TimerTask {
	private Handler handler;
	private Context context;

	public WifiScanTimerTask(Context context, Handler handler) {
		this.handler = handler;
		this.context = context;
	}

	@Override
	public void run() {
		// 每隔一段时间自动扫描
		if(MachineStatusForMrFrture.isExiting){
			return;
		}
		handler.sendEmptyMessage(200);
		WifiManager wifiMg = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiMg.isWifiEnabled())
			wifiMg.startScan();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

		}

		handler.sendEmptyMessage(201);
	}

}
