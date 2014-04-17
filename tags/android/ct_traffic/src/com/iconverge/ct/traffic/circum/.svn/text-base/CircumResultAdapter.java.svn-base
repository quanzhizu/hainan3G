package com.iconverge.ct.traffic.circum;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.util.ImageUtil;
import com.mapabc.mapapi.core.PoiItem;

public class CircumResultAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<PoiItem> result;

	Drawable drawable;

	public CircumResultAdapter(Context context) {
		mContext = context;
		this.result = new ArrayList<PoiItem>();
		mInflater = LayoutInflater.from(mContext);
		drawable = mContext.getResources().getDrawable(R.drawable.avatar_default);
	}

	public List<PoiItem> getResult() {
		return result;
	}

	public PoiItem[] getResultArray() {
		PoiItem[] poiItems = null;
		if (!result.isEmpty()) {
			poiItems = result.toArray(new PoiItem[] {});
		}
		return poiItems;
	}

	public void setResult(List<PoiItem> result) {
		this.result = result;
	}

	public void clear() {
		this.result.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (result != null) {
			return result.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return result.get(position);
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
			convertView = mInflater.inflate(R.layout.hotspot_search_result_item, null);
			vh.ivResult = (ImageView) convertView.findViewById(R.id.hotspot_search_result_item_iv_hotspot);
			vh.tvTitle = (TextView) convertView.findViewById(R.id.hotspot_search_result_item_tv_title);
			vh.tvDistance = (TextView) convertView.findViewById(R.id.hotspot_search_result_item_tv_distance);
			vh.tvTel = (TextView) convertView.findViewById(R.id.hotspot_search_result_item_tv_tel_value);
			vh.tvSnippet = (TextView) convertView.findViewById(R.id.hotspot_search_result_item_tv_snippet_value);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		PoiItem item = result.get(position);
		if (item.getmMarker() == null)
			vh.ivResult.setImageResource(R.drawable.avatar_default);
		else {
			Bitmap bmp = ImageUtil.drawBitmap(item.getmMarker(), drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			if (bmp != null)
				vh.ivResult.setImageBitmap(bmp);
			else
				vh.ivResult.setImageResource(R.drawable.avatar_default);
		}
		vh.tvTitle.setText(item.getTitle());
		int distance = item.getDistance();
		if (distance >= 1000) {
			float floatDistance = (float) (Math.round(((float) distance / 1000) * 100)) / 100;
			vh.tvDistance.setText(floatDistance + "km");
		} else {
			vh.tvDistance.setText(item.getDistance() + "m");
		}
		vh.tvTel.setText(item.getTel());
		vh.tvSnippet.setText(item.getSnippet());
		// Log.i("PoiItem", " Title:" + item.getTitle() + " PoiId:" +
		// item.getPoiId() + "\n Snippet:" + item.getSnippet() + " AdCode:" +
		// item.getAdCode() + " Distance:" + item.getDistance() + "\n Tel:" +
		// item.getTel() + " TypeCode:" + item.getTypeCode() + " TypeDes:" +
		// item.getTypeDes() + "\n Point:" + item.getPoint().toString());
		return convertView;
	}

	class ViewHolder {
		ImageView ivResult;
		TextView tvTitle;
		TextView tvTel;
		TextView tvDistance;
		TextView tvSnippet;
	}

}
