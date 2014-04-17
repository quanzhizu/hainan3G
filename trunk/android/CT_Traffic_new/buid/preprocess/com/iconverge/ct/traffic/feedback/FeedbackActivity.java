package com.iconverge.ct.traffic.feedback;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.PoiItem;

public class FeedbackActivity extends BaseActivity {

	ActionBar actionBar;

	private Context context;
	private MyHandler myHandler;
	public final static int SUCCESSED = 0;
	public final static int FAILED = 1;
	private TextView nameTextView;
	private TextView addressTextView;
	private TextView typeTextView;
	private LinearLayout phoneLayout;
	private TextView telTextView;
	private CheckBox nameCheckBox;
	private CheckBox addressCheckBox;
	private CheckBox telCheckBox;
	private CheckBox placeCheckBox;
	private CheckBox otherCheckBox;
	private EditText descEditText;
	private EditText contactEditText;
	private Button submitBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(ResUtil.getInstance(this).getLayout("ct_traffic__feedback"));
		context = this;
		actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(this).getString("ct_traffic__feedback")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		myHandler = new MyHandler(this);
		nameTextView = (TextView) findViewById(ResUtil.getInstance(this).getId("name"));
		addressTextView = (TextView) findViewById(ResUtil.getInstance(this).getId("address"));
		phoneLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("phone_layout"));
		telTextView = (TextView) findViewById(ResUtil.getInstance(this).getId("tel"));
		typeTextView = (TextView) findViewById(ResUtil.getInstance(this).getId("type"));

		nameCheckBox = (CheckBox) findViewById(ResUtil.getInstance(this).getId("name_error_checkbox"));
		LinearLayout nameLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("name_error_layout"));
		nameLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nameCheckBox.setChecked(!nameCheckBox.isChecked());

			}
		});
		addressCheckBox = (CheckBox) findViewById(ResUtil.getInstance(this).getId("address_error_checkbox"));
		LinearLayout addressLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("address_error_layout"));
		addressLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addressCheckBox.setChecked(!addressCheckBox.isChecked());

			}
		});
		telCheckBox = (CheckBox) findViewById(ResUtil.getInstance(this).getId("tel_error_checkbox"));
		LinearLayout telLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("tel_error_layout"));
		telLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				telCheckBox.setChecked(!telCheckBox.isChecked());

			}
		});
		placeCheckBox = (CheckBox) findViewById(ResUtil.getInstance(this).getId("place_error_checkbox"));
		LinearLayout placeLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("place_error_layout"));
		placeLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				placeCheckBox.setChecked(!placeCheckBox.isChecked());

			}
		});
		otherCheckBox = (CheckBox) findViewById(ResUtil.getInstance(this).getId("other_error_checkbox"));
		LinearLayout otherLayout = (LinearLayout) findViewById(ResUtil.getInstance(this).getId("other_error_layout"));
		otherLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				otherCheckBox.setChecked(!otherCheckBox.isChecked());

			}
		});
		descEditText = (EditText) findViewById(ResUtil.getInstance(this).getId("description_edit"));
		contactEditText = (EditText) findViewById(ResUtil.getInstance(this).getId("contact_edit"));
		submitBtn = (Button) findViewById(ResUtil.getInstance(this).getId("submit"));
		nameLayout.setFocusable(true);
		nameLayout.setFocusableInTouchMode(true);
		nameLayout.requestFocus();
		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();

			}
		});
		Intent intent = getIntent();
		if (intent != null) {
			PoiItem item = intent.getParcelableExtra("poi");
			if (item != null) {
				nameTextView.setText(item.getTitle());
				typeTextView.setText(item.getTypeDes());
				addressTextView.setText(item.getSnippet());
				if (item.getTel() == null || "".equals(item.getTel())) {
					phoneLayout.setVisibility(View.GONE);
				} else {
					phoneLayout.setVisibility(View.VISIBLE);
					telTextView.setText(item.getTel());
				}
			}else finish();
		}
	}

	private void submit() {
		StringBuilder buidler = new StringBuilder();
		if (nameCheckBox.isChecked())
			buidler.append(getString(ResUtil.getInstance(context).getString("ct_traffic__feedback_name_error"))).append(",");
		if (addressCheckBox.isChecked())
			buidler.append(getString(ResUtil.getInstance(context).getString("ct_traffic__feedback_address_error"))).append(",");
		if (telCheckBox.isChecked())
			buidler.append(getString(ResUtil.getInstance(context).getString("ct_traffic__feedback_tel_error"))).append(",");
		if (placeCheckBox.isChecked())
			buidler.append(getString(ResUtil.getInstance(context).getString("ct_traffic__feedback_place_error"))).append(",");
		if (otherCheckBox.isChecked())
			buidler.append(getString(ResUtil.getInstance(context).getString("ct_traffic__feedback_other_error"))).append(",");
		buidler.append(descEditText.getEditableText() == null ? "" : descEditText.getEditableText());
		if (buidler.length() == 0) {
			showToast(getString(ResUtil.getInstance(context).getString("ct_traffic__input_description")));
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String ld = sdf.format(new Date());
		String su = contactEditText.getEditableText() == null ? "" : contactEditText.getEditableText().toString();
		String url = "http://116.228.55.155:6060/UGC/ErrorServer?hblbs=E1&it=" + getString(ResUtil.getInstance(context).getString("ct_traffic__app_name")) + "&cc=" + Const.admincode + "&et=" + typeTextView.getText() + "&ei=" + buidler.toString() + "&ld=" + ld + "&su=" + su;
		showProgressBar("");
		BVBHttpRequest httpRequest = BVBHttpRequest.requestWithURL(this, url);
		httpRequest.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				myHandler.sendEmptyMessage(Const.HIDE_PROGRESSBAR);
				if (text != null) {
					if(text.contains("成功")){
						myHandler.sendEmptyMessage(SUCCESSED);
					}else{
						myHandler.sendEmptyMessage(FAILED);
					}
				}else{
					myHandler.sendEmptyMessage(FAILED);
				}

			}
		});
	}

	private static class MyHandler extends Handler {
		WeakReference<FeedbackActivity> mActivity;

		MyHandler(FeedbackActivity activity) {
			mActivity = new WeakReference<FeedbackActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			FeedbackActivity activity = mActivity.get();
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
