package com.iconverge.ct.traffic.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {

	public final static Bitmap drawBitmap(Drawable drawable, int width, int height) {

		try {
			Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
			if (bmp == null)
				return null;
			int w = bmp.getWidth();
			int h = bmp.getHeight();
			float scaleWidth = ((float) (width)) / w;
			float scaleHeight = ((float) (height)) / h;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
			return bm;
		} catch (OutOfMemoryError err) {
			// #debug error
			err.printStackTrace();
			return null;
		} catch (Exception e) {
			// #debug error
			e.printStackTrace();
			return null;
		}

	}
}
