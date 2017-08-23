package com.qcjkjg.trafficrules.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.activity.BaseActivity;
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
                    "  [answer_num] INTEGER DEFAULT 0, " +//答过次数
                    "  [error_num] INTEGER DEFAULT 0, " +//答错次数 1是答错 0是默认
                    "  [seq_answer] varchar(10) DEFAULT '-1', " +//顺序
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  [class_answer] varchar(10) DEFAULT '-1', " +//专属
                    "  [vip_answer] varchar(10) DEFAULT '-1', " +//vip
                    "  [top_answer] varchar(10) DEFAULT '-1', " +//top
                    "  [collect_answer] varchar(10) DEFAULT '-1', " +//收藏
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [answer_status] INTEGER DEFAULT 0, " +//0:未作答1:正确2:错误
                    "  [answer_choice] varchar(10))", //答题结果

            "CREATE TABLE IF NOT EXISTS [qc_sub_collect] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id]) ON CONFLICT IGNORE);"

    };

    private static final String[] UPDATE_SQLS = {
            "drop table if exists qc_sub_answer;",
            "drop table if exists qc_sub_collect;",

            "CREATE TABLE IF NOT EXISTS [qc_sub_answer] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  [answer_num] INTEGER DEFAULT 0, " +//答过次数
                    "  [error_num] INTEGER DEFAULT 0, " +//答错次数 1是答错 0是默认
                    "  [seq_answer] varchar(10) DEFAULT '-1', " +//顺序
                    "  [chapter_answer] varchar(10) DEFAULT '-1', " +//章节
                    "  [class_answer] varchar(10) DEFAULT '-1', " +//专属
                    "  [vip_answer] varchar(10) DEFAULT '-1', " +//vip
                    "  [top_answer] varchar(10) DEFAULT '-1', " +//top
                    "  [collect_answer] varchar(10) DEFAULT '-1', " +//收藏
                    "  [sub_type] INTEGER DEFAULT 1, " +//科目
                    "  [answer_status] INTEGER DEFAULT 0, " +//0:未作答1:正确2:错误
                    "  [answer_choice] varchar(10))", //答题结果

            "CREATE TABLE IF NOT EXISTS [qc_sub_collect] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [user_id] varchar(10) , " +//用户id
                    "  [sub_id] INTEGER , " +//题目id
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([user_id], [sub_id]) ON CONFLICT IGNORE);"
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
            values.put("answer_num",subjectSelect.getAnswerNum());
            values.put("error_num", subjectSelect.getErrorNum());
            values.put("seq_answer",subjectSelect.getSeqAnswer());
            values.put("chapter_answer", subjectSelect.getChapterAnswer());
            values.put("class_answer", subjectSelect.getClassAnswer());
            values.put("vip_answer", subjectSelect.getVipAnswer());
            values.put("top_answer", subjectSelect.getTopAnswer());
            values.put("collect_answer", subjectSelect.getCollectAnswer());
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
            cursor = this.mDb.rawQuery("select * from qc_sub_answer where user_id=? and sub_id=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and sub_type=? order by sub_id", new String[]{subjectSelect.getUserId(),subjectSelect.getSubId()+"",subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getSubType()+""});
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
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and sub_id=? order by sub_id", new String[]{subjectSelect.getUserId(),subjectSelect.getSubId()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return cursor.getCount();
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
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and sub_id=? and error_num=? order by sub_id", new String[]{subjectSelect.getUserId(),subjectSelect.getSubId()+"",subjectSelect.getErrorNum()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return cursor.getCount();
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
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_answer where user_id=? and answer_status=? and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and sub_type=? order by sub_id", new String[]{((BaseActivity)ctx).getUserInfo(1),subjectSelect.getAnswerStatus()+"",subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getSubType()+""});
            while (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndex("sub_id"));
            }
            return cursor.getCount();
        } catch (Exception e) {
            return cursor.getCount();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            this.close();
        }
    }

//    public final void addCollectSub(String subId){
//        try {
//            this.open(this.ctx);
//            this.mDb.beginTransaction();
//            ContentValues values = new ContentValues();
//            values.put("user_id", ((BaseActivity)ctx).getUserInfo(1));
//            values.put("sub_id",subId);
//            this.mDb.insert("qc_sub_collect", null, values);
//            this.mDb.setTransactionSuccessful();
//        } catch (Exception e) {
//
//        } finally {
//            try {
//                this.mDb.endTransaction();
//            } catch (Exception e) {
//
//            }
//            this.close();
//        }
//    }
//
//    public final void delectCollectSub(String subId) {
//        this.open(this.ctx);
//        try {
//            this.mDb.delete("qc_sub_collect", "user_id=? and sub_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId});
//        } catch (Exception e) {
//
//        } finally {
//            this.close();
//        }
//    }
//
//    public final boolean selectCollectSub(String subId) {
//        Cursor cursor = null;
//        try {
//            this.open(this.ctx);
//            cursor = this.mDb.rawQuery("select sub_id from qc_sub_collect where user_id=? and sub_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId});
//            return cursor.moveToFirst();
//        } catch (Exception e) {
//            return cursor.moveToFirst();
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//            this.close();
//        }
//    }
    public final void addCollectSub(String subId){
        try {
            this.open(this.ctx);
            this.mDb.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("user_id", ((BaseActivity)ctx).getUserInfo(1));
            values.put("sub_id",subId);
            this.mDb.insert("qc_sub_collect", null, values);
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

    public final void delectCollectSub(String subId) {
        this.open(this.ctx);
        try {
            this.mDb.delete("qc_sub_collect", "user_id=? and sub_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId});
        } catch (Exception e) {

        } finally {
            this.close();
        }
    }

    public final boolean selectCollectSub(String subId) {
        Cursor cursor = null;
        try {
            this.open(this.ctx);
            cursor = this.mDb.rawQuery("select sub_id from qc_sub_collect where user_id=? and sub_id=?", new String[]{((BaseActivity) ctx).getUserInfo(1), subId});
            return cursor.moveToFirst();
        } catch (Exception e) {
            return cursor.moveToFirst();
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
            cursor = this.mDb.rawQuery("select sub_id,answer_status from qc_sub_answer where user_id=?  and seq_answer=? and chapter_answer=? and class_answer=? and vip_answer=? and top_answer=? and collect_answer=? and sub_type=? order by sub_id", new String[]{subjectSelect.getUserId(),subjectSelect.getSeqAnswer(),subjectSelect.getChapterAnswer(),subjectSelect.getClassAnswer(),subjectSelect.getVipAnswer(),subjectSelect.getCollectAnswer(),subjectSelect.getTopAnswer(),subjectSelect.getSubType()+""});
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
            cursor = this.mDb.rawQuery("select distinct sub_id from qc_sub_answer where user_id=? and sub_type=? order by sub_id", new String[]{subjectSelect.getUserId(),subjectSelect.getSubType()+""});
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
}