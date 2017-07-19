package com.qcjkjg.trafficrules.activity.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeActivity extends BaseActivity{
    private ListView themeLV;
    private MyThemeAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_theme);
        initView();
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        themeLV= (ListView) findViewById(R.id.themeLV);
        adapter=new MyThemeAdapter(MyThemeActivity.this,null);
        themeLV.setAdapter(adapter);
    }
}
