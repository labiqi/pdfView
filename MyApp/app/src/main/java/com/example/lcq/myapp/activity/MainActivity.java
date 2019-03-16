package com.example.lcq.myapp.activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.base.BaseActivity;
import com.example.lcq.myapp.constant.Constants;
import com.example.lcq.myapp.service.MyService;

public class MainActivity extends BaseActivity {

    private static final  String tag ="MainActivity";
    private boolean landscape = false;
    private Intent intentService;
    private ServiceConnection sc = new MyServiceConnection();
    private MyService.MyBinder myBinder;
    private MyService myService;
    private boolean mBound;

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w(tag, "in MyServiceConnection onServiceConnected");
            myBinder = (MyService.MyBinder) service;
            myService = myBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(tag, "in MyServiceConnection onServiceDisConnected");
            mBound = false;
        }
    }

    @Override
    protected boolean initData() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    /*
      页面跳转
    */

    public void onClick(View view) {
        int count =0,dd = 100;
       for(int i=0;i<10;i++) {
           if(i ==0 || i== 2) {
               continue;
           }
           for(int j=0;j<10;j++) {
               if((5-i) == (j+3)) {
                   break;
               }
           }
           count ++;
           dd--;
       }
        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);
    }

    /*
      打电话
    */

    public void call(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(("tel:10086")));
        startActivity(intent);
    }


   /*
      横竖屏切换
    */

    public void transfer(View view){
        int orientation = this.getResources().getConfiguration().orientation;
        if(landscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            landscape = !landscape;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            landscape = !landscape;
        }
    }


    /*
      开始service
    */

    public void startService(View view) {
        intentService = new Intent(MainActivity.this,MyService.class);
        intentService.putExtra("name","我是 service！");
        startService(intentService);
    }

    /*
      停止service
    */

    public void stopService(View view) {
        stopService(intentService);
    }

    /*
      绑定service
    */

    public void bind(View view) {
        Intent intent = new Intent(MainActivity.this,MyService.class);
        bindService(intent,sc,Context.BIND_AUTO_CREATE);
    }

    /*
      解绑service
    */

    public void unbind(View view) {
        excuteUnbindService();
    }


    /**
      跳转到pdf阅读
    */
    public void onPdfView(View view) {
        Intent intent = new Intent(MainActivity.this,PdfActivity.class);
        intent.putExtra(Constants.PDF_FROM_KEY,2);
        intent.putExtra(Constants.PDF_FROM_ADDRESS_KEY,"http://pdf.dfcfw.com/pdf/H3_AP201704010460506701_1.pdf");
        startActivity(intent);
    }

    private void excuteUnbindService() {
        if (mBound) {
            unbindService(sc);
            mBound = false;
        }
    }


    public void goWebview(View view){
        startActivity(new Intent(MainActivity.this,WebviewActivity.class));

    }


    public void goViewPagerWebview(View view) {
        startActivity(new Intent(MainActivity.this,TabLayoutActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(tag,"onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(tag,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(tag,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(tag,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(tag,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(tag,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(tag,"onDestroy");
    }
}
