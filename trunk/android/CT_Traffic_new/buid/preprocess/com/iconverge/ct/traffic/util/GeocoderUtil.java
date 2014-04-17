package com.iconverge.ct.traffic.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.iconverge.ct.traffic.data.Const;

/**
 * 用给定的坐标数据实现逆地理编码
 */
public class GeocoderUtil {

	public static void getAdmincode(Context context, double lat, double lon) {
		String url = "http://116.228.55.155:6060/dataquery/query?sid=702&key="+Const.MAP_API_KEY+"&cenx=" + lon + "&ceny=" + lat + "&poinum=1&restype=json";
		BVBHttpRequest request = BVBHttpRequest.requestWithURL(context, url);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						JSONObject jObject1 = jObject.getJSONObject("response");
						String error = jObject1.getString("error");
						if (!"".equals(error)) {
							return;
						}
						JSONArray jsonArray = jObject1.getJSONArray("docs");
						if (jsonArray != null) {
							JSONObject o = jsonArray.getJSONObject(0);
							String str = o.getString("admincode");
							if(str != null && str.length() == 6){
								Const.admincode = str.substring(0, 2).concat("0000");
							}
						}
					} catch (Exception e) {
						// #debug debug
//@						e.printStackTrace();
					}
				}
			}
		});
	}

}
