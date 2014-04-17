package com.iconverge.ct.traffic.park;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.bean.RegionBean;
import com.iconverge.ct.traffic.data.Const;

/**
 * 停车场
 * 
 * @author guozhaoge
 * 
 */
public class RegionActivity extends BaseActivity {

	private RegionAdapter regionAdapter;
	private RegionHandler handler;

	private LinearLayout loadLayout;
	private ProgressBar progressBar;
	private TextView noDataTextView;
	ListView listView;

	private static class RegionHandler extends Handler {
		WeakReference<RegionActivity> mActivity;

		RegionHandler(RegionActivity activity) {
			mActivity = new WeakReference<RegionActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			RegionActivity activity = mActivity.get();
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
		setContentView(R.layout.region);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(R.string.car_park));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		handler = new RegionHandler(this);

		loadLayout = (LinearLayout) findViewById(R.id.load_progress_layout);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		noDataTextView = (TextView) findViewById(R.id.no_data);

		listView = (ListView) findViewById(R.id.region_lv_regions);
		regionAdapter = new RegionAdapter(this);
		listView.setAdapter(regionAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				RegionBean region = (RegionBean) regionAdapter.getItem(position);
				Intent intent = new Intent(RegionActivity.this, ParkActivity.class);
				intent.putExtra("regionId", region.getRegionId());
				intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(intent);
			}
		});

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("m", "listRegions"));
		nameValuePair.add(new BasicNameValuePair("username", Const.URL_USERNAME));
		nameValuePair.add(new BasicNameValuePair("password", Const.URL_PASSWORD));

		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(RegionActivity.this, Const.URL_, nameValuePair);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						int resultCode = jObject.getInt("resultCode");
						if (resultCode == 0) {// 成功
							Type listType = new TypeToken<List<RegionBean>>() {
							}.getType();
							Gson gson = new Gson();
							List<RegionBean> regions = gson.fromJson(jObject.getString("regionList"), listType);
							if (regions != null && !regions.isEmpty()) {
								listView.setVisibility(View.VISIBLE);
								loadLayout.setVisibility(View.GONE);
								regionAdapter.setRegions(regions);
								regionAdapter.notifyDataSetChanged();
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
		noDataTextView.setText(getString(R.string.query_error));
	}

}
