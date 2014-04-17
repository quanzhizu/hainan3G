package com.bvb.http;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

public class BVBDownloadParams {
	
	/**** 下载路径 **/
	private URL downLoadUrl;

	/**** 在状态栏的标题 **/
	private String titleName;

	/** 保存文件名 */
	String fileName;

	Context mContext;

	int notifyId = 0;

	public URL getDownLoadUrl() {
		return downLoadUrl;
	}

	public BVBDownloadParams(URL downLoadUrl, String titleName,
			String fileName, Context mContext, int notifyId) {
		super();
		this.downLoadUrl = downLoadUrl;
		this.titleName = titleName;
		this.mContext = mContext;
		this.fileName = fileName;
		this.notifyId = notifyId;
	}

	public BVBDownloadParams(String downLoadUrl, String titleName,
			String fileName, Context mContext, int notifyId) {
		super();
		try {
			this.downLoadUrl = new URL(downLoadUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.titleName = titleName;
		this.mContext = mContext;
		this.fileName = fileName;
		this.notifyId = notifyId;
	}

	public void setDownLoadUrl(URL downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
