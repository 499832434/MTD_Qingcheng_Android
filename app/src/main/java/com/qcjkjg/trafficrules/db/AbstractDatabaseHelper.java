package com.qcjkjg.trafficrules.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.qcjkjg.trafficrules.InitApp;


/**
 * Created by Administrator on 2017/7/18 0018.
 */
public abstract class AbstractDatabaseHelper {

    protected SQLiteDatabase mDb = null;

    private static InnerDBHelper mDbHelper = null;

    protected abstract String getTag();

    protected abstract String getDatabaseName();

    protected abstract int getDatabaseVersion();

    protected abstract String[] createDBTables();

    protected abstract String[] updateDBTables();

    public static String Lock = "dblock";


    /**
     * 打开或者创建一个指定名称的数据库
     *
     * @param ctx
     */
    public void open(Context ctx) {
//        Log.e(getTag(), "Open database '" + getDatabaseName() + "'");
        synchronized(Lock) {
            if(mDbHelper == null) mDbHelper = new InnerDBHelper(ctx);
            try {
                mDb = mDbHelper.getWritableDatabase();
            } catch (Exception e) {

            }
        }
    }

    /**
     * 关闭数据库
     */
    public void close() {
        try {
            if (mDbHelper != null) {
//                Log.e(getTag(), "Close database '" + getDatabaseName() + "'");
                mDbHelper.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 数据库帮助类
     */
    private class InnerDBHelper extends SQLiteOpenHelper {


        public InnerDBHelper(Context ctx) {
            super(ctx, InitApp.DB_NAME, null, getDatabaseVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            Log.e(getTag(), "creating database " + InitApp.DB_NAME);
            String[] createSql = createDBTables();
            if (createSql.length > 0) executeBatch(createDBTables(), db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.e(getTag(), InitApp.DB_NAME + "Upgrading database '" + "' from version " + oldVersion + " to " + newVersion);
            String[] dropSql = updateDBTables();
            if (dropSql.length > 0) {
                executeBatch(updateDBTables(), db);
            }
        }

        private void executeBatch(String[] sqls, SQLiteDatabase db) {
            if (sqls == null) return;
            db.beginTransaction();
            try {
                int len = sqls.length;
                for (int i = 0; i < len; i++) {
                    db.execSQL(sqls[i]);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {

            } finally {
                db.endTransaction();
            }
        }
    }
}
