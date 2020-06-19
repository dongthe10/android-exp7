package com.example.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    //创建book table sql语句
    public static final String CREATE_BOOK= "create table Book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text," +
            "category_id integer" +
            ")";

    //创建目录表sql语句
    public static final String CREATE_CATAGORY= "create table Category (" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";


    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行sql语句
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATAGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //加了会清空表的数据
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Cataogry");
        onCreate(db);
    }
}
