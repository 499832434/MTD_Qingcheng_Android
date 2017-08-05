package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.MyMoneyAdapter;

/**
 * Created by zongshuo on 2017/8/5.
 */
public class MyMoneyActivity extends BaseActivity{
    private ListView moneyLV;
    private MyMoneyAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_my_money);

        initView();
    }

    private void initView(){
        moneyLV= (ListView) findViewById(R.id.moneyLV);
        adapter=new MyMoneyAdapter(MyMoneyActivity.this,null);
        moneyLV.setAdapter(adapter);
    }
}
