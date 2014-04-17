package com.iconverge.ct.traffic.view;

import com.iconverge.ct.traffic.ResUtil;
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
		dialog = new Dialog(context, ResUtil.getInstance(context).getStyle("ct_traffic__dialog_dim"));
		View view0 = (View) LayoutInflater.from(context).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__dialog_progressbar"), null);
		
		LinearLayout v = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_layout"));
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		titleView = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("progress_txt"));

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
