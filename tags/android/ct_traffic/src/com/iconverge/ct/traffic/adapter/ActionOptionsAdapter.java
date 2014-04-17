package com.iconverge.ct.traffic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iconverge.ct.traffic.R;

public class ActionOptionsAdapter extends BaseAdapter {

	private String[] arrs;
	private Activity activity = null;

	public ActionOptionsAdapter(Activity activity, String[] arrs) {
		this.activity = activity;
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.action_option_item, null);
			holder.textView = (TextView) convertView.findViewById(R.id.item_text);
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
