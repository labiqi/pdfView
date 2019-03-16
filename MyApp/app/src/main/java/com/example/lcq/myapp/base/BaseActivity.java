package com.example.lcq.myapp.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.widgets.util.SimpleObservable;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseActivity extends Activity implements Observer {
    private final SimpleObservable observable = new SimpleObservable();
//    private AppProgressDialog progressDialog;
    protected DisplayMetrics metrics;
    protected TextView tvTitle;
     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     requestWindowFeature(Window.FEATURE_NO_TITLE);
     if(!initData()) {
        return;
     }
     setContentView(layoutId());
     initView();
     setAnalytics();
    }


    /**
     * 数据埋点
     */
    protected void setAnalytics(){
    }


    /**
     * 初始化页面有关数据
     * @return 是否初始化成功
     */
    protected abstract boolean initData();

    /**
     * 添加页面布局
     * @return 布局ID
     */
    protected abstract int layoutId();


    protected void initView() {
        initPageBg();
        initNavigationBar();
        metrics = getResources().getDisplayMetrics();
    }

        private void initPageBg() {
        View view = findViewById(R.id.v_content);
//        view.setBackgroundResource(R.color._6a788f);
    }


    /**
     * 初始化导航栏
     */
    private void initNavigationBar() {
        //设置导航栏
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
//        ibBack.setImageResource(ResourceManager.getResourceId(ResourceKeys.CO_IB_BACK_RES));
        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvTitle.setTextColor(ResourceManager.getColorValue(ResourceKeys.CO_TITLE_TEXT_COLOR));
    }





    /**
     * 统一自定义toast
     * @param sequence 文案
     */
    protected void toast(boolean warn, CharSequence sequence) {
//        if (toast == null) {
//            toast = new Toast(this);
//            View view = getLayoutInflater().inflate(R.layout.co_toast, null);
//            tvToastMessage = (TextView) view.findViewById(R.id.tv_message);
//            tvToastMessage.setTextColor(ResourceManager.getColorValue(ResourceKeys.CO_TOAST_TEXT_COLOR));
//            tvToastMessage.setBackgroundResource(ResourceManager.getResourceId(ResourceKeys.CO_TOAST_BG));
//            toast.setView(view);
//            int yOffset = 154 * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
//            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, yOffset);
//            toast.setDuration(Toast.LENGTH_SHORT);
//        }
//        tvToastMessage.setText(sequence);
//        int resId;
//        if (warn) {
//            resId = ResourceManager.getResourceId(ResourceKeys.CO_TOAST_ICON_WARN);
//        } else {
//            resId = ResourceManager.getResourceId(ResourceKeys.CO_TOAST_ICON_SUCCESS);
//        }
//        tvToastMessage.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
//        toast.show();
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * 顶部导航返回按钮的点击
     *
     * @param view
     */
    public void onBack(View view) {
        finish();
    }

    protected void toast(CharSequence sequence) {
        Display display = getWindowManager().getDefaultDisplay();
        // 获取屏幕高度
        int height = display.getHeight();
        Toast toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        toast.setText(sequence);
        toast.setGravity(Gravity.BOTTOM, 0, height / 4);
        toast.show();
    }

    /**
     * 添加观察者
     *
     * @param observer
     */
    protected void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    /**
     * 通知观察者
     *
     * @param object
     */
    protected void notifyObservers(Object object) {
        observable.notifyObservers(object);
    }


}
