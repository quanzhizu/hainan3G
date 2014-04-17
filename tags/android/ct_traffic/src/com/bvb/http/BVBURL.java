package com.bvb.http;

import java.util.HashMap;

public class BVBURL {
	public static class URLFiled {
		public static final String URL = "url";
		public static final String LASTMODIFIED = "lastModified";
		public static final String ETAG = "eTag";
		public static final String LOCALDATA = "localData";
	}

	private String url;
	private String lastModified;
	private String eTag;
	private String localData;
	private HashMap<String, String> postData;

	public BVBURL() {
	}

	public BVBURL(String url, String lastModified, String etag, String localData) {
		this.url = url;
		this.lastModified = lastModified;
		this.eTag = etag;
		this.localData = localData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	public String getLocalData() {
		return localData;
	}

	public void setLocalData(String data) {
		this.localData = data;
	}

	public HashMap<String, String> getPostData() {
		if (postData == null) {
			postData = new HashMap<String, String>();
		}
		return postData;
	}

	public void setPostData(HashMap<String, String> postData) {
		this.postData = postData;
	}

	@Override
	public String toString() {
		if (url != null) {
			return url;
		}
		return super.toString();
	}
}
