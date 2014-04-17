package com.bvb.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BVBDownLoadTask extends
		AsyncTask<BVBDownloadParams, Integer, Void> {

	private static final String TAG = "BVBDownLoadTask";
	public static final int bufferSize = 512 * 1024;
	private static HashMap<String, BVBDownLoadTask> runningMap = new HashMap<String, BVBDownLoadTask>();

	Context mContext;
	BVBDownloadParams downloadParams = null;
	IBVBDownLoadTaskListener listener;

	String filePath;

	String titleName;
	int notifyId;
	boolean isFailed;
	boolean isCancel = false;
	File saveFile;
	int filesize;

	int errCode = 0;
	int speed;
	NotificationManager mNotificationManager;

	public BVBDownLoadTask(Context context, IBVBDownLoadTaskListener listener) {
		mContext = context;
		this.listener = listener;
	}

	@Override
	protected Void doInBackground(BVBDownloadParams... params) {
		downloadParams = params[0];

		URL downloadURL = downloadParams.getDownLoadUrl();
		mContext = downloadParams.mContext;
		notifyId = downloadParams.notifyId;

		filePath = downloadParams.getFileName();
		titleName = downloadParams.getTitleName();

		File file = new File(filePath);

		if (runningMap.containsKey(filePath)) {
			cancel(true);
			return null;
		}
		runningMap.put(filePath, this);
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		saveFile = file;

		if (saveFile.exists() && this.listener != null
				&& this.listener.onLoadFileExisting(mContext, downloadParams)) {
			return null;
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) downloadURL
					.openConnection();
			BVBURL bvbURL = BVBDBHelper.getInstance(mContext).getURL(
					downloadURL.toString());
			if (bvbURL != null) {
				if (bvbURL.getLastModified() != null
						&& !"".equals(bvbURL.getLastModified())) {
					conn.setRequestProperty("If-Modified-Since",
							bvbURL.getLastModified());
				}
				if (bvbURL.getETag() != null && !"".equals(bvbURL.getETag())) {
					conn.setRequestProperty("If-None-Match", bvbURL.getETag());
				}
			}
			int statucode = conn.getResponseCode();
			int totalSize;
			switch (statucode) {
			case HttpURLConnection.HTTP_OK:
				if (saveFile.exists()) {
					saveFile.delete();
					saveFile.createNewFile();
				}
				bis = new BufferedInputStream(conn.getInputStream(), bufferSize);
				bos = new BufferedOutputStream(new FileOutputStream(saveFile),
						bufferSize);
				totalSize = conn.getContentLength();
				filesize = totalSize;
				readFromInputStream(bis, bos);
				break;
			case HttpURLConnection.HTTP_NOT_MODIFIED:
				// 从缓存读取数据
				File cacheFile = new File(bvbURL.getLocalData());
				conn.disconnect();
				bis = new BufferedInputStream(new FileInputStream(cacheFile),
						bufferSize);
				bos = new BufferedOutputStream(new FileOutputStream(saveFile),
						bufferSize);
				totalSize = (int) cacheFile.length();
				filesize = totalSize;
				readFromInputStream(bis, bos);
				break;
			default:
				isFailed = true;
				errCode = statucode;
				break;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			isFailed = true;
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}

			} catch (IOException e) {
			}
		}

		return null;
	}

	private void readFromInputStream(BufferedInputStream bis,
			BufferedOutputStream bos) throws IOException {
		byte[] buf = new byte[bufferSize];
		int progress = 0;
		int finishedSize = 0;
		int readLen = -1;
		long time = System.currentTimeMillis();
		int lencount = 0;
		while ((readLen = bis.read(buf)) != -1 && !isCancel) {
			bos.write(buf, 0, readLen);
			finishedSize += readLen;
			lencount += readLen;
			// 计算新进度
			int newProgress = (int) (((double) finishedSize / filesize) * 100);
			long curTime = System.currentTimeMillis();
			if (newProgress - progress > 0) {
				if (curTime - time > 1000) {
					speed = (int) (((lencount * 1000) >> 10) / (curTime - time));
					lencount = 0;
					time = curTime;
				}
				publishProgress(newProgress);
			}
			progress = newProgress;
		}
		if (isCancel && finishedSize != filesize) {
		} else {
			publishProgress(100);// 下载完成
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (runningMap != null) {
			runningMap.remove(filePath);
		}
		if (isFailed) {
			if (this.listener != null) {
				this.listener.onLoadFailed(mContext, downloadParams, errCode);
			}
			return;
		}
		if (isCancel) {
			if (this.listener != null) {
				this.listener.onLoadCancel(mContext, downloadParams);
			}
			return;
		}
		if (this.listener != null) {
			this.listener.onLoadFinish(mContext, downloadParams);
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		int progress = values[0];
		// float size = ((int) ((filesize >> 10) * 10.0f / 1024)) / 10.0f;
		// CharSequence contentTitle = titleName + " [" + size + "M]";
		// CharSequence contentText = "正在下载，已完成  " + progress + "%," + speed
		// + "k/s";
		if (this.listener != null) {
			this.listener.onLoadProgress(mContext, downloadParams, progress,
					filesize, speed);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		if (runningMap != null) {
			runningMap.remove(filePath);
		}
		if (this.listener != null) {
			this.listener.onLoadCancel(mContext, downloadParams);
		}
		super.onCancelled();
	}

	// 是否正在下载，fileName不包含路径名
	public static boolean isDownLoadingFile(String fileName) {
		return runningMap.containsKey(fileName);
	}

	public static void cancelDownload(String filename) {
		try {
			if (isDownLoadingFile(filename)) {
				runningMap.get(filename).isCancel = true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
	}

}
