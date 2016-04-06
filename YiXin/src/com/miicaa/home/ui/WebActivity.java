package com.miicaa.home.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.miicaa.home.R;

/**
 * Created by LM on 14-9-15.
 */
public class WebActivity extends Activity {
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String url = bundle.getString("url");
        if (url == null || "".equals(url)){
            return;
        }
        setContentView(R.layout.web_activity_view);
        mWebView = (WebView)findViewById(R.id.about_web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许执行Javascript
        webSettings.setLoadWithOverviewMode(true);
        mWebView.loadUrl(url);
    }
}
