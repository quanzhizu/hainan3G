package com.bvb.http;

import java.io.InputStream;

import org.apache.http.HttpResponse;

public interface IBVBHttpRequestListener {

	/**
	 * loadFinished
	 * @param is
	 * @param fromcache
	 */
	public void loadFinished(InputStream is, boolean fromcache);

	/**
	 * loadFailed
	 * @param respone
	 * @param cacheInputStream
	 */
	public void loadFailed(HttpResponse respone, InputStream cacheInputStream);
}
