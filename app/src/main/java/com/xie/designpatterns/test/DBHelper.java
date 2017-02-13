package com.xie.designpatterns.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * des:
 * author: marc
 * date:  2017/1/21 10:00
 * email：aliali_ha@yeah.net
 */

public class DBHelper extends SQLiteOpenHelper{
    //数据库名
    public final static String DATABASENAME = "diaryOpenHelper.db";
    //版本
    public final static int DATABASESERVERSION=1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "create table diary (_id integer primary key autoincrement,topic varchar(100) , content varchar(1000) )";
        db.execSQL(sql);
    }

    //版本库有变化，则调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists diary";
        db.execSQL(sql);
        this.onCreate(db);
    }
}
