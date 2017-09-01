package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.account.SettingQuestionActivity;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.zaaach.citypicker.CityPickerActivity;

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

        currentView.findViewById(R.id.areaIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivityForResult(new Intent(mActivity, CityPickerActivity.class),MainActivity.REQUEST_CODE_PICK_CITY);
            }
        });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mActivity.REQUEST_CODE_PICK_CITY && resultCode == mActivity.RESULT_OK){
            if("yes".equals(mActivity.getUserInfo(11))){
                startActivity(new Intent(mActivity, SettingQuestionActivity.class));
            }
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                if(city.indexOf("-")!=-1){
                    PrefUtils.putString(mActivity, InitApp.USER_PRIVATE_DATA, InitApp.USER_PROVINCE_KEY, city.split("-")[0]);
                    PrefUtils.putString(mActivity, InitApp.USER_PRIVATE_DATA, InitApp.USER_CITY_KEY,city.split("-")[1]);
                    PrefUtils.putString(mActivity, InitApp.USER_PRIVATE_DATA, InitApp.FIRST_OPEN_KEY,"no");
                    Toast.makeText(mActivity, city, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
