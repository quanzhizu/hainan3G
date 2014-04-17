package com.iconverge.ct.traffic.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iconverge.ct.traffic.bean.CategoryBean;

public class CategoryDao extends DBHelper {

	public CategoryDao(Context context) {
		super(context);
	}

	public CategoryBean getCategoryById(long id) {
		CategoryBean bean = new CategoryBean();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME_CATEGORY, null, FIELD_CATEGORY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(FIELD_CATEGORY_NAME));
			long pId = cursor.getLong(cursor.getColumnIndex(FIELD_CATEGORY_PARENTID));
			bean.setId(id);
			bean.setName(name);
			bean.setpId(pId);
		}
		cursor.close();
		db.close();

		return bean;
	}
	
	public ArrayList<CategoryBean> getCategoryByPId(long pId) {
		ArrayList<CategoryBean> lists = new ArrayList<CategoryBean>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME_CATEGORY, null, FIELD_CATEGORY_PARENTID + "=?", new String[] { String.valueOf(pId) }, null, null, FIELD_CATEGORY_ID);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(FIELD_CATEGORY_NAME));
			long id = cursor.getLong(cursor.getColumnIndex(FIELD_CATEGORY_ID));

			CategoryBean bean = new CategoryBean();
			bean.setId(id);
			bean.setName(name);
			bean.setpId(pId);
			lists.add(bean);
		}
		cursor.close();
		db.close();

		return lists;
	}
	
	/**
	 * 执行sql文件
	 * 
	 * @return
	 */
	public boolean execSqlFile(InputStream is) {
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			InputStreamReader inputReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(inputReader);
			String sql = null;
			db.beginTransaction();
			while ((sql = br.readLine()) != null) {
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
			br.close();
			inputReader.close();
			db.endTransaction();

		} catch (Exception e) {
			// #debug debug
			e.printStackTrace();
			return false;
		}
		db.close();
		return true;
	}

}
