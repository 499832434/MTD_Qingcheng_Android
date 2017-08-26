package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ExamFragment extends Fragment{
    private View currentView = null;
    private ViewPager viewPager;
    public List<Fragment> fragments;
    protected MainActivity mActivity;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        fragments=new ArrayList<Fragment>();
        fragments.add(ExamOneFragment.newInstance(1));
        fragments.add(ExamSecondFragment.newInstance(2));
        fragments.add(ExamSecondFragment.newInstance(3));
        fragments.add(ExamOneFragment.newInstance(4));

        //设置tabLayout的属性
        tabLayout = (TabLayout) currentView.findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#666666"), Color.parseColor("#3fb7f3"));//设置tab上文字的颜色，第一个参数表示没有选中状态下的文字颜色，第二个参数表示选中后的文字颜色
        //tabLayout.setSelectedIndicatorColor(Color.parseColor("#0ddcff"));//设置tab选中的底部的指示条的颜色
        viewPager = (ViewPager) currentView.findViewById(R.id.paraViewPager);
        viewPager.setOffscreenPageLimit(5);


        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(mActivity.getSupportFragmentManager()) {
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
                        return "科一";
                    case 1:
                        return "科二";
                    case 2:
                        return "科三";
                    case 3:
                        return "科四";

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
        this.mActivity= (MainActivity)context;
    }
}
