package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.SettingQuestionAdapter;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/4.
 */
public class SettingQuestionActivity extends BaseActivity implements View.OnClickListener{

    private List<View> list=new ArrayList<View>();
    private RelativeLayout xiaocheRL,huocheRL,kecheRL,motuocheRL;
    private int flag=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_question);

        initView();
    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        xiaocheRL= (RelativeLayout) findViewById(R.id.xiaocheRL);xiaocheRL.setOnClickListener(this);
        huocheRL= (RelativeLayout) findViewById(R.id.huocheRL);huocheRL.setOnClickListener(this);
        kecheRL= (RelativeLayout) findViewById(R.id.kecheRL);kecheRL.setOnClickListener(this);
        motuocheRL= (RelativeLayout) findViewById(R.id.motuocheRL);motuocheRL.setOnClickListener(this);
        list.add(xiaocheRL);
        list.add(huocheRL);
        list.add(kecheRL);
        list.add(motuocheRL);
        findViewById(R.id.commitTV).setOnClickListener(this);

        if("1".equals(getUserInfo(5))){
            xiaocheRL.performClick();
        }else if("2".equals(getUserInfo(5))){
            huocheRL.performClick();
        }else if("3".equals(getUserInfo(5))){
            kecheRL.performClick();
        }else {
            motuocheRL.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.xiaocheRL:
                setStatus(0);
                flag=1;
                 break;
            case R.id.huocheRL:
                setStatus(1);
                flag=2;
                break;
            case R.id.kecheRL:
                setStatus(2);
                flag=3;
                break;
            case R.id.motuocheRL:
                setStatus(3);
                flag=4;
                break;
            case R.id.commitTV:
                PrefUtils.putString(SettingQuestionActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CAR_TYPE_KEY, flag+"");
                toast(SettingQuestionActivity.this,"设置成功");
                finish();
                break;
        }
    }

    private void setStatus(int position){
        for(int i=0;i<list.size();i++){
            if(i==position){
                list.get(i).setBackgroundResource(R.drawable.ic_bg_position_n);
                ((RelativeLayout)list.get(i)).getChildAt(3).setVisibility(View.VISIBLE);
            }else{
                list.get(i).setBackgroundResource(R.drawable.ic_bg_position_un);
                ((RelativeLayout)list.get(i)).getChildAt(3).setVisibility(View.GONE);
            }
        }
    }
}
