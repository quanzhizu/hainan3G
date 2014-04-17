package com.iconverge.ct.traffic.hotspot;

import com.iconverge.ct.traffic.ResUtil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotspotPopupTypesAdapter extends BaseAdapter {

	private String[] arrs;
	private Activity activity = null;
	private Context mContext;

	public HotspotPopupTypesAdapter(Activity activity, String[] arrs) {
		this.mContext = activity;
		this.arrs = arrs;
	}

	@Override
	public int getCount() {
		return arrs.length;
	}

	@Override
	public Object getItem(int position) {
		return arrs[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__hotspot_popup_types_item"), null);
			holder.textView = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("hotspot_popup_types_item_tv_type"));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(arrs[position]);

		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}
}
