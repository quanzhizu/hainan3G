package com.bvb.bvbhttptest.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

import com.bvb.bvbhttptest.bean.VideoBean;

public class XmlParser {

	List<VideoBean> videos = new ArrayList<VideoBean>();
	VideoBean videoBean;

	public List<VideoBean> readXml(String text) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(getRootElement().getContentHandler());
			reader.parse(new InputSource(new StringReader(text)));
			return videos;
		} catch (Exception e) {
			// #debug debug
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return 返回设置好处理机制的rootElement
	 */
	private RootElement getRootElement() {
		/* rootElement代表着根节点，参数为根节点的tagName */
		RootElement rootElement = new RootElement("data");
		/*
		 * 获取一类子节点，并为其设置相应的事件 这里需要注意，虽然我们只设置了一次beauty的事件，但是我们文档中根节点下的所有
		 * beauty却都可以触发这个事件。
		 */

		Element checkElement = rootElement.getChild("state");
		checkElement.setEndTextElementListener(new EndTextElementListener() {

			@Override
			public void end(String body) {
				if ("Y".equals(body)) {
					videos = new ArrayList<VideoBean>();
				} else {
					videos = null;
				}
			}
		});

		Element parambufElement = rootElement.getChild("parambuf");

		Element infoElement = parambufElement.getChild("info");

		// 读到元素开始位置时触发，如读到<beauty>时
		infoElement.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				// Log.i("通知", "start");
				videoBean = new VideoBean();
			}
		});
		// 读到元素结束位置时触发，如读到</beauty>时
		infoElement.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				videos.add(videoBean);
			}
		});

		Element vidElement = infoElement.getChild("vid");
		// 读到文本的末尾时触发,这里的body即为文本的内容部分
		vidElement.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				videoBean.setvId(body);
			}
		});

		Element nameElement = infoElement.getChild("name");
		nameElement.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				videoBean.setName(body);
			}
		});

		Element stateElement = infoElement.getChild("state");
		stateElement.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				videoBean.setState(body);
			}
		});
		return rootElement;

	}
}
