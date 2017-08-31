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
import me.codeboy.android.cycleviewpager.CycleViewPager;

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
    private CycleViewPager cycleViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        initData();
        initView();
    }

    private void initData(){
        Resources res =getResources();
        String[] array1=res.getStringArray(R.array.vip1);
        String[] array2=res.getStringArray(R.array.vip2);

        fragmentType=getIntent().getStringExtra("fragmentType");
        if("1".equals(getUserInfo(5))){
            if("1".equals(fragmentType)){
                list= Arrays.asList(array1);
            }else{
                list= Arrays.asList(array2);
            }
        }else if("3".equals(getUserInfo(5))){
            if("1".equals(fragmentType)){
                List<String> list1 = Arrays.asList(array1);
                list= new ArrayList(list1);
                list.add("8.客车专项-vip客车专项");
            }else{
                list= Arrays.asList(array2);
            }
        }else if("2".equals(getUserInfo(5))){
            if("1".equals(fragmentType)){
                List<String> list1 = Arrays.asList(array1);
                list= new ArrayList(list1);
                list.add("8.货车专项-vip货车专项");
            }else{
                list= Arrays.asList(array2);
            }
        }else {
            if("1".equals(fragmentType)){
                list.add("1.摩托专项-vip摩托1");
                list.add("2.摩托专项-vip摩托2");
                list.add("3.摩托专项-vip摩托3");
            }else{
                list.add("1.摩托专项-vip摩托");
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
        vipLV= (ListView) findViewById(R.id.vipLV);
        adapter=new SubVipAdapter(VipActivity.this,list,fragmentType);
        vipLV.setAdapter(adapter);

        cycleViewPager= (CycleViewPager)findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);
        adlist("5", cycleViewPager);


        setShareView(findViewById(R.id.shareLL));
    }
}
