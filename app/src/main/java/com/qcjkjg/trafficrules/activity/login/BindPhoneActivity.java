package com.qcjkjg.trafficrules.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.utils.TimesUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class BindPhoneActivity extends BaseActivity{
    private TextView verificationCodeTV;
    private TimesUtils timesUtils=null;
    private String flag="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

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
                timesUtils = new TimesUtils(60000, 1000, verificationCodeTV, BindPhoneActivity.this);
                timesUtils.start();
            }
        });

    }
}
