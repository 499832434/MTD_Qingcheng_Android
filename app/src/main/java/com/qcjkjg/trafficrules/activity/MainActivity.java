package com.qcjkjg.trafficrules.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.igexin.sdk.PushManager;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.fragment.SignupFragment;
import com.qcjkjg.trafficrules.service.QingChenIntentService;
import com.qcjkjg.trafficrules.service.QingChenPushService;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.CustomViewPager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {
    private List<String> firstList=new ArrayList<String>();
    private Map<String,List<String>> map=new HashMap<String,List<String>>();
    public MyFragAdapter mAdapter;
    private FragmentManager mFragmentManager;
    public CustomViewPager masterViewPager;
    public static String SINGUPTAG = "singup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushManager.getInstance().initialize(this.getApplicationContext(), QingChenPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), QingChenIntentService.class);
        initView();
    }

    private void initView(){
//        //打开数据库输出流
//        DbCreateHelper s = new DbCreateHelper();
//        SQLiteDatabase db =s.openDatabase(getApplicationContext());
//
//
//        Cursor cursor = db.rawQuery("select Name,ID from areacode4 where Dep=?", new String[]{"5"});
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex("Name"));
//            int id=cursor.getInt(cursor.getColumnIndex("ID"));
//            firstList.add(name);
//            List<String> secondList=new ArrayList<String>();
//            Cursor cursor1 = db.rawQuery("select Name,ID from areacode4 where Dep=? and ParentID=?", new String[]{"6",id+""});
//            while (cursor1.moveToNext()) {
//                String name1 = cursor1.getString(cursor.getColumnIndex("Name"));
//                int id1=cursor1.getInt(cursor.getColumnIndex("ID"));
//                secondList.add(name1);
//                Log.e("aaa",name+"===="+name1+"===="+id1);
//            }
//            Log.e("ssss",secondList.size()+"");
//            map.put(name,secondList);
//        }
//
//        int numbe=0;
//        for(String key:map.keySet()){
//            numbe+=map.get(key).size();
//            Log.e("ccc",key+"===="+map.get(key).size()+"===="+numbe);
//        }

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
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SignupFragment();
            } else if (position == 1) {
                return new Fragment();
            } else if (position == 2) {
                return new CircleFragment();
            } else if (position == 3) {
                return new Fragment();
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
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MainActivity.this).onActivityResult(requestCode,resultCode,data);
    }

}
