package com.qcjkjg.trafficrules.activity.exam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;

import java.util.List;

/**
 * Created by zongshuo on 2017/8/23.
 */
public class ErrorCollectActivity extends BaseActivity{
    private ViewPager viewPager;
    public List<Fragment> fragments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_collect);
        initView();
    }

    private void initView(){

    }
}
