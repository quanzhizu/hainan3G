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

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.PoiResultBean;

public class StationAdapt extends BaseAdapter {

	private List<PoiResultBean> list;
	private ViewHolder vh;
	private Context mContext;
	private LayoutInflater inflater;

	public StationAdapt(Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void initData(List<PoiResultBean> list) {
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
			convertView = inflater.inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__busstation_listview"), null);
			vh.station_name = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("station_name"));
			vh.distance = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("distance"));
			vh.route = (ImageView) convertView.findViewById(ResUtil.getInstance(mContext).getId("route"));
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		PoiResultBean bean = list.get(position);
		vh.station_name.setText(bean.getName());
		vh.distance.setText(MessageFormat.format(mContext.getString(ResUtil.getInstance(mContext).getString("ct_traffic___meter")), bean.getDistance()));
		return convertView;
	}

	class ViewHolder {
		TextView station_name, distance;
		ImageView route;
	}

}
