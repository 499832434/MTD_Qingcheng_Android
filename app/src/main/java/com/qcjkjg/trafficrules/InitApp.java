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
import com.qcjkjg.trafficrules.utils.Md5;
import com.qcjkjg.trafficrules.utils.PackageUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;

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
    public static String VERSION = "4.1";
    public static String DB_NAME = "sci99_mobile_news2.db";
    private String appPkgName = null;
    public static String PUBLIC_KEY = "56CD79BAB508342D8B2BAEEABD70021B";
    public static InitApp initApp;
    private Mac mac;
    private SecretKeySpec signingKey;
    public static final String HMAC_SHA_1 = "HmacSHA1";
    private static UUID uuid;
    public static String DEVICE_TOKEN = "";
    //推送相关
    public static final String APP_STATIC_PREF = "APP_STATIC_PREF";
    public static final String PREF_DEVICE_TOKEN_KEY = "PREF_DEVICE_TOKEN_KEY";
    public static final String PREFS_FILE = "device_id.xml";
    public static final String PREFS_DEVICE_ID = "device_id";
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        initApp=this;
        VERSION = PackageUtils.getAppVersionName(this);
        initSignatureTools();
        initUserPref();
    }


    public void initUserPref() {
        initDeviceId(getApplicationContext());
        DEVICE_TOKEN = new Md5(uuid == null ? "null" : uuid.toString()).compute();
        PrefUtils.putString(this, APP_STATIC_PREF, PREF_DEVICE_TOKEN_KEY, DEVICE_TOKEN);
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
