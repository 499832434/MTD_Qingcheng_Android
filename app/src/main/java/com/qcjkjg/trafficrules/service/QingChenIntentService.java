package com.qcjkjg.trafficrules.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.circle.CircleDetailActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.activity.signup.SignupContentActivity;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Signup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/27 0027.
 */
public class QingChenIntentService extends GTIntentService {
    private final Random random = new Random(System.currentTimeMillis());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public QingChenIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {

        byte[] payload = msg.getPayload();



        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data;
            try {
                data = new String(payload, "UTF-8");
                Log.e(TAG, data);
                JSONObject jo = new JSONObject(data);
                int pushType=jo.getInt("push_type");
                String title="";
                if(1==pushType){
                    title="您收到了一个赞";
                }else if(0==pushType){
                    title=jo.getString("content");
                }else{
                    title=jo.getString("title");
                }

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification.Builder builder1 = new Notification.Builder(context);
                builder1.setSmallIcon(R.mipmap.ic_qicheng); //设置图标
                builder1.setContentTitle("通知"); //设置标题
                builder1.setContentText(title); //消息内容
                builder1.setWhen(System.currentTimeMillis()); //发送时间
                builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
                builder1.setAutoCancel(true);//打开程序后图标消失


                if(1==pushType){
                    Intent intent = new Intent(context, BaseListViewActivity.class);
                    intent.putExtra("flag", 2);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intent, 0);
                    builder1.setContentIntent(pendingIntent);
                    Notification notification1 = builder1.build();
                    notificationManager.notify((int) (System.currentTimeMillis() / 1000) + random.nextInt(), notification1); // 通过通知管理器发送通知
                }else if(0==pushType){
                    Intent intent = new Intent(context, MessageReplyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intent, 0);
                    builder1.setContentIntent(pendingIntent);
                    Notification notification1 = builder1.build();
                    notificationManager.notify((int) (System.currentTimeMillis() / 1000) + random.nextInt(), notification1);
                }else{
                    Signup sign=new Signup();
                    sign.setNewsId(jo.getInt("news_id"));
                    Intent intent = new Intent(context, SignupContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(MainActivity.SINGUPTAG, sign);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, intent, 0);
                    builder1.setContentIntent(pendingIntent);
                    Notification notification1 = builder1.build();
                    notificationManager.notify((int) (System.currentTimeMillis() / 1000) + random.nextInt(), notification1);
                }

            } catch (Exception e) {
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
//                            Log.e("signService",response);
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
                params.put("client_id", clientId);
                params.put("phone", PrefUtils.getString(context, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, ""));
                params.put("device_type", InitApp.DEVICE_TYPE);
                params.put("device_token", InitApp.DEVICE_TOKEN);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(req);
    }

}