package com.qcjkjg.trafficrules.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.igexin.sdk.PushManager;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.account.SettingQuestionActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.event.NewMessageEvent;
import com.qcjkjg.trafficrules.event.RefreshExamNumEvent;
import com.qcjkjg.trafficrules.fragment.AccountFragment;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.fragment.ExamFragment;
import com.qcjkjg.trafficrules.fragment.SignupFragment;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.service.QingChenIntentService;
import com.qcjkjg.trafficrules.service.QingChenPushService;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.CustomViewPager;
import com.qcjkjg.trafficrules.vo.AccountMoney;
import com.qcjkjg.trafficrules.vo.AddSub;
import com.qcjkjg.trafficrules.vo.Subject;
import com.umeng.socialize.UMShareAPI;
import com.zaaach.citypicker.CityPickerActivity;
import de.greenrobot.event.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class MainActivity extends BaseActivity {
    private List<String> firstList=new ArrayList<String>();
    private Map<String,List<String>> map=new HashMap<String,List<String>>();
    public MyFragAdapter mAdapter;
    private FragmentManager mFragmentManager;
    public CustomViewPager masterViewPager;
    public CircleFragment circleFragment;
    public SignupFragment signupFragment;
    public ExamFragment examFragment;
    public static final int REQUEST_CODE_PICK_CITY = 0;
    public static List<String> errorList=new ArrayList<String>();
    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateTiku();//更新题库

        if("yes".equals(getUserInfo(11))){
            startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),REQUEST_CODE_PICK_CITY);
        }

        PushManager.getInstance().initialize(this.getApplicationContext(), QingChenPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), QingChenIntentService.class);
        initView();
        sign();
        getErrcnt();//获取错题数跟星级



    }

    private void initView(){
        signupFragment=new SignupFragment();
        examFragment=new ExamFragment();
        circleFragment=new CircleFragment();
        fragmentList.add(signupFragment);
        fragmentList.add(examFragment);
        fragmentList.add(circleFragment);
        fragmentList.add(new AccountFragment());
        masterViewPager = (CustomViewPager) findViewById(R.id.masterViewPager);
        mFragmentManager = getSupportFragmentManager();
        mAdapter = new MyFragAdapter(mFragmentManager);
        masterViewPager.setOffscreenPageLimit(5);
        masterViewPager.setAdapter(mAdapter);


        findViewById(R.id.signupRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masterViewPager.setCurrentItem(0);
            }
        });
        findViewById(R.id.examinationRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterViewPager.setCurrentItem(1);
            }
        });

        findViewById(R.id.circleRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterViewPager.setCurrentItem(2);
            }
        });

        findViewById(R.id.accountRB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterViewPager.setCurrentItem(3);
            }
        });
    }


    public class MyFragAdapter extends SmartFragmentStatePagerAdapter {

        public MyFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return fragmentList.get(0);
            } else if (position == 1) {
                return fragmentList.get(1);
            } else if (position == 2) {
                return fragmentList.get(2);
            } else if (position == 3) {
                return fragmentList.get(3);
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        // Sparse array to keep track of registered fragments in memory
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if("yes".equals(getUserInfo(11))){
                startActivity(new Intent(MainActivity.this, SettingQuestionActivity.class));
            }
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                if(city.indexOf("-")!=-1){
                    PrefUtils.putString(MainActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PROVINCE_KEY,city.split("-")[0]);
                    PrefUtils.putString(MainActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CITY_KEY,city.split("-")[1]);
                    PrefUtils.putString(MainActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.FIRST_OPEN_KEY,"no");
                    examFragment.setArea(city.split("-")[1]);
                    signupFragment.setArea(city.split("-")[1]);
                    circleFragment.setArea(city.split("-")[1]);
                    EventBus.getDefault().post(new RefreshExamNumEvent());
                    Toast.makeText(MainActivity.this,city,Toast.LENGTH_SHORT).show();
                }
            }

        }
        UMShareAPI.get(MainActivity.this).onActivityResult(requestCode, resultCode, data);
    }


    private void getErrcnt(){
        if (!NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            if(!TextUtils.isEmpty(getUserInfo(14))){
                try {
                    JSONObject jsonObject = new JSONObject(getUserInfo(14));
                    if (jsonObject.getString("code").equals("0")) {
                        JSONArray infoArr=jsonObject.getJSONArray("info");
                        for(int i=0;i<infoArr.length();i++){
                            JSONObject obj=infoArr.getJSONObject(i);
                            errorList.add(obj.getString("sub_id")+","+obj.getString("stars")+","+obj.getString("err_cnt")+","+obj.getString("total_cnt"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.GET_ERRCNT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("errorRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    errorList.add(obj.getString("sub_id")+","+obj.getString("stars")+","+obj.getString("err_cnt")+","+obj.getString("total_cnt"));
                                }
                                PrefUtils.putString(MainActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.STARS_RECORD, response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }


    private void updateTiku(){
        if (!NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.UPDATE_TIKU_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("updateTikuRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                List<AddSub> list=new ArrayList<AddSub>();
                                PrefUtils.putString(MainActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.UPDATE_TIKU, jsonObject.getString("update_time"));
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                if(infoArr.length()>0){
                                    for(int i=0;i<infoArr.length();i++){
                                        JSONObject obj=infoArr.getJSONObject(i);
                                        AddSub addSub=new AddSub();
                                        addSub.setJqyy(obj.getString("jqyy"));
                                        addSub.setVip(obj.getString("vip"));
                                        addSub.setId(obj.getString("id"));
                                        addSub.setErrorNum(obj.getString("error_num"));
                                        addSub.setExamType(obj.getString("exam_type"));
                                        addSub.setCity(obj.getString("city"));
                                        addSub.setSubInfos(obj.getString("sub_infos"));
                                        addSub.setSubTitle(obj.getString("sub_title"));
                                        addSub.setSubType(obj.getString("sub_type"));
                                        addSub.setAnswer(obj.getString("answer"));
                                        addSub.setVipPic(obj.getString("vip_pic"));
                                        addSub.setSubPic(obj.getString("sub_pic"));
                                        addSub.setSubClass(obj.getString("sub_class"));
                                        addSub.setDtjq(obj.getString("dtjq"));
                                        addSub.setSubChapte(obj.getString("sub_chapte"));
                                        addSub.setInfoPic(obj.getString("info_pic"));
                                        addSub.setD(obj.getString("d"));
                                        addSub.setA(obj.getString("a"));
                                        addSub.setCarType(obj.getString("car_type"));
                                        addSub.setC(obj.getString("c"));
                                        addSub.setB(obj.getString("b"));
                                        addSub.setVipId(obj.getString("vip_id"));
                                        addSub.setSubId(obj.getString("sub_id"));
                                        addSub.setPubtime(obj.getString("pubtime"));
                                        addSub.setOrderId(obj.getString("order_id"));
                                        list.add(addSub);
                                    }
                                    if(list.size()>0){
                                        DbCreateHelper helper=new DbCreateHelper(MainActivity.this);
                                        for(int i=0;i<list.size();i++){
                                            helper.updateTiku(list.get(i));
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("pubtime",getUserInfo(13));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    public void refresh(){
        fragmentList.clear();
        fragmentList.add(new AccountFragment());
        fragmentList.add(new AccountFragment());
        fragmentList.add(new AccountFragment());
        fragmentList.add(new AccountFragment());
        mAdapter.notifyDataSetChanged();
    }
}
