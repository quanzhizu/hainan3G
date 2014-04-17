package com.iconverge.ct.traffic.park;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.ParkBean;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.PoiItem;

public class ParkActivity extends BaseActivity {

	private Context context;
	private ParkAdapter parkAdapter;
	private ParkHandler handler;
	private int regionId;

	private LinearLayout loadLayout;
	private ProgressBar progressBar;
	private TextView noDataTextView;
	ListView listView;

	private static class ParkHandler extends Handler {
		WeakReference<ParkActivity> mActivity;

		ParkHandler(ParkActivity activity) {
			mActivity = new WeakReference<ParkActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			ParkActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case Const.ERROR:
				activity.showError();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		setContentView(ResUtil.getInstance(context).getLayout("ct_traffic__park"));
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__car_park")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		handler = new ParkHandler(this);

		Intent it = getIntent();
		if (it != null) {
			regionId = it.getIntExtra("regionId", -1);
		}

		loadLayout = (LinearLayout) findViewById(ResUtil.getInstance(context).getId("load_progress_layout"));
		progressBar = (ProgressBar) findViewById(ResUtil.getInstance(context).getId("progressbar"));
		noDataTextView = (TextView) findViewById(ResUtil.getInstance(context).getId("no_data"));

		listView = (ListView) findViewById(ResUtil.getInstance(context).getId("park_lv_parks"));
		parkAdapter = new ParkAdapter(this);
		listView.setAdapter(parkAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				ParkBean park = (ParkBean) parkAdapter.getItem(position);
				GeoPoint geoPoint = new GeoPoint((int) (park.getLat() * 1E6), (int) (park.getLon() * 1E6));
				PoiItem[] poiItems = new PoiItem[1];
				PoiItem poiItem = new PoiItem(park.getId(), geoPoint, park.getName(), park.getAddress());
				poiItem.setTypeCode("1202");
				poiItem.setTypeDes(getString(ResUtil.getInstance(context).getString("ct_traffic__car_park")));
				poiItems[0] = poiItem;
				Intent intent = new Intent(ParkActivity.this, MainActivity.class);
				intent.putExtra("poiItems", poiItems);
				intent.putExtra("page", Const.PAGE_PARK);
				intent.putExtra("position", 0);
				intent.putExtra("zoom", 13);
				intent.setType(Const.TYPE_POI);
				startActivity(intent);
			}
		});

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("m", "listParks"));
		nameValuePair.add(new BasicNameValuePair("username", Const.URL_USERNAME));
		nameValuePair.add(new BasicNameValuePair("password", Const.URL_PASSWORD));
		nameValuePair.add(new BasicNameValuePair("regionId", String.valueOf(regionId)));

		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(ParkActivity.this, Const.URL_, nameValuePair);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						int resultCode = jObject.getInt("resultCode");
						if (resultCode == 0) {// 成功
							Type listType = new TypeToken<List<ParkBean>>() {
							}.getType();
							Gson gson = new Gson();
							List<ParkBean> parks = gson.fromJson(jObject.getString("parkList"), listType);
							if (parks != null && !parks.isEmpty()) {
								listView.setVisibility(View.VISIBLE);
								loadLayout.setVisibility(View.GONE);
								parkAdapter.setParks(parks);
								parkAdapter.notifyDataSetChanged();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showError() {
		listView.setVisibility(View.GONE);
		loadLayout.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		noDataTextView.setVisibility(View.VISIBLE);
		noDataTextView.setText(getString(ResUtil.getInstance(context).getString("ct_traffic__query_error")));
	}
}
