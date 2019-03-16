package com.example.lcq.myapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.lcq.myapp.R;

public class WebviewActivity extends Activity {

    private WebView webView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        webView = findViewById(R.id.webview);
    }


    public void webview1(View view) {
        webView.loadUrl("https://blog.csdn.net/yaojie5519/article/details/82344838");

    }

    public void webview2(View view) {
        webView.loadUrl("https://blog.csdn.net/xyq046463/article/details/51769728");

    }

    public void webview3(View view) {
        webView.loadUrl("https://blog.csdn.net/qq_20280683/article/details/77964208");

    }

    public void webview4(View view) {
        webView.loadUrl("https://zhuanlan.zhihu.com/p/25213586");

    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}
