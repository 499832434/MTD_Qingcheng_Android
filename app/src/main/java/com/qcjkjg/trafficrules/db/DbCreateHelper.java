package com.qcjkjg.trafficrules.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.vo.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class DbCreateHelper {

    private Context context;
    //数据库存储路径
    String filePath = "data/data/com.qcjkjg.trafficrules/QCData.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    String pathStr = "data/data/com.qcjkjg.trafficrules";

    SQLiteDatabase database;

    public DbCreateHelper(Context context) {
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
                InputStream is=am.open("QCData.db");
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

    //专项加强(功能分类)
    public List<Subject>  getSubjectList(String examType,String subClass,String type){
        SQLiteDatabase db =openDatabase(context);
        List<Subject> subjectList=new ArrayList<Subject>();
        Cursor cursor =null;
        if("subseq".equals(type)){
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType});
        }else if("subclass".equals(type)){
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and sub_class=? order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType,subClass});
        }else if("subchapter".equals(type)){
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and sub_chapter=? order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType,subClass});
        }else if("subvip".equals(type)){
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and vip=? order by vip_id", new String[]{((BaseActivity)context).getUserInfo(5),examType,subClass});
        }
        while (cursor.moveToNext()) {
            Subject subject=new Subject();
            subject.setSubId(cursor.getString(cursor.getColumnIndex("sub_id")));
            subject.setSubType(cursor.getString(cursor.getColumnIndex("sub_type")));
            subject.setSubTitle(cursor.getString(cursor.getColumnIndex("sub_title")));
            subject.setSubPic(cursor.getString(cursor.getColumnIndex("sub_pic")));
            subject.setSubA(cursor.getString(cursor.getColumnIndex("a")));
            subject.setSubB(cursor.getString(cursor.getColumnIndex("b")));
            subject.setSubC(cursor.getString(cursor.getColumnIndex("c")));
            subject.setSubD(cursor.getString(cursor.getColumnIndex("d")));
            subject.setSubAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            subject.setSubInfos(cursor.getString(cursor.getColumnIndex("sub_infos")));
            subject.setSubInfoPic(cursor.getString(cursor.getColumnIndex("info_pic")));
            subject.setErrorNum(cursor.getString(cursor.getColumnIndex("error_num")));
            subject.setStar(cursor.getString(cursor.getColumnIndex("star")));
            subject.setVipInfos(cursor.getString(cursor.getColumnIndex("dtjq")));
            subject.setVipSound(cursor.getString(cursor.getColumnIndex("jqyy")));
            subject.setVipPic(cursor.getString(cursor.getColumnIndex("vip_pic")));
            subject.setSubChapter(cursor.getString(cursor.getColumnIndex("sub_chapter")));
            subject.setSubClass(cursor.getString(cursor.getColumnIndex("sub_class")));
            subject.setSubVip(cursor.getString(cursor.getColumnIndex("vip")));
            subjectList.add(subject);
        }
        return subjectList;
    }
    //专项加强(文字 图片)flag:true 带图
    public List<Subject>  getSubjectListPicture(String examType,boolean flag){
        SQLiteDatabase db =openDatabase(context);
        List<Subject> subjectList=new ArrayList<Subject>();
        Cursor cursor =null;
        if(flag){
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and sub_pic is not null order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType});
        }else {
            cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and sub_pic is null order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType});
        }
        while (cursor.moveToNext()) {
            Subject subject=new Subject();
            subject.setSubId(cursor.getString(cursor.getColumnIndex("sub_id")));
            subject.setSubType(cursor.getString(cursor.getColumnIndex("sub_type")));
            subject.setSubTitle(cursor.getString(cursor.getColumnIndex("sub_title")));
            subject.setSubPic(cursor.getString(cursor.getColumnIndex("sub_pic")));
            subject.setSubA(cursor.getString(cursor.getColumnIndex("a")));
            subject.setSubB(cursor.getString(cursor.getColumnIndex("b")));
            subject.setSubC(cursor.getString(cursor.getColumnIndex("c")));
            subject.setSubD(cursor.getString(cursor.getColumnIndex("d")));
            subject.setSubAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            subject.setSubInfos(cursor.getString(cursor.getColumnIndex("sub_infos")));
            subject.setSubInfoPic(cursor.getString(cursor.getColumnIndex("info_pic")));
            subject.setErrorNum(cursor.getString(cursor.getColumnIndex("error_num")));
            subject.setStar(cursor.getString(cursor.getColumnIndex("star")));
            subject.setVipInfos(cursor.getString(cursor.getColumnIndex("dtjq")));
            subject.setVipSound(cursor.getString(cursor.getColumnIndex("jqyy")));
            subject.setVipPic(cursor.getString(cursor.getColumnIndex("vip_pic")));
            subject.setSubChapter(cursor.getString(cursor.getColumnIndex("sub_chapter")));
            if(flag){
                subject.setSubClass("图片题");
            }else{
                subject.setSubClass("文字题");
            }
            subject.setSubVip(cursor.getString(cursor.getColumnIndex("vip")));
            subjectList.add(subject);
        }
        return subjectList;
    }

    //专项加强(单选 多选 判断)2:单选3:多选1:判断
    public List<Subject>  getSubjectListType(String examType,int flag){
        SQLiteDatabase db =openDatabase(context);
        List<Subject> subjectList=new ArrayList<Subject>();
        Cursor cursor =null;
        cursor = db.rawQuery("select * from tiku where car_type=? and exam_type=? and sub_type=? order by sub_id", new String[]{((BaseActivity)context).getUserInfo(5),examType,flag+""});
        while (cursor.moveToNext()) {
            Subject subject=new Subject();
            subject.setSubId(cursor.getString(cursor.getColumnIndex("sub_id")));
            subject.setSubType(cursor.getString(cursor.getColumnIndex("sub_type")));
            subject.setSubTitle(cursor.getString(cursor.getColumnIndex("sub_title")));
            subject.setSubPic(cursor.getString(cursor.getColumnIndex("sub_pic")));
            subject.setSubA(cursor.getString(cursor.getColumnIndex("a")));
            subject.setSubB(cursor.getString(cursor.getColumnIndex("b")));
            subject.setSubC(cursor.getString(cursor.getColumnIndex("c")));
            subject.setSubD(cursor.getString(cursor.getColumnIndex("d")));
            subject.setSubAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            subject.setSubInfos(cursor.getString(cursor.getColumnIndex("sub_infos")));
            subject.setSubInfoPic(cursor.getString(cursor.getColumnIndex("info_pic")));
            subject.setErrorNum(cursor.getString(cursor.getColumnIndex("error_num")));
            subject.setStar(cursor.getString(cursor.getColumnIndex("star")));
            subject.setVipInfos(cursor.getString(cursor.getColumnIndex("dtjq")));
            subject.setVipSound(cursor.getString(cursor.getColumnIndex("jqyy")));
            subject.setVipPic(cursor.getString(cursor.getColumnIndex("vip_pic")));
            subject.setSubChapter(cursor.getString(cursor.getColumnIndex("sub_chapter")));
            if(1==flag){
                subject.setSubClass("判断题");
            }else if(2==flag){
                subject.setSubClass("单选题");
            } else {
                subject.setSubClass("多选题");
            }
            subject.setSubVip(cursor.getString(cursor.getColumnIndex("vip")));
            subjectList.add(subject);
        }
        return subjectList;
    }


    //查询收藏题目
    public List<Subject>  getSubjectCollectList(String examType,List<String> list){
        String str=argsArrayToString(list);
        SQLiteDatabase db =openDatabase(context);
        List<Subject> subjectList=new ArrayList<Subject>();
        Cursor cursor =null;
        cursor=db.rawQuery("select * from tiku where "+String.format(" car_type=? and exam_type=? and sub_id  IN %s order by sub_id",str),new String[]{((BaseActivity)context).getUserInfo(5),examType});
        while (cursor.moveToNext()) {
            Subject subject=new Subject();
            subject.setSubId(cursor.getString(cursor.getColumnIndex("sub_id")));
            subject.setSubType(cursor.getString(cursor.getColumnIndex("sub_type")));
            subject.setSubTitle(cursor.getString(cursor.getColumnIndex("sub_title")));
            subject.setSubPic(cursor.getString(cursor.getColumnIndex("sub_pic")));
            subject.setSubA(cursor.getString(cursor.getColumnIndex("a")));
            subject.setSubB(cursor.getString(cursor.getColumnIndex("b")));
            subject.setSubC(cursor.getString(cursor.getColumnIndex("c")));
            subject.setSubD(cursor.getString(cursor.getColumnIndex("d")));
            subject.setSubAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            subject.setSubInfos(cursor.getString(cursor.getColumnIndex("sub_infos")));
            subject.setSubInfoPic(cursor.getString(cursor.getColumnIndex("info_pic")));
            subject.setErrorNum(cursor.getString(cursor.getColumnIndex("error_num")));
            subject.setStar(cursor.getString(cursor.getColumnIndex("star")));
            subject.setVipInfos(cursor.getString(cursor.getColumnIndex("dtjq")));
            subject.setVipSound(cursor.getString(cursor.getColumnIndex("jqyy")));
            subject.setVipPic(cursor.getString(cursor.getColumnIndex("vip_pic")));
            subject.setSubChapter(cursor.getString(cursor.getColumnIndex("sub_chapter")));
            subject.setSubClass(cursor.getString(cursor.getColumnIndex("sub_class")));
            subject.setSubVip(cursor.getString(cursor.getColumnIndex("vip")));
            subjectList.add(subject);
        }
        return subjectList;
    }

    //未做题目
    public List<Subject>  getNodoneSubjectList(List<String> list,String examType){
        String str=argsArrayToString(list);
        SQLiteDatabase db =openDatabase(context);
        List<Subject> subjectList=new ArrayList<Subject>();
        Cursor cursor =null;
//        cursor=db.query("tiku",new String[]{"sub_id","sub_type","sub_title","sub_pic","a","b","c","d","answer","sub_infos","info_pic","error_num","star","dtjq","jqyy","vip_pic"},String.format(" car_type=? and exam_type=? and sub_id NOT IN %s",str),new String[]{((BaseActivity)context).getUserInfo(5),examType}, null,null,"sub_id asc",null);
        cursor=db.rawQuery("select * from tiku where "+String.format(" car_type=? and exam_type=? and sub_id NOT IN %s order by sub_id",str),new String[]{((BaseActivity)context).getUserInfo(5),examType});
        while (cursor.moveToNext()) {
            Subject subject=new Subject();
            subject.setSubId(cursor.getString(cursor.getColumnIndex("sub_id")));
            subject.setSubType(cursor.getString(cursor.getColumnIndex("sub_type")));
            subject.setSubTitle(cursor.getString(cursor.getColumnIndex("sub_title")));
            subject.setSubPic(cursor.getString(cursor.getColumnIndex("sub_pic")));
            subject.setSubA(cursor.getString(cursor.getColumnIndex("a")));
            subject.setSubB(cursor.getString(cursor.getColumnIndex("b")));
            subject.setSubC(cursor.getString(cursor.getColumnIndex("c")));
            subject.setSubD(cursor.getString(cursor.getColumnIndex("d")));
            subject.setSubAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            subject.setSubInfos(cursor.getString(cursor.getColumnIndex("sub_infos")));
            subject.setSubInfoPic(cursor.getString(cursor.getColumnIndex("info_pic")));
            subject.setErrorNum(cursor.getString(cursor.getColumnIndex("error_num")));
            subject.setStar(cursor.getString(cursor.getColumnIndex("star")));
            subject.setVipInfos(cursor.getString(cursor.getColumnIndex("dtjq")));
            subject.setVipSound(cursor.getString(cursor.getColumnIndex("jqyy")));
            subject.setVipPic(cursor.getString(cursor.getColumnIndex("vip_pic")));
            subjectList.add(subject);
        }
        return subjectList;

    }

    private String argsArrayToString(List<String> args) {
        StringBuilder argsBuilder = new StringBuilder();
        argsBuilder.append("(");
        final int argsCount = args.size();
        for (int i=0; i<argsCount; i++) {
            argsBuilder.append(args.get(i));
            if (i < argsCount - 1) {
                argsBuilder.append(",");
            }
        }
        argsBuilder.append(")");
        return argsBuilder.toString();
    }

}
