package com.qcjkjg.trafficrules;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.mob.MobSDK;
import com.qcjkjg.trafficrules.service.LocationService;
import com.qcjkjg.trafficrules.utils.Md5;
import com.qcjkjg.trafficrules.utils.PackageUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class InitApp extends Application{


    public static final String USER_PRIVATE_DATA = "USER_PRIVATE_DATA";
    public static final String USER_NAME_KEY = "USER_NAME_KEY";
    public static final String USER_PHONE_KEY = "USER_PHONE_KEY";
    public static final String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    public static final String USER_IS_VIP_KEY = "USER_IS_VIP_KEY";
    public static final String USER_PLATFORM_KEY = "USER_PLATFORM_KEY";//0:QQ 1:WEIXIN
    public static final String USER_CLIENT_ID_KEY = "USER_CLIENT_ID_KEY";
    public static final String USER_CAR_TYPE_KEY = "USER_CAR_TYPE_KEY";//驾驶证类型
    public static final String USER_CLIENT_ID_KEY_FLAG = "USER_CLIENT_ID_KEY_FLAG";
    public static final String USER_COLLECT_SUB_KEY = "USER_COLLECT_SUB_KEY";//收藏题目




    public static String VERSION = "4.1";
    public static String DEVICE_TYPE = "0";
    public static String DB_NAME = "qcAnswer.db";
    private String appPkgName = null;
    public static String PUBLIC_KEY = "56CD79BAB508342D8B2BAEEABD70021B";
    public static InitApp initApp;
    private Mac mac;
    private SecretKeySpec signingKey;
    public static final String HMAC_SHA_1 = "HmacSHA1";
    private static UUID uuid;
    public static String DEVICE_TOKEN = "";
    //推送相关
    public static final String PREF_DEVICE_TOKEN_KEY = "PREF_DEVICE_TOKEN_KEY";
    public static final String PREFS_FILE = "device_id.xml";
    public static final String PREFS_DEVICE_ID = "device_id";
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;
    public static LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        initApp=this;
        VERSION = PackageUtils.getAppVersionName(this);
        initUmeng();
        initSignatureTools();
        initUserPref();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
    }

    private void initUmeng(){
        Config.DEBUG=false;
        UMShareAPI.get(this);
//        PlatformConfig.setWeixin("", "");
//        PlatformConfig.setQQZone("1106277938","FVCOAjS49n9P53UN");
//        PlatformConfig.setSinaWeibo("3566099207", "6960dfc3805f1967b773ed812bc43b14", "");
        //微信
        PlatformConfig.setWeixin("wxd2a8a6d807625124", "8798b3a08be04f6cf493dd907e1ffa01");
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com/sina2/callback");
        //QQ
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    public void initUserPref() {
        initDeviceId(getApplicationContext());
        DEVICE_TOKEN = new Md5(uuid == null ? "null" : uuid.toString()).compute();
    }

    public void initDeviceId(Context context) {
        if (uuid == null) {
            if (uuid == null) {
                final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    uuid = UUID.fromString(id);
                } else {
                    try {
                        initUUID(context, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        try {
                            initUUID(context);
                        } catch (Exception ex) {
                        }
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                }
            }
        }
    }

    private void initUUID(Context context) throws UnsupportedEncodingException {
        initUUID(context, null);
    }

    private void initUUID(Context context, String encoding) throws UnsupportedEncodingException {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (encoding != null) {//encoding == null判断反
            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes(encoding)) : null;
        } else {
            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes()) : null;
        }
        if (uuid == null) {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId != null) {
                if (encoding != null) {//encoding == null判断反
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes(encoding));
                } else {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes());
                }

            } else {
                uuid = UUID.fromString("null");
            }
        }
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty

        RetryPolicy retryPolicy = new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(retryPolicy);

        getRequestQueue().add(req);
    }


    private String sort(List<String> reqParams) {
        Collections.sort(reqParams);
        StringBuilder sb = new StringBuilder();
        for (String item : reqParams) {
            sb.append(item);
        }
        return sb.toString();
    }


    public synchronized String getSig(Map<String, String> params) {
        List<String> paramsList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsList.add(entry.getKey() + entry.getValue());
        }
        byte[] rawHmac = null;
        try {
            rawHmac = mac.doFinal(sort(paramsList).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            rawHmac = mac.doFinal(sort(paramsList).getBytes());
        }
        String result = Base64.encodeToString(rawHmac, Base64.NO_WRAP);
        return result;
    }

    private void initSignatureTools() {
        try {
            signingKey = new SecretKeySpec(PUBLIC_KEY.getBytes("UTF-8"), HMAC_SHA_1);
        } catch (UnsupportedEncodingException e) {
            signingKey = new SecretKeySpec(PUBLIC_KEY.getBytes(), HMAC_SHA_1);
        }
        try {
            mac = Mac.getInstance(HMAC_SHA_1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(signingKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
