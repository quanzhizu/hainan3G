package com.iconverge.ct.traffic;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

public class ResUtil {
	private static final String TAG = ResUtil.class.getName();
	private static ResUtil instance;
	private Context context;
	private static Class<?> id = null;
	private static Class<?> drawable = null;
	private static Class<?> layout = null;
	private static Class<?> anim = null;
	private static Class<?> style = null;
	private static Class<?> string = null;
	private static Class<?> array = null;
	private static Class<?> menu = null;
	private static Class<?> color = null;
	private static Class<?> attr = null;
	private static Class<?> bool = null;
	private static Class<?> styleable = null;

	private ResUtil(Context context) {
		this.context = context.getApplicationContext();
		try {
			drawable = Class.forName(this.context.getPackageName() + ".R$drawable");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			layout = Class.forName(this.context.getPackageName() + ".R$layout");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			id = Class.forName(this.context.getPackageName() + ".R$id");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			anim = Class.forName(this.context.getPackageName() + ".R$anim");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			style = Class.forName(this.context.getPackageName() + ".R$style");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			string = Class.forName(this.context.getPackageName() + ".R$string");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			array = Class.forName(this.context.getPackageName() + ".R$array");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			menu = Class.forName(this.context.getPackageName() + ".R$menu");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			color = Class.forName(this.context.getPackageName() + ".R$color");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			attr = Class.forName(this.context.getPackageName() + ".R$attr");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			bool = Class.forName(this.context.getPackageName() + ".R$bool");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
		try {
			styleable = Class.forName(this.context.getPackageName() + ".R$styleable");
		} catch (ClassNotFoundException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	public static ResUtil getInstance(Context context) {
		if (instance == null)
			instance = new ResUtil(context);
		return instance;
	}

	public int getAnim(String paramString) {
		return getResOfR(anim, paramString);
	}

	public int getId(String paramString) {
		return getResOfR(id, paramString);
	}

	public int getDrawable(String paramString) {
		return getResOfR(drawable, paramString);
	}

	public int getLayout(String paramString) {
		return getResOfR(layout, paramString);
	}

	public int getStyle(String paramString) {
		return getResOfR(style, paramString);
	}

	public int getString(String paramString) {
		return getResOfR(string, paramString);
	}

	public int getArray(String paramString) {
		return getResOfR(array, paramString);
	}

	public int getMenu(String paramString) {
		return getResOfR(menu, paramString);
	}

	public int getColor(String paramString) {
		return getResOfR(color, paramString);
	}

	public int getAttr(String paramString) {
		return getResOfR(attr, paramString);
	}

	public int getBool(String paramString) {
		return getResOfR(bool, paramString);
	}

	public int getStyleable(String paramString) {
		return getResOfR(styleable, paramString);
	}

	private int getResOfR(Class<?> paramClass, String paramString) {
		if (paramClass == null) {
			Log.d(TAG, "getRes(null," + paramString + ")");
			throw new IllegalArgumentException("ResClass is not initialized.");
		}
		try {
			Field localField = paramClass.getField(paramString);
			int k = localField.getInt(paramString);
			return k;
		} catch (Exception localException) {
			Log.d(TAG, "getRes(" + paramClass.getName() + ", " + paramString + ")");
			Log.d(TAG, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
			Log.d(TAG, localException.getMessage());
		}
		return -1;
	}
}
