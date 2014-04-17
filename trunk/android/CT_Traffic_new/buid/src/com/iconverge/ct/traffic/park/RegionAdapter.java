package com.iconverge.ct.traffic.park;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.RegionBean;

public class RegionAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<RegionBean> regions;
	private ViewHolder vh;

	public RegionAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		regions = new ArrayList<RegionBean>();
	}

	public void setRegions(List<RegionBean> regions) {
		this.regions = regions;
	}

	public void removeAndRefresh() {
		this.regions.removeAll(regions);
		this.notifyDataSetChanged();
	}

	public void removeAll() {
		this.regions.removeAll(regions);
	}

	@Override
	public int getCount() {
		return regions == null ? 0 : regions.size();
	}

	@Override
	public Object getItem(int position) {
		return regions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__region_item"), null);
			vh.tvName = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("region_item_tv_name"));
			vh.tvId = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("region_item_tv_id"));
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		RegionBean region = regions.get(position);
		vh.tvId.setText("" + region.getId());
		vh.tvName.setText("" + region.getName());

		return convertView;
	}

	class ViewHolder {
		TextView tvName, tvId;
	}

}
