package com.qcjkjg.trafficrules.activity.signup;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.fragment.MessageReplyFragment;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/20.
 */
public class MessageReplyActivity extends BaseActivity{
    private TextView myReplyTV,replyMeTV;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView cursorIV;
    private ViewPager viewPager;
    public List<Fragment> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_reply);
        initImageView();
        initView();
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        myReplyTV= (TextView) findViewById(R.id.myReplyTV);
        myReplyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        replyMeTV= (TextView) findViewById(R.id.replyMeTV);
        replyMeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        cursorIV= (ImageView) findViewById(R.id.cursorIV);

        fragments=new ArrayList<Fragment>();
        fragments.add(MessageReplyFragment.newInstance(MessageReplyActivity.this,0));//我的回复
        fragments.add(MessageReplyFragment.newInstance(MessageReplyActivity.this,1));//回复我的

        viewPager = (ViewPager) findViewById(R.id.viewPager);
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


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        myReplyTV.setTextColor(Color.parseColor("#3eb7f3"));
                        replyMeTV.setTextColor(Color.parseColor("#333333"));
                        setPosition(one, position);
                        break;
                    case 1:
                        myReplyTV.setTextColor(Color.parseColor("#333333"));
                        replyMeTV.setTextColor(Color.parseColor("#3eb7f3"));
                        setPosition(one, position);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try{
            viewPager.setCurrentItem(0);
        }catch (Exception e){

        }
    }

    /**
     * 初始化动画
     */

    private void initImageView() {
        cursorIV= (ImageView) findViewById(R.id.cursorIV);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cursor_line).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursorIV.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void setPosition(int one,int arg0){
        Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
        currIndex = arg0;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursorIV.startAnimation(animation);
    }
}
