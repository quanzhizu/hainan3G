package com.iconverge.ct.traffic.route;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.bean.PointBean;

public class PointResultAdapter extends BaseAdapter {

	private List<PointBean> list = new ArrayList<PointBean>();
	private Activity activity = null;
	private int type;
	private Handler mHandler;

	public PointResultAdapter(Activity activity,Handler handler) {
		this.activity = activity;
		this.mHandler = handler;
	}
	
	public PointResultAdapter(Activity activity, List<PointBean> list) {
		this.activity = activity;
		this.list = list;
	}

	public void setDataAndType(List<PointBean> list,int type){
		this.list = list;
		this.type = type;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.point_result_option_item, null);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
			holder.textViewName = (TextView) convertView.findViewById(R.id.item_text_name);
			holder.textViewAddress = (TextView) convertView.findViewById(R.id.item_text_address);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("sel", position);
				msg.setData(data);
				msg.what = type;
				mHandler.sendMessage(msg);
			}
		});
		PointBean bean = list.get(position);
		holder.textViewName.setText(bean.getName());
		holder.textViewAddress.setText(bean.getAddress());

		return convertView;
	}

	class ViewHolder {
		LinearLayout layout;
		TextView textViewName;
		TextView textViewAddress;
	}
}
