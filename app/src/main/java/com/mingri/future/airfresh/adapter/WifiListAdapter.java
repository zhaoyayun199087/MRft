package com.mingri.future.airfresh.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mingri.future.airfresh.R;

import java.util.List;

import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.NetUtils;

/**
 * Created by pengl on 2016/1/17.
 */
public class WifiListAdapter extends BaseAdapter {

	private List<ScanResult> list;
	private String mConnectedSSid = null;
	private Context context;
	private LayoutInflater inflater;

	private boolean isConnected;

	public WifiListAdapter(Context context, List<ScanResult> list) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);

		isConnected = NetUtils.isWiFiActive(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_wifi_item, null);
			holder = new ViewHolder();
			holder.ssid = (TextView) convertView
					.findViewById(R.id.tv_wifi_ssid);
			holder.status = (ImageView) convertView
					.findViewById(R.id.tv_wifi_status);
			holder.img = (ImageView) convertView.findViewById(R.id.iv_wifi_ico);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ScanResult result = list.get(position);


		holder.ssid.setText(result.SSID);

		String wifi;
		wifi = result.capabilities.replace("[", "").replace("]", "");
		if (wifi.equalsIgnoreCase("ESS")) {
			if (result.level > -50) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_wifi4);
			} else if (result.level > -60) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_wifi3);
			} else if (result.level > -80) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_wifi2);
			} else if (result.level > -90) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_wifi1);
			} else {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_wifi0);
			}

		} else {
			if (result.level > -50) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_swifi4);
			} else if (result.level > -60) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_swifi3);
			} else if (result.level > -80) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_swifi2);
			} else if (result.level > -90) {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_swifi1);
			} else {
				holder.img.setImageResource(R.mipmap.peiz_list_icon_swifi0);
			}

		}
		if (mConnectedSSid != null) {
			LogUtils.d("mconnect " + mConnectedSSid + " ssid " + result.SSID);
			String curSSid = "\"" + result.SSID + "\"";
			if (mConnectedSSid.equals(curSSid)) {
				holder.status.setImageResource(R.mipmap.peiz_icon_ok);
			}else
				holder.status.setImageResource(R.mipmap.peiz_icon_next);
		} else{
			holder.status.setImageResource(R.mipmap.peiz_icon_next);
		}
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void setList(List<ScanResult> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setConnectedSSid(String mConnectedSSid) {
		this.mConnectedSSid = mConnectedSSid;
	}

	class ViewHolder {
		TextView ssid;
		ImageView status;
		ImageView img;
	}
}
