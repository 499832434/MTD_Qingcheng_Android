package com.qcjkjg.trafficrules.activity.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SystemMessageAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SystemMessageActivity extends BaseActivity{

    private ListView messageLV;
    private SystemMessageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);

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
        messageLV= (ListView) findViewById(R.id.messageLV);
        adapter=new SystemMessageAdapter(SystemMessageActivity.this,null);
        messageLV.setAdapter(adapter);
    }
}
