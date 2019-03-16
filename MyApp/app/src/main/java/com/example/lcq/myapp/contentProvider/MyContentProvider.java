package com.example.lcq.myapp.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.example.lcq.myapp.db.ContentData;
import com.example.lcq.myapp.db.DBHelper;

import static com.example.lcq.myapp.db.ContentData.UserTableData.CONTENT_TYPE;
import static com.example.lcq.myapp.db.ContentData.UserTableData.CONTENT_TYPE_ITME;
import static com.example.lcq.myapp.db.ContentData.UserTableData.TEACHER;
import static com.example.lcq.myapp.db.ContentData.UserTableData.TEACHERS;

public class MyContentProvider extends ContentProvider {

    private DBHelper dbOpenHelper = null;
    private static UriMatcher uriMatcher;

    static {
        // 常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 如果match()方法匹配content://com.example.lcq.myapp.contentProvider/teacher n路径,返回匹配码为TEACHERS
        uriMatcher.addURI(ContentData.AUTHORITY, "teacher", TEACHERS);

        // 如果match()方法匹配content://com.example.lcq.myapp.contentProvider/teacher/230,路径，返回匹配码为TEACHER
        uriMatcher.addURI(ContentData.AUTHORITY, "teacher/#", TEACHER);
    }

    @Override
    public boolean onCreate() {
        //这里会调用 DBOpenHelper的构造函数创建一个数据库；
        dbOpenHelper = new DBHelper(this.getContext(), ContentData.DATABASE_NAME, ContentData.DATABASE_VERSION);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                return CONTENT_TYPE;
            case TEACHER:
                return CONTENT_TYPE_ITME;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                return db.query("teacher", projection, selection, selectionArgs, null, null, sortOrder);
            case TEACHER:
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid;// 获取指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
                return db.query("teacher", projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * 当执行这个方法的时候，如果没有数据库，他会创建，同时也会创建表，但是如果没有表，下面在执行insert的时候就会出错
     * 这里的插入数据也完全可以用sql语句书写，然后调用 db.execSQL(sql)执行。
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //多线程加类锁
        synchronized (MyContentProvider.class) {
            //获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建；
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            long id = 0;
            int uu = uriMatcher.match(uri);
            switch (uriMatcher.match(uri)) {
                case TEACHERS:
                    id = db.insert("teacher", null, values);    // 返回的是记录的行号，主键为int，实际上就是主键值
                    if(id > 0) {
                        notifyChanged();
                    }
                    return ContentUris.withAppendedId(uri, id);
                case TEACHER:
                    id = db.insert("teacher", null, values);
                    if(id > 0 ) {
                        notifyChanged();
                    }
                    String path = uri.toString();
                    return Uri.parse(path.substring(0, path.lastIndexOf("/")) + id); // 替换掉id
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    //通知指定URI数据已改变
    private void notifyChanged() {
        getContext().getContentResolver().notifyChange(ContentData.UserTableData.CONTENT_URI,null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //多线程加类锁
        synchronized (MyContentProvider.class) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case TEACHERS:
                    count = db.delete("teacher", selection, selectionArgs);
                    if(count > 0 ) {
                        notifyChanged();
                    }
                    break;
                case TEACHER:
                    // 下面的方法用于从URI中解析出id，对这样的路径content://hb.android.teacherProvider/teacher/10
                    // 进行解析，返回值为10
                    long personid = ContentUris.parseId(uri);
                    String where = "_ID=" + personid;    // 删除指定id的记录
                    where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";    // 把其它条件附加上
                    count = db.delete("teacher", where, selectionArgs);
                    if(count > 0 ) {
                        notifyChanged();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            db.close();
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //多线程加类锁
        synchronized (MyContentProvider.class) {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case TEACHERS:
                    count = db.update("teacher", values, selection, selectionArgs);
                    if(count > 0 ) {
                        notifyChanged();
                    }
                    break;
                case TEACHER:
                    // 下面的方法用于从URI中解析出id，对这样的路径content://com.ljq.provider.personprovider/person/10
                    // 进行解析，返回值为10
                    long personid = ContentUris.parseId(uri);
                    String where = "_ID=" + personid;// 获取指定id的记录
                    where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
                    count = db.update("teacher", values, where, selectionArgs);
                    if(count > 0 ) {
                        notifyChanged();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            db.close();
            return count;
        }
    }
}
