package com.qcjkjg.trafficrules.activity.exam;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SubchapterAdapter;
import com.qcjkjg.trafficrules.adapter.SubclassGridAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/22.
 */
public class SubchapterActivity extends BaseActivity{
    private ListView chapterLV;
    private List<String> list=new ArrayList<String>();
    private SubchapterAdapter adapter;
    private int fragmentPositon;//第几个fragment
    private List<Subject> subjectList=new ArrayList<Subject>();
    private String fragmentType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subchapter);

        initData();
        initView();
    }

    private void initData(){
        Resources res =getResources();
        String[] subtype1=res.getStringArray(R.array.subtype1_chapter);
        String[] subtype4=res.getStringArray(R.array.subtype4_chapter);
        List<String> list1= Arrays.asList(subtype1);
        List<String> list4= Arrays.asList(subtype4);

        fragmentType=getIntent().getStringExtra("fragmentType");
        if("1".equals(fragmentType)){
            if("1".equals(getUserInfo(5))){
                list.add(list1.get(0));
                list.add(list1.get(1));
                list.add(list1.get(2));
                list.add(list1.get(3));
                list.add(list1.get(9));
            }else if("2".equals(getUserInfo(5))){
                list.add(list1.get(0));
                list.add(list1.get(1));
                list.add(list1.get(2));
                list.add(list1.get(3));
                list.add(list1.get(4));
                list.add(list1.get(9));
            }else if("3".equals(getUserInfo(5))){
                list.add(list1.get(0));
                list.add(list1.get(1));
                list.add(list1.get(2));
                list.add(list1.get(3));
                list.add(list1.get(5));
                list.add(list1.get(9));
            }else if("4".equals(getUserInfo(5))){
                list.add(list1.get(6));
                list.add(list1.get(7));
                list.add(list1.get(8));
                list.add(list1.get(9));
            }
        }else if("4".equals(fragmentType)){
            if("1".equals(getUserInfo(5))||"2".equals(getUserInfo(5))||"3".equals(getUserInfo(5))){
                list.add(list4.get(0));
                list.add(list4.get(1));
                list.add(list4.get(2));
                list.add(list4.get(3));
                list.add(list4.get(4));
                list.add(list4.get(5));
                list.add(list4.get(6));
                list.add(list4.get(7));
            }else if("4".equals(getUserInfo(5))){
                list.add(list1.get(7));
                list.add(list1.get(8));
            }
        }
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        chapterLV= (ListView) findViewById(R.id.chapterLV);
        adapter=new SubchapterAdapter(SubchapterActivity.this,list,fragmentType);
        chapterLV.setAdapter(adapter);
    }
}
