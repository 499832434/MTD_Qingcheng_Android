package com.qcjkjg.trafficrules.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mob.MobSDK;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.utils.TimesUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Signup;
import com.qcjkjg.trafficrules.vo.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class BindPhoneActivity extends BaseActivity{
    private TextView verificationCodeTV;
    private TimesUtils timesUtils=null;
    private String flag="";//1:手机号登录 0:第三方登录
    private EventHandler eventHandler;
    private String phone="";
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        MobSDK.init(BindPhoneActivity.this, "1fb79eff89450", "b724cc5addb70159fe06b2ae0555838a");
        flag=getIntent().getStringExtra("flag");
        if("0".equals(flag)){
            user=getIntent().getParcelableExtra(LoginActivity.LOGINFLAG);
        }
        initView();
    }


    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if("0".equals(flag)){
            ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView("绑定手机");
        }else{
            ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView("手机验证码登录");
            findViewById(R.id.phoneTV).setVisibility(View.VISIBLE);
        }
        verificationCodeTV= (TextView) findViewById(R.id.verificationCodeTV);
        verificationCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = ((EditText) findViewById(R.id.phoneET)).getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(BindPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else {
//                    //定义需要匹配的正则表达式的规则
//                    String REGEX_MOBILE_SIMPLE = "[1][358]\\d{9}";
//                    //把正则表达式的规则编译成模板
//                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
//                    //把需要匹配的字符给模板匹配，获得匹配器
//                    Matcher matcher = pattern.matcher(phone);
//                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
//                    if (matcher.find()) {//匹配手机号是否存在
//                        timesUtils = new TimesUtils(60000, 1000, verificationCodeTV, BindPhoneActivity.this);
//                        timesUtils.start();
//                        SMSSDK.getVerificationCode("86", phone);
//                    } else {
//                        Toast.makeText(BindPhoneActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
//                    }
                    timesUtils = new TimesUtils(60000, 1000, verificationCodeTV, BindPhoneActivity.this);
                    timesUtils.start();
                    SMSSDK.getVerificationCode("86", phone);
                }

            }
        });

        findViewById(R.id.commitB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = ((EditText) findViewById(R.id.phoneET)).getText().toString().trim();
                String code = ((EditText) findViewById(R.id.codeET)).getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(BindPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(BindPhoneActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("86", phone, code);//提交验证码  在eventHandler里面查看验证结果
                }
            }
        });


        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable)data;
                    String msg = throwable.getMessage();
                    toast1(msg);
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            toast1("验证码发送成功");
                        } else {
                            toast1("验证码发送失败");
                        }
                    }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            toast1("验证码验证成功");
                            if("1".equals(flag)){
                                request1();
                            }else{
                                request0();
                            }
                        } else {
                            toast1("验证码验证失败");
                        }
                    }
                }
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    //吐司的一个小方法
    private void toast1(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BindPhoneActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 网络请求
     */
    private void request1() {
        if (!NetworkUtils.isNetworkAvailable(BindPhoneActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.PHONE_LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("phoneLoginRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject info=jsonObject.getJSONObject("info");
                                String name=info.getString("nick_name");
                                String phone=info.getString("phone");
                                String avatar=info.getString("avatar");
                                String isvip=info.getString("is_vip");
                                loginInfo(name,phone,avatar,isvip,"");
                                sign();
                                finish();
                            }else{
                                Toast.makeText(BindPhoneActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("phone", ((EditText) findViewById(R.id.phoneET)).getText().toString().trim());
                params.put("client_id", getUserInfo(4));
                params.put("device_type", InitApp.DEVICE_TYPE);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void request0() {
        if (!NetworkUtils.isNetworkAvailable(BindPhoneActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.BIND_PHONE_LOGIN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("bindPhoneRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject info=jsonObject.getJSONObject("info");
                                String name=info.getString("nick_name");
                                String phone=info.getString("phone");
                                String avatar=info.getString("avatar");
                                String isvip=info.getString("is_vip");
                                loginInfo(name,phone,avatar,isvip, user.getPlatformType());
                                sign();
                                finish();
                            }else{
                                Toast.makeText(BindPhoneActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("openid", user.getOpenid());
                params.put("nick_name", user.getNickName());
                params.put("avatar", user.getAvatar());
                params.put("platform_type", user.getPlatformType());
                params.put("client_id", PrefUtils.getString(BindPhoneActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY, ""));
                params.put("phone", phone);
                params.put("device_type", InitApp.DEVICE_TYPE);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
