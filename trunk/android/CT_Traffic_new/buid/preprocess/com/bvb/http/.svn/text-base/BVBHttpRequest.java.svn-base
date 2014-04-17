package com.bvb.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

public class BVBHttpRequest {
	private static final String TAG = "BVBHttpRequest";
	public static final String CACHE_ROOT = "com_bvb_httprequest_cache";
	public static final int BUFFER_SIZE = 4 * 1024;
	// 使用线程池，来重复利用线程，优化内存
	private static final int DEFAULT_THREAD_POOL_SIZE = 10;
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
	private static Handler mHandler = new Handler();
	private Context mContext;
	private IBVBHttpRequestListener callBack;
	private IBVBHttpLoadImageCallBack imageLoadCallBack;
	private IBVBHttpLoadTextCallBack textLoadCallBack;
	private BVBURL bvbURL;
	private ArrayList<Header> headers;
	private boolean cacheEnable = true;
	private boolean writeTocache = true;

	public BVBHttpRequest() {
		super();
	}

	/**
	 * @Title: requestWithURL
	 * @Description:带参数的url，支持组装参数
	 * @param @param context
	 * @param @param baseUrl
	 * @param @param params
	 * @param @return 传入参数名字
	 * @return BVBHttpRequest 返回类型
	 * @throw
	 */
	public static BVBHttpRequest requestWithURL(Context context, String baseUrl, NameValuePair... params) {
		String url = baseUrl + concatParams(params);
		BVBHttpRequest request = new BVBHttpRequest(context, url);
		return request;
	}

	public static BVBHttpRequest requestWithURL(Context context, String baseUrl, List<NameValuePair> params) {
		String url = baseUrl + concatParams(params);
		BVBHttpRequest request = new BVBHttpRequest(context, url);
		return request;
	}

	public static BVBHttpRequest requestWithURL(Context context, String url) {
		BVBHttpRequest request = new BVBHttpRequest(context, url);
		return request;
	}

	public static BVBHttpRequest requestWithURL(Context context, String url, Header... headers) {
		BVBHttpRequest request = new BVBHttpRequest(context, url);
		ArrayList<Header> headList = new ArrayList<Header>();
		for (Header header : headers) {
			headList.add(header);
		}
		request.setHeaders(headList);
		return request;
	}

	private static String concatParams(NameValuePair[] params) {
		if (params == null || params.length <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < params.length; i++) {
				NameValuePair param = params[i];
				if (i == 0) {
					sb.append("?");
					sb.append(URLEncoder.encode(param.getName(), "UTF-8") + "=" + URLEncoder.encode(param.getValue(), "UTF-8"));
				} else {
					sb.append("&");
					sb.append(URLEncoder.encode(param.getName(), "UTF-8") + "=" + URLEncoder.encode(param.getValue(), "UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static String concatParams(List<NameValuePair> params) {
		if (params == null || params.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < params.size(); i++) {
				NameValuePair param = params.get(i);
				if (i == 0) {
					sb.append("?");
					sb.append(URLEncoder.encode(param.getName(), "UTF-8") + "=" + URLEncoder.encode(param.getValue(), "UTF-8"));
				} else {
					sb.append("&");
					sb.append(URLEncoder.encode(param.getName(), "UTF-8") + "=" + URLEncoder.encode(param.getValue(), "UTF-8"));
				}
			}
		} catch (Exception e) {
			//#debug debug
//@			e.printStackTrace();
		}
		return sb.toString();
	}

	public BVBHttpRequest(Context context, String url) {
		this.mContext = context;
		initBVBURL(url);
	}

	public BVBHttpRequest(Context context) {
		this.mContext = context;
	}

	public void setURL(String url) {
		initBVBURL(url);
	}

	private void initBVBURL(String url) {
		bvbURL = BVBDBHelper.getInstance(mContext).getURL(url);
		if (bvbURL == null) {
			bvbURL = new BVBURL();
			bvbURL.setUrl(url);
		}
	}

	public void setPostValueForKey(String key, String value) {
		if (bvbURL != null) {
			bvbURL.getPostData().put(key, value);
		}
	}

	/**
	 * @Title: startAsynchronous
	 * @Description:异步请求
	 * @param 传入参数名字
	 * @return void 返回类型
	 * @throw
	 */
	public void startAsynchronous() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				HttpResponse response = requestHttp(true, true);
				if (callBack == null) {
					return;
				}
				if (response == null) {
					callBack.loadFailed(response, null);
					return;
				}
				try {
					int statusCode = response.getStatusLine().getStatusCode();
					switch (statusCode) {
					case 200: {
						InputStream is = getISFromRespone(response);
						callBack.loadFinished(is, false);
						break;
					}
					case 304: {
						InputStream is = getISFromCache();
						if (is != null) {
							callBack.loadFinished(is, true);
						} else {
							HttpResponse strickResponse = requestHttp(false, false);
							is = getISFromRespone(strickResponse);
							callBack.loadFinished(is, false);
						}
						break;
					}
					default: {
						if (!cacheEnable) {
							return;
						}
						try {
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(bvbURL.getLocalData()));
							callBack.loadFailed(response, bis);
						} catch (Exception e) {
							callBack.loadFailed(response, null);
						}
						break;
					}
					}
				} catch (Exception e) {
					Log.i(TAG, e.getMessage());
					callBack.loadFailed(response, null);
				}
			}
		});
	}

	public void startAsynRequestString(IBVBHttpLoadTextCallBack callBack) {
		setTextLoadCallBack(callBack);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final String content = startSyncRequestString();
				if (textLoadCallBack != null && content != null) {
					if (mHandler != null) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								textLoadCallBack.textLoaded(content);
							}
						});
					} else {
						textLoadCallBack.textLoaded(content);
					}
				} else {
					textLoadCallBack.textLoaded(null);
				}
			}
		});
	};

	public void startAsynRequestBitmap(IBVBHttpLoadImageCallBack callBack) {
		setImageLoadCallBack(callBack);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap = startSyncRequestBitmap();
				if (imageLoadCallBack != null && bitmap != null) {
					if (mHandler != null) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								imageLoadCallBack.imageLoaded(bitmap);
							}
						});
					} else {
						imageLoadCallBack.imageLoaded(bitmap);
					}
				} else {
					imageLoadCallBack.imageLoaded(null);
				}
			}
		});
	};

	/**
	 * @Title: startSynchronous
	 * @Description:同步请求，返回的是有缓冲的InputStream
	 * @param @return 传入参数名字
	 * @return InputStream 返回类型
	 */
	public InputStream startSynchronous() {
		HttpResponse response = requestHttp(true, true);
		if (response == null) {
			return null;
		}
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case 200: {
				InputStream is = getISFromRespone(response);
				return is;
			}
			case 304: {
				InputStream is = getISFromCache();
				if (is != null) {
					return is;
				} else {
					response = requestHttp(false, false);
					is = getISFromRespone(response);
					return is;
				}
			}
			default:
				if (!cacheEnable) {
					return null;
				}
				try {
					InputStream is = getISFromCache();
					return is;
				} catch (Exception e) {
					return null;
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		return null;
	}

	/**
	 * @Title: startDownLoadFile
	 * @Description:必须在主线程调用
	 * @param @param context
	 * @param @param params
	 * @param @param listener 传入参数名字
	 * @return void 返回类型
	 * @throw
	 */
	public static void startDownLoadFile(Context context, BVBDownloadParams params, IBVBDownLoadTaskListener listener) {
		if (listener == null) {
			listener = new SimpleDownLoadTaskListener(context);
		}
		BVBDownLoadTask task = new BVBDownLoadTask(context, listener);
		task.execute(params);
	}

	/**
	 * @Title: startGetStringSynchronous
	 * @Description:同步请求String
	 * @param @return 传入参数名字
	 * @return String 返回类型
	 * @throw
	 */
	public String startSyncRequestString() {
		InputStream is = startSynchronous();
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = 0;
		try {
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			is.close();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return null;
		}
		return baos.toString();
	}

	/**
	 * @Title: startGetBitmapSynchronous
	 * @Description:同步请求图片
	 * @param @return 传入参数名字
	 * @return Bitmap 返回类型
	 * @throw
	 */
	public Bitmap startSyncRequestBitmap() {
		Bitmap cache = getBitmapFromCache();
		if (cache != null) {
			return cache;
		}
		InputStream is = startSynchronous();
		if (is == null) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		return bitmap;
	}

	public BVBURL getBVBurl() {
		return bvbURL;
	}

	private HttpResponse requestHttp(boolean haveLastModified, boolean haveEtag) {
		if (bvbURL == null || bvbURL.getUrl() == null || "".equals(bvbURL.getUrl()) || "null".equals(bvbURL.getUrl())) {
			return null;
		}
		HttpResponse response = null;
		try {
			if (bvbURL.getPostData().size() > 0) {
				HttpPost request = new HttpPost(bvbURL.getUrl());
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				HashMap<String, String> params = bvbURL.getPostData();
				Set<String> keyset = params.keySet();
				for (String key : keyset) {
					String value = params.get(key);
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
				if (bvbURL.getLastModified() != null || !"".equals(bvbURL.getLastModified()) && haveLastModified) {
					request.addHeader("If-Modified-Since", bvbURL.getLastModified());
				}
				if (bvbURL.getETag() != null && !"".equals(bvbURL.getETag()) && haveEtag) {
					request.addHeader("If-None-Match", bvbURL.getETag());
				}
				if (headers != null) {
					for (Header header : headers) {
						request.addHeader(header);
					}
				}

				request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
				response = BVBHttpClient.execute(mContext, request);
			} else {
				HttpGet request = new HttpGet(bvbURL.getUrl());
				if (bvbURL.getLastModified() != null && !"".equals(bvbURL.getLastModified())) {
					request.addHeader("If-Modified-Since", bvbURL.getLastModified());
				}
				if (bvbURL.getETag() != null && !"".equals(bvbURL.getETag())) {
					request.addHeader("If-None-Match", bvbURL.getETag());
				}
				response = BVBHttpClient.execute(mContext, request);
			}
		} catch (Exception e) {
			// #debug debug
//@			Log.i(TAG, "error");
		}
		return response;
	}

	private InputStream getISFromRespone(HttpResponse response) {
		try {
			if (writeTocache) {
				String filepath = writeInputSteamToCache(response.getEntity().getContent());
				if (filepath != null && !"".equals(filepath)) {
					String lastModified = getHeader(response, "Last-Modified");
					String eTag = getHeader(response, "ETag");
					bvbURL.setLastModified(lastModified);
					bvbURL.setETag(eTag);
					bvbURL.setLocalData(filepath);
					if (BVBDBHelper.getInstance(mContext).existURL(bvbURL.getUrl())) {
						BVBDBHelper.getInstance(mContext).updateURL(bvbURL);
					} else {
						BVBDBHelper.getInstance(mContext).insertURL(bvbURL);
					}
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(bvbURL.getLocalData()));
					return bis;
				}
			} else {
				return new BufferedInputStream(response.getEntity().getContent());
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return null;
		}
		return null;
	}

	private Bitmap getBitmapFromCache() {
		if (bvbURL == null || bvbURL.getLocalData() != null || "".equals(bvbURL.getLocalData())) {
			return null;
		}
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(bvbURL.getLocalData());
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	private InputStream getISFromCache() {
		if (bvbURL == null || bvbURL.getLocalData() == null || "".equals(bvbURL.getLocalData())) {
			return null;
		}
		File cache = new File(bvbURL.getLocalData());
		try {
			return new BufferedInputStream(new FileInputStream(cache));
		} catch (Exception e) {
			return null;
		}
	}

	private String writeInputSteamToCache(InputStream is) {
		try {
			File cachedir = mContext.getDir(CACHE_ROOT, 0);
			BufferedInputStream bis = new BufferedInputStream(is);
			final String fileName = MD5.encodeMD5String(bvbURL.getUrl());
			File file = new File(cachedir, fileName);
			if (file.exists()) {
				file.delete();
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
			bis.close();
			return file.getAbsolutePath();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return null;
		}
	}

	private String getHeader(HttpResponse responese, String headerName) {
		if (headerName == null || "".equals(headerName) || responese == null) {
			return null;
		}
		Header[] headers = responese.getHeaders(headerName);
		if (headers != null && headers.length > 0) {
			return headers[0].getValue();
		}
		return null;
	}

	public void setBVBurl(BVBURL bvbURL) {
		this.bvbURL = bvbURL;
	}

	public IBVBHttpRequestListener getListener() {
		return callBack;
	}

	public void setListener(IBVBHttpRequestListener callBack) {
		this.callBack = callBack;
	}

	public IBVBHttpLoadImageCallBack getImageLoadCallBack() {
		return imageLoadCallBack;
	}

	public void setImageLoadCallBack(IBVBHttpLoadImageCallBack imageLoadCallBack) {
		this.imageLoadCallBack = imageLoadCallBack;
	}

	public IBVBHttpLoadTextCallBack getTextLoadCallBack() {
		return textLoadCallBack;
	}

	public void setTextLoadCallBack(IBVBHttpLoadTextCallBack textLoadCallBack) {
		this.textLoadCallBack = textLoadCallBack;
	}

	public ArrayList<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<Header> headers) {
		this.headers = headers;
	}

	public boolean isCacheEnable() {
		return cacheEnable;
	}

	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}

	public boolean isWriteTocache() {
		return writeTocache;
	}

	public void setWriteTocache(boolean writeTocache) {
		this.writeTocache = writeTocache;
	}

}
