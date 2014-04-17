package com.iconverge.ct.traffic.camera;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.bvb.bvbhttptest.bean.VideoBean;

public class ReadXmlByPullService {

	public static List<VideoBean> ReadXmlByPull(String xmlStr) throws Exception {
		List<VideoBean> videoList = null;
		ByteArrayInputStream stream = new ByteArrayInputStream(xmlStr.getBytes());
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(stream, "utf-8");
		int eventCode = xmlpull.getEventType();
		VideoBean videoBean = null;
		while (eventCode != XmlPullParser.END_DOCUMENT) {
			switch (eventCode) {
			case XmlPullParser.START_DOCUMENT: {
				videoList = new ArrayList<VideoBean>();
				break;
			}
			case XmlPullParser.START_TAG: {
				if ("info".equals(xmlpull.getName())) {
					videoBean = new VideoBean();
				} else if (videoBean != null) {
					if (("vid".equals(xmlpull.getName()))) {
						videoBean.setvId(xmlpull.nextText());
					} else if ("name".equals(xmlpull.getName())) {
						videoBean.setName(xmlpull.nextText());
					} else if ("state".equals(xmlpull.getName())) {
						videoBean.setState(xmlpull.nextText());
					}
				}
				break;
			}

			case XmlPullParser.END_TAG: {
				if ("info".equals(xmlpull.getName()) && videoBean != null) {
					videoList.add(videoBean);
					videoBean = null;
				}
				break;
			}

			}

			eventCode = xmlpull.next();

		}

		return videoList;
	}

	public static List<VideoBean> readXML(String text) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
			parser.setInput(stream, "UTF-8");
			int eventType = parser.getEventType();

			VideoBean currentVedio = null;
			List<VideoBean> videoBeans = null;

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
					videoBeans = new ArrayList<VideoBean>();
					break;
				case XmlPullParser.START_TAG:// 开始元素事件
					String name = parser.getName();
					if (name.equalsIgnoreCase("state")) {
						if ("Y".equals(parser.nextText())) {
							currentVedio = new VideoBean();
						}
					} else if (name.equalsIgnoreCase("errorcode")) {

					} else if (name.equalsIgnoreCase("parambuf")) {

						if (name.equalsIgnoreCase("name")) {

							currentVedio.setName(parser.nextText());// 如果后面是Text元素,即返回它的值

						} 

					}

					break;

				case XmlPullParser.END_TAG:// 结束元素事件

					if (parser.getName().equalsIgnoreCase("info") && currentVedio != null) {

						videoBeans.add(currentVedio);

						currentVedio = null;

					}

					break;

				}

				eventType = parser.next();

			}

			stream.close();

			return videoBeans;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}
}
