package com.iconverge.ct.traffic.route;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.bean.CameraPoiBean;
import com.iconverge.ct.traffic.bean.Step;
import com.iconverge.ct.traffic.data.Const;
import com.iconverge.ct.traffic.data.DataManager;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.route.Route;
import com.mapabc.mapapi.route.Segment;

public class RouteListActivity extends BaseActivity {

	private TextView startText;
	private TextView endText;
	private int mode;
	private String description;
	private PoiItem[] poiItems;
	private RouteHandler handler;
	private final static int SUCCESSED = 1;

	private static class RouteHandler extends Handler {
		WeakReference<RouteListActivity> mActivity;

		RouteHandler(RouteListActivity activity) {
			mActivity = new WeakReference<RouteListActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			RouteListActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case SUCCESSED:
			case Const.ERROR: {
//				//TODO test
//				Route route = DataManager.getInstance().routes.get(0);
//				activity.poiItems = new PoiItem[route.getStepCount()-2];
//				for (int i = 1; i < route.getStepCount()-1; i++) {
//					Segment segment = route.getStep(i);
//					PoiItem poi = new PoiItem("", segment.getFirstPoint(), route.getStepedDescription(i), "22");
//					activity.poiItems[i-1] = poi;
//				}
//				//--------
				activity.hideProgressBar();
				Intent intent = new Intent(activity, MainActivity.class);
				intent.putExtra("mode", activity.mode);
				intent.putExtra("start", activity.startText.getText());
				intent.putExtra("end", activity.endText.getText());
				intent.putExtra("subTitle", activity.description);
				if (activity.poiItems != null)
					intent.putExtra("poiItems", activity.poiItems);
				intent.setType(Const.TYPE_ROUTE_ON_MAP);
				activity.startActivity(intent);
				break;
			}
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.route_list);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(R.string.route));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
		} else {
			handler = new RouteHandler(this);
			Route route = DataManager.getInstance().routes.get(0);
			startText = (TextView) findViewById(R.id.from_add);
			startText.setText(intent.getStringExtra("start"));
			endText = (TextView) findViewById(R.id.to_add);
			endText.setText(intent.getStringExtra("end"));
			TextView info = (TextView) findViewById(R.id.route_info);
			StringBuilder builder = new StringBuilder(getString(R.string.route_));
			builder.append("  ");
			builder.append(MessageFormat.format(getString(R.string._meter), route.getLength()));
			info.setText(builder.toString());

			mode = intent.getIntExtra("mode", -1);
			/*
			 * TextView routeOnMap = (TextView) findViewById(R.id.route_on_map);
			 * routeOnMap.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { Intent intent = new
			 * Intent(RouteListActivity.this, MainActivity.class);
			 * intent.putExtra("mode", mode);
			 * intent.setType(Const.TYPE_ROUTE_ON_MAP); startActivity(intent); }
			 * });
			 */
			description = builder.toString();
			ListView listView = (ListView) findViewById(R.id.list);
			RouteListAdapter adapter = new RouteListAdapter(this);
			adapter.setData(route);
			listView.setAdapter(adapter);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, getResources().getString(R.string.route_on_map)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			showProgressBar(getString(R.string.get_rtsp));
			getCameraPoi();
			break;
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getCameraPoi() {
		poiItems = null;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("m", "listCameras"));
		nameValuePair.add(new BasicNameValuePair("username", Const.URL_USERNAME));
		nameValuePair.add(new BasicNameValuePair("password", Const.URL_PASSWORD));
		Route route = DataManager.getInstance().routes.get(0);
		Gson gson = new Gson();
		List<Step> steps = new ArrayList<Step>();
		for (int i = 0; i < route.getStepCount(); i++) {
			Segment segment = route.getStep(i);
			Step step = new Step();
			step.setStartPointLat(segment.getFirstPoint().getLatitudeE6() / 1E6);
			step.setStartPointLon(segment.getFirstPoint().getLongitudeE6() / 1E6);
			step.setEndPointLat(segment.getLastPoint().getLatitudeE6() / 1E6);
			step.setEndPointLon(segment.getLastPoint().getLongitudeE6() / 1E6);
			//#debug debug
			System.out.println("step:"+i+" ,StartPointLat == "+step.getStartPointLat() + " ,StartPointLon == "+step.getStartPointLon()+" ,EndPointLat == "+step.getEndPointLat()+" ,EndPointLon == "+step.getEndPointLon());
			steps.add(step);
		}

		String str = gson.toJson(steps);

//		nameValuePair.add(new BasicNameValuePair("steps", str));

		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(RouteListActivity.this, Const.URL_, nameValuePair);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						int resultCode = jObject.getInt("resultCode");
						if (resultCode == 0) {// 成功
							Type listType = new TypeToken<List<CameraPoiBean>>() {
							}.getType();
							Gson gson = new Gson();
							List<CameraPoiBean> cameraPoiItems = gson.fromJson(jObject.getString("cameraList"), listType);
							if (cameraPoiItems != null && !cameraPoiItems.isEmpty()) {
								poiItems = new PoiItem[cameraPoiItems.size()];
								int i = 0;
								for (CameraPoiBean bean : cameraPoiItems) {
									PoiItem poiItem = new PoiItem("", new GeoPoint((int) (bean.getLat() * 1E6), (int) (bean.getLon() * 1E6)), bean.getName(), String.valueOf(bean.getVid()));
									poiItems[i] = poiItem;
									i++;
								}
								handler.sendEmptyMessage(SUCCESSED);
							} else {
								handler.sendEmptyMessage(Const.ERROR);
							}
						} else {// 失败
							handler.sendEmptyMessage(Const.ERROR);
						}
					} catch (JSONException e) {
						// #debug debug
						e.printStackTrace();
						handler.sendEmptyMessage(Const.ERROR);
					}

				} else {
					handler.sendEmptyMessage(Const.ERROR);
				}

			}
		});
	}

}
