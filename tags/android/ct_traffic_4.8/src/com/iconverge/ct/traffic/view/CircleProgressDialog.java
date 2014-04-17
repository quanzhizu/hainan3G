package com.iconverge.ct.traffic.view;

import com.iconverge.ct.traffic.R;
import com.iconverge.ct.traffic.data.Const;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CircleProgressDialog {

	Dialog dialog;
	TextView titleView;

	public CircleProgressDialog(Context context) {
		dialog = new Dialog(context, R.style.dialog_dim);
		View view0 = (View) LayoutInflater.from(context).inflate(R.layout.dialog_progressbar, null);
		
		LinearLayout v = (LinearLayout) view0.findViewById(R.id.dialog_layout);
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		titleView = (TextView) view0.findViewById(R.id.progress_txt);

		dialog.getWindow().setContentView(view0);
		dialog.setCancelable(false);
	}

	public void setMessage(String message) {
		titleView.setText(message == null ? "" : message);

	}

	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void show() {
		dialog.show();
	}
	
	public void dismiss(){
		dialog.dismiss();
	}

}
