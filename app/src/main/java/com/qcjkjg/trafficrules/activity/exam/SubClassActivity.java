package com.qcjkjg.trafficrules.activity.exam;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SubclassGridAdapter;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.fragment.AnswerFragment;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/21.
 */
public class SubClassActivity extends BaseActivity{
    private GridView subclassGV;
    private List<String> list=new ArrayList<String>();
    private SubclassGridAdapter adapter;
    private int fragmentPositon;//第几个fragment
    private List<Subject> subjectList=new ArrayList<Subject>();
    private String fragmentType;
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


    }
    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        subclassGV= (GridView) findViewById(R.id.subclassGV);
        adapter=new SubclassGridAdapter(SubClassActivity.this,list,fragmentType);
        subclassGV.setAdapter(adapter);
//        subclassGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(SubClassActivity.this, AnswerActivity.class);
//                intent.putExtra("fragmentType",fragmentType);
//                intent.putExtra("type","subclass");
//                startActivity(intent);
//            }
//        });
    }
}
