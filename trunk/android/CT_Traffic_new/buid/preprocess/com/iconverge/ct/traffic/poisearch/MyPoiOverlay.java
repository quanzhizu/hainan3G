package com.iconverge.ct.traffic.poisearch;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.feedback.PoiInfoActivity;
import com.iconverge.ct.traffic.route.RouteActivity;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MapView.LayoutParams;
import com.mapabc.mapapi.map.PoiOverlay;

public class MyPoiOverlay extends PoiOverlay {
	private Context context;
	private Drawable drawable;
	private int number = 0;
	private List<PoiItem> poiItem;
	private LayoutInflater mInflater;
	private int height;
	private List<String> poiIds;

	public MyPoiOverlay(Context context, Drawable drawable, List<PoiItem> poiItem, List<String> poiIds) {
		super(drawable, poiItem);
		this.context = context;
		this.poiItem = poiItem;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
		this.poiIds = poiIds;
	}

	@Override
	protected Drawable getPopupBackground() {
		// TODO Auto-generated method stub
		drawable = context.getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__tip_pointer_button_normal"));
		return drawable;
	}

	@Override
	protected View getPopupView(final PoiItem item) {
		// TODO Auto-generated method stub

		View view = mInflater.inflate(ResUtil.getInstance(context).getLayout("ct_traffic__popup"), null);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		TextView nameTextView = (TextView) view.findViewById(ResUtil.getInstance(context).getId("PoiName"));
		nameTextView.setMaxWidth((int) (dm.widthPixels * 0.6));
		TextView addressTextView = (TextView) view.findViewById(ResUtil.getInstance(context).getId("PoiAddress"));
		nameTextView.setText(item.getTitle());
		String address = item.getSnippet();
		if (address == null || address.length() == 0) {
			address = "";
		}
		addressTextView.setText(address);
		ImageView feedback = (ImageView) view.findViewById(ResUtil.getInstance(context).getId("feedback"));
		feedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PoiInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("poi", item);
				if (poiIds != null && !poiIds.isEmpty())
					intent.putExtra("poiId", poiIds.get(number));
				context.startActivity(intent);
			}
		});
		LinearLayout layout = (LinearLayout) view.findViewById(ResUtil.getInstance(context).getId("LinearLayoutPopup"));
		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context, RouteActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle bundle = new Bundle();
				bundle.putString("name", item.getTitle());
				bundle.putParcelable("geo", item.getPoint());
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void addToMap(MapView arg0) {
		// TODO Auto-generated method stub
		super.addToMap(arg0);
	}

	@Override
	protected LayoutParams getLayoutParam(int arg0) {
		// TODO Auto-generated method stub
		LayoutParams params = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, poiItem.get(number).getPoint(), 0, -height, LayoutParams.BOTTOM_CENTER);

		return params;
	}

	@Override
	protected Drawable getPopupMarker(PoiItem arg0) {
		// TODO Auto-generated method stub
		return super.getPopupMarker(arg0);
	}

	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		number = index;
		return super.onTap(index);
	}

}
