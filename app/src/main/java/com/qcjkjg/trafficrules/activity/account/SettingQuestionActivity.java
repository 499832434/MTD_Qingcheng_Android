package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SettingQuestionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/4.
 */
public class SettingQuestionActivity extends BaseActivity{
    private GridView gridView;
    private List<Integer> list=new ArrayList<Integer>();
    private SettingQuestionAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_setting_question);

        initView();
    }
    private void initView(){
        list.add(R.drawable.ic_qq_share);
        list.add(R.drawable.ic_qq_share);
        list.add(R.drawable.ic_qq_share);
        list.add(R.drawable.ic_qq_share);
        gridView= (GridView) findViewById(R.id.gridview);
        adapter=new SettingQuestionAdapter(SettingQuestionActivity.this,list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }
}
