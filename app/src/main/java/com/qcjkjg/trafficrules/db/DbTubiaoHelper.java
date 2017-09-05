package com.qcjkjg.trafficrules.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class DbTubiaoHelper {

    private Context context;
    //数据库存储路径
    String filePath = "data/data/com.qcjkjg.trafficrules/dubiao.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    String pathStr = "data/data/com.qcjkjg.trafficrules";

    SQLiteDatabase database;

    public DbTubiaoHelper(Context context) {
        this.context = context;
    }

    public  SQLiteDatabase openDatabase(Context context){
        System.out.println("filePath:"+filePath);
        File jhPath=new File(filePath);
        //查看数据库文件是否存在
        if(jhPath.exists()){
//            Log.e("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        }else{
            //不存在先创建文件夹
            File path=new File(pathStr);
//            Log.e("test", "pathStr="+path);
//            if (path.mkdir()){
//                Log.e("test", "创建成功");
//            }else{
//                Log.e("test", "创建失败");
//            };
            try {
                //得到资源
                AssetManager am= context.getAssets();
                //得到数据库的输入流
                InputStream is=am.open("dubiao.db");
//                Log.e("test", is+"");
                //用输出流写到SDcard上面
                FileOutputStream fos=new FileOutputStream(jhPath);
//                Log.e("test", "fos="+fos);
//                Log.e("test", "jhPath="+jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer=new byte[1024];
                int count = 0;
                while((count = is.read(buffer))>0){
                    Log.e("test", "得到");
                    fos.write(buffer,0,count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

    public List<Tubiao>  getTubiaoList1(){
        SQLiteDatabase db =openDatabase(context);
        List<Tubiao> tubiaoList=new ArrayList<Tubiao>();
        Cursor cursor =null;
        cursor = db.rawQuery("select name,type,count(*) as a from tubiao group by type",null);
        while (cursor.moveToNext()) {
            Tubiao tubiao=new Tubiao();
            tubiao.setType(cursor.getString(cursor.getColumnIndex("type")));
            tubiao.setFlag(cursor.getString(cursor.getColumnIndex("name")));
            tubiao.setNum(cursor.getString(cursor.getColumnIndex("a")));
            tubiaoList.add(tubiao);
        }
        return tubiaoList;
    }
    public List<Tubiao>  getTubiaoList(String type){
        SQLiteDatabase db =openDatabase(context);
        List<Tubiao> tubiaoList=new ArrayList<Tubiao>();
        Cursor cursor =null;
        cursor = db.rawQuery("select class_code,class,count(*) as a from tubiao where type=? group by class_code", new String[]{type});
        while (cursor.moveToNext()) {
            Tubiao tubiao=new Tubiao();
            tubiao.setCode(cursor.getString(cursor.getColumnIndex("class_code")));
            tubiao.setName(cursor.getString(cursor.getColumnIndex("class")));
            tubiao.setNum(cursor.getString(cursor.getColumnIndex("a")));
            tubiaoList.add(tubiao);
        }
        return tubiaoList;
    }

    public List<String>  getTubiaoListLimit1(String type){
        SQLiteDatabase db =openDatabase(context);
        List<String> tubiaoList=new ArrayList<String>();
        Cursor cursor =null;
        cursor = db.rawQuery("select pic from tubiao where type=? limit 4", new String[]{type});
        while (cursor.moveToNext()) {
            tubiaoList.add(cursor.getString(cursor.getColumnIndex("pic")));
        }
        return tubiaoList;
    }

    public List<String>  getTubiaoListLimit(String type,String code){
        SQLiteDatabase db =openDatabase(context);
        List<String> tubiaoList=new ArrayList<String>();
        Cursor cursor =null;
        cursor = db.rawQuery("select pic from tubiao where type=? and class_code=? limit 4", new String[]{type,code});
        while (cursor.moveToNext()) {
            tubiaoList.add(cursor.getString(cursor.getColumnIndex("pic")));
        }
        return tubiaoList;
    }

    public List<Tubiao>  getTubiaoDetailList(String code){
        SQLiteDatabase db =openDatabase(context);
        List<Tubiao> tubiaoList=new ArrayList<Tubiao>();
        Cursor cursor =null;
        cursor = db.rawQuery("select pic,jx from tubiao where class_code=?", new String[]{code});
        while (cursor.moveToNext()) {
            Tubiao tubiao=new Tubiao();
            tubiao.setContent(cursor.getString(cursor.getColumnIndex("jx")));
            tubiao.setPictureUrl(cursor.getString(cursor.getColumnIndex("pic")));
            tubiaoList.add(tubiao);
        }
        return tubiaoList;
    }

}
