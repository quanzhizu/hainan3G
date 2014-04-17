package com.iconverge.ct.traffic.util;

import java.util.List;

import android.content.Context;
import android.location.Address;

import com.iconverge.ct.traffic.R;
import com.mapabc.mapapi.core.MapAbcException;
import com.mapabc.mapapi.geocoder.Geocoder;

/**
 * 用给定的坐标数据实现逆地理编码，并将得到的地名用Toast打印在地图上
 */
public class GeocoderUtil {

	private Geocoder coder;// 逆地理编码

	public GeocoderUtil(Context context) {
		coder = new Geocoder(context, context.getResources().getString(R.string.maps_api_key));// 根据给定的API
	}

	public String getAddress(final double mlat, final double mLon) {
		String addressName = "";
		try {
			List<Address> address = coder.getFromLocation(mlat, mLon, 1);// 根据给定的经纬度信息、并指定最大结果数为3，进行逆地理编码
			if (address != null && address.size() > 0) {
				Address addres = address.get(0);
				// String type = addres.getPremises();// Address 类型
				addressName = addres.getAdminArea();
				if (addres.getLocality() != null) {
					addressName += addres.getLocality();
				}
				if (addres.getSubLocality() != null) {
					addressName += addres.getSubLocality();
				}
				addressName += addres.getFeatureName();
				// #debug debug
				System.out.println("address : " + addressName);
			}
		} catch (MapAbcException e) {
			// #debug debug
			e.printStackTrace();
		}
		return addressName;
	}

}
