package com.qcjkjg.trafficrules.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zongshuo on 2017/8/8.
 */
public class DateUtils {
    public static String getInterval(long inputTime) {
        String result= null;
        try {
            // 用现在距离1970年的时间间隔new
            // Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long time = new Date().getTime() - inputTime*1000;// 得出的时间间隔是毫秒
            if (time / 1000 < 60 ) {
                // 如果时间间隔小于60秒则显示多少秒前
                int se = (int) ((time % 60000) / 1000);
                result= se + "秒前";
            } else if (time / 60000 < 60 ) {
                // 如果时间间隔小于60分钟则显示多少分钟前
                int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
                result= m + "分钟前";
            } else if (time / 3600000 < 24 ) {
                // 如果时间间隔小于24小时则显示多少小时前
                int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
                result= h + "小时前";
            }else if (time / 86400000 < 2 ) {
                result= "1天前";
            }else if (time / 86400000 < 3 ) {
                result= "2天前";
            }else if (time / 86400000 < 4 ) {
                result= "3天前";
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                result= sdf.format(new Date(inputTime* 1000));
            }
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date(inputTime* 1000));
        }
        return result;
    }

}
