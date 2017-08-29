package com.qcjkjg.trafficrules.activity.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/8/29.
 */
public class MockExamActivity extends BaseActivity{
    private String fragmentType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_exam);

        initView();
    }
    private void initView(){
        fragmentType=getIntent().getStringExtra("fragmentType");
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String title="";
        if("1".equals(fragmentType)){
            title="科目一模拟考试";
        }else{
            title="科目四模拟考试";
        }
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView(title);
        if(getUserIsLogin()){
            getNetWorkPicture(getUserInfo(2), (CircleImageView) findViewById(R.id.advatarCIV));
        }else{
            findViewById(R.id.loginTV).setVisibility(View.VISIBLE);
            findViewById(R.id.loginTV).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MockExamActivity.this, LoginActivity.class));
                }
            });
        }

        if("1".equals(getUserInfo(5))){
            ((TextView)findViewById(R.id.carTypeTV)).setText("小车  C1/C2/C3");
        }else if("2".equals(getUserInfo(5))){
            ((TextView)findViewById(R.id.carTypeTV)).setText("货车  A2/B2");
        }else if("3".equals(getUserInfo(5))){
            ((TextView)findViewById(R.id.carTypeTV)).setText("客车  A1/A3/B1");
        }else {
            ((TextView)findViewById(R.id.carTypeTV)).setText("摩托车  D/E/F");
        }


        findViewById(R.id.moni1TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MockExamActivity.this, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType);
                intent.putExtra("type","submoni1");
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.moni2TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MockExamActivity.this, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType);
                intent.putExtra("type","submoni2");
                startActivity(intent);
                finish();
            }
        });
    }
}
