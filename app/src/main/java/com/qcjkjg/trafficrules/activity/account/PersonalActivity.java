package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;

/**
 * Created by zongshuo on 2017/7/31.
 */
public class PersonalActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_personal);
    }
}
