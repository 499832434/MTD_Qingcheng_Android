package com.qcjkjg.trafficrules.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.utils.StatusBarColorCompat;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.User;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zongshuo on 2017/7/19.
 */
public class LoginActivity extends BaseActivity{
    private String TAG = this.getClass().getSimpleName();
    public static String LOGINFLAG = "loginflag";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);
        initView();
    }


    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.phoneLoginTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                intent.putExtra("flag", "1");
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.qqLoginRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                intent.putExtra("flag", "0");
//                startActivity(intent);
                authorization(SHARE_MEDIA.QQ);
            }
        });

        findViewById(R.id.weixinLoginRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                intent.putExtra("flag", "0");
//                startActivity(intent);
                authorization(SHARE_MEDIA.WEIXIN);
            }
        });
    }


    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");

                Toast.makeText(getApplicationContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();

                //拿到信息去请求登录接口。。。
                String platformType = "1";
                if (share_media == SHARE_MEDIA.QQ) {
                    platformType = "0";
                }
                request(openid, name, iconurl, platformType);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 网络请求
     */
    private void request(final String openid,final String name,final String iconurl,final String platformType) {
        if (!NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CHECK_BIND_PHONE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("otherLoginRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject info=jsonObject.getJSONObject("info");
                                String name=info.getString("nick_name");
                                String phone=info.getString("phone");
                                String avatar=info.getString("avatar");
                                String isvip=info.getString("is_vip");
                                loginInfo(name,phone,avatar,isvip,platformType);
                                sign();
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                                User user=new User();
                                user.setNickName(name);
                                user.setAvatar(iconurl);
                                user.setPlatformType(platformType);
                                user.setOpenid(openid);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(LoginActivity.LOGINFLAG,user);
                                intent.putExtras(bundle);
                                intent.putExtra("flag", "0");
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("openid", openid);
                params.put("nick_name", name);
                params.put("avatar", iconurl);
                params.put("platform_type", platformType);
                params.put("client_id", PrefUtils.getString(LoginActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY, ""));
                params.put("device_type", InitApp.DEVICE_TYPE);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
