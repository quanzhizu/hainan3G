package com.iconverge.ct.traffic.feedback;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iconverge.ct.traffic.bean.CategoryBean;

public class CategoryAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<CategoryBean> lists = new ArrayList<CategoryBean>();

	public CategoryAdapter(Context context) {
		this.mContext = context;
	}

	public void refresh(ArrayList<CategoryBean> lists){
		this.lists = lists;
	}
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.isEmpty() ? null : lists.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
	}

	class ViewHolder {
		TextView textView;
	}
	
	 private View createViewFromResource(int position, View convertView, ViewGroup parent,
	            int resource) {
		 ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(resource, null);
				holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(lists.get(position).getName());

			return convertView;
	    }
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
	}
}
