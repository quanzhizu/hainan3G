package com.iconverge.ct.traffic.util;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Environment;

import com.iconverge.ct.traffic.data.Const;

public class FileUtils {
	public static String getSdcardPath() {
		String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		// #debug debug
//@		System.out.println("sdcardPath:" + sdcardPath);
		return sdcardPath;
	}

	public static boolean isSDCardAvailableNow() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static String getAppSdcardPath() {
		File file = null;
		if (isSDCardAvailableNow()) {
			file = new File(Environment.getExternalStorageDirectory(), Const.SDCARD_BASEPATH);
			if (!file.exists()) {
				if (!file.mkdir()) {
					// #debug debug
//@					System.out.println("file create failed");
				}
			}
		}
		return file == null ? "" : file.getAbsolutePath();
	}

	public final static String getFileSize(long size, boolean decimal) {
		DecimalFormat df;
		if (decimal)
			df = new DecimalFormat("###.##");
		else
			df = new DecimalFormat("###");
		float f;
		if (size <= 0) {
			return "0.0K";
		} else if (size < 1024 * 1024) {
			f = (float) ((float) size / (float) 1024);
			return (df.format(Float.valueOf(f)) + "K");
		} else {
			f = (float) ((float) size / (float) (1024 * 1024));
			return (df.format(Float.valueOf(f)) + "M");
		}
	}

	public static File createFile(String userName) {
		String path = getAppSdcardPath() + "/" + userName + "/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

}
