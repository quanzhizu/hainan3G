package com.iconverge.ct.traffic.feedback;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.CategoryBean;
import com.iconverge.ct.traffic.data.Const;
import com.iconverge.ct.traffic.db.CategoryDao;
import com.iconverge.ct.traffic.view.CustomDialog;
import com.mapabc.mapapi.core.PoiItem;

public class PoiInfoActivity extends BaseActivity {

	ActionBar actionBar;
	private Context context;
	private MyHandler myHandler;
	public final static int SUCCESSED = 0;
	public final static int FAILED = 1;

	private TextView nameTextView;
	private TextView addressTextView;
	private TextView typeTextView;
	private LinearLayout telLayout;
	private TextView telTextView;

	private Button findbugBtn;
	private Button addBtn;
	private Button editBtn;
	private Button delBtn;
	private PoiItem item;
	private String poiId;

	CategoryDao categoryDao;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context = this;
		setContentView(ResUtil.getInstance(context).getLayout("ct_traffic__poi_info"));
		actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__poi_info")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		categoryDao = new CategoryDao(this);
		initDB();
		myHandler = new MyHandler(this);
		nameTextView = (TextView) findViewById(ResUtil.getInstance(context).getId("name"));
		addressTextView = (TextView) findViewById(ResUtil.getInstance(context).getId("address"));
		telLayout = (LinearLayout) findViewById(ResUtil.getInstance(context).getId("tel_layout"));
		telTextView = (TextView) findViewById(ResUtil.getInstance(context).getId("tel"));
		typeTextView = (TextView) findViewById(ResUtil.getInstance(context).getId("type"));

		findbugBtn = (Button) findViewById(ResUtil.getInstance(context).getId("findbug"));
		findbugBtn.setOnClickListener(clickListener);
		addBtn = (Button) findViewById(ResUtil.getInstance(context).getId("add"));
		addBtn.setOnClickListener(clickListener);
		editBtn = (Button) findViewById(ResUtil.getInstance(context).getId("edit"));
		editBtn.setOnClickListener(clickListener);
		delBtn = (Button) findViewById(ResUtil.getInstance(context).getId("del"));
		delBtn.setOnClickListener(clickListener);
		Intent intent = getIntent();
		if (intent != null) {
			item = intent.getParcelableExtra("poi");
			if (item != null) {
				nameTextView.setText(item.getTitle());
				if (item.getTypeDes() != null) {
					typeTextView.setText(item.getTypeDes());
				} else {
					typeTextView.setText(getCategoryById(item.getTypeCode()));
				}
				addressTextView.setText(item.getSnippet());
				poiId = intent.getStringExtra("poiId");
				if (item.getTel() == null || "".equals(item.getTel())) {
					telLayout.setVisibility(View.GONE);
				} else {
					telLayout.setVisibility(View.VISIBLE);
					telTextView.setText(item.getTel());
				}
			}
		} else
			finish();
	}

	private String getCategoryById(String id) {
		CategoryBean bean = null;
		try {
			bean = categoryDao.getCategoryById(Long.parseLong(id));
		} catch (Exception e) {
			// #debug debug
//@			e.printStackTrace();
		}
		return bean == null ? id : bean.getName();
	}

	private void initDB() {
		if (Const.dbVersion == 1)
			return;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open("sql/category.sql");
			if (is == null)
				return;
			if (categoryDao.execSqlFile(is)) {
				SharedPreferences sp = getSharedPreferences(Const.LOCALE_COM_ICONVERGE, 4);
				sp.edit().putInt("dbVersion", 1).commit();
				Const.dbVersion = 1;
			}
		} catch (Exception e) {
			// #debug debug
//@			e.printStackTrace();
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == ResUtil.getInstance(context).getId("findbug")) {
				Intent intent = new Intent(PoiInfoActivity.this, FeedbackActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("poi", item);
				startActivity(intent);
			} else if (v.getId() == ResUtil.getInstance(context).getId("add")) {
				Intent intent = new Intent(PoiInfoActivity.this, AddPoiActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("poi", item);
				intent.putExtra("poiId", poiId);
				intent.putExtra("flag", -2);
				startActivity(intent);
			} else if (v.getId() == ResUtil.getInstance(context).getId("edit")) {
				Intent intent = new Intent(PoiInfoActivity.this, AddPoiActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("poi", item);
				intent.putExtra("poiId", poiId);
				intent.putExtra("flag", 0);
				startActivity(intent);
			} else if (v.getId() == ResUtil.getInstance(context).getId("del")) {
				showDelLayout();
			}

		}
	};

	private Dialog delDialog;

	private void showDelLayout() {
		final View view = LayoutInflater.from(this).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__feedback_delpoi"), null);
		delDialog = CustomDialog.createDialog(this, getString(ResUtil.getInstance(context).getString("ct_traffic__delete")), view, getString(ResUtil.getInstance(context).getString("ct_traffic__cancel")), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				delDialog.dismiss();

			}
		}, getString(ResUtil.getInstance(context).getString("ct_traffic__ok")), new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText descText = (EditText) view.findViewById(ResUtil.getInstance(context).getId("description_edit"));
				String desc = descText.getEditableText().toString();
				if ("".equals(desc)) {
					showToast(getString(ResUtil.getInstance(context).getString("ct_traffic__input_del_description")));
					return;
				}
				EditText contactText = (EditText) view.findViewById(ResUtil.getInstance(context).getId("contact_edit"));
				String contact = contactText.getEditableText().toString();
				if ("".equals(contact)) {
					showToast(getString(ResUtil.getInstance(context).getString("ct_traffic__please_input_feedback_contact")));
					return;
				}
				delPoi(desc, contact);
				delDialog.dismiss();
			}
		});
		delDialog.show();
	}

	private void delPoi(String desc, String contact) {
		// http://116.228.55.155:6060/UGC/poiServer?hblbs=MP2&permID=DYG027654&cn=爱知大厦&cc=310000&x=121.2345&y=31.0234&pi=已拆迁&lUser=tom&lTel=02120901111
		if (item.getPoiId() == null || "".equals(item.getPoiId())) {
			return;
		}
		double lat = item.getPoint().getLatitudeE6() / 1E6;
		double lon = item.getPoint().getLongitudeE6() / 1E6;
		String url = "http://116.228.55.155:6060/UGC/poiServer?hblbs=MP2&permID=" + item.getPoiId() + "&cn=" + item.getTitle() + "&cc=" + Const.admincode + "&x=" + lon + "&y=" + lat + "&pi=" + desc + "&lUser=" + contact;

		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(this, url);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					if (text.contains("成功")) {
						myHandler.sendEmptyMessage(SUCCESSED);
					} else {
						myHandler.sendEmptyMessage(FAILED);
					}
				} else {
					myHandler.sendEmptyMessage(FAILED);
				}

			}
		});
	}

	private static class MyHandler extends Handler {
		WeakReference<PoiInfoActivity> mActivity;

		MyHandler(PoiInfoActivity activity) {
			mActivity = new WeakReference<PoiInfoActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			PoiInfoActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case SUCCESSED:
				activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__thanks_for_feedback")));
				break;
			case FAILED:
				activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__submit_error")));
				break;
			default:
				break;
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
