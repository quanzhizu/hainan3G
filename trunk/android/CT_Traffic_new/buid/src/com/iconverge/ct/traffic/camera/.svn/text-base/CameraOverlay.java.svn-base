package com.iconverge.ct.traffic.camera;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvb.bvbhttptest.util.RtspXmlParser;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.bvb.http.MD5;
import com.iconverge.ct.traffic.ResUtil;
import com.iconverge.ct.traffic.data.Const;
import com.mapabc.mapapi.core.PoiItem;
import com.mapabc.mapapi.map.MapView;
import com.mapabc.mapapi.map.MapView.LayoutParams;
import com.mapabc.mapapi.map.PoiOverlay;

public class CameraOverlay extends PoiOverlay {
	private Context context;
	private Drawable drawable;
	private int number = 0;
	private List<PoiItem> poiItem;
	private LayoutInflater mInflater;
	private int height;

	public CameraOverlay(Context context, Drawable drawable, List<PoiItem> poiItem) {
		super(drawable, poiItem);
		this.context = context;
		this.poiItem = poiItem;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
	}

	@Override
	protected Drawable getPopupBackground() {
		// TODO Auto-generated method stub
		drawable = context.getResources().getDrawable(ResUtil.getInstance(context).getDrawable("ct_traffic__tip_pointer_button"));
		return drawable;
	}

	@Override
	protected View getPopupView(final PoiItem item) {
		// TODO Auto-generated method stub

		View view = mInflater.inflate(ResUtil.getInstance(context).getLayout("ct_traffic__camera_popup"), null);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		TextView nameTextView = (TextView) view.findViewById(ResUtil.getInstance(context).getId("PoiName"));
		nameTextView.setMaxWidth((int) (dm.widthPixels * 0.6));
		nameTextView.setText(item.getTitle());
		LinearLayout layout = (LinearLayout) view.findViewById(ResUtil.getInstance(context).getId("LinearLayoutPopup"));
		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (item.getSnippet() == null || "".equals(item.getSnippet())) {
					Toast.makeText(context, context.getString(ResUtil.getInstance(context).getString("ct_traffic__rtsp_illegal")), Toast.LENGTH_SHORT).show();
					return;
				}
				getCamera(item.getTitle(), item.getSnippet());
			}
		});

		return view;
	}

	@Override
	public void addToMap(MapView arg0) {
		// TODO Auto-generated method stub
		super.addToMap(arg0);
	}

	@Override
	protected LayoutParams getLayoutParam(int arg0) {
		// TODO Auto-generated method stub
		LayoutParams params = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, poiItem.get(number).getPoint(), 0, -height, LayoutParams.BOTTOM_CENTER);

		return params;
	}

	@Override
	protected Drawable getPopupMarker(PoiItem arg0) {
		// TODO Auto-generated method stub
		return super.getPopupMarker(arg0);
	}

	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		number = index;
		return super.onTap(index);
	}

	private String md5(String str) {
		return MD5.encodeMD5String(str);
	}

	private void getCamera(final String title, String vid) {
		long timestamp = System.currentTimeMillis();
		String url = Const.CAMERA_URL + "?icpname=" + Const.CAMERA_ICPNAME + "&timestamp=" + timestamp + "&vid=" + vid + "&authstring=" + md5(Const.CAMERA_ICPNAME + timestamp + vid + Const.CAMERA_KEY);
		// #debug debug
		System.out.println("url == " + url);
		BVBHttpRequest request = BVBHttpRequest.requestWithURL(context, url);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					// #debug
					System.out.println("==text==" + text);
					try {
						RtspXmlParser xmlParser = new RtspXmlParser();
						text = text.replace("\r", "");
						text = text.replace("\n", "");
						String rtspStr = xmlParser.readXml(text);
						// #debug
						System.out.println("rtspStr==" + rtspStr);
						if (!"".equals(rtspStr)) {
							// Intent intent = new Intent(context,
							// VideoPlayerActivity.class);
							// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							// intent.putExtra("rtsp", rtspStr);
							// intent.putExtra("title", title);
							// context.startActivity(intent);

							Intent returnIt = new Intent("road.newcellcom.cq.ui.fx");
							// 文件输出动作，road.newcellcom.cq.ui.fx为指定标识，不可用更改
							returnIt.setType("text/plain");
							returnIt.putExtra("vurl",rtspStr);
							context.startActivity(Intent.createChooser(returnIt, title));

						} else {
							Toast.makeText(context, context.getString(ResUtil.getInstance(context).getString("ct_traffic__rtsp_illegal")), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// #debug debug
						e.printStackTrace();
						Toast.makeText(context, context.getString(ResUtil.getInstance(context).getString("ct_traffic__rtsp_illegal")), Toast.LENGTH_SHORT).show();
					}
				}

			}
		});

	}
}