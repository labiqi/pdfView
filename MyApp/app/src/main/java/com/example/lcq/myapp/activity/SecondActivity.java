package com.example.lcq.myapp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.base.BaseActivity;
import com.example.lcq.myapp.service.MyMessagerService;

import java.util.List;

public class SecondActivity extends BaseActivity {
    private static final String tag = "SecondActivity";
    private boolean mBound;
    private Messenger mServerMessager;
    private Handler mClientHander = new MyClientHandler();
        private Messenger mClientMessager = new Messenger(mClientHander);

    class MyClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MyMessagerService.MSG_FROM_SERVER_TO_CLIENT) {
                Log.e(tag, "receive msg from server");
                Toast.makeText(SecondActivity.this, "从server 传过来的消息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(tag, "onServiceConnected");
            mServerMessager = new Messenger(service);
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void bindService(View view) {
        Log.e(tag, "bindService");
        Intent intent = new Intent(SecondActivity.this, MyMessagerService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        Log.e(tag, "unbindService");
        excuteUnbindService();
    }

    public void sendMessager(View view) {
        Log.e(tag, "sendMessager");
        sendMsg();
    }

    private void sendMsg() {
        if (!mBound) {
            return;
        }
        Message msg = Message.obtain(null, MyMessagerService.MSG_FROM_CLIENT_TO_SERVER, 0, 0);
        msg.replyTo = mClientMessager;
        try {
            mServerMessager.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void excuteUnbindService() {
        if (mBound) {
            unbindService(sc);
            mBound = false;
        }
    }

    @Override
    protected boolean initData() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.second_activity;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(tag, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(tag, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(tag, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(tag, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(tag, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(tag, "onDestroy");
    }

    public void nextPage(View view){
        ActivityManager am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);


        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        String name = componentInfo.getClassName();
        String bb = SecondActivity.class.getName();
        if(name.equals(bb)){
            finish();
//            componentInfo.finsh();
        }

        startActivity(new Intent(SecondActivity.this,ThirdActivity.class));
    }


    class MyIntentService extends IntentService {

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public MyIntentService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            //TODO 在这里执行耗时操作
        }
    }
}
