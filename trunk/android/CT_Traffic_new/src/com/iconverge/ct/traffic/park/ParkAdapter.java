package com.iconverge.ct.traffic.park;

import java.util.ArrayList;
import java.util.List;

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.ParkBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParkAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ParkBean> parks;
	private ViewHolder vh;

	public ParkAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		parks = new ArrayList<ParkBean>();
	}

	public void setParks(List<ParkBean> parks) {
		this.parks = parks;
	}

	public void removeAndRefresh() {
		this.parks.removeAll(parks);
		this.notifyDataSetChanged();
	}

	public void removeAll() {
		this.parks.removeAll(parks);
	}

	@Override
	public int getCount() {
		return parks == null ? 0 : parks.size();
	}

	@Override
	public Object getItem(int position) {
		return parks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__park_item"), null);
			vh.tvName = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("park_item_tv_name"));
			vh.tvStatus = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("park_item_tv_status"));
			vh.tvRemainderTotal = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("park_item_tv_remainder_total"));
			vh.tvAddress = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("park_item_tv_address"));
			vh.tvTime = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("park_item_tv_time"));
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ParkBean park = parks.get(position);
		vh.tvName.setText("" + park.getName());
		vh.tvStatus.setText("1".equals(park.getStatus())?mContext.getString(ResUtil.getInstance(mContext).getString("ct_traffic__car_park_open")):mContext.getString(ResUtil.getInstance(mContext).getString("ct_traffic__car_park_stop")));
		vh.tvRemainderTotal.setText(park.getRemainder() + " / " + park.getTotal());
		vh.tvAddress.setText(park.getAddress());
		vh.tvTime.setText(park.getTime().substring(0, park.getTime().length()-2));
		return convertView;
	}

	class ViewHolder {
		TextView tvParkId, tvId, tvRegionId, tvName, tvRemainderTotal, tvTime, tvStatus, tvAddress, tvLon, tvLat;
	}

}
