package com.qcjkjg.trafficrules.activity.exam;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SubVipAdapter;
import com.qcjkjg.trafficrules.adapter.SubchapterAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/23.
 */
public class VipActivity extends BaseActivity{
    private ListView vipLV;
    private List<String> list=new ArrayList<String>();
    private SubVipAdapter adapter;
    private int fragmentPositon;//第几个fragment
    private List<Subject> subjectList=new ArrayList<Subject>();
    private String fragmentType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        initData();
        initView();
    }

    private void initData(){
        Resources res =getResources();
        String[] array=res.getStringArray(R.array.vip);
        list= Arrays.asList(array);

        fragmentType=getIntent().getStringExtra("fragmentType");
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        vipLV= (ListView) findViewById(R.id.vipLV);
        adapter=new SubVipAdapter(VipActivity.this,list,fragmentType);
        vipLV.setAdapter(adapter);
    }
}
