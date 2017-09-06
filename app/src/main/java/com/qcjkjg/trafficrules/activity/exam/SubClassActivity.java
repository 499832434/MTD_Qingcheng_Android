package com.qcjkjg.trafficrules.activity.exam;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.tubiao.TubiaoActivity;
import com.qcjkjg.trafficrules.activity.tubiao.TubiaoDetailActivity;
import com.qcjkjg.trafficrules.adapter.SubclassGridAdapter;
import com.qcjkjg.trafficrules.adapter.TubiaoAdapter;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.fragment.AnswerFragment;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/21.
 */
public class SubClassActivity extends BaseActivity{
    private MyGridView subclassGV;
    private List<String> list=new ArrayList<String>();
    private SubclassGridAdapter adapter;
    private String fragmentType;
    private List<Tubiao> tubiaoList=new ArrayList<Tubiao>();
    private MyListView tubiaoLV;
    private TubiaoAdapter adapter1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subclass);

        initData();
        initView();
    }

    private void initData(){
        Resources res =getResources();
        String[] subclass=res.getStringArray(R.array.subclass);
        list= Arrays.asList(subclass);

        fragmentType=getIntent().getStringExtra("fragmentType");

        DbTubiaoHelper helper = new DbTubiaoHelper(SubClassActivity.this);
        tubiaoList = helper.getTubiaoList1();
        
    }
    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        subclassGV= (MyGridView) findViewById(R.id.subclassGV);
        adapter=new SubclassGridAdapter(SubClassActivity.this,list,fragmentType);
        subclassGV.setAdapter(adapter);


        tubiaoLV= (MyListView) findViewById(R.id.tubiaoLV);
        adapter1=new TubiaoAdapter(SubClassActivity.this,tubiaoList,"",1);
        tubiaoLV.setAdapter(adapter1);
        tubiaoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DbTubiaoHelper helper = new DbTubiaoHelper(SubClassActivity.this);
                List<Tubiao> list1 = helper.getTubiaoList(tubiaoList.get(i).getType());
                if (list1.size() == 1) {
                    Intent intent = new Intent(SubClassActivity.this, TubiaoDetailActivity.class);
                    intent.putExtra("code", list1.get(0).getCode());
                    intent.putExtra("title", list1.get(0).getName());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SubClassActivity.this, TubiaoActivity.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("flag", tubiaoList.get(i).getType());
                    startActivity(intent);
                }
            }
        });
    }
}
