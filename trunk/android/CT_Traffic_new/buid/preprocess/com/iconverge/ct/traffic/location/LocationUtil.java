package com.iconverge.ct.traffic.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.location.LocationManagerProxy;

public class LocationUtil implements LocationListener {

	private LocationManagerProxy locationManager = null;
	private LocationListenerProxy mLocationListener = null;
	private static final long mLocationUpdateMinTime = 1000 * 60;
	private static final float mLocationUpdateMinDistance = 20.0f;

	private Context mContext;
	private Handler mHandler;

	public LocationUtil(Context context,Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	public boolean enableMyLocation() {
		boolean result = true;
		if (mLocationListener == null) {
			mLocationListener = new LocationListenerProxy(locationManager);
			result = mLocationListener.startListening(this, mLocationUpdateMinTime, mLocationUpdateMinDistance);
		}
		return result;
	}

	public void disableMyLocation() {
		if (mLocationListener != null) {
			mLocationListener.stopListening();
		}
		mLocationListener = null;
	}

	public void stop() {
		disableMyLocation();
	}

	public void start() {
		locationManager = LocationManagerProxy.getInstance(mContext, Const.MAP_API_KEY);
		enableMyLocation();
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			//#debug debug
//@			System.out.println("location is not null");
			Message msg = new Message();
			Bundle bundle = location.getExtras();
			if(bundle == null)bundle = new Bundle();
			bundle.putDouble("geoLat", location.getLatitude());
			bundle.putDouble("geoLng", location.getLongitude());
			msg.setData(bundle);
			msg.what = MainActivity.LOCATION;
			mHandler.sendMessage(msg);
		}else{
			//#debug debug
//@			System.out.println("location is null");
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
