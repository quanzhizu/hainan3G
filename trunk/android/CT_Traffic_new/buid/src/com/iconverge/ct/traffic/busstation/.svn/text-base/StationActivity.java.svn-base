package com.iconverge.ct.traffic.busstation;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.PoiResultBean;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.PoiItem;

public class StationActivity extends BaseActivity {

	private StationAdapt sAdapt;
	private StationHandler stationHandler;
	private LinearLayout loadLayout;
	private ProgressBar progressBar;
	private TextView noDataTextView;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(ResUtil.getInstance(this).getLayout("ct_traffic__busstation"));
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(this).getString("ct_traffic__bus_stop")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		stationHandler = new StationHandler(this);
		
		loadLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("load_progress_layout"));
		progressBar = (ProgressBar) findViewById(ResUtil.getInstance(this).getId("progressbar"));
		noDataTextView = (TextView) findViewById(ResUtil.getInstance(this).getId("no_data"));
		
		String url = "http://116.228.55.155:6060/dataquery/query?sid=102&key=" + Const.MAP_API_KEY + "&city="+Const.admincode+"&srctype=BUS&range=1500&page=1&rows=10&cenx=" + Const.longitude + "&ceny=" + Const.latitude + "&restype=json";
		// #debug
		System.out.println("url == " + url);
		sAdapt = new StationAdapt(getApplicationContext());
		listView = (ListView) findViewById(ResUtil.getInstance(this).getId("busstation_lv"));
		listView.setAdapter(sAdapt);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				PoiResultBean bean = (PoiResultBean) arg0.getItemAtPosition(position);
				if (bean != null) {
					PoiItem[] poiItems = new PoiItem[1];
					String[] str = bean.getCoord().split(",");
					if (str != null && str.length == 2) {
						GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(str[0]) * 1E6), (int) (Double.valueOf(str[1]) * 1E6));
						PoiItem poiItem = new PoiItem(bean.getPermanentid(), geoPoint, bean.getName(), bean.getAddr());
						poiItem.setTypeCode(bean.getFeatcode());
						poiItem.setTel(bean.getTel());
						poiItem.setDistance((int)(bean.getDistance()));
						poiItems[0] = poiItem;
						ArrayList<String> poiIds = new ArrayList<String>();
						poiIds.add(bean.getPoiid());
						Intent intent = new Intent(StationActivity.this, MainActivity.class);
						intent.putExtra("poiItems", poiItems);
						intent.putExtra("position", 0);
						intent.putExtra("page", Const.PAGE_BUS);
						intent.putExtra("zoom", 16);
						intent.putExtra("poiIds",poiIds);
						intent.setType(Const.TYPE_POI);
						startActivity(intent);
					}
				}

			}
		});
//		showProgressBar(getString(R.string.query_ing));
		BVBHttpRequest request = BVBHttpRequest.requestWithURL(StationActivity.this, url);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						JSONObject jObject1 = jObject.getJSONObject("response");
						Type listType = new TypeToken<List<PoiResultBean>>() {
						}.getType();
						Gson gson = new Gson();
						List<PoiResultBean> station = gson.fromJson(jObject1.getString("docs"), listType);
						if (station != null && !station.isEmpty()) {
							listView.setVisibility(View.VISIBLE);
							loadLayout.setVisibility(View.GONE);
							sAdapt.initData(sortStationBean(station));
							sAdapt.notifyDataSetChanged();
						}else{
							stationHandler.sendEmptyMessage(Const.ERROR);
						}
					} catch (Exception e) {
						// #debug debug
						e.printStackTrace();
						stationHandler.sendEmptyMessage(Const.ERROR);
					}
				} else {
					stationHandler.sendEmptyMessage(Const.ERROR);
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

	public List<PoiResultBean> sortStationBean(List<PoiResultBean> lists) {
		if (lists != null && !lists.isEmpty()) {
			Collections.sort(lists, new Comparator<PoiResultBean>() {
				@Override
				public int compare(PoiResultBean c1, PoiResultBean c2) {
					double s1 = c1.getDistance();
					double s2 = c2.getDistance();
					if (s1 < s2)
						return -1;
					else
						return 1;
				}
			});
		}
		return lists;
	}

	private static class StationHandler extends Handler {
		WeakReference<StationActivity> mActivity;

		StationHandler(StationActivity activity) {
			mActivity = new WeakReference<StationActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			StationActivity activity = mActivity.get();
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
	
	private void showError(){
//		showToast(getString(R.string.query_error));
		listView.setVisibility(View.GONE);
		loadLayout.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		noDataTextView.setVisibility(View.VISIBLE);
		noDataTextView.setText(getString(ResUtil.getInstance(this).getString("ct_traffic__query_error")));
	}
}
