package com.qcjkjg.trafficrules.activity.tubiao;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.TubiaoAdapter;
import com.qcjkjg.trafficrules.adapter.TubiaoDetailAdapter;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/26.
 */
public class TubiaoDetailActivity extends BaseActivity{
    private GridView tubiaoGV;
    private TubiaoDetailAdapter adapter;
    private List<Tubiao> list=new ArrayList<Tubiao>();
    private String code,title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tubiao_detail);

        initData();
        initView();
    }


    private void initData() {
        code=getIntent().getStringExtra("code");
        title=getIntent().getStringExtra("title");
        DbTubiaoHelper helper = new DbTubiaoHelper(TubiaoDetailActivity.this);
        list = helper.getTubiaoDetailList(code);
    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView(title);
        tubiaoGV= (GridView) findViewById(R.id.tubiaoGV);
        adapter=new TubiaoDetailAdapter(TubiaoDetailActivity.this,list);
        tubiaoGV.setAdapter(adapter);

    }
}
