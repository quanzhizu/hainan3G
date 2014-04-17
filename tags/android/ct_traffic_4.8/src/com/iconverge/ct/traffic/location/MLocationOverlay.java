package com.iconverge.ct.traffic.location;

import android.content.Context;
import android.widget.Toast;

import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MyLocationOverlay;

public class MLocationOverlay extends MyLocationOverlay {
	
	private Context context;

	public MLocationOverlay(Context context, MapView arg1) {
		super(context, arg1);
		this.context = context;
	}
	
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		// TODO Auto-generated method stub
		return super.onTap(arg0, arg1);
	}
	
	@Override
	protected boolean dispatchTap() {

		return super.dispatchTap();
	}
	
}
