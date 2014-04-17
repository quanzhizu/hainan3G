package com.iconverge.ct.traffic.busstation;

import java.text.MessageFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.bean.StationBean;

public class StationAdapt extends BaseAdapter {

	private List<StationBean> list;
	private ViewHolder vh;
	private Context mContext;
	private LayoutInflater inflater;

	public StationAdapt(Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void initData(List<StationBean> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.busstation_listview, null);
			vh.station_name = (TextView) convertView.findViewById(R.id.station_name);
			vh.distance = (TextView) convertView.findViewById(R.id.distance);
			vh.route = (ImageView) convertView.findViewById(R.id.route);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		StationBean bean = list.get(position);
		vh.station_name.setText(bean.getName());
		vh.distance.setText(MessageFormat.format(mContext.getString(R.string._meter), bean.getDistance()));
		return convertView;
	}

	class ViewHolder {
		TextView station_name, distance;
		ImageView route;
	}

}
