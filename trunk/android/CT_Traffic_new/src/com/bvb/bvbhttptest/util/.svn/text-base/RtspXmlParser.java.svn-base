package com.bvb.bvbhttptest.util;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;

public class RtspXmlParser {

	String retStr;

	public String readXml(String text) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(getRootElement().getContentHandler());
			reader.parse(new InputSource(new StringReader(text)));
			return retStr;
		} catch (Exception e) {
		}
		return "";
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
				} else {
					retStr = "";
				}
			}
		});

		Element parambufElement = rootElement.getChild("parambuf");

		Element infoElement = parambufElement.getChild("info");

		Element rtspElement = infoElement.getChild("rtsp");
		// 读到文本的末尾时触发,这里的body即为文本的内容部分
		rtspElement.setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				retStr = body;
			}
		});

		return rootElement;

	}
}
