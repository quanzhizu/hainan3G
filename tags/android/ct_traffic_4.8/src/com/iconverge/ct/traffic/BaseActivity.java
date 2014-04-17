package com.iconverge.ct.traffic;


import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.iconverge.ct.traffic.view.CircleProgressDialog;

public class BaseActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	public void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	private CircleProgressDialog progressBar;
	public void showProgressBar(String msg) {
		if (progressBar == null) {
			progressBar = new CircleProgressDialog(this);
		}
		if (!progressBar.isShowing()) {
			progressBar.setMessage(msg);
			progressBar.show();
		}
	}

	public void hideProgressBar() {
		if (progressBar != null && progressBar.isShowing())
			progressBar.dismiss();
	}
}
