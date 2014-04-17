package com.iconverge.ct.traffic.location;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MapView.LayoutParams;
import com.mapabc.mapapi.map.PoiOverlay;

public class CustomLocationOverlay extends PoiOverlay {
	private Context context;
	private Drawable drawable;
	private int number = 0;
	public List<PoiItem> poiItems;
	private LayoutInflater mInflater;
	private int height;

	public CustomLocationOverlay(Context context, Drawable drawable, List<PoiItem> poiItems) {
		super(drawable, poiItems);
		this.context = context;
		this.poiItems = poiItems;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
	}

	@Override
	protected Drawable getPopupBackground() {
		// TODO Auto-generated method stub
		drawable = context.getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__tip_pointer_button"));
		return drawable;
	}

	@Override
	protected View getPopupView(final PoiItem item) {
		// TODO Auto-generated method stub

		View view = mInflater.inflate(ResUtil.getInstance(context).getLayout("ct_traffic__popup"), null);
		TextView nameTextView = (TextView) view.findViewById(ResUtil.getInstance(context).getId("PoiName"));
		TextView addressTextView = (TextView) view.findViewById(ResUtil.getInstance(context).getId("PoiAddress"));
		nameTextView.setText(item.getTitle());
		String address = item.getSnippet();
		if (address == null || address.length() == 0) {
			address = "";
		}
		addressTextView.setText(address);
		LinearLayout layout = (LinearLayout) view.findViewById(ResUtil.getInstance(context).getId("LinearLayoutPopup"));
		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent intent = new Intent(context, Route2Activity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// Bundle bundle = new Bundle();
				// bundle.putString("name", item.getTitle());
				// bundle.putParcelable("geo", item.getPoint());
				// intent.putExtras(bundle);
				// context.startActivity(intent);
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
		LayoutParams params = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, poiItems.get(number).getPoint(), 0, -height, LayoutParams.BOTTOM_CENTER);

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
