package com.example.lcq.myapp.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.base.BaseActivity;
import com.example.lcq.myapp.contentProvider.TeacherObserver;
import com.example.lcq.myapp.db.ContentData;

import java.util.Date;

public class ThirdActivity extends BaseActivity implements View.OnClickListener {
    private static final String tag = "ThirdActivity";
    private int count = 0;
    private boolean sex = false;
    Button insert;
    Button query;
    Button update;
    Button delete;
    Button querys;
    Button deleteAll;
    Uri uri = Uri.parse("content://com.example.lcq.myapp.contentProvider/teacher");

    @Override
    protected boolean initData() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.third_layout;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //update records.
            requery();
        };
    };


    @Override
    protected void initView() {
        super.initView();
        insert = findViewById(R.id.insert);
        query = findViewById(R.id.query);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        deleteAll = findViewById(R.id.delete_all);
        querys = findViewById(R.id.querys);
        insert.setOnClickListener(this);
        query.setOnClickListener(this);
        querys.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        //注册变化通知
        getContentResolver().registerContentObserver(ContentData.UserTableData.CONTENT_URI,true,new TeacherObserver(handler));
    }

    @Override
    public void onClick(View v) {
        ContentResolver cr = getContentResolver();
        Cursor c;
        ContentValues cv = new ContentValues();
        switch (v.getId()) {
            case R.id.insert:
                cv.put("title", "教书"+count);
                cv.put("name", "教师"+count);
                cv.put("sex", !sex);
                Uri uri2 = cr.insert(uri, cv);
                Log.e(tag, uri2.toString());
                Toast.makeText(this, "插入的内容是： "+uri2.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.query:
                // 查找id为1的数据
                c = cr.query(uri, null, "_ID=?", new String[]{"1"}, null);
                //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
                if (c.moveToFirst() == false) {
                    // 为空的Cursor
                    return;
                }
                int name = c.getColumnIndex("name");
                System.out.println(c.getString(name));
                Toast.makeText(this, c.getString(name), Toast.LENGTH_SHORT).show();
                c.close();
                break;
            case R.id.update:
                cv.put("name", "曾老师");
                cv.put("date_added", (new Date()).toString());
                int uri3 = cr.update(uri, cv, "_ID=?", new String[]{"3"});
                System.out.println("updated" + ":" + uri3);
                Toast.makeText(this, "updated" + ":" + uri3, Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                cr.delete(uri, "_ID=?", new String[]{"2"});
                break;
            case R.id.querys:
                // 查找id为1的数据
                c = cr.query(uri, null, null, null, null);
                Log.e(tag, String.valueOf(c.getCount()));
                Toast.makeText(this, String.valueOf(c.getCount()), Toast.LENGTH_SHORT).show();
                c.close();
                break;
            case R.id.delete_all:
                cr.delete(uri,null,null);
                break;
            default:
                break;
        }

    }

    /**
     * 重新查询
     */
    private void requery() {
        //实际操作中可以查询集合信息后Adapter.notifyDataSetChanged();
//        query(null);
    }
}
