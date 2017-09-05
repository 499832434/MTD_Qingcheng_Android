package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.activity.exam.ErrorCollectActivity;
import com.qcjkjg.trafficrules.activity.exam.RankActivity;
import com.qcjkjg.trafficrules.adapter.SubErrorCollectAdapter;
import com.qcjkjg.trafficrules.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class RankFragment extends Fragment{
    private View currentView = null;
    protected RankActivity mActivity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_rank, container, false);
        initView();
        return currentView;
    }

    private void initView(){

        fragments=new ArrayList<Fragment>();
        fragments.add(RankListFragment.newInstance(2));
        fragments.add(RankListFragment.newInstance(1));
        fragments.add(RankListFragment.newInstance(0));


        //设置tabLayout的属性
        tabLayout = (TabLayout) currentView.findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#3fb7f3"));//设置tab上文字的颜色，第一个参数表示没有选中状态下的文字颜色，第二个参数表示选中后的文字颜色
        //tabLayout.setSelectedIndicatorColor(Color.parseColor("#0ddcff"));//设置tab选中的底部的指示条的颜色
        viewPager = (ViewPager) currentView.findViewById(R.id.paraViewPager);
        viewPager.setOffscreenPageLimit(3);


        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
                switch (position) {
                    case 0:
                        return mActivity.getUserInfo(8);
                    case 1:
                        return mActivity.getUserInfo(7);
                    case 2:
                        return "全国";

                }
                return null;
            }

        });


        //然后让TabLayout和ViewPager关联，只需要一句话，简直也是没谁了.
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (RankActivity)context;
    }


}
