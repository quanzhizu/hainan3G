package com.iconverge.ct.traffic.hotspot;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.iconverge.ct.traffic.BaseActivity;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;

public class HotspotGridActivity extends BaseActivity{

	private Context context;
	private GridView gvGrid;

	HotspotHandler hotspotHandler;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		setContentView(ResUtil.getInstance(context).getLayout("ct_traffic__hotspot"));
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(getString(ResUtil.getInstance(context).getString("ct_traffic__hotspot")));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		hotspotHandler = new HotspotHandler(this);

		gvGrid = (GridView) findViewById(ResUtil.getInstance(context).getId("hotspot_grid"));

		gvGrid.setAdapter(new HotspotGridAdapter(this));

		gvGrid.setColumnWidth(Const.screen_width / 5);
		gvGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent it = new Intent(HotspotGridActivity.this, HotspotSearchResultActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Bundle bundle = new Bundle();
				bundle.putString("type", Const.strHotspotGrids[position]);
				it.putExtras(bundle);
				HotspotGridActivity.this.startActivity(it);
			}
		});

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
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class HotspotHandler extends Handler {
		WeakReference<HotspotGridActivity> mActivity;

		HotspotHandler(HotspotGridActivity activity) {
			mActivity = new WeakReference<HotspotGridActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			HotspotGridActivity activity = mActivity.get();
			if (activity == null)
				return;
			switch (msg.what) {
			case 0: {

				break;
			}
			default:
				break;
			}
		}
	}

}
