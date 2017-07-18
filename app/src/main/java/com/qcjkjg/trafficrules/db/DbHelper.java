package com.qcjkjg.trafficrules.db;

import android.content.Context;
import com.qcjkjg.trafficrules.InitApp;

/**
 * Created by zongshuo on 2017/7/18 0018.
 */
public class DbHelper extends AbstractDatabaseHelper {

    private static final String TAG = "DbHelper";
    private static final int DB_VERSION = 1;

    private static final String[] CREATE_SQLS = {
            "CREATE TABLE IF NOT EXISTS [sci_news] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [news_id] INTEGER NOT NULL, " +
                    "  [news_type] TINYINT NOT NULL, " +
                    "  [title] VARCHAR(1024) NOT NULL, " +
                    "  [scc_id] INTEGER NOT NULL DEFAULT 0, " +//栏目id
                    "  [class_id] INTEGER, " +//产品id
                    "  [class_name] VARCHAR(256), " +//产品名称
                    "  [sub_col_name] VARCHAR(128), " +//二级栏目名称
                    "  [pub_time_stamp] INTEGER, " +
                    "  [read_status] TINYINT NOT NULL, " +
                    "  [create_time] INTEGER NOT NULL, " +
                    "  [news_content] TEXT, " +
                    "  [collect] VARCHAR(32) NOT NULL DEFAULT '0', " +
                    "  [zan] VARCHAR(32) NOT NULL DEFAULT '0', " +
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([news_id], [scc_id], [news_type],[class_id]) ON CONFLICT IGNORE);"//TODO 重设约束条件
    };

    private static final String[] UPDATE_SQLS = {
            "drop table if exists Mobile_news;",
            "drop table if exists Mobile_news_class;",
            "drop table if exists Mobile_news_fav;",
            "drop table if exists Mobile_channel_fav;",
            "drop table if exists Mobile_product_category;",
            "drop table if exists Mobile_child_product_list",

            "drop table if exists sci_news;",

            "CREATE TABLE IF NOT EXISTS [sci_news] (" +
                    "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, " +
                    "  [news_id] INTEGER NOT NULL, " +
                    "  [news_type] TINYINT NOT NULL, " +
                    "  [title] VARCHAR(1024) NOT NULL, " +
                    "  [scc_id] INTEGER NOT NULL DEFAULT 0, " +//栏目id
                    "  [class_id] INTEGER, " +//产品id
                    "  [class_name] VARCHAR(256), " +//产品名称
                    "  [sub_col_name] VARCHAR(128), " +//二级栏目名称
                    "  [pub_time_stamp] INTEGER, " +
                    "  [read_status] TINYINT NOT NULL, " +
                    "  [create_time] INTEGER NOT NULL, " +
                    "  [news_content] TEXT, " +
                    "  [collect] VARCHAR(32) NOT NULL DEFAULT '0', " +
                    "  [zan] VARCHAR(32) NOT NULL DEFAULT '0', " +
                    "  CONSTRAINT [Constraint_On_Unique_News] UNIQUE([news_id], [scc_id], [news_type],[class_id]) ON CONFLICT IGNORE);"//TODO 重设约束条件
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


}