package com.iconverge.ct.traffic.route;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
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
import com.iconverge.ct.traffic.data.DataManager;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.MapAbcException;
import com.mapabc.mapapi.route.Route;

public class RouteActivity extends BaseActivity {

	private Context context;
	private boolean needToSearch = true;
	private RouteHandler mHandler;
	private boolean flag = false;
	private int mode;
	private ImageButton driveButton;
	private ImageButton transitButton;
	private ImageButton walkButton;

	private EditText startText;
	private EditText endText;
	private ProgressBar startProgress;
	private ProgressBar endProgress;
	private ImageView spinnerStrat;
	private ImageView spinnerEnd;

	// private LinearLayout getRouteLayout;

	private GeoPoint startPoint;
	private GeoPoint endPoint;

	private PopupWindow resulePopupWindow = null;
	private PointResultAdapter resultAdapter = null;

	private final static int HANDLER_SEARCH_START_SHOW_PROGRESSBAR = 0;
	private final static int HANDLER_SEARCH_START_HIDE_PROGRESSBAR = HANDLER_SEARCH_START_SHOW_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_START = HANDLER_SEARCH_START_HIDE_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_END_SHOW_PROGRESSBAR = HANDLER_SEARCH_START + 1;
	private final static int HANDLER_SEARCH_END_HIDE_PROGRESSBAR = HANDLER_SEARCH_END_SHOW_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_END = HANDLER_SEARCH_END_HIDE_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_ROUTE = HANDLER_SEARCH_END + 1;
	private final static int HANDLER_SEARCH_ROUTE_SHOW_PROGRESSBAR = HANDLER_SEARCH_ROUTE + 1;
	private final static int HANDLER_SEARCH_ROUTE_HIDE_PROGRESSBAR = HANDLER_SEARCH_ROUTE_SHOW_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_ROUTE_SUCCESSED = HANDLER_SEARCH_ROUTE_HIDE_PROGRESSBAR + 1;
	private final static int HANDLER_SEARCH_ROUTE_ERROR = HANDLER_SEARCH_ROUTE_SUCCESSED + 1;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context = this;
		setContentView(ResUtil.getInstance(context).getLayout("ct_traffic__route"));

		mHandler = new RouteHandler(this);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__route")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!flag) {
			initWedget();
			flag = true;
		}
	}

	private void initWedget() {
		startText = (EditText) findViewById(ResUtil.getInstance(context).getId("start_edit"));
		endText = (EditText) findViewById(ResUtil.getInstance(context).getId("end_edit"));
		spinnerStrat = (ImageView) findViewById(ResUtil.getInstance(context).getId("spinner_start"));
		spinnerStrat.setOnClickListener(spinnerClickListener);
		startProgress = (ProgressBar) findViewById(ResUtil.getInstance(context).getId("start_progressbar"));
		endProgress = (ProgressBar) findViewById(ResUtil.getInstance(context).getId("end_progressbar"));
		spinnerEnd = (ImageView) findViewById(ResUtil.getInstance(context).getId("spinner_end"));
		spinnerEnd.setOnClickListener(spinnerClickListener);
		startText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && !"".equals(s.toString()) && !getString(ResUtil.getInstance(context).getString("ct_traffic__my_point")).equals(s.toString()) && !getString(ResUtil.getInstance(context).getString("ct_traffic__point_on_map")).equals(s.toString()) && needToSearch) {
					if (s.length() >= 2) {
						searchPoi(HANDLER_SEARCH_START, s.toString());
					}
				} else {
					needToSearch = true;
				}

			}
		});

		endText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && !"".equals(s.toString()) && !getString(ResUtil.getInstance(context).getString("ct_traffic__my_point")).equals(s.toString()) && !getString(ResUtil.getInstance(context).getString("ct_traffic__point_on_map")).equals(s.toString()) && needToSearch) {
					if (s.length() >= 2){
						searchPoi(HANDLER_SEARCH_END, s.toString());
					}
				} else {
					needToSearch = true;
				}

			}
		});

		mode = Route.DrivingDefault;
		driveButton = (ImageButton) findViewById(ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_driving"));
		transitButton = (ImageButton) findViewById(ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_transit"));
		walkButton = (ImageButton) findViewById(ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_walk"));
		driveButton.setOnClickListener(clickListener);
		transitButton.setOnClickListener(clickListener);
		walkButton.setOnClickListener(clickListener);

		startPoint = new GeoPoint((int) (Const.latitude * 1E6), (int) (Const.longitude * 1E6));
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				needToSearch = false;
				endText.setText(bundle.getString("name") == null ? "" : bundle.getString("name"));
				endPoint = bundle.getParcelable("geo");
			}
		}
	}

	private void searchPoi(final int type, final String param) {
		String url = "http://116.228.55.155:6060/dataquery/query?sid=101&restype=json&keyword=" + param + "&city=" + Const.admincode + "&key=" + Const.MAP_API_KEY;
		BVBHttpRequest request = BVBHttpRequest.requestWithURL(RouteActivity.this, url);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				switch (type) {
				case HANDLER_SEARCH_START:
					mHandler.sendEmptyMessage(HANDLER_SEARCH_START_SHOW_PROGRESSBAR);
					break;
				case HANDLER_SEARCH_END:
					mHandler.sendEmptyMessage(HANDLER_SEARCH_END_SHOW_PROGRESSBAR);
					break;
				default:
					break;
				}
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						JSONObject jObject1 = jObject.getJSONObject("response");
						Type listType = new TypeToken<List<PoiResultBean>>() {
						}.getType();
						Gson gson = new Gson();
						List<PoiResultBean> poiResults = gson.fromJson(jObject1.getString("docs"), listType);
						if (poiResults == null || poiResults.isEmpty()) {
							mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_ERROR);
						} else {
							View parent = null;
							switch (type) {
							case HANDLER_SEARCH_START:
								if (!param.equals(startText.getText().toString())) {
									return;
								}
								mHandler.sendEmptyMessage(HANDLER_SEARCH_START_HIDE_PROGRESSBAR);
								parent = startText;
								break;
							case HANDLER_SEARCH_END:
								if (!param.equals(endText.getText().toString())) {
									return;
								}
								mHandler.sendEmptyMessage(HANDLER_SEARCH_END_HIDE_PROGRESSBAR);
								parent = endText;
								break;
							default:
								break;
							}
							if (resulePopupWindow == null)
								initPopuWindow();
							resultAdapter.setDataAndType(poiResults, type);
							resultAdapter.notifyDataSetChanged();
							if (!resulePopupWindow.isShowing()) {
								popupWindwShowing(parent);
							}
						}
					} catch (Exception e) {
						// #debug
						e.printStackTrace();
						mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_ERROR);
					}

				} else {
					mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_ERROR);
				}

			}
		});
	}

	private static class RouteHandler extends Handler {
		WeakReference<RouteActivity> mActivity;

		RouteHandler(RouteActivity activity) {
			mActivity = new WeakReference<RouteActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			RouteActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case HANDLER_SEARCH_START_SHOW_PROGRESSBAR:
				activity.startProgress.setVisibility(View.VISIBLE);
				break;
			case HANDLER_SEARCH_START_HIDE_PROGRESSBAR:
				activity.startProgress.setVisibility(View.GONE);
				break;
			case HANDLER_SEARCH_END_SHOW_PROGRESSBAR:
				activity.endProgress.setVisibility(View.VISIBLE);
				break;
			case HANDLER_SEARCH_END_HIDE_PROGRESSBAR:
				activity.endProgress.setVisibility(View.GONE);
				break;
			case HANDLER_SEARCH_START: {
				activity.needToSearch = false;
				Bundle bundle = msg.getData();
				PoiResultBean bean = (PoiResultBean) activity.resultAdapter.getItem(bundle.getInt("sel"));
				activity.startText.setText(bean.getName());
				String[] str = bean.getCoord().split(",");
				GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(str[0]) * 1E6), (int) (Double.valueOf(str[1]) * 1E6));
				activity.startPoint = geoPoint;
				activity.resulePopupWindow.dismiss();
				break;
			}
			case HANDLER_SEARCH_END: {
				activity.needToSearch = false;
				Bundle bundle = msg.getData();
				PoiResultBean bean = (PoiResultBean) activity.resultAdapter.getItem(bundle.getInt("sel"));
				activity.endText.setText(bean.getName());
				String[] str = bean.getCoord().split(",");
				GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(str[0]) * 1E6), (int) (Double.valueOf(str[1]) * 1E6));
				activity.endPoint = geoPoint;
				activity.resulePopupWindow.dismiss();
				break;
			}
			case HANDLER_SEARCH_ROUTE_SHOW_PROGRESSBAR:
				activity.showProgressBar(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__searching_route")));
				break;
			case HANDLER_SEARCH_ROUTE_HIDE_PROGRESSBAR:
				activity.hideProgressBar();
				break;
			case HANDLER_SEARCH_ROUTE:
				break;
			case HANDLER_SEARCH_ROUTE_ERROR:
				activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__query_error")));
				break;
			case HANDLER_SEARCH_ROUTE_SUCCESSED: {
				Intent intent = new Intent(activity, RouteListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.putExtra("start", activity.startText.getEditableText().toString());
				intent.putExtra("end", activity.endText.getEditableText().toString());
				intent.putExtra("mode", activity.mode);
				activity.startActivity(intent);
				break;
			}
			default:
				break;
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null) {
			switch (requestCode) {
			case Const.REQUESTCODE_ROUTE_START:
				startText.setText(getString(ResUtil.getInstance(context).getString("ct_traffic__point_on_map")));
				startPoint = (GeoPoint) (data.getParcelableExtra("geoPoint"));
				break;
			case Const.REQUESTCODE_ROUTE_END:
				endText.setText(getString(ResUtil.getInstance(context).getString("ct_traffic__point_on_map")));
				endPoint = (GeoPoint) (data.getParcelableExtra("geoPoint"));
				break;

			default:
				break;
			}
		}
	}

	private OnClickListener spinnerClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RouteActivity.this, MainActivity.class);
			int requestCode = -1;
			if(v.getId() == ResUtil.getInstance(context).getId("spinner_start")){
				intent.putExtra("title", getString(ResUtil.getInstance(context).getString("ct_traffic__choice_start")));
				if (endPoint != null) {
					intent.putExtra("geoPoint", endPoint);
				}
				intent.setType(Const.TYPE_ROUTE_START);
				requestCode = Const.REQUESTCODE_ROUTE_START;
			}else if(v.getId() == ResUtil.getInstance(context).getId("spinner_end")){
				if (startPoint != null) {
					intent.putExtra("geoPoint", startPoint);
				}
				intent.putExtra("title", getString(ResUtil.getInstance(context).getString("ct_traffic__choice_end")));
				intent.setType(Const.TYPE_ROUTE_END);
				requestCode = Const.REQUESTCODE_ROUTE_END;
			}
			startActivityForResult(intent, requestCode);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, getResources().getString(ResUtil.getInstance(context).getString("ct_traffic__get_route"))).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		getSupportMenuInflater().inflate(ResUtil.getInstance(context).getMenu("ct_traffic__route_menu"), menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			if (startPoint == null) {
				Toast.makeText(RouteActivity.this, getString(ResUtil.getInstance(context).getString("ct_traffic__tips_choice_start")), Toast.LENGTH_SHORT).show();
				break;
			}
			if (endPoint == null) {
				Toast.makeText(RouteActivity.this, getString(ResUtil.getInstance(context).getString("ct_traffic__tips_choice_end")), Toast.LENGTH_SHORT).show();
				break;
			}
			showProgressBar(getString(ResUtil.getInstance(context).getString("ct_traffic__searching_route")));
			new SearchRouteTask().execute(startPoint, endPoint);
			break;
		case android.R.id.home: {
			onBackPressed();
			break;
		}
		default:
			if(item.getItemId() == ResUtil.getInstance(context).getId("settings_swapped_start_end")){
				String str = startText.getText() == null ? "" : startText.getText().toString();
				needToSearch = false;
				startText.setText(endText.getText() == null ? "" : endText.getText().toString());
				needToSearch = false;
				endText.setText(str);

				GeoPoint strGeo = startPoint;
				startPoint = endPoint;
				endPoint = strGeo;
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v.getId() == ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_driving")){
				mode = Route.DrivingDefault;
				driveButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_driving_focused"));
				transitButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_transit_off"));
				walkButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_walk_off"));
			}else if(v.getId() == ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_transit")){
				mode = Route.BusDefault;
				driveButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_driving_off"));
				transitButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_transit_focused"));
				walkButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_walk_off"));
			}else if(v.getId() == ResUtil.getInstance(context).getId("imagebtn_roadsearch_tab_walk")){
				mode = Route.BusLeaseWalk;
				driveButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_driving_off"));
				transitButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_transit_off"));
				walkButton.setImageResource(ResUtil.getInstance(context).getDrawable("ct_traffic__mode_walk_focused"));
			}
		}
	};

	private class SearchRouteTask extends AsyncTask<GeoPoint, Void, List<Route>> {

		@Override
		protected List<Route> doInBackground(GeoPoint... params) {
			// mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_SHOW_PROGRESSBAR);
			Route.FromAndTo fromAndTo = new Route.FromAndTo(params[0], params[1]);
			List<Route> result = null;
			try {
				result = Route.calculateRoute(RouteActivity.this, Const.MAP_API_KEY, fromAndTo, mode);
			} catch (MapAbcException e) {
				// #debug debug
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Route> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_HIDE_PROGRESSBAR);
			if (result == null) {
				mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_ERROR);
			} else {
				// #debug debug
				System.out.println("result size == " + result.size());
				DataManager.getInstance().routes = result;
				mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_SUCCESSED);
			}
		}
	}

	/*private class SearchPoiTask extends AsyncTask<String, Void, PoiPagedResult> {

		private int type;
		private String param;

		public SearchPoiTask(int type) {
			this.type = type;
		}

		@Override
		protected PoiPagedResult doInBackground(String... params) {
			switch (type) {
			case HANDLER_SEARCH_START:
				mHandler.sendEmptyMessage(HANDLER_SEARCH_START_SHOW_PROGRESSBAR);
				break;
			case HANDLER_SEARCH_END:
				mHandler.sendEmptyMessage(HANDLER_SEARCH_END_SHOW_PROGRESSBAR);
				break;
			default:
				break;
			}
			Query startQuery = new Query(param = params[0], PoiTypeDef.All, Const.citycode);
			PoiSearch poiSearch = new PoiSearch(RouteActivity.this, Const.MAP_API_KEY, startQuery);
			poiSearch.setPoiNumber(10);
			PoiPagedResult result = null;
			try {
				result = poiSearch.searchPOI();
			} catch (MapAbcException e) {
				// #debug debug
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(PoiPagedResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			View parent = null;
			switch (type) {
			case HANDLER_SEARCH_START:
				if (!param.equals(startText.getText().toString())) {
					return;
				}
				mHandler.sendEmptyMessage(HANDLER_SEARCH_START_HIDE_PROGRESSBAR);
				parent = startText;
				break;
			case HANDLER_SEARCH_END:
				if (!param.equals(endText.getText().toString())) {
					return;
				}
				mHandler.sendEmptyMessage(HANDLER_SEARCH_END_HIDE_PROGRESSBAR);
				parent = endText;
				break;
			default:
				break;
			}
			if (result == null) {
				mHandler.sendEmptyMessage(HANDLER_SEARCH_ROUTE_ERROR);
			} else {
				// TODO
				if (result.getPageCount() > 0) {
					if (resulePopupWindow == null)
						initPopuWindow();
					try {
						List<PoiItem> poiItems = result.getPage(1);
						List<PointBean> datas = new ArrayList<PointBean>();
						for (int i = 0; i < poiItems.size(); i++) {
							if (i == 10)
								break;
							PoiItem item = poiItems.get(i);
							PointBean bean = new PointBean();
							bean.setName(item.getTitle());
							bean.setAddress(item.getSnippet());
							bean.setPoint(item.getPoint());
							datas.add(bean);
						}
						resultAdapter.setDataAndType(datas, type);
						resultAdapter.notifyDataSetChanged();
						if (!resulePopupWindow.isShowing()) {
							popupWindwShowing(parent);
						}
					} catch (MapAbcException e) {
						// #debug debug
						e.printStackTrace();
					}
				}
			}

		}
	}*/

	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {

		// initDatas();

		// PopupWindow浮动下拉框布局
		View resultView = (View) this.getLayoutInflater().inflate(ResUtil.getInstance(context).getLayout("ct_traffic__point_result_options"), null);
		ListView listView = (ListView) resultView.findViewById(ResUtil.getInstance(context).getId("list"));

		// 设置自定义Adapter
		resultAdapter = new PointResultAdapter(this, mHandler);
		listView.setAdapter(resultAdapter);
		// 获取下拉框依附的组件宽度
		resulePopupWindow = new PopupWindow(resultView, startText.getWidth(), LayoutParams.WRAP_CONTENT, false);
		resulePopupWindow.setOutsideTouchable(true);
		resulePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing(View parent) {
		resulePopupWindow.showAsDropDown(parent, 0, 0);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		resulePopupWindow.dismiss();
	}
}
