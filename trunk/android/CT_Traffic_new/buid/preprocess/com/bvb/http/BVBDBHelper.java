package com.bvb.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BVBDBHelper extends SQLiteOpenHelper {
	private static BVBDBHelper instance;

	public static final String TAG = "BVBDBHelper";

	// 版本号，字段名
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "com_bvb_httprequest_db";

	// 表名
	public static final String TB_NAME = "httprecord_table";

	// 字段名
	public static final String HTTP_URL = BVBURL.URLFiled.URL;
	public static final String HTTP_LASTMODIFIED = BVBURL.URLFiled.LASTMODIFIED;
	public static final String HTTP_ETAG = BVBURL.URLFiled.ETAG;
	public static final String HTTP_LOCALDATA = BVBURL.URLFiled.LOCALDATA;

	public static final String[] QUERY_COLUMNS = { HTTP_URL, HTTP_LASTMODIFIED,
			HTTP_ETAG, HTTP_LOCALDATA };

	public static final String SQL_CREATETABLE = "CREATE TABLE IF NOT EXISTS "
			+ TB_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + HTTP_URL
			+ " TEXT, " + HTTP_LASTMODIFIED + " TEXT, " + HTTP_ETAG + " TEXT, "
			+ HTTP_LOCALDATA + " TEXT);";

	private Context context;

	public static BVBDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new BVBDBHelper(context);
		}
		return instance;
	}

	public BVBDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.setContext(context);
		this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(SQL_CREATETABLE);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
	}

	// 本次版本更新时，调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "database update successfully. new version : " + newVersion);
		// TODO 更新时做点什么
	}

	public synchronized boolean notEmpty() {
		Cursor cursor = query();
		boolean res = cursor.moveToFirst();
		cursor.close();
		return res;
	}

	public synchronized int deleteURL(String url) {
		SQLiteDatabase db = this.getWritableDatabase();
		// 执行删除
		int res = db.delete(TB_NAME, HTTP_URL + "=? ", new String[] { url });
		Log.i(TAG, "affect +" + res + " row data， delete url =" + url
				+ " successfully!");
		return res;
	}

	// 清空表
	public synchronized int clear() {
		// 获取SQLiteDatabase实例
		SQLiteDatabase db = this.getWritableDatabase();

		// 执行删除
		int res = db.delete(TB_NAME, null, null);
		Log.i(TAG, "affect +" + res + " row data， delete table =" + TB_NAME
				+ " successfully!");
		return res;
	}

	@Override
	public synchronized void close() {
		try {
			getWritableDatabase().close();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
	}

	public synchronized boolean existURL(String url) {
		SQLiteDatabase db = this.getWritableDatabase();
		String[] columns = new String[] { HTTP_URL };
		String selection = HTTP_URL + "=?";
		String[] selectionArgs = { url };
		Cursor c = db.query(TB_NAME, columns, selection, selectionArgs, null,
				null, null);
		boolean res = c.moveToFirst();
		c.close();
		return res;
	}

	public long insert(ContentValues values) {
		// 获取SQLiteDatabase实例
		SQLiteDatabase db = this.getWritableDatabase();

		long err = db.insert(TB_NAME, null, values);
		db.close();
		return err;
	}

	public synchronized boolean insertURL(BVBURL bvbRUL) {
		try {
			ContentValues values = new ContentValues();
			values.put(HTTP_URL, bvbRUL.getUrl());
			values.put(HTTP_LASTMODIFIED, bvbRUL.getLastModified());
			values.put(HTTP_ETAG, bvbRUL.getETag());
			values.put(HTTP_LOCALDATA, bvbRUL.getLocalData());

			long err = insert(values);
			if (err == -1) {
				Log.i(TAG, "Error from insertURL:" + err);
				return false;
			} else {
				Log.i(TAG, "insertURL successful! ");
				return true;
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return false;
		}
	}

	public void updateURL(BVBURL bvbURL) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		if (bvbURL.getLastModified() != null
				&& !"".equals(bvbURL.getLastModified())) {
			contentValues.put(HTTP_LASTMODIFIED, bvbURL.getLastModified());
		}
		if (bvbURL.getETag() != null && !"".equals(bvbURL.getETag())) {
			contentValues.put(HTTP_ETAG, bvbURL.getETag());
		}
		contentValues.put(HTTP_LOCALDATA, bvbURL.getLocalData());
		try {
			int num = db.update(TB_NAME, contentValues, HTTP_URL + "=?",
					new String[] { bvbURL.getUrl() });
			if (num == 0) {
				insert(contentValues);
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
	}

	public synchronized BVBURL getURL(String url) {
		try {
			BVBURL galurl = null;
			Cursor cursor = null;
			SQLiteDatabase db = getWritableDatabase();
			String[] columns = { HTTP_URL, HTTP_LASTMODIFIED, HTTP_ETAG,
					HTTP_LOCALDATA };
			cursor = db.query(TB_NAME, columns, HTTP_URL + "=?",
					new String[] { url }, null, null, null);
			if (cursor.moveToFirst()) {
				String lastModified = cursor.getString(cursor
						.getColumnIndex(HTTP_LASTMODIFIED));
				String etag = cursor
						.getString(cursor.getColumnIndex(HTTP_ETAG));
				String localdata = cursor.getString(cursor
						.getColumnIndex(HTTP_LOCALDATA));
				galurl = new BVBURL(url, lastModified, etag, localdata);
			}
			cursor.close();
			return galurl;

		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return null;
		}
	}

	public Cursor query() {
		// 获取SQLiteDatabase实例
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.query(TB_NAME, null, null, null, null, null, null);
		return c;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
