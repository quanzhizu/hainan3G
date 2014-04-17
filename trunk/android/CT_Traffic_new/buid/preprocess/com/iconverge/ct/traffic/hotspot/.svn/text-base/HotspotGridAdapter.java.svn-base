package com.iconverge.ct.traffic.hotspot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;

public class HotspotGridAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public HotspotGridAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return Const.strHotspotGrids.length;
	}

	@Override
	public Object getItem(int position) {
		return Const.strHotspotGrids[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = mInflater.inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__hotspot_grid_item"), null);
			vh.ivHotspot = (ImageView) convertView.findViewById(ResUtil.getInstance(mContext).getId("hotspot_grid_item_iv_hotspot"));
			vh.tvHotspot = (TextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("hotspot_grid_item_tv_hotspot"));
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.ivHotspot.setImageResource(Const.intHotspotGrids[position]);
		vh.tvHotspot.setText(Const.strHotspotGrids[position]);

		return convertView;
	}

	class ViewHolder {
		ImageView ivHotspot;
		TextView tvHotspot;
	}

}
