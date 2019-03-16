package com.example.lcq.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

public class MyService extends Service {
    private static final  String tag = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(tag,"in onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(tag, "in onStartCommand");
        String name = intent.getStringExtra("name");
        Log.w(tag, "name:" + name);
        return START_STICKY;
    }



    private MyBinder myBinder = new MyBinder();

    public class MyBinder extends Binder {

         public MyService getService() {
             Log.e(tag,"getService");
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(tag,"in onBind");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(tag,"unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(tag,"onDestroy");
        super.onDestroy();
    }
}
