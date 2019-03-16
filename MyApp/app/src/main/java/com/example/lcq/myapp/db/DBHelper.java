package com.example.lcq.myapp.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
    DB
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String tag = "DBHelper";
    public DBHelper( Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper( Context context,  String name, SQLiteDatabase.CursorFactory factory, int version,  DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context, String databaseName, int databaseVersion) {
        this(context,databaseName,null,databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(tag,"create table");
        db.execSQL("create table " + ContentData.UserTableData.TABLE_NAME
                + "(" + ContentData.UserTableData._ID
                + " INTEGER PRIMARY KEY autoincrement,"
                + ContentData.UserTableData.NAME + " varchar(20),"
                + ContentData.UserTableData.TITLE + " varchar(20),"
                + ContentData.UserTableData.DATE_ADDED + " long,"
                + ContentData.UserTableData.SEX + " boolean)" + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
