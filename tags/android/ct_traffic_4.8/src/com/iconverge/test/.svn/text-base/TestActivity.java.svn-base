/*package com.iconverge.test;

import java.util.List;

import org.videolan.vlc.gui.video.VideoPlayerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bvb.bvbhttptest.bean.VideoBean;
import com.bvb.bvbhttptest.util.RtspXmlParser;
import com.bvb.bvbhttptest.util.XmlParser;
import com.bvb.http.BVBHttpRequest;
import com.bvb.http.IBVBHttpLoadTextCallBack;
import com.bvb.http.MD5;
import com.iconverge.ct.traffic.R;

public class TestActivity extends Activity {

	private ListView listView;
	VideoListViewAdapter adapter;
	private String icpName = "iptvuser";
	private String key = "tykjt123456";
	private List<VideoBean> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new VideoListViewAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final VideoBean bean = (VideoBean) arg0.getItemAtPosition(arg2);
				long timestamp = System.currentTimeMillis();
				String url = "http://183.65.36.55/RoadViewServer_CQ/roadview_getrtsp_3.flow?icpname=" + icpName + "&timestamp=" + timestamp +"&vid=" + bean.getvId() + "&authstring=" + md5(icpName + timestamp + bean.getvId() + key);
				System.out.println("url == "+url);
				BVBHttpRequest request = BVBHttpRequest.requestWithURL(TestActivity.this, url);
				request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

					@Override
					public void textLoaded(String text) {
						if (text != null) {
							// #debug debug
							System.out.println("==text==" + text);
							try {
								RtspXmlParser xmlParser = new RtspXmlParser();
								text = text.replace("\r", "");
								text = text.replace("\n", "");
								String rtspStr = xmlParser.readXml(text);
//								rtspStr = rtspStr.replace("&amp;", "&");
//								rtspStr = rtspStr.replace("//", "//"+icpName+":"+key+"@");
								System.out.println("rtspStr=="+rtspStr);
//								rtspStr = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";
//								rtspStr = "rtsp://real.cctv.com.cn/news/xwlb/56K/1227.rm";
//								rtspStr = "rtsp://pub1.qmoon.net/911pop";
//								rtspStr = "rtsp://211.89.225.101/live1";
//								rtspStr = "rtsp://61.164.128.51/yyt";
//								Uri uri = Uri.parse(rtspStr);
//								Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////								intent.setType("video/*");
////								intent.setDataAndType(uri, "video/*");
//								startActivity(intent);
								if(!"".equals(rtspStr)){
									Intent intent = new Intent(TestActivity.this, VideoPlayerActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.putExtra("rtsp", rtspStr);
									intent.putExtra("title", bean.getName());
									startActivity(intent);
								}else{
									Toast.makeText(TestActivity.this, "rtsp error", Toast.LENGTH_LONG).show();
								}
							} catch (Exception e) {
								//#debug debug
								e.printStackTrace();
							}
						}

					}
				});

			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final VideoBean bean = (VideoBean) arg0.getItemAtPosition(arg2);
				long timestamp = System.currentTimeMillis();
				String url = "http://183.65.36.55/RoadViewServer_CQ/roadview_getrtsp_3.flow?icpname=" + icpName + "&timestamp=" + timestamp +"&vid=" + bean.getvId() + "&authstring=" + md5(icpName + timestamp + bean.getvId() + key);
				System.out.println("url == "+url);
				Toast.makeText(TestActivity.this, url, Toast.LENGTH_LONG).show();
				return false;
			}
		});

		String urlStr = "http://183.65.36.55/RoadViewServer_CQ/roadview_getlist_3.flow?icpname=" + icpName + "&timestamp=" + System.currentTimeMillis() + "&authstring=" + md5(icpName + System.currentTimeMillis() + key);

		BVBHttpRequest request = BVBHttpRequest.requestWithURL(this, urlStr);
		request.startAsynRequestString(new IBVBHttpLoadTextCallBack() {

			@Override
			public void textLoaded(String text) {
				if (text != null) {
					// #debug debug
					System.out.println("==text==" + text);
					try {
						XmlParser xmlParser = new XmlParser();
						text = text.replace("\r", "");
						text = text.replace("\n", "");
						datas = xmlParser.readXml(text);
						if(datas.isEmpty()){
							Toast.makeText(TestActivity.this, "datas is empty", Toast.LENGTH_LONG).show();
						}else{
							// datas = ReadXmlByPullService.readXML(text);
							adapter.initData(datas);
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

	}

	private String md5(String str) {
		return MD5.encodeMD5String(str);
	}

}
*/