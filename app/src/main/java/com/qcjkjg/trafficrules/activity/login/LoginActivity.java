package com.qcjkjg.trafficrules.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class LoginActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent=new Intent(LoginActivity.this, BindPhoneActivity.class);
                intent.putExtra("flag","1");
                startActivity(intent);
            }
        });

        findViewById(R.id.qqLoginRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, BindPhoneActivity.class);
                intent.putExtra("flag","0");
                startActivity(intent);
            }
        });

        findViewById(R.id.weixinLoginRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, BindPhoneActivity.class);
                intent.putExtra("flag","0");
                startActivity(intent);
            }
        });
    }


}
