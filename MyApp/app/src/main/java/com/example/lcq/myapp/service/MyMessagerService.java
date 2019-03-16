package com.example.lcq.myapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyMessagerService extends Service {
    public static final String TAG = "MyMessengerService";

    public static final int MSG_FROM_CLIENT_TO_SERVER = 1;
    public static final int MSG_FROM_SERVER_TO_CLIENT = 2;

    private Messenger mClientMessager;
    private Messenger mSericeMessager = new Messenger(new ServerBinder());

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind");
        return mSericeMessager.getBinder();
    }

    public class ServerBinder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.w(TAG, "thread name:" + Thread.currentThread().getName());
            switch (msg.what) {
                case MSG_FROM_CLIENT_TO_SERVER:
                    mClientMessager = msg.replyTo;
                        Message toClinetMsg = Message.obtain(null,MSG_FROM_SERVER_TO_CLIENT);
                    try {
                        Log.w(TAG, "server begin send msg to client");
                        Toast.makeText(MyMessagerService.this,"从Client 传过来的消息",Toast.LENGTH_SHORT).show();
                        mClientMessager.send(toClinetMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                        break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }
}
