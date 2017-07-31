package com.qcjkjg.trafficrules.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zongshuo on 2017/7/27 0027.
 */
public class QingChenIntentService extends GTIntentService {

    public QingChenIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();


        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data;
            try {
                data = new String(payload, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }




    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        sign(context,clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }


    private void sign(final Context context, final String clientId) {
        String oldClientId = PrefUtils.getString(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY, "");
        if (oldClientId.equalsIgnoreCase(clientId)
                && PrefUtils.getBoolean(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY_FLAG, false)) {
            return;
        }
        PrefUtils.putString(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY, clientId);

        final String url = ApiConstants.SIGN_API;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            Log.e("signService",response);
                            if (jo.has("code")) {
                                if ("0".equalsIgnoreCase(jo.getString("code"))) {
                                    Toast.makeText(context,"个推sevice签到",Toast.LENGTH_SHORT).show();
                                    PrefUtils.putBoolean(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY_FLAG, true);
                                    PrefUtils.putString(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, jo.getString("is_vip"));
                                } else {
                                    PrefUtils.putBoolean(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY_FLAG, false);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                //POST 参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("clientid", clientId);
                params.put("phone", PrefUtils.getString(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, ""));
                params.put("device_type", InitApp.DEVICE_TYPE);
                params.put("device_token", InitApp.DEVICE_TOKEN);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(req);
    }

}