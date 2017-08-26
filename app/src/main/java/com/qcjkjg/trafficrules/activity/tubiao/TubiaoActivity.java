package com.qcjkjg.trafficrules.activity.tubiao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.TubiaoAdapter;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/26.
 */
public class TubiaoActivity extends BaseActivity{
    private ListView tubiaoLV;
    private TubiaoAdapter adapter;
    private List<Tubiao> list=new ArrayList<Tubiao>();
    private String type;//科目几
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tubiao);

        initData();
        initView();
    }


    private void initData() {
        if ("1".equals(getIntent().getStringExtra("type"))) {
            type="一";
        }else if("2".equals(getIntent().getStringExtra("type"))){
            type="二";
        }else if("3".equals(getIntent().getStringExtra("type"))){
            type="三";
        }else {
            type="四";
        }
        DbTubiaoHelper helper = new DbTubiaoHelper(TubiaoActivity.this);
        list = helper.getTubiaoList(type);
    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tubiaoLV= (ListView) findViewById(R.id.tubiaoLV);
        adapter=new TubiaoAdapter(TubiaoActivity.this,list,type);
        tubiaoLV.setAdapter(adapter);
        tubiaoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(TubiaoActivity.this,TubiaoDetailActivity.class);
                intent.putExtra("code",list.get(i).getCode());
                intent.putExtra("title",list.get(i).getName());
                startActivity(intent);
            }
        });
    }
}
