package com.bvb.http;

import android.content.Context;

public interface IBVBDownLoadTaskListener {
	/** 如果下载不再重新下载则返回true,在子线程调用 */
	public boolean onLoadFileExisting(Context context, BVBDownloadParams params);

	public void onLoadProgress(Context context, BVBDownloadParams params,
			int progress, long allsize, int kbpersecond);

	public void onLoadFinish(Context context, BVBDownloadParams params);

	public void onLoadFailed(Context context, BVBDownloadParams params, int err);

	public void onLoadCancel(Context context, BVBDownloadParams params);
}
