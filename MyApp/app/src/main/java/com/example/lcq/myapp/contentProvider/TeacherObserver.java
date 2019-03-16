package com.example.lcq.myapp.contentProvider;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TeacherObserver extends ContentObserver {
    public static final String TAG = "TeacherObserver";
    private Handler handler;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public TeacherObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(TAG, "data changed, try to requery.");
        //向handler发送消息,更新查询记录
        Message msg = new Message();
        handler.sendMessage(msg);
    }
}
