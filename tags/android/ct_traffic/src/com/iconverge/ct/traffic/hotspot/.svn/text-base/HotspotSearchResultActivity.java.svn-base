package com.iconverge.ct.traffic.hotspot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.MainActivity;
import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.adapter.ActionOptionsAdapter;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.MapAbcException;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.poisearch.PoiPagedResult;
import com.mapabc.mapapi.poisearch.PoiSearch;
import com.mapabc.mapapi.poisearch.PoiSearch.SearchBound;
import com.mapabc.mapapi.poisearch.PoiTypeDef;

/**
 * 热点信息查询结果页面
 * 
 * @author guozhaoge
 * 
 */
@SuppressWarnings("deprecation")
public class HotspotSearchResultActivity extends BaseActivity implements View.OnClickListener {

	ActionBar actionBar;

	private LinearLayout llProgressBar;
	private Button btnDistance, btnType, btnOrder;
	private ListView lvResult;

	private String type = "全部类型";
	private String strDistance = "1000米";
	private int intDistance = 1000;
	private String order = "默认排序";

	private PopupWindow popupWindowDistances = null;
	private PopupWindow popupWindowTypes = null;

	HotspotSearchResultHandler resultHandler;
	private HotspotSearchResultAdapter resultAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.hotspot_search_result);
		resultHandler = new HotspotSearchResultHandler(this);
		resultAdapter = new HotspotSearchResultAdapter(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getString("type");
		}

		actionBar = getSupportActionBar();
		actionBar.setTitle(type);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		llProgressBar = (LinearLayout) findViewById(R.id.hotspot_search_result_ll_progressbar);
		btnDistance = (Button) findViewById(R.id.hotspot_search_result_btn_distance);
		btnType = (Button) findViewById(R.id.hotspot_search_result_btn_type);
		btnOrder = (Button) findViewById(R.id.hotspot_search_result_btn_order);
		lvResult = (ListView) findViewById(R.id.hotspot_search_result_lv_result);

		btnDistance.setText(strDistance);
		btnType.setText(type);
		btnOrder.setText(order);

		setConditionEnabled(false);
		btnDistance.setOnClickListener(this);
		btnType.setOnClickListener(this);
		btnOrder.setOnClickListener(this);
		lvResult.setAdapter(resultAdapter);
		lvResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(HotspotSearchResultActivity.this, MainActivity.class);
				intent.putExtra("poiItems", resultAdapter.getResultArray());
				intent.putExtra("position", position);
				intent.putExtra("page", Const.PAGE_HOTSPOT);
				intent.putExtra("zoom", 16);
				intent.setType(Const.TYPE_POI);
				startActivity(intent);
			}
		});

		new HotspotSearchTask().execute();

		initPopupWindowDistance();
		initPopuWindowTypes();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hotspot_search_result_btn_distance:
			if (!popupWindowDistances.isShowing()) {
				popupWindowDistances.showAsDropDown(btnDistance, btnDistance.getLeft(), 0);
			}
			break;
		case R.id.hotspot_search_result_btn_type:
			if (!popupWindowTypes.isShowing()) {
				popupWindowTypes.showAsDropDown(btnType, 0, 0);
			}
			break;
		case R.id.hotspot_search_result_btn_order:
			showToast(btnOrder.getText().toString());
			break;
		default:
			break;
		}
	}

	private void initPopupWindowDistance() {
		View viewPopupWindow = (View) this.getLayoutInflater().inflate(R.layout.hotspot_popup_types, null);
		ListView listView = (ListView) viewPopupWindow.findViewById(R.id.hotspot_popup_types_lv_types);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				strDistance = Const.strDistances[position];
				intDistance = Const.intDistances[position];
				popupWindowDistances.dismiss();
				new HotspotSearchTask().execute();
			}
		});
		ActionOptionsAdapter optionsAdapter = new ActionOptionsAdapter(this, Const.strDistances);
		listView.setAdapter(optionsAdapter);
		popupWindowDistances = new PopupWindow(viewPopupWindow, (int) (Const.screen_width * 0.5), LayoutParams.WRAP_CONTENT, true);
		popupWindowDistances.setBackgroundDrawable(new BitmapDrawable());
	}

	private void initPopuWindowTypes() {
		View popupWindow = (View) this.getLayoutInflater().inflate(R.layout.hotspot_popup_types, null);
		ListView listView = (ListView) popupWindow.findViewById(R.id.hotspot_popup_types_lv_types);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				type = Const.strHotspotGrids[position];
				popupWindowTypes.dismiss();
				new HotspotSearchTask().execute();
			}
		});
		ActionOptionsAdapter optionsAdapter = new ActionOptionsAdapter(this, Const.strHotspotGrids);
		listView.setAdapter(optionsAdapter);

		popupWindowTypes = new PopupWindow(popupWindow, (int) (Const.screen_width * 0.5), LayoutParams.WRAP_CONTENT, true);
		popupWindowTypes.setBackgroundDrawable(new BitmapDrawable());
	}

	public void setConditionEnabled(boolean isEnabled) {
		btnDistance.setEnabled(isEnabled);
		btnType.setEnabled(isEnabled);
		btnOrder.setEnabled(isEnabled);
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

	private static class HotspotSearchResultHandler extends Handler {
		WeakReference<HotspotSearchResultActivity> mActivity;

		HotspotSearchResultHandler(HotspotSearchResultActivity activity) {
			mActivity = new WeakReference<HotspotSearchResultActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			HotspotSearchResultActivity activity = mActivity.get();
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

	class HotspotSearchTask extends AsyncTask<Void, Void, List<PoiItem>> {
		List<PoiItem> poiItems = new ArrayList<PoiItem>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setConditionEnabled(false);
			btnDistance.setText(strDistance);
			btnType.setText(type);
			resultAdapter.clear();
			llProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<PoiItem> doInBackground(Void... params) {
			PoiSearch poiSearch = new PoiSearch(HotspotSearchResultActivity.this,Const.MAP_API_KEY, new PoiSearch.Query(type, PoiTypeDef.All, Const.citycode)); // 设置搜索字符串，"0571为城市区号"
			GeoPoint point = new GeoPoint((int) (Const.latitude * 1E6), (int) (Const.longitude * 1E6));
			poiSearch.setBound(new SearchBound(point, intDistance));
			poiSearch.setPoiNumber(20);
			try {
				PoiPagedResult result = poiSearch.searchPOI();
				int pageCount = result.getPageCount();
				Log.i("PageCount:", pageCount + "");
				if (pageCount > 1) {
					for (int i = 1; i <= pageCount; i++) {
						List<PoiItem> items = result.getPage(i);
						poiItems.addAll(items);
					}
				} else if (pageCount == 1) {
					poiItems.addAll(result.getPage(1));
				}
			} catch (MapAbcException e) {
				e.printStackTrace();
			}
			return poiItems;
		}

		@Override
		protected void onPostExecute(List<PoiItem> result) {
			super.onPostExecute(result);
			setConditionEnabled(true);
			llProgressBar.setVisibility(View.GONE);
			actionBar.setSubtitle("【共" + result.size() + "家】");
			resultAdapter.setResult(result);
			resultAdapter.notifyDataSetChanged();
		}

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

}
