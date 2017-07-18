package com.qcjkjg.trafficrules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.qcjkjg.trafficrules.db.DbCreateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {
    private List<String> firstList=new ArrayList<String>();
    private Map<String,List<String>> map=new HashMap<String,List<String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        //打开数据库输出流
        DbCreateHelper s = new DbCreateHelper();
        SQLiteDatabase db =s.openDatabase(getApplicationContext());


        Cursor cursor = db.rawQuery("select Name,ID from areacode4 where Dep=?", new String[]{"5"});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            int id=cursor.getInt(cursor.getColumnIndex("ID"));
            firstList.add(name);
            List<String> secondList=new ArrayList<String>();
            Cursor cursor1 = db.rawQuery("select Name,ID from areacode4 where Dep=? and ParentID=?", new String[]{"6",id+""});
            while (cursor1.moveToNext()) {
                String name1 = cursor1.getString(cursor.getColumnIndex("Name"));
                int id1=cursor1.getInt(cursor.getColumnIndex("ID"));
                secondList.add(name1);
                Log.e("aaa",name+"===="+name1+"===="+id1);
            }
            Log.e("ssss",secondList.size()+"");
            map.put(name,secondList);
        }

        int numbe=0;
        for(String key:map.keySet()){
            numbe+=map.get(key).size();
            Log.e("ccc",key+"===="+map.get(key).size()+"===="+numbe);
        }
    }


}
