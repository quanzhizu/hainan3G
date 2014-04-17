package com.actionbarsherlock.internal.widget;

import static android.view.View.MeasureSpec.EXACTLY;

import com.iconverge.ct.traffic.ResUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;

public class FakeDialogPhoneWindow extends LinearLayout {
    final TypedValue mMinWidthMajor = new TypedValue();
    final TypedValue mMinWidthMinor = new TypedValue();

    public FakeDialogPhoneWindow(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, ResUtil.getInstance(context).getStyleable("SherlockTheme", false)/*CT_Traffic_R.styleable.SherlockTheme()*/);

        a.getValue(ResUtil.getInstance(context).getStyleable("SherlockTheme_windowMinWidthMajor")/*CT_Traffic_R.styleable.SherlockTheme_windowMinWidthMajor()*/, mMinWidthMajor);
        a.getValue(ResUtil.getInstance(context).getStyleable("SherlockTheme_windowMinWidthMinor")/*CT_Traffic_R.styleable.SherlockTheme_windowMinWidthMinor()*/, mMinWidthMinor);

        a.recycle();
    }

    /* Stolen from PhoneWindow */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final boolean isPortrait = metrics.widthPixels < metrics.heightPixels;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        boolean measure = false;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, EXACTLY);

        final TypedValue tv = isPortrait ? mMinWidthMinor : mMinWidthMajor;

        if (tv.type != TypedValue.TYPE_NULL) {
            final int min;
            if (tv.type == TypedValue.TYPE_DIMENSION) {
                min = (int)tv.getDimension(metrics);
            } else if (tv.type == TypedValue.TYPE_FRACTION) {
                min = (int)tv.getFraction(metrics.widthPixels, metrics.widthPixels);
            } else {
                min = 0;
            }

            if (width < min) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(min, EXACTLY);
                measure = true;
            }
        }

        // TODO: Support height?

        if (measure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
