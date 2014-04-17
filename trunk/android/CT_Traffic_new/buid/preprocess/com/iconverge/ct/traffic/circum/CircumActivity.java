package com.iconverge.ct.traffic.circum;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.MapAbcException;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.geocoder.Geocoder;
import com.mapabc.mapapi.poisearch.PoiPagedResult;
import com.mapabc.mapapi.poisearch.PoiSearch;
import com.mapabc.mapapi.poisearch.PoiSearch.SearchBound;
import com.mapabc.mapapi.poisearch.PoiTypeDef;

public class CircumActivity extends BaseActivity{
	
	ActionBar actionBar;
	private Context mContext;

	private GeoPoint point;

	private TextView tvMyLocation;
	private RadioGroup rgDistance;
	private LinearLayout llProgressBar;
	private ListView lvResult;

	//private String type = "周边美食";
	private int intDistance = 500;

	CircumHandler circumHandler;
	private CircumResultAdapter resultAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mContext = this;
		setContentView(ResUtil.getInstance(this).getLayout("ct_traffic__circum"));
		actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(this).getString("ct_traffic__circum")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
//		Bundle bundle = getIntent().getExtras();
		// point = new GeoPoint((int) (39.982402 * 1E6), (int) (116.305304 *
		// 1E6));
		point = new GeoPoint((int)(Const.latitude * 1E6), (int) (Const.longitude * 1E6));
		circumHandler = new CircumHandler(this);
		resultAdapter = new CircumResultAdapter(this);
//		if (bundle != null) {
//			type = bundle.getString("type");
//		}

		tvMyLocation = (TextView) findViewById(ResUtil.getInstance(this).getId("circum_tv_mylocation_value"));
		rgDistance = (RadioGroup) findViewById(ResUtil.getInstance(this).getId("circum_rg_distance"));
		llProgressBar = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("circum_ll_progressbar"));
		lvResult = (ListView) findViewById(ResUtil.getInstance(this).getId("circum_lv_result"));

		tvMyLocation.setText(Const.address);
		lvResult.setAdapter(resultAdapter);
		rgDistance.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == ResUtil.getInstance(mContext).getId("circum_rb_500")) {
					intDistance = 500;
				} else if (checkedId == ResUtil.getInstance(mContext).getId("circum_rb_1000")) {
					intDistance = 1000;
				} else if (checkedId == ResUtil.getInstance(mContext).getId("circum_rb_3000")) {
					intDistance = 3000;
				} else if (checkedId == ResUtil.getInstance(mContext).getId("circum_rb_5000")) {
					intDistance = 5000;
				}
				new CircumFoodTask().execute();
			}
		});
		lvResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PoiItem[] poiItems = resultAdapter.getResultArray();
				Intent intent = new Intent(CircumActivity.this, MainActivity.class);
				intent.putExtra("poiItems", poiItems);
				intent.putExtra("position", position);
				intent.putExtra("page", Const.PAGE_CIRCUM);
				intent.putExtra("zoom", 16);
				intent.setType(Const.TYPE_POI);
				startActivity(intent);
				
			}
		});
		new CircumFoodTask().execute();

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

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static class CircumHandler extends Handler {
		WeakReference<CircumActivity> mActivity;

		CircumHandler(CircumActivity activity) {
			mActivity = new WeakReference<CircumActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			CircumActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case 0: {
				break;
			}
			default:
				break;
			}
		}
	}

	class CircumFoodTask extends AsyncTask<Void, Void, List<PoiItem>> {
		List<PoiItem> poiItems = new ArrayList<PoiItem>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			rgDistance.setEnabled(false);
			resultAdapter.clear();
			llProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<PoiItem> doInBackground(Void... params) {
			PoiSearch poiSearch = new PoiSearch(CircumActivity.this,Const.MAP_API_KEY, new PoiSearch.Query("", PoiTypeDef.FoodBeverages, Const.citycode)); // 设置搜索字符串，"0571为城市区号"

			poiSearch.setBound(new SearchBound(point, intDistance));
			poiSearch.setPoiNumber(20);
			try {
				PoiPagedResult result = poiSearch.searchPOI();
				int pageCount = result.getPageCount();
				if (pageCount > 1) {
					for (int i = 1; i <= pageCount; i++) {
						List<PoiItem> items = result.getPage(i);
						poiItems.addAll(items);
					}
				} else if (pageCount == 1) {
					poiItems.addAll(result.getPage(1));
				}
			} catch (MapAbcException e) {
				//#debug debug
//@				e.printStackTrace();
			}
			return poiItems;
		}

		@Override
		protected void onPostExecute(List<PoiItem> result) {
			super.onPostExecute(result);
			rgDistance.setEnabled(true);
			llProgressBar.setVisibility(View.GONE);
			actionBar.setSubtitle(MessageFormat.format(getString(ResUtil.getInstance(mContext).getString("ct_traffic__total_of")), result.size()));
			resultAdapter.setResult(result);
			resultAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 根据经纬度获取地址描述信息
	 * 
	 * @param point
	 *            经纬度对象
	 * @param maxResult
	 *            最大结果集
	 * @return 地址描述信息
	 */
	public String getLocationByGeoPoint(GeoPoint point, int maxResult) {
		String strLocation = "";
		try {
			if (point.toString() != "") {
				Geocoder mGeocoder = new Geocoder(CircumActivity.this);
				int lat = point.getLatitudeE6();
				double lat1 = ((double) lat) / 1000000;
				int lon = point.getLongitudeE6();
				double lon1 = ((double) lon) / 1000000;
				List<Address> address = mGeocoder.getFromLocation(lat1, lon1, maxResult);
				if (address.size() != 0) {
					for (int i = 0; i < address.size(); i++) {
						Address ads = address.get(i);
						strLocation = getLocationByAddress(ads);
					}
				} else {
					strLocation = "Address GeoPoint Not Found";
				}
			}
		} catch (Exception e) {
			//#debug
//@			e.printStackTrace();
		}
		return strLocation;
	}

	/**
	 * 根据地址Address对象获取地址详细描述信息,比如：浙江省杭州市西湖区-西溪路附近
	 * 
	 * @param address
	 *            地址对象
	 * @return 地址详细描述信息
	 */
	public String getLocationByAddress(Address address) {
		String location = "";
		if (address.getAdminArea() != null) {
			location += address.getAdminArea();
		}
		if (address.getSubAdminArea() != null) {
			location += address.getSubAdminArea();
		}
		if (address.getLocality() != null) {
			location += address.getLocality();
		}
		if (address.getSubLocality() != null) {
			location += address.getSubLocality();
		}
		if (address.getThoroughfare() != null) {
			location += address.getThoroughfare();
		}
		if (address.getSubThoroughfare() != null) {
			location += address.getSubThoroughfare();
		}

		location += "-" + address.getFeatureName() + "附近";

		return location;
	}

}
