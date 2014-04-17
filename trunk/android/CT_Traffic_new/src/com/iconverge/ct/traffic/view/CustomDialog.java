package com.iconverge.ct.traffic.view;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;

public class CustomDialog {

	public final static Dialog confirmDialog(Context context, String title, String message, String positive, View.OnClickListener positiveClick) {
		return confirmDialog(context, title, message, positive, positiveClick, null, null, null, null);
	}

	public final static Dialog confirmDialog(Context context, String title, String message, String positive, View.OnClickListener positiveClick, String negative, View.OnClickListener negativeClick) {
		return confirmDialog(context, title, message, positive, positiveClick, null, null, negative, negativeClick);
	}

	public final static Dialog confirmDialog(Context context, String title, String message, String positive, View.OnClickListener positiveClick, String neutral, View.OnClickListener neutralClick, String negative, View.OnClickListener negativeClick) {
		Dialog dialog = new Dialog(context, ResUtil.getInstance(context).getStyle("ct_traffic__dialog_dim"));
		View view0 = (View) LayoutInflater.from(context).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__dialog_yes_no_message"), null);

		LinearLayout v = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_layout"));
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView titleView = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("dialog_title_textview"));
		titleView.setText(title == null ? context.getResources().getString(ResUtil.getInstance(context).getString("ct_traffic__tips")) : title);

		TextView messageText = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("dialog_message_textview"));
		messageText.setSingleLine(false);
		messageText.setMaxLines(10);
		messageText.setScrollbarFadingEnabled(true);
		messageText.setMovementMethod(ScrollingMovementMethod.getInstance());
		messageText.setText(message == null ? "" : message);

		Button positiveBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_positive"));
		if (positive != null) {
			positiveBtn.setText(positive);
			positiveBtn.setOnClickListener(positiveClick);
			positiveBtn.setVisibility(View.VISIBLE);
		} else {
			positiveBtn.setVisibility(View.GONE);
		}

		Button neutralBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_neutral"));
		if (neutral != null) {
			neutralBtn.setText(neutral);
			neutralBtn.setOnClickListener(neutralClick);
			neutralBtn.setVisibility(View.VISIBLE);
		} else {
			neutralBtn.setVisibility(View.GONE);
		}

		Button negativeBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_negative"));
		if (negative != null) {
			negativeBtn.setText(negative);
			negativeBtn.setOnClickListener(negativeClick);
			negativeBtn.setVisibility(View.VISIBLE);
		} else {
			negativeBtn.setVisibility(View.GONE);
		}

		dialog.getWindow().setContentView(view0);

		return dialog;
	}

	public final static Dialog createDialog(Context context, String title, View view, String positive, View.OnClickListener positiveClick) {
		return createDialog(context, title, view, positive, positiveClick, null, null, null, null);
	}

	public final static Dialog createDialog(Context context, String title, View view, String positive, View.OnClickListener positiveClick, String negative, View.OnClickListener negativeClick) {
		return createDialog(context, title, view, positive, positiveClick, null, null, negative, negativeClick);
	}

	public final static Dialog createDialog(Context context, String title, View view, String positive, View.OnClickListener positiveClick, String neutral, View.OnClickListener neutralClick, String negative, View.OnClickListener negativeClick) {
		Dialog dialog = new Dialog(context, ResUtil.getInstance(context).getStyle("ct_traffic__dialog_dim"));
		View view0 = (View) LayoutInflater.from(context).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__dialog"), null);

		LinearLayout v = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_layout"));
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView titleView = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("dialog_title_textview"));
		titleView.setText(title == null ? context.getResources().getString(ResUtil.getInstance(context).getString("ct_traffic__tips")) : title);

		LinearLayout contentView = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("view"));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		contentView.addView(view, params);

		Button positiveBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_positive"));
		if (positive != null) {
			positiveBtn.setText(positive);
			positiveBtn.setOnClickListener(positiveClick);
			positiveBtn.setVisibility(View.VISIBLE);
		} else {
			positiveBtn.setVisibility(View.GONE);
		}

		Button neutralBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_neutral"));
		if (neutral != null) {
			neutralBtn.setText(neutral);
			neutralBtn.setOnClickListener(neutralClick);
			neutralBtn.setVisibility(View.VISIBLE);
		} else {
			neutralBtn.setVisibility(View.GONE);
		}

		Button negativeBtn = (Button) view0.findViewById(ResUtil.getInstance(context).getId("dialog_button_negative"));
		if (negative != null) {
			negativeBtn.setText(negative);
			negativeBtn.setOnClickListener(negativeClick);
			negativeBtn.setVisibility(View.VISIBLE);
		} else {
			negativeBtn.setVisibility(View.GONE);
		}

		dialog.getWindow().setContentView(view0);

		return dialog;
	}

	public final static Dialog listDialog(Context context, String title, String[] items, ListView listView) {
		Dialog dialog = new Dialog(context, ResUtil.getInstance(context).getStyle("ct_traffic__dialog_dim"));
		View view0 = (View) LayoutInflater.from(context).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__dialog_list"), null);

		LinearLayout v = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_layout"));
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView titleView = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("dialog_title_textview"));
		titleView.setText(title == null ? context.getResources().getString(ResUtil.getInstance(context).getString("ct_traffic__tips")) : title);

		LinearLayout contentView = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_bg"));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setAddStatesFromChildren(true);
		listView.setCacheColorHint(context.getResources().getColor(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint")));
		listView.setDividerHeight(0);
		CheckedListAdapter listAdapter = new CheckedListAdapter(context);
		listAdapter.mData = items;
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(context.getResources().getColor(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint")));
		listView.setSelector(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint"));
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		listParams.gravity = Gravity.CENTER;
		listView.setLayoutParams(listParams);
		contentView.addView(listView);

		dialog.getWindow().setContentView(view0);
		return dialog;

	}

	public final static Dialog multiChoiceItemsDialog(Context context, String title, String[] items, boolean[] checkPos, ListView listView) {
		Dialog dialog = new Dialog(context, ResUtil.getInstance(context).getStyle("ct_traffic__dialog_dim"));
		View view0 = (View) LayoutInflater.from(context).inflate(ResUtil.getInstance(context).getLayout("ct_traffic__dialog_list"), null);

		LinearLayout v = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_layout"));
		v.setLayoutParams(new LinearLayout.LayoutParams(Const.dialog_width, LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView titleView = (TextView) view0.findViewById(ResUtil.getInstance(context).getId("dialog_title_textview"));
		titleView.setText(title == null ? context.getResources().getString(ResUtil.getInstance(context).getString("ct_traffic__tips")) : title);

		LinearLayout contentView = (LinearLayout) view0.findViewById(ResUtil.getInstance(context).getId("dialog_bg"));

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAddStatesFromChildren(true);
		listView.setCacheColorHint(context.getResources().getColor(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint")));
		listView.setDividerHeight(0);
		CheckedListAdapter listAdapter = new CheckedListAdapter(context);
		listAdapter.setType(ListView.CHOICE_MODE_MULTIPLE);
		listAdapter.mData = items;
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(context.getResources().getColor(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint")));
		listView.setSelector(ResUtil.getInstance(context).getColor("ct_traffic__color_list_cache_hint"));
		for (int i = 0; i < checkPos.length; i++) {
			listView.setItemChecked(i, checkPos[i]);
		}
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		listParams.gravity = Gravity.CENTER;
		listView.setLayoutParams(listParams);

		contentView.addView(listView);

		dialog.getWindow().setContentView(view0);
		return dialog;

	}
}

class CheckedListAdapter extends BaseAdapter {
	private Context mContext;
	String[] mData;
	private int type;

	public void setType(int type) {
		this.type = type;
	}

	public CheckedListAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {

		return mData == null ? 0 : mData.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if (type == ListView.CHOICE_MODE_SINGLE)
				convertView = LayoutInflater.from(mContext).inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__single_checked_item"), null);
			else if (type == ListView.CHOICE_MODE_MULTIPLE)
				convertView = LayoutInflater.from(mContext).inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__multi_checked_item"), null);
			else
				convertView = LayoutInflater.from(mContext).inflate(ResUtil.getInstance(mContext).getLayout("ct_traffic__none_checked_item"), null);
			holder.layout = (LinearLayout) convertView.findViewById(ResUtil.getInstance(mContext).getId("layout1"));
			holder.textView = (CheckedTextView) convertView.findViewById(ResUtil.getInstance(mContext).getId("text1"));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (type == 0) {
			if (mData.length == 1) {
				holder.layout.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_bg"));
			} else {
				if (position == 0) {
					holder.layout.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_top"));
				} else if (position == getCount() - 1) {
					holder.layout.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_bottom"));
				} else {
					holder.layout.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_center"));
				}
			}
		} else {
			if (mData.length == 1) {
				holder.textView.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_bg"));
			} else {
				if (position == 0) {
					holder.textView.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_top"));
				} else if (position == getCount() - 1) {
					holder.textView.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_bottom"));
				} else {
					holder.textView.setBackgroundResource(ResUtil.getInstance(mContext).getDrawable("ct_traffic__setting_item_center"));
				}
			}
		}
		holder.textView.setText(mData[position]);
		return convertView;
	}

	private final static class ViewHolder {
		private LinearLayout layout;
		private CheckedTextView textView;
	}
}
