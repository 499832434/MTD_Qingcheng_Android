package com.qcjkjg.trafficrules.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;

/**
 * Created by zongshuo on 2017/7/20.
 */
public class ViewFactory {

    /**
     * 获取一个简单的视图
     *
     * @param text content
     * @return view view
     */
    public static ImageView getView(Context context) {
        ImageView v = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.cycleviewpager_imageview, null);
       return v;
    }
}
