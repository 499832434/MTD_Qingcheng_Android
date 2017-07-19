package com.qcjkjg.trafficrules;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import com.qcjkjg.trafficrules.utils.PackageUtils;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class InitApp extends Application{
    public static String VERSION = "4.1";
    public static String DB_NAME = "sci99_mobile_news2.db";
    private String appPkgName = null;
    public static String PUBLIC_KEY = "56CD79BAB508342D8B2BAEEABD70021B";
    public static InitApp initApp;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        initApp=this;
    }
}
