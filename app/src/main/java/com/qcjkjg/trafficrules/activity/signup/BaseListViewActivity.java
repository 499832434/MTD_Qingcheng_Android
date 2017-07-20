package com.qcjkjg.trafficrules.activity.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.MessageFabulousAdapter;
import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
import com.qcjkjg.trafficrules.adapter.SystemMessageAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageFabulous;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class BaseListViewActivity extends BaseActivity{

    private ListView listView;
    private SystemMessageAdapter adapter0;
    private MyThemeAdapter adapter1;
    private MessageFabulousAdapter adapter2;
    private int flag;//0:系统消息1:我的主题2:收到的赞
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_listview);
        initData();
        initView();
    }

    private void initData(){
        flag=getIntent().getIntExtra("flag",0);
    }
    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        listView= (ListView) findViewById(R.id.listView);
        switch (flag){
            case 0:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("系统消息");
                adapter0=new SystemMessageAdapter(BaseListViewActivity.this,null);
                listView.setAdapter(adapter0);
                break;
            case 1:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("我的主题");
                adapter1=new MyThemeAdapter(BaseListViewActivity.this,null);
                listView.setAdapter(adapter1);
                break;
            case 2:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("收到的赞");
                adapter2=new MessageFabulousAdapter(BaseListViewActivity.this,null);
                listView.setAdapter(adapter2);
                break;
        }
    }
}
