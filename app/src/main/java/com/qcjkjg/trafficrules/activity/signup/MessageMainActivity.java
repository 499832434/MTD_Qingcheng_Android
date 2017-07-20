package com.qcjkjg.trafficrules.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageMainActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setRightImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        findViewById(R.id.systemRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageMainActivity.this, BaseListViewActivity.class);
                intent.putExtra("flag", 0);
                startActivity(intent);
            }
        });
        findViewById(R.id.themeRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MessageMainActivity.this, BaseListViewActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
        findViewById(R.id.replyRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageMainActivity.this,MessageReplyActivity.class));
            }
        });
        findViewById(R.id.praiseRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageMainActivity.this, BaseListViewActivity.class);
                intent.putExtra("flag", 2);
                startActivity(intent);
            }
        });
    }
}
