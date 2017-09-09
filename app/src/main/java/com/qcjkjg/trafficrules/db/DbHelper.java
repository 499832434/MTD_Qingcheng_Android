package com.qcjkjg.trafficrules.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.vo.ExamScore;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class DbHelper extends AbstractDatabaseHelper {

    private static final String TAG = "DbHelper";
    private static final int DB_VERSION = 1;


    private static final String[] CREATE_SQLS = {
            "CREATE TABLE IF NOT EXISTS [qc_sub_answer] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [answer_num] INTEGER DEFAULT 0, " +//答过次数
                    "  [error_num] INTEGER DEFAULT 0, " +//答错次数 1是答错 0是默认
                    "  [seq_answer] varchar(10) DEFAULT '-1', " +//顺序
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  [class_answer] varchar(10) DEFAULT '-1', " +//专属
                    "  [vip_answer] varchar(10) DEFAULT '-1', " +//vip
                    "  [top_answer] varchar(10) DEFAULT '-1', " +//top
                    "  [collect_answer] varchar(10) DEFAULT '-1', " +//收藏
                    "  [error_answer] varchar(10) DEFAULT '-1', " +//错题
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [answer_status] INTEGER DEFAULT 0, " +//0:未作答1:正确2:错误
                    "  [answer_choice] varchar(10))", //答题结果

            "CREATE TABLE IF NOT EXISTS [qc_sub_collect] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id],[car_id],[sub_type]) ON CONFLICT IGNORE);",

            "CREATE TABLE IF NOT EXISTS [qc_sub_error] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id],[car_id],[sub_type]) ON CONFLICT IGNORE);",

            "CREATE TABLE IF NOT EXISTS [exam_result] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户
                    "  [time] varchar(10) , " +//用时
                    "  [score] varchar(10), " +//分数
                    "  [date] varchar(10), " +//答题日期
                    "  [answer] text, "+//答案
                    "  [subs] text, " +//题号
                    "  [sub_type] INTEGER DEFAULT 1); ", //科目

            "CREATE TABLE IF NOT EXISTS [exam_position] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [position] INTEGER DEFAULT 0 , " +//位置
                    "  [sub_type] INTEGER DEFAULT 1 );" //科目

    };

    private static final String[] UPDATE_SQLS = {
            "drop table if exists qc_sub_answer;",
            "drop table if exists qc_sub_collect;",
            "drop table if exists qc_sub_error;",

            "CREATE TABLE IF NOT EXISTS [qc_sub_answer] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [answer_num] INTEGER DEFAULT 0, " +//答过次数
                    "  [error_num] INTEGER DEFAULT 0, " +//答错次数 1是答错 0是默认
                    "  [seq_answer] varchar(10) DEFAULT '-1', " +//顺序
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  [class_answer] varchar(10) DEFAULT '-1', " +//专属
                    "  [vip_answer] varchar(10) DEFAULT '-1', " +//vip
                    "  [top_answer] varchar(10) DEFAULT '-1', " +//top
                    "  [collect_answer] varchar(10) DEFAULT '-1', " +//收藏
                    "  [error_answer] varchar(10) DEFAULT '-1', " +//错题
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [answer_status] INTEGER DEFAULT 0, " +//0:未作答1:正确2:错误
                    "  [answer_choice] varchar(10))", //答题结果

            "CREATE TABLE IF NOT EXISTS [qc_sub_collect] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id],[car_id],[sub_type]) ON CONFLICT IGNORE);",

            "CREATE TABLE IF NOT EXISTS [qc_sub_error] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [car_id] INTEGER DEFAULT 1, " +//车型id
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id],[car_id],[sub_type]) ON CONFLICT IGNORE);",

            "CREATE TABLE IF NOT EXISTS [exam_result] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户
                    "  [time] varchar(10) , " +//用时
                    "  [score] varchar(10), " +//分数
                    "  [date] varchar(10), " +//日期
                    "  [answer] text, "+//答案
                    "  [subs] text, " +//题号
                    "  [sub_type] INTEGER DEFAULT 1) " //科目
    };

    private Context ctx;

    public DbHelper(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected String getDatabaseName() {
        return InitApp.DB_NAME;
    }

    @Override
    protected int getDatabaseVersion() {
        return DB_VERSION;
    }

    @Override
    protected String[] createDBTables() {
        return CREATE_SQLS;
    }

    @Override
    protected String[] updateDBTables() {
        return UPDATE_SQLS;
    }

    //添加答题记录
    public final void addSub(SubjectSelect subjectSelect) {
        try {
            this.open(this.ctx);
            this.mDb.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("user_id", ((BaseActivity)ctx).getUserInfo(1));
            values.put("sub_id", subjectSelect.getSubId());
            values.put("car_id", ((BaseActivity)ctx).getUserInfo(5));
            values.put("answer_num",subjectSelect.getAnswerNum());
            values.put("error_num", subjectSelect.getErrorNum());
            values.put("seq_answer",subjectSelect.getSeqAnswer());
            values.put("chapter_answer", subjectSelect.getChapterAnswer());
            values.put("class_answer", subjectSelect.getClassAnswer());
            values.put("vip_answer", subjectSelect.getVipAnswer());
            values.put("top_answer", subjectSelect.getTopAnswer());
            values.put("collect_answer", subjectSelect.getCollectAnswer());
            values.put("error_answer", subjectSelect.getErrorAnswer());
            values.put("sub_type", subjectSelect.getSubType());
            values.put("answer_choice", subjectSelect.getAnswerChoice());
            values.put("answer_status", subjectSelect.getAnswerStatus());

            this.mDb.insert("qc_sub_answer", null, values);
            this.mDb.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            try {
                this.mDb.endTransaction();
            } catch (Exception e) {

            }
            this.close();
        }
    }

    //查询答题答案
    public final SubjectSelect querySub(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        SubjectSelect subjectSelect1=new SubjectSelect();
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select * from qc_sub_answer where user_id=? and sub_id=? and car_id=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and error_answer=? and sub_type=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),subjectSelect.getSubId()+"",((BaseActivity)ctx).getUserInfo(5),subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getErrorAnswer(),subjectSelect.getSubType()+""});
            while (cursor.moveToNext()) {
                subjectSelect1.setAnswerChoice(cursor.getString(cursor.getColumnIndex("answer_choice")));
            }
            return subjectSelect1;
        } catch (Exception e) {
            return subjectSelect1;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询该题目答过次数
    public final int querySubAnswerNum(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and sub_id=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),subjectSelect.getSubId()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询该题目答错次数
    public final int querySubErrorNum(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and sub_id=? and error_num=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),subjectSelect.getSubId()+"",subjectSelect.getErrorNum()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询一套题答对答错次数
    public final int queryWholeSubNum(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and car_id=? and answer_status=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and error_answer=? and sub_type=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),((BaseActivity)ctx).getUserInfo(5),subjectSelect.getAnswerStatus()+"",subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getErrorAnswer(),subjectSelect.getSubType()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询一套题答过次数
    public final int queryWholeSubNum1(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and car_id=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and error_answer=? and sub_type=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),((BaseActivity)ctx).getUserInfo(5),subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getErrorAnswer(),subjectSelect.getSubType()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //flag: true 收藏 ,false 错题
    public final void addCollectSub(Boolean flag,String subId,String sub_type,String chapter_answer){
        try {
            this.open(this.ctx);
            this.mDb.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("user_id", ((BaseActivity) ctx).getUserInfo(1));
            values.put("sub_id", subId);
            values.put("car_id", ((BaseActivity)ctx).getUserInfo(5));
            values.put("sub_type",sub_type);
            values.put("chapter_answer", chapter_answer);
            if(flag){
                this.mDb.insert("qc_sub_collect", null, values);
            }else{
                this.mDb.insert("qc_sub_error", null, values);
            }
            this.mDb.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            try {
                this.mDb.endTransaction();
            } catch (Exception e) {

            }
            this.close();
        }
}

    //删除收藏题目
    public final void delectCollectSub(Boolean flag,String subId,String sub_type) {
        this.open(this.ctx);
        try {
            if(flag){
                this.mDb.delete("qc_sub_collect", "user_id=? and sub_id=? and car_id=? and sub_type=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId, ((BaseActivity) ctx).getUserInfo(5), sub_type});
            }else{
                this.mDb.delete("qc_sub_error", "user_id=? and sub_id=? and car_id=? and sub_type=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId, ((BaseActivity) ctx).getUserInfo(5), sub_type});
            }
        } catch (Exception e) {

        } finally {
            this.close();
        }
    }

    //删除答题题目
    public final void delectSub(String examType,String subClass,String type) {
        this.open(this.ctx);
        try {
            if("subseq".equals(type)){
                this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and seq_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,"0"});
            }else if("subclass".equals(type)){
                this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and class_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,subClass});
            }else if("subchapter".equals(type)){
                if(subClass.equals("110")||subClass.equals("210")){
                    this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and chapter_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,subClass+"-"+((BaseActivity) ctx).getUserInfo(8)});
                }else{
                    this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and chapter_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,subClass});
                }
            }else if("subvip".equals(type)){
                this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and vip_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,subClass});
            }else if("subnanti".equals(type)){
                this.mDb.delete("qc_sub_answer", "user_id=? and car_id=? and sub_type=? and seq_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1),  ((BaseActivity) ctx).getUserInfo(5), examType,"1"});
            }
        } catch (Exception e) {

        } finally {
            this.close();
        }
    }

    public final boolean selectCollectSub(Boolean flag,String subId,String sub_type) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            if(flag){
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_collect where user_id=? and sub_id=? and car_id=? and sub_type=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId,((BaseActivity) ctx).getUserInfo(5),sub_type});
            }else{
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_error where user_id=? and sub_id=? and car_id=? and sub_type=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId,((BaseActivity) ctx).getUserInfo(5),sub_type});
            }
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }
    //查询收藏表中 按章节分类的数量
    public final List<String> selectCollectChapter(Boolean flag,String sub_type) {
        Cursor cursor = null;
        try {
            List<String> list=new ArrayList<String>();
            this.open(this.ctx);
            if(flag){
                cursor = this.mDb.rawQuery("select chapter_answer,count(1) as a from qc_sub_collect where user_id=?  and car_id=? and sub_type=? group by chapter_answer", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type});
            }else{
                cursor = this.mDb.rawQuery("select chapter_answer,count(1) as a from qc_sub_error where user_id=?  and car_id=? and sub_type=? group by chapter_answer", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type});
            }
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("chapter_answer"))+"-"+
                        cursor.getString(cursor.getColumnIndex("a")));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<String>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询收藏表中 按章节分类的题目id
    public final List<String> selectCollectChapterSubid(Boolean flag,String sub_chapter,String sub_type) {
        Cursor cursor = null;
        try {
            List<String> list=new ArrayList<String>();
            this.open(this.ctx);
            if(flag){
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_collect where user_id=?  and car_id=? and sub_type=? and chapter_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type,sub_chapter});
            }else{
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_error where user_id=?  and car_id=? and sub_type=? and chapter_answer=?", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type,sub_chapter});
            }
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("sub_id")));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<String>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询收藏表中 全部收藏题目id
    public final List<String> selectCollectAllSubid(Boolean flag,String sub_type) {
        Cursor cursor = null;
        try {
            List<String> list=new ArrayList<String>();
            this.open(this.ctx);
            if(flag){
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_collect where user_id=?  and car_id=? and sub_type=? ", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type});
            }else{
                cursor = this.mDb.rawQuery("select sub_id from qc_sub_error where user_id=?  and car_id=? and sub_type=? ", new String[]{((BaseActivity) ctx).getUserInfo(1), ((BaseActivity) ctx).getUserInfo(5),sub_type});
            }
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("sub_id")));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<String>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询一整套题错题对题状态
    public final List<SubjectSelect> queryWholeSub(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        List<SubjectSelect> subjectSelectList=new ArrayList<SubjectSelect>();
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id,answer_status from qc_sub_answer where user_id=?  and car_id=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and error_answer=? and sub_type=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),((BaseActivity)ctx).getUserInfo(5),subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getErrorAnswer(),subjectSelect.getSubType()+""});
            while (cursor.moveToNext()) {
                SubjectSelect subjectSelect1=new SubjectSelect();
                subjectSelect1.setSubId(cursor.getInt(cursor.getColumnIndex("sub_id")));
                subjectSelect1.setAnswerStatus(cursor.getInt(cursor.getColumnIndex("answer_status")));
                subjectSelectList.add(subjectSelect1);
            }
            return subjectSelectList;
        } catch (Exception e) {
            return subjectSelectList;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询未做题目
    public final List<String> queryNodoneSub(SubjectSelect subjectSelect) {
        Cursor cursor = null;
        List<String> list=new ArrayList<String>();
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select distinct sub_id from qc_sub_answer where user_id=? and sub_type=? and car_id=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),subjectSelect.getSubType()+"",((BaseActivity)ctx).getUserInfo(5)});
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("sub_id")));
            }
            return list;
        } catch (Exception e) {
            return list;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //添加考试记录
    public final void addExamScore(ExamScore examScore,String fragmentType) {
        try {
            this.open(this.ctx);
            this.mDb.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("user_id", ((BaseActivity)ctx).getUserInfo(1));
            values.put("time", examScore.getTime());
            values.put("score", examScore.getScore());
            values.put("date",examScore.getDate());
            values.put("answer", examScore.getAnswer());
            values.put("subs",examScore.getSubs());
            values.put("sub_type", fragmentType);

            this.mDb.insert("exam_result", null, values);
            this.mDb.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            try {
                this.mDb.endTransaction();
            } catch (Exception e) {

            }
            this.close();
        }
    }

    //查询考试记录
    public final List<ExamScore> selectExamScore(String fragmentType) {
        Cursor cursor = null;
        try {
            List<ExamScore> list=new ArrayList<ExamScore>();
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select * from exam_result where user_id=? and sub_type=? order by date desc", new String[]{((BaseActivity) ctx).getUserInfo(1),fragmentType});
            while (cursor.moveToNext()) {
                ExamScore examScore=new ExamScore();
                examScore.setTime(cursor.getString(cursor.getColumnIndex("time")));
                Log.e("eeee", cursor.getString(cursor.getColumnIndex("time")));
                examScore.setScore(cursor.getString(cursor.getColumnIndex("score")));
                examScore.setDate(cursor.getString(cursor.getColumnIndex("date")));
                examScore.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                examScore.setSubs(cursor.getString(cursor.getColumnIndex("subs")));
                list.add(examScore);
            }
            return list;
        } catch (Exception e) {
            Log.e("eee",e.toString());
            return new ArrayList<ExamScore>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询考试成绩(最高分)
    public final int selectExamScoreMax(String fragmentType) {
        Cursor cursor = null;
        try {
            int max=0;
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select * from exam_result where user_id=? and sub_type=? order by score desc limit 1", new String[]{((BaseActivity) ctx).getUserInfo(1), fragmentType});
            if(cursor.getCount()==0){
                return -1;
            }
            while (cursor.moveToNext()) {
                max=Integer.parseInt(cursor.getString(cursor.getColumnIndex("score")));
            }
            return max;
        } catch (Exception e) {
            Log.e("eee",e.toString());
            return -1;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

    //查询顺序答题位置
    public final int selectPosition(String fragmentType) {
        Cursor cursor = null;
        try {
            int position=0;
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select * from exam_position where user_id=? and sub_type=? and car_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), fragmentType,((BaseActivity) ctx).getUserInfo(5)});
            if(cursor.getCount()==0){
                return -1;
            }
            while (cursor.moveToNext()) {
                position=Integer.parseInt(cursor.getString(cursor.getColumnIndex("position")));
            }
            return position;
        } catch (Exception e) {
            return -1;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }


    //添加顺序答题位置
    public final void addPosition(String fragmentType,String position) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select * from exam_position where user_id=? and sub_type=? and car_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), fragmentType,((BaseActivity) ctx).getUserInfo(5)});
            this.mDb.beginTransaction();
            ContentValues values = new ContentValues();
            if(cursor.getCount()==0){
                values.put("user_id", ((BaseActivity)ctx).getUserInfo(1));
                values.put("car_id", ((BaseActivity)ctx).getUserInfo(5));
                values.put("position", position);
                values.put("sub_type", fragmentType);
                this.mDb.insert("exam_position", null, values);
            }else{
                values.put("position", position);
                this.mDb.update("exam_position", values, "user_id=? and sub_type=? and car_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), fragmentType,((BaseActivity) ctx).getUserInfo(5)});
            }
            this.mDb.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            try {
                this.mDb.endTransaction();
            } catch (Exception e) {

            }
            this.close();
        }
    }
}