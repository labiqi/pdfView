package com.example.lcq.myapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.example.lcq.myapp.R;
import com.example.lcq.myapp.widgets.PagerAdapter;
import com.example.lcq.myapp.widgets.TabFragment;

import java.util.ArrayList;

public class TabLayoutActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] table = new String[]{"新闻","公告","研报","最新"};
    private String[] url = new String[]{"https://blog.csdn.net/yaojie5519/article/details/82344838",
    "https://blog.csdn.net/xyq046463/article/details/51769728","https://blog.csdn.net/qq_20280683/article/details/77964208"
    ,"https://zhuanlan.zhihu.com/p/25213586"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_layout);
        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        for(int i=0;i<table.length;i++) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url",url[i]);
            tabFragment.setArguments(bundle);
            fragments.add(tabFragment);
            tabLayout.addTab(tabLayout.newTab());
        }

        tabLayout.setupWithViewPager(viewPager,false);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);

        for(int i=0;i<table.length;i++) {
            tabLayout.getTabAt(i).setText(table[i]);
        }

    }

    @Override
    protected void onDestroy() {
        tabLayout.removeAllTabs();
        tabLayout.clearOnTabSelectedListeners();
        viewPager.clearOnPageChangeListeners();
        viewPager.removeAllViews();
        super.onDestroy();
    }
}
