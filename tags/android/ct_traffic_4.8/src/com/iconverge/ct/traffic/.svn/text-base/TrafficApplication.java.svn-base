package com.iconverge.ct.traffic;

import java.util.Locale;

import org.videolan.vlc.BitmapCache;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class TrafficApplication extends Application {
	public final static String TAG = "TrafficApplication";
	private static TrafficApplication instance;

	public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";
	public Handler handler;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// #debug debug
		System.out.println(TAG + " onCreate");
		// Are we using advanced debugging - locale?
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String p = pref.getString("set_locale", "");
		if (p != null && !p.equals("")) {
			Locale locale;
			// workaround due to region code
			if (p.equals("zh-TW")) {
				locale = Locale.TRADITIONAL_CHINESE;
			} else if (p.startsWith("zh")) {
				locale = Locale.CHINA;
			} else {
				locale = new Locale(p);
			}
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}

		instance = this;
	}
	

	/**
	 * Called when the overall system is running low on memory
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.w(TAG, "System is running low on memory");

		BitmapCache.getInstance().clear();
	}

	/**
	 * @return the main context of the Application
	 */
	public static Context getAppContext() {
		return instance;
	}

	/**
	 * @return the main resources from the Application
	 */
	public static Resources getAppResources() {
		return instance.getResources();
	}
}
