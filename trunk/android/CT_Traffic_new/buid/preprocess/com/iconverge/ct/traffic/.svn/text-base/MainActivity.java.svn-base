package com.iconverge.ct.traffic;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iconverge.ct.traffic.bean.PoiResultBean;
import com.iconverge.ct.traffic.busstation.StationActivity;
import com.iconverge.ct.traffic.camera.CameraOverlay;
import com.iconverge.ct.traffic.circum.CircumActivity;
import com.iconverge.ct.traffic.data.Const;
import com.iconverge.ct.traffic.data.DataManager;
import com.iconverge.ct.traffic.hotspot.HotspotGridActivity;
import com.iconverge.ct.traffic.location.LocationUtil;
import com.iconverge.ct.traffic.location.MLocationOverlay;
import com.iconverge.ct.traffic.park.RegionActivity;
import com.iconverge.ct.traffic.poisearch.MyPoiOverlay;
import com.iconverge.ct.traffic.route.RouteActivity;
import com.iconverge.ct.traffic.util.GeocoderUtil;
import com.iconverge.ct.traffic.view.CustomDialog;
import com.mapabc.mapapi.core.GeoPoint;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.map.MapController;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.Overlay;
import com.mapabc.mapapi.map.PoiOverlay;
import com.mapabc.mapapi.map.RouteMessageHandler;
import com.mapabc.mapapi.map.RouteOverlay;
import com.mapabc.mapapi.route.Route;

public class MainActivity extends BaseMapActivity implements ActionBar.OnNavigationListener {

	private int currentPage;

	ActionBar actionBar;
	Context context;
	SearchView searchView;

	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point;
	private MLocationOverlay mLocationOverlay;

	private MainHandler mainHandler;
	LocationUtil location;

	private ImageView mylocationImg;

	private final static int SEARCH = 1;
	public final static int LOCATION = 2;
	public final static int LOCATION_MY = 3;

	private PoiOverlay poiOverlay;
	private List<PoiResultBean> poiResults;

	@Override
	protected void onCreate(Bundle arg0) {
		this.setCachInInstalledPackage(true);
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		context = this;
		setContentView(ResUtil.getInstance(context).getLayout("ct_traffic__main"));
		initConst();
		actionBar = getSupportActionBar();
		context = actionBar.getThemedContext();
		searchView = new SearchView(context);
		searchView.setQueryHint(getString(ResUtil.getInstance(context).getString("ct_traffic__main_search_hint")));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				queryPoi(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(context, ResUtil.getInstance(context).getArray("ct_traffic__actions"),ResUtil.getInstance(context).getLayout("ct_traffic__sherlock_spinner_item"));
		listAdapter.setDropDownViewResource(ResUtil.getInstance(context).getLayout("ct_traffic__sherlock_spinner_dropdown_item"));
		actionBar.setListNavigationCallbacks(listAdapter, this);
		if (Build.VERSION.SDK_INT < 14) {
			BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__bg_striped"));
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			actionBar.setBackgroundDrawable(bg);
			BitmapDrawable bgSplit = (BitmapDrawable) getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__bg_striped_split_img"));
			bgSplit.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			actionBar.setSplitBackgroundDrawable(bgSplit);
		}


		mainHandler = new MainHandler(this);

		setMapMode(MAP_MODE_BITMAP);// 设置地图为矢量模式
		mMapView = (MapView) findViewById(ResUtil.getInstance(context).getId("ct_traffic__mapView"));

		mMapController = mMapView.getController();
		point = new GeoPoint((int) (Const.latitude * 1E6), (int) (Const.longitude * 1E6));
		mMapController.setCenter(point); // 设置地图中心点
		mMapController.setZoom(12); // 设置地图缩放级别
		mLocationOverlay = new MLocationOverlay(this, mMapView);// 构造MyLocationOverlay对象。
		mLocationOverlay.enableMyLocation();
		mMapView.getOverlays().add(mLocationOverlay);// 将mLocationOverlay添加到地图中
		mLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mainHandler.sendMessage(Message.obtain(mainHandler, LOCATION_MY));
			}
		});

		mylocationImg = (ImageView) findViewById(ResUtil.getInstance(context).getId("ct_traffic__mylocation"));
		mylocationImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMapController.setCenter(point); // 设置地图中心点
				mMapController.animateTo(point);
				mMapController.setZoom(13); // 设置地图缩放级别
			}
		});

		location = new LocationUtil(this, mainHandler);
		location.start();
	}

	private void initConst() {
		Const.MAP_API_KEY = getString(ResUtil.getInstance(context).getString("ct_traffic__maps_api_key"));
		DisplayMetrics dm = this.getResources().getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
		Const.screen_height = dm.heightPixels;
		Const.screen_width = dm.widthPixels;
		Const.actions = getResources().getStringArray(ResUtil.getInstance(context).getArray("ct_traffic__actions"));
		Const.dialog_width = (int) (dm.widthPixels * 0.8);
		SharedPreferences sp = getSharedPreferences(Const.LOCALE_COM_ICONVERGE, MODE_PRIVATE);
		Const.latitude = sp.getFloat("latitude", (float) (29.52596916));
		Const.longitude = sp.getFloat("longitude", (float) (106.53957367));
		Const.address = sp.getString("address", "");
		Const.citycode = sp.getString("citycode", "023");
		Const.admincode = sp.getString("admincode", "500000");
		Const.dbVersion = sp.getInt("dbVersion", 0);
	}

	@Override
	public void onBackPressed() {
		if (currentPage == Const.PAGE_MAIN) {
			if (exit())
				finish();
		} else {
			super.onBackPressed();
		}
	}

	// 退出控制
	private long fTime;
	private boolean isExit;

	private boolean exit() {
		if (isExit && (System.currentTimeMillis() - fTime < 3000)) {
			return true;
		} else {
			Toast.makeText(this, getString(ResUtil.getInstance(context).getString("ct_traffic__press_again_to_exit")), Toast.LENGTH_SHORT).show();
			isExit = true;
			fTime = System.currentTimeMillis();
			return false;
		}
	}

	private void queryPoi(String text) {
		mainHandler.sendEmptyMessage(Const.SHOW_PROGRESSBAR);
		String url = "http://116.228.55.155:6060/dataquery/query?sid=101&restype=json&keyword=" + text + "&city=" + Const.admincode + "&key=" + Const.MAP_API_KEY;
		BVBHttpRequest request = BVBHttpRequest.requestWithURL(MainActivity.this, url);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {
			@Override
			public void textLoaded(String text) {
				mainHandler.sendEmptyMessage(Const.HIDE_PROGRESSBAR);
				if (text != null) {
					try {
						JSONObject jObject = new JSONObject(text);
						JSONObject jObject1 = jObject.getJSONObject("response");
						Type listType = new TypeToken<List<PoiResultBean>>() {
						}.getType();
						Gson gson = new Gson();
						poiResults = gson.fromJson(jObject1.getString("docs"), listType);
					} catch (Exception e) {
						// #debug
//@						e.printStackTrace();
					}
				}
				mainHandler.sendMessage(Message.obtain(mainHandler, SEARCH));
			}
		});
	}

	private void setActionBar(int page, Intent intent) {
		currentPage = page;
		switch (page) {
		case Const.PAGE_MAIN:
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setHomeButtonEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setSelectedNavigationItem(0);
			break;
		case Const.PAGE_ROUTE:
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(intent.getStringExtra("title"));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		case Const.PAGE_ROUTE_ON_MAP: {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			// SpannableStringBuilder ssb = new
			// SpannableStringBuilder(MessageFormat.format(getString(R.string.from_a_to_b),
			// Html.fromHtml("<font color=#02591b>" +
			// intent.getStringExtra("start") +
			// "</font>"),Html.fromHtml("<font color=#02591b>" +
			// intent.getStringExtra("end") + "</font>")));
			SpannableStringBuilder ssb = new SpannableStringBuilder(getString(ResUtil.getInstance(context).getString("ct_traffic__from")));
			ssb.append(" ").append(Html.fromHtml("<font size=18 color=#FFFFFF>" + intent.getStringExtra("start") + "</font>"));
			ssb.append(" ").append(getString(ResUtil.getInstance(context).getString("ct_traffic__to")));
			ssb.append(" ").append(Html.fromHtml("<font size=18 color=#FFFFFF>" + intent.getStringExtra("end") + "</font>"));
			actionBar.setTitle(ssb);
			actionBar.setSubtitle(intent.getStringExtra("subTitle"));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		}
		case Const.PAGE_CIRCUM:
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__circum")));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		case Const.PAGE_HOTSPOT:
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__hotspot")));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		case Const.PAGE_PARK:
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__car_park")));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		case Const.PAGE_BUS:
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__bus_stop")));
			actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
			break;
		case Const.PAGE_SEARCH:
			break;

		default:
			break;
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if (currentPage == Const.PAGE_MAIN) {
			getSupportMenuInflater().inflate(ResUtil.getInstance(context).getMenu("ct_traffic__main_menu"), menu);
			MenuItem searchMenuItem = menu.add(0, 1, 0, "Search");
			searchMenuItem.setIcon(ResUtil.getInstance(context).getDrawable("ct_traffic__ic_search")).setActionView(searchView).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {
					if (poiOverlay != null)
						poiOverlay.removeFromMap();
					return true;
				}

				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					if (poiOverlay != null)
						poiOverlay.removeFromMap();
					return true;
				}
			});
			menu.add(0, 2, 0, getString(ResUtil.getInstance(context).getString("ct_traffic__route"))).setIcon(ResUtil.getInstance(context).getDrawable("ct_traffic__ic_navigation")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add(0, 3, 0, getString(ResUtil.getInstance(context).getString("ct_traffic__car_park"))).setIcon(ResUtil.getInstance(context).getDrawable("ct_traffic__ic_car_park")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add(0, 4, 0, getString(ResUtil.getInstance(context).getString("ct_traffic__bus_stop"))).setIcon(ResUtil.getInstance(context).getDrawable("ct_traffic__ic_bus")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: {
			break;
		}
		case 2: {
			Intent intent = new Intent(MainActivity.this, RouteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 3: {
			Intent intent = new Intent(MainActivity.this, RegionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 4: {
			Intent intent = new Intent(MainActivity.this, StationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		if(item.getItemId() == ResUtil.getInstance(context).getId("ct_traffic__settings_real_time_traffic")){
			item.setChecked(!item.isChecked());
			mMapView.setTraffic(item.isChecked());
		}else if(item.getItemId() == ResUtil.getInstance(context).getId("ct_traffic__settings_satellite_view")){
			item.setChecked(!item.isChecked());
			mMapView.setSatellite(item.isChecked());
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onSearchRequested() {
		if (poiOverlay != null) {
			poiOverlay.removeFromMap();
		}
		mMapView.invalidate();
		return super.onSearchRequested();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent != null) {
			String type = intent.getType();
			if (Const.TYPE_ROUTE_START.equals(type)) {
				setActionBar(Const.PAGE_ROUTE, intent);
				showCustomDialog(getString(ResUtil.getInstance(context).getString("ct_traffic__tips_choice_start")), getResources().getStringArray(ResUtil.getInstance(context).getArray("ct_traffic__choice_items")), intent.getParcelableExtra("geoPoint"));
				showToast(getString(ResUtil.getInstance(context).getString("ct_traffic__choice_start")));
			} else if (Const.TYPE_ROUTE_END.equals(type)) {
				setActionBar(Const.PAGE_ROUTE, intent);
				showCustomDialog(getString(ResUtil.getInstance(context).getString("ct_traffic__tips_choice_end")), getResources().getStringArray(ResUtil.getInstance(context).getArray("ct_traffic__choice_items")), intent.getParcelableExtra("geoPoint"));
				showToast(getString(ResUtil.getInstance(context).getString("ct_traffic__choice_end")));
			} else if (Const.TYPE_ROUTE_ON_MAP.equals(type)) {
				setActionBar(Const.PAGE_ROUTE_ON_MAP, intent);
				routeOnMap(intent);
			} else if (Const.TYPE_POI.equals(type)) {
				setActionBar(intent.getIntExtra("page", -1), intent);
				Drawable drawable = getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__da_marker_red"));
				poiOnMap(intent.getParcelableArrayExtra("poiItems"), drawable, intent.getIntExtra("zoom", 13), intent.getIntExtra("position", 0), intent.getStringArrayListExtra("poiIds"));
			} else {
				setActionBar(Const.PAGE_MAIN, intent);
			}
		}
	}

	private void poiOnMap(Parcelable[] parcelables, Drawable drawable, int zoom, int position, List<String> poiIds) {
		List<PoiItem> poiItems = new ArrayList<PoiItem>();
		for (Parcelable parcelable : parcelables) {
			if (parcelable instanceof PoiItem) {
				poiItems.add((PoiItem) parcelable);
			}
		}
		poiOnMap(poiItems, drawable, zoom, position, poiIds);
	}

	private void poiOnMap(List<PoiItem> poiItems, Drawable drawable, int zoom, int position, List<String> poiIds) {
		GeoPoint geoPoint = poiItems.get(0).getPoint();
		if (poiOverlay != null) {
			poiOverlay.removeFromMap();
		}
		poiOverlay = new MyPoiOverlay(this, drawable, poiItems, poiIds); // 将结果的第一页添加到PoiOverlay
		poiOverlay.addToMap(mMapView); // 将poiOverlay标注在地图上
		if (position != -1)
			poiOverlay.showPopupWindow(position);
		mMapController.setCenter(geoPoint);
		// mMapController.animateTo(geoPoint);
		mMapController.setZoom(zoom);
		mMapView.invalidate();
	}

	CameraOverlay cameraOverlay;

	private void cameraOnMap(List<PoiItem> poiItems, Drawable drawable) {
		if (cameraOverlay != null) {
			cameraOverlay.removeFromMap();
		}
		cameraOverlay = new CameraOverlay(this, drawable, poiItems); // 将结果的第一页添加到PoiOverlay
		cameraOverlay.addToMap(mMapView); // 将poiOverlay标注在地图上
		mMapView.invalidate();
	}

	RouteOverlay routeOverlay;

	private void routeOnMap(Intent intent) {
		if (DataManager.getInstance().routes == null)
			return;
		Route route = DataManager.getInstance().routes.get(0);
		routeOverlay = new RouteOverlay(this, route);
		// 新增自定义路线样式
		Paint carLine = new Paint();
		carLine.setStyle(Style.STROKE);
		carLine.setColor(Color.rgb(9, 129, 240));
		carLine.setAlpha(180);
		carLine.setStrokeWidth(6.5f);
		carLine.setStrokeJoin(Join.ROUND);
		carLine.setStrokeCap(Cap.ROUND);
		carLine.setAntiAlias(true);
		int mode = intent.getIntExtra("mode", -1);
		if (mode == Route.BusDefault) {
			routeOverlay.setBusLinePaint(carLine);
		} else if (mode == Route.DrivingDefault) {
			routeOverlay.setCarLinePaint(carLine);
		} else {
			routeOverlay.setFootLinePaint(carLine);
		}
		mMapController.setCenter(route.getStartPos());
		mMapController.setZoom(14);
		// 获取第一条路径的Overlay
		routeOverlay.registerRouteMessage(routeMessageHandler); // 注册消息处理函数
		routeOverlay.addToMap(mMapView);
		Parcelable[] parcelables = intent.getParcelableArrayExtra("poiItems");
		if (parcelables != null && parcelables.length > 0) {
			List<PoiItem> poiItems = new ArrayList<PoiItem>();
			for (Parcelable parcelable : parcelables) {
				if (parcelable instanceof PoiItem) {
					poiItems.add((PoiItem) parcelable);
				}
			}
			cameraOnMap(poiItems, getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__da_marker_camera")));
		}
	}

	private RouteMessageHandler routeMessageHandler = new RouteMessageHandler() {

		@Override
		public boolean onRouteEvent(MapView arg0, RouteOverlay arg1, int arg2, int arg3) {
			return false;
		}

		@Override
		public void onDragEnd(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {
			mMapController.setCenter(arg3);

		}

		@Override
		public void onDragBegin(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {

		}

		@Override
		public void onDrag(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {

		}
	};

	Dialog customDialog;
	private MapPointOverlay mapPointOverlay;

	private void showCustomDialog(String title, String[] items, Parcelable parcelable) {
		ListView lView = new ListView(this);
		if (parcelable != null && parcelable instanceof GeoPoint) {
			GeoPoint geoPoint = (GeoPoint) parcelable;
			mMapController.setCenter(geoPoint);
		}

		mapPointOverlay = new MapPointOverlay(this);
		customDialog = CustomDialog.listDialog(this, title, items, lView);
		lView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					mMapView.getOverlays().add(mapPointOverlay);
					customDialog.dismiss();
				}
			}
		});
		customDialog.setCancelable(false);
		customDialog.setCanceledOnTouchOutside(false);
		if (!customDialog.isShowing())
			customDialog.show();
	}

	public class MapPointOverlay extends Overlay {
		private LayoutInflater inflater;
		private View popUpView;

		public MapPointOverlay(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
		}

		@Override
		public boolean onTap(final GeoPoint point, final MapView view) {
			if (popUpView != null) {
				view.removeView(popUpView);
			}
			// Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
			popUpView = inflater.inflate(ResUtil.getInstance(context).getLayout("ct_traffic__popup_choice"), null);
			// TextView address = (TextView)
			// popUpView.findViewById(R.id.PoiAddress);
			// address.setText(point.);
			MapView.LayoutParams lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, point, 0, 0, MapView.LayoutParams.BOTTOM_CENTER);
			view.addView(popUpView, lp);
			popUpView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					view.removeView(popUpView);
					view.getOverlays().remove(mapPointOverlay);

					Intent data = new Intent();
					data.putExtra("geoPoint", point);
					setResult(RESULT_OK, data);
					onBackPressed();
				}
			});
			return super.onTap(point, view);
		}

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
		SharedPreferences sp = getSharedPreferences(Const.LOCALE_COM_ICONVERGE, MODE_PRIVATE);
		sp.edit().putString("address", Const.address).commit();
		sp.edit().putString("citycode", Const.citycode).commit();
		sp.edit().putString("admincode", Const.admincode).commit();
		sp.edit().putFloat("latitude", (float) Const.latitude).commit();
		sp.edit().putFloat("longitude", (float) Const.longitude).commit();

		DataManager.setData(null);
		location.stop();
	}

	private static class MainHandler extends Handler {
		WeakReference<MainActivity> mActivity;

		MainHandler(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case SEARCH: {
				try {
					if (activity.poiResults == null || activity.poiResults.isEmpty()) {
						activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__query_error")));
						break;
					}
					int size = activity.poiResults.size();
					ArrayList<PoiItem> poiItems = new ArrayList<PoiItem>(size);
					ArrayList<String> permIds = new ArrayList<String>(size);
					for (PoiResultBean bean : activity.poiResults) {
						String[] str = bean.getCoord().split(",");
						GeoPoint geoPoint = new GeoPoint((int) (Double.valueOf(str[0]) * 1E6), (int) (Double.valueOf(str[1]) * 1E6));
						PoiItem poiItem = new PoiItem(bean.getPermanentid(), geoPoint, bean.getName(), bean.getAddr());
						poiItem.setTypeCode(bean.getFeatcode());
						poiItem.setTel(bean.getTel());
						poiItem.setDistance((int) (bean.getDistance()));
						poiItems.add(poiItem);
						permIds.add(bean.getPermanentid());
					}

					activity.mMapController.setZoom(13);
					activity.mMapController.animateTo(poiItems.get(0).getPoint());
					if (activity.poiOverlay != null) {
						activity.poiOverlay.removeFromMap();
					}
					Drawable drawable = activity.getResources().getDrawable(ResUtil.getInstance(activity).getDrawable("ct_traffic__da_marker_red"));
					activity.poiOverlay = new MyPoiOverlay(activity, drawable, poiItems, permIds); // 将结果的第一页添加到PoiOverlay
					activity.poiOverlay.addToMap(activity.mMapView); // 将poiOverlay标注在地图上
					activity.poiOverlay.showPopupWindow(0);
					activity.mMapView.invalidate();
				} catch (Exception e) {
					// #debug
//@					e.printStackTrace();
					activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__query_error")));
				}
				break;
			}
			case LOCATION_MY: {
				Location location = activity.mLocationOverlay.getLastFix();
				activity.point = activity.mLocationOverlay.getMyLocation();
				if (activity.currentPage == Const.PAGE_MAIN) {
					// activity.mMapController.setCenter(activity.point); //
					// 设置地图中心点
					activity.mMapController.animateTo(activity.point);
					activity.mMapController.setZoom(12); // 设置地图缩放级别
				}
				Const.latitude = location.getLatitude();
				Const.longitude = location.getLongitude();
				GeocoderUtil.getAdmincode(activity, location.getLatitude(), location.getLongitude());
				if (location.getExtras() != null) {
					Const.citycode = location.getExtras().getString("citycode");
					Const.address = location.getExtras().getString("desc");// activity.gercoderUtil.getAddress(geoLat,
																			// geoLng);
				}
				// Const.address =
				// activity.gercoderUtil.getAddress(location.getLatitude(),
				// location.getLongitude());
				break;
			}
			case LOCATION: {
				Bundle bundle = msg.getData();
				double geoLat = bundle.getDouble("geoLat");
				double geoLng = bundle.getDouble("geoLng");
				if (Const.latitude != geoLat || Const.longitude != geoLng) {
					Const.latitude = geoLat;
					Const.longitude = geoLng;
					activity.point = new GeoPoint((int) (geoLat * 1E6), (int) (geoLng * 1E6));
					Const.citycode = bundle.getString("citycode");
					Const.address = bundle.getString("desc");
					GeocoderUtil.getAdmincode(activity, Const.latitude, Const.longitude);
				}
				break;
			}
			case Const.SHOW_PROGRESSBAR:
				activity.showProgressBar(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__query_ing")));
				break;
			case Const.HIDE_PROGRESSBAR:
				activity.hideProgressBar();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		switch (itemPosition) {
		case 0:
			break;
		case 1: {
			Intent intent = new Intent(MainActivity.this, RouteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 2: {
			Intent intent = new Intent(MainActivity.this, RegionActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 3: {
			Intent intent = new Intent(MainActivity.this, StationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 4: {
			Intent intent = new Intent(MainActivity.this, CircumActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		case 5: {
			Intent intent = new Intent(MainActivity.this, HotspotGridActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
		return true;
	}

}
