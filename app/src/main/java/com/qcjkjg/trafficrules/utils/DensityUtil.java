package com.qcjkjg.trafficrules.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

/**
 * Created by zongshuo on 2015/11/20.
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }
    /**
     * 获取控件的宽
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height =view.getMeasuredHeight();
        int width =view.getMeasuredWidth();
        return width;
    }
    /**
     * 设置控件的宽
     */
    public static void setWidth(View view,int width) {
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height=width;
        params.width=width;
        view.setLayoutParams(params);
    }

    /**
     * 返回屏幕【高，宽】数组
     *
     * @param activity
     * @return
     */
    public static int[] getResolution(Activity activity) {
        int width = 480, height = 200;
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Method mGetRawH = null, mGetRawW = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {// For JellyBean 4.2 (API 17) and onward
                display.getRealMetrics(metrics);
                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                mGetRawH = Display.class.getMethod("getHeight");//getRawHeight
                mGetRawW = Display.class.getMethod("getWidth");//getRawWidth
                width = (Integer) mGetRawW.invoke(display);
                height = (Integer) mGetRawH.invoke(display);
            }
        } catch (Exception e) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            e.printStackTrace();
        }
        return new int[]{height, width};
    }


}
