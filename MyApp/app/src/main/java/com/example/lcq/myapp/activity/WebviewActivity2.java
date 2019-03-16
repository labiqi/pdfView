package com.example.lcq.myapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;

import com.example.lcq.myapp.R;

public class WebviewActivity2 extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private String[] table = new String[]{"加载1","加载2","加载3","加载4"};
    private TableLayout tableLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout2);
        initView();
    }

    private void initView() {
//        tableLayout = findViewById(R.id.table_layout);
        viewPager = findViewById(R.id.view_pager);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    class WebviewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return false;
        }
    }
}
