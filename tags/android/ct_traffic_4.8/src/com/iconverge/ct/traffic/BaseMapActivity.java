package com.iconverge.ct.traffic;

import com.iconverge.ct.traffic.view.CircleProgressDialog;

import android.os.Bundle;
import android.widget.Toast;


public class BaseMapActivity extends SherlockMapActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		this.setCachInInstalledPackage(true);
		super.onCreate(arg0);
	}
	
	public void showToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
