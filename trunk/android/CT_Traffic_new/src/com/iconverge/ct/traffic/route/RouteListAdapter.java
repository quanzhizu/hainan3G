package com.iconverge.ct.traffic.route;

import java.text.MessageFormat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.mapabc.mapapi.route.Route;
import com.mapabc.mapapi.route.Segment;

public class RouteListAdapter extends BaseAdapter {

	private Route route;
	private Activity activity = null;

	public RouteListAdapter(Activity activity) {
		this.activity = activity;
	}

	public void setData(Route route){
		this.route = route;
	}
	
	@Override
	public int getCount() {
		return route == null ?0:route.getStepCount() ;
	}

	@Override
	public Object getItem(int position) {
		return route.getStep(position);
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
			convertView = LayoutInflater.from(activity).inflate(ResUtil.getInstance(activity).getLayout("ct_traffic__route_list_item"), null);
			holder.layout = (LinearLayout) convertView.findViewById(ResUtil.getInstance(activity).getId("item_layout"));
			holder.textViewName = (TextView) convertView.findViewById(ResUtil.getInstance(activity).getId("item_text_name"));
			holder.textViewDescription = (TextView) convertView.findViewById(ResUtil.getInstance(activity).getId("item_text_description"));
			holder.textViewTime = (TextView) convertView.findViewById(ResUtil.getInstance(activity).getId("item_text_time"));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		Segment segment = route.getStep(position);
		holder.textViewName.setText(route.getStepedDescription(position));
		holder.textViewDescription.setText(route.getStepedDescription(position));
		StringBuilder builder = new StringBuilder();
		builder.append(MessageFormat.format(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__need_time")), segment.getConsumeTime()));
		holder.textViewTime.setText(builder.toString());

		return convertView;
	}

	class ViewHolder {
		LinearLayout layout;
		TextView textViewName;
		TextView textViewDescription;
		TextView textViewTime;
	}
}
