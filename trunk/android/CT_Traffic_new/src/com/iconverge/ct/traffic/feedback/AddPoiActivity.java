package com.iconverge.ct.traffic.feedback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.bean.CategoryBean;
import com.iconverge.ct.traffic.data.Const;
import com.iconverge.ct.traffic.db.CategoryDao;
import com.mapabc.mapapi.core.PoiItem;

public class AddPoiActivity extends BaseActivity {

	ActionBar actionBar;

	private MyHandler myHandler;
	public final static int SUCCESSED = 0;
	public final static int FAILED = 1;

	private Spinner spinnerCategory1;
	private Spinner spinnerCategory2;
	private Spinner spinnerCategory3;

	private LinearLayout piLayout;
	private EditText cnNameEditText;
	private EditText enNameEditText;
	private EditText anNameEditText;
	private EditText piEditText;
	private EditText kwEditText;
	private EditText telEditText;
	private EditText addressEditText;
	private EditText lUserEditText;
	private EditText lTelEditText;

	private Button submitBtn;
	private PoiItem item;
	// private String poiId;

	private String cc;// 城市代码

	private CategoryDao categoryDao;
	ArrayList<CategoryBean> category1 = new ArrayList<CategoryBean>();
	ArrayList<CategoryBean> category2 = new ArrayList<CategoryBean>();
	ArrayList<CategoryBean> category3 = new ArrayList<CategoryBean>();
	CategoryAdapter adapter1;
	CategoryAdapter adapter2;
	CategoryAdapter adapter3;

	double lat;
	double lon;

	private int flag = -1;
	private String categoryId = "";
	private Context mContext;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		setContentView(ResUtil.getInstance(mContext).getLayout("ct_traffic__poi_add"));
		Intent intent = getIntent();
		if (intent != null) {
			item = intent.getParcelableExtra("poi");
			flag = intent.getIntExtra("flag", flag);
			// poiId = intent.getStringExtra("poiId");
			if (item != null) {
				lat = item.getPoint().getLatitudeE6() / 1E6;
				lon = item.getPoint().getLongitudeE6() / 1E6;
				getCC(this, lat, lon);
			}
		} else
			finish();
		actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(mContext).getString("ct_traffic__poi_info")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		myHandler = new MyHandler(this);
		categoryDao = new CategoryDao(this);
		cnNameEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("cn_name_edit"));
		enNameEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("en_name_edit"));
		anNameEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("an_name_edit"));
		piLayout = (LinearLayout) findViewById(ResUtil.getInstance(mContext).getId("pi_layout"));
		piEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("pi_edit"));
		kwEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("kw_edit"));
		telEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("tel_edit"));
		addressEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("addr_edit"));
		lUserEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("luser_edit"));
		lTelEditText = (EditText) findViewById(ResUtil.getInstance(mContext).getId("ltel_edit"));

		if (flag == 0) {
			piLayout.setVisibility(View.VISIBLE);
			cnNameEditText.setText(item.getTitle());
			addressEditText.setText(item.getSnippet());
			telEditText.setText(item.getTel());
			categoryId = item.getTypeCode();
		}
		initCategory();
		submitBtn = (Button) findViewById(ResUtil.getInstance(mContext).getId("submit"));
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitPoi();
			}
		});
	}

	private void initCategory() {
		category1 = categoryDao.getCategoryByPId(0);
		spinnerCategory1 = (Spinner) findViewById(ResUtil.getInstance(mContext).getId("spinner_category1"));
		spinnerCategory2 = (Spinner) findViewById(ResUtil.getInstance(mContext).getId("spinner_category2"));
		spinnerCategory3 = (Spinner) findViewById(ResUtil.getInstance(mContext).getId("spinner_category3"));
		adapter1 = new CategoryAdapter(this);
		adapter2 = new CategoryAdapter(AddPoiActivity.this);
		adapter3 = new CategoryAdapter(AddPoiActivity.this);
		spinnerCategory1.setAdapter(adapter1);
		// spinnerCategory2.setAdapter(adapter2);
		// spinnerCategory3.setAdapter(adapter3);
		CategoryBean bean = null;
		if (categoryId.length() >= 2) {
			long id = Long.valueOf(categoryId.substring(0, 2));
			bean = categoryDao.getCategoryById(id);
		} else
			bean = category1.get(0);
		adapter1.refresh(category1);
		adapter1.notifyDataSetChanged();
		spinnerCategory1.setSelection(bean == null ? 0 : category1.indexOf(bean));
		spinnerCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CategoryBean bean = category1.get(arg2);
				category2 = categoryDao.getCategoryByPId(bean.getId());
				CategoryBean bean2 = null;
				if (categoryId != null && categoryId.length() >= 4) {
					long id = Long.valueOf(categoryId.substring(0, 4));
					bean2 = categoryDao.getCategoryById(id);
				} else
					bean2 = category2.get(0);
				spinnerCategory2.setAdapter(adapter2);
				adapter2.refresh(category2);
				adapter2.notifyDataSetChanged();
				spinnerCategory2.setSelection(bean2 == null ? 0 : category2.indexOf(bean2));
				spinnerCategory2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg20, View arg21, int arg22, long arg3) {
						CategoryBean bean = category2.get(arg22);
						category3 = categoryDao.getCategoryByPId(bean.getId());
						CategoryBean bean3 = null;
						if (categoryId != null && categoryId.length() >= 8) {
							long id = Long.valueOf(categoryId.substring(0, 8));
							bean3 = categoryDao.getCategoryById(id);
						} else {
							if (category3 != null && !category3.isEmpty())
								bean3 = category3.get(0);
						}
						spinnerCategory3.setAdapter(adapter3);
						adapter3.refresh(category3);
						adapter3.notifyDataSetChanged();
						spinnerCategory3.setSelection(bean3 == null ? 0 : category3.indexOf(bean3));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void getCC(Context context, double lat, double lon) {
		String url = "http://116.228.55.155:6060/dataquery/query?sid=702&key=" + Const.MAP_API_KEY + "&cenx=" + lon + "&ceny=" + lat + "&poinum=1&restype=json";
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
							if (str != null && str.length() == 6) {
								cc = str.substring(0, 2).concat("0000");
							}
						}
					} catch (Exception e) {
						// #debug debug
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void submitPoi() {
		if (item.getPoiId() == null || "".equals(item.getPoiId())) {
			return;
		}
		String cnName = cnNameEditText.getEditableText().toString().trim();
		if ("".equals(cnName)) {
			showToast(getString(ResUtil.getInstance(mContext).getString("ct_traffic__input_cn_name")));
			return;
		}
		String enName = enNameEditText.getEditableText().toString().trim();
		String anName = anNameEditText.getEditableText().toString().trim();
		Object obj = spinnerCategory3.getSelectedItem();
		CategoryBean bean = null;
		if (obj != null && obj instanceof CategoryBean) {
			bean = (CategoryBean) obj;
		}
		if (obj == null) {
			showToast(getString(ResUtil.getInstance(mContext).getString("ct_traffic__select_type")));
			return;
		}
		long tc = bean.getId();
		if ("".equals(cc)) {
			cc = Const.admincode;
		}
		int ctag = 0;
		String pi = "";
		if (flag == 0) {
			ctag = 0;
			pi = piEditText.getEditableText().toString().trim();
		} else if (flag == -2) {
			ctag = -2;
			pi = "";
		}
		String kw = kwEditText.getEditableText().toString().trim();
		if ("".equals(kw)) {
			showToast(getString(ResUtil.getInstance(mContext).getString("ct_traffic__input_kw")));
			return;
		}
		String addr = addressEditText.getEditableText().toString().trim();
		if ("".equals(addr)) {
			showToast(getString(ResUtil.getInstance(mContext).getString("ct_traffic__input_address")));
			return;
		}
		String tel = telEditText.getEditableText().toString().trim();
		String lUser = lUserEditText.getEditableText().toString().trim();
		if ("".equals(lUser)) {
			lUser = getString(ResUtil.getInstance(mContext).getString("ct_traffic__app_name"));
		}
		String lTel = lTelEditText.getEditableText().toString().trim();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String url = "http://116.228.55.155:6060/UGC/poiServer?hblbs=MP1&cn=" + cnName + "&en=" + enName + "&tc=" + tc + "&cc=" + cc + "&pi=" + pi + "&an=" + anName + "&kw=" + kw + "&x=" + lon + "&y=" + lat + "&tel=" + tel + "&addr=" + addr + "&lUser=" + lUser + "&lTel=" + lTel + "&ctag=" + ctag + "&guid=" + uuid;

		showProgressBar("");
		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(this, url);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				myHandler.sendEmptyMessage(Const.HIDE_PROGRESSBAR);
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
		WeakReference<AddPoiActivity> mActivity;

		MyHandler(AddPoiActivity activity) {
			mActivity = new WeakReference<AddPoiActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			AddPoiActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case Const.HIDE_PROGRESSBAR:
				activity.hideProgressBar();
				break;
			case SUCCESSED:
				activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("ct_traffic__thanks_for_feedback")));
				activity.finish();
				break;
			case FAILED:
				activity.showToast(activity.getString(ResUtil.getInstance(activity).getString("submit_error")));
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
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
