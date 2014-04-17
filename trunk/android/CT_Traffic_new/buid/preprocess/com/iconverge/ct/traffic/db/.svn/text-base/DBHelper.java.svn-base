package com.iconverge.ct.traffic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public final static String DB_NAME = "ct_traffic_db";
	public final static int DB_VERSION = 1;
	
	public final static String TABLE_NAME_CATEGORY = "poi_catagory";
	public final static String FIELD_CATEGORY_SID = "sid";
	public final static String FIELD_CATEGORY_ID = "cid";
	public final static String FIELD_CATEGORY_PARENTID = "parent_id";
	public final static String FIELD_CATEGORY_NAME = "name";
	

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public DBHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
	}

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// #debug debug
//@		System.out.println("@@==DBHelper====onCreate=");
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CATEGORY + "(" 
				+ FIELD_CATEGORY_SID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ FIELD_CATEGORY_ID + " INTEGER UNIQUE,"
				+ FIELD_CATEGORY_PARENTID + " INTEGER,"
				+ FIELD_CATEGORY_NAME + " TEXT)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
