package com.qcjkjg.trafficrules.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.mob.MobSDK;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.utils.TimesUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class BindPhoneActivity extends BaseActivity{
    private TextView verificationCodeTV;
    private TimesUtils timesUtils=null;
    private String flag="";
    private EventHandler eventHandler;
    private String phone="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        MobSDK.init(BindPhoneActivity.this, "1f97ad5a0c9d0", "df1eb1c4ae7b059cad5595777c341b27");
        flag=getIntent().getStringExtra("flag");
        initView();
    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if("1".equals(flag)){
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
                    //定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE = "[1][358]\\d{9}";
                    //把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    //把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);
                    // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {//匹配手机号是否存在
                        timesUtils = new TimesUtils(60000, 1000, verificationCodeTV, BindPhoneActivity.this);
                        timesUtils.start();
                        SMSSDK.getVerificationCode("86", phone);
                    } else {
                        Toast.makeText(BindPhoneActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        findViewById(R.id.commitB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    toast(msg);
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            toast("验证码发送成功");
                        } else {
                            toast("验证码发送失败");
                        }
                    }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(BindPhoneActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            toast("验证码验证失败");
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
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BindPhoneActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
