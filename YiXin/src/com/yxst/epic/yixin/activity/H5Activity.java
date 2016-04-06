package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.itri.html5webview.HTML5WebView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.miicaa.home.R;

@EActivity
public class H5Activity extends ActionBarActivity {

	private static final String TAG = "H5Activity";

	@Extra
	String remoteDisplayName;
	
	@Extra
	String url;
	
//	@ViewById(R.id.webView)
	HTML5WebView mWebView;
	
	ProgressBar progressbar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html5);
        
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        FrameLayout layoutWebView = (FrameLayout) findViewById(R.id.layoutWebView);
        
        mWebView = new HTML5WebView(this);
        layoutWebView.addView(mWebView.getLayout());
        
        WebChromeClient client = new WebChromeClient();
        mWebView.setWebChromeClient(client);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        
        if (savedInstanceState != null) {
        	mWebView.restoreState(savedInstanceState);
        } else {
        	Log.d(TAG, "url:" + url);
        	mWebView.loadUrl(url);
//        	mWebView.loadUrl("http://3g.163.com/ntes/special/0034073A/wechat_article.html?docid=9N3IMHLJ0001121M&from=singlemessage&isappinstalled=1");
//        	mWebView.loadUrl("http://freebsd.csie.nctu.edu.tw/~freedom/html5/");
        	//mWebView.loadUrl("file:///data/bbench/index.html");
        }
        
        initActionBar();
    }
	
	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }
	
	@Override
	protected void onDestroy() {
		mWebView.stopLoading();
		mWebView.destroy();
		
		super.onDestroy();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		this.finish();
		return true;
	}
	
	void initActionBar() {
		final ActionBar bar = getSupportActionBar();
		bar.setTitle(remoteDisplayName);

		int flags = ActionBar.DISPLAY_HOME_AS_UP;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setDisplayOptions(change, flags);
		
		bar.setHomeAsUpIndicator(R.drawable.abc_ic_clear);
		bar.setIcon(null);
		bar.setDisplayUseLogoEnabled(false);
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mWebView.saveState(outState);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	mWebView.stopLoading();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
            	mWebView.hideCustomView();
            	return true;
            }
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
    }
}
