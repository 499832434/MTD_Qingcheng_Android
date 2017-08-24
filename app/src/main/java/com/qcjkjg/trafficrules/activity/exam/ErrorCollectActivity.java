package com.qcjkjg.trafficrules.activity.exam;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.fragment.ErrorCollectFragment;
import com.qcjkjg.trafficrules.fragment.ExamOneFragment;
import com.qcjkjg.trafficrules.fragment.ExamSecondFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/23.
 */
public class ErrorCollectActivity extends BaseActivity{
    private ViewPager viewPager;
    public List<Fragment> fragments;
    private String fragmentType;//科目几
    private TextView errorTV,collectTV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_collect);
        initView();
    }

    private void initView(){
        findViewById(R.id.backIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        errorTV= (TextView) findViewById(R.id.errorTV);
        collectTV= (TextView) findViewById(R.id.collectTV);
        errorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                errorTV.setTextColor(Color.parseColor("#3eb7f3"));
                errorTV.setTextSize(18);
                collectTV.setTextColor(Color.parseColor("#cccccc"));
                collectTV.setTextSize(16);
            }
        });
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                errorTV.setTextColor(Color.parseColor("#cccccc"));
                errorTV.setTextSize(16);
                collectTV.setTextColor(Color.parseColor("#3eb7f3"));
                collectTV.setTextSize(18);
            }
        });


        fragmentType=getIntent().getStringExtra("fragmentType");
        fragments=new ArrayList<Fragment>();
        fragments.add(ErrorCollectFragment.newInstance(fragmentType,"suberror"));
        fragments.add(ErrorCollectFragment.newInstance(fragmentType,"subcollect"));


        viewPager = (ViewPager) findViewById(R.id.paraViewPager);
        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    errorTV.performClick();
                }else{
                    collectTV.performClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
