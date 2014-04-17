package com.iconverge.test;

import java.util.List;

import com.iconverge.ct.traffic.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bvb.bvbhttptest.bean.VideoBean;

public class VideoListViewAdapter extends BaseAdapter {

	public List<VideoBean> datas;
	private Activity mContext;

	public VideoListViewAdapter(Activity context) {
		this.mContext = context;
	}

	public void initData(List<VideoBean> datas) {
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas == null ? null : datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.video_list_item1, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.vid = (TextView) convertView.findViewById(R.id.vid);
			holder.content = (TextView) convertView.findViewById(R.id.content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		VideoBean bean = datas.get(position);
		// TODO 异步加载头像
		holder.name.setText(bean.getName());
		holder.vid.setText(bean.getvId());
		holder.content.setText(bean.getState());

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView vid;
		TextView content;

	}
}
