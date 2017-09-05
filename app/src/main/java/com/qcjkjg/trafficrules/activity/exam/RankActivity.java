package com.qcjkjg.trafficrules.activity.exam;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.fragment.*;
import com.qcjkjg.trafficrules.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/28.
 */
public class RankActivity extends BaseActivity{
    private CustomViewPager viewPager;
    public List<Fragment> fragments;
    private TextView rankTV,scoreTV;
    private String fragmentType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initView();

    }



    private void initView(){
        fragmentType=getIntent().getStringExtra("type");
        findViewById(R.id.backIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rankTV= (TextView) findViewById(R.id.rankTV);
        scoreTV= (TextView) findViewById(R.id.scoreTV);
        rankTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                rankTV.setTextColor(Color.parseColor("#3eb7f3"));
                scoreTV.setTextColor(Color.parseColor("#cccccc"));
            }
        });
        scoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                rankTV.setTextColor(Color.parseColor("#cccccc"));
                scoreTV.setTextColor(Color.parseColor("#3eb7f3"));
            }
        });


        fragments=new ArrayList<Fragment>();
        fragments.add(new RankFragment());
        fragments.add(MyScoreFragment.newInstance(fragmentType));


        viewPager = (CustomViewPager) findViewById(R.id.paraViewPager);
        viewPager.setOffscreenPageLimit(5);
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
                if (position == 0) {
                    rankTV.performClick();
                } else {
                    scoreTV.performClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if("rank".equals(getIntent().getStringExtra("name"))){
            viewPager.setCurrentItem(0);
        }else{
            viewPager.setCurrentItem(1);
        }

    }

}
