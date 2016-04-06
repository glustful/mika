package com.miicaa.home.ui.enterprise;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.EntirpiseInfo;
import com.miicaa.home.data.net.RequstManager;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.data.storage.LocalPath;
import com.miicaa.home.provider.EnterpiceProvider;

@EActivity(R.layout.enterpaice_main_view)
public class EnterpriceMainActivity extends Activity {

	@Override
	public void onBackPressed() {
		if(backAction==null||backAction.equals("pop")){
			super.onBackPressed();
		}else{
			if(mWebView!=null&&mWebView.canGoBack()){
				mWebView.goBack();
				
			}else{
			super.onBackPressed();
			}
		}
	}
	@Extra
	String url;
	@Extra
	String title;
	private MyWebChromeClient mWebChromeClient;
	Context mContext;
	@ViewById(R.id.webview)
	WebView mWebView;
	@ViewById(R.id.pay_cancleButton)
	Button leftButton;
	@ViewById(R.id.pay_headTitle)
	TextView titleButton;
	@ViewById(R.id.pay_commitButton)
	Button rightButton;
	String backAction = "pop";
	
	HashMap<String, String> title_map = new HashMap<String, String>();//返回时标题不同步的临时解决办法

	@Click(R.id.pay_cancleButton)
	void cancel(){
		onBackPressed();
		
	}
	
	@Click(R.id.pay_commitButton)
	void commit(){
		MyServiceActivity_.intent(mContext)
		.start();
		
	}
	
	@AfterInject
	void initData() {
		this.mContext = this;
		title= (title==null || title.equals(""))?"企业服务":title;
	}

	@AfterViews
	void initUI() {
		EntirpiseInfo entirpiseInfo = new EntirpiseInfo();
		entirpiseInfo.userEmail = AccountInfo.instance().getLastUserInfo().getEmail();
		entirpiseInfo.userCode = AccountInfo.instance().getLastUserInfo().getCode();
		entirpiseInfo.locationType = EnterpriseLocation.YunanReadedSuccess;
		entirpiseInfo.gonggaoCount = EnterpriseLocation.getInstance().getGonggaoCount();
		EntirpiseInfo.saveOrUpdateLcation(mContext, entirpiseInfo);
		mContext.getContentResolver().notifyChange(EnterpiceProvider.CONTENT_URI, null);
		titleButton.setText(title);
		
		rightButton.setText("我的服务");
		
		initWebView();

		
		if(url==null||url.equals(""))
		mWebView.loadUrl(UserAccount.mSeverHost
				+ mContext.getString(R.string.enterprice_url),initCookie());
		else{
			mWebView.loadUrl(UserAccount.mSeverHost+url,initCookie());
		}
		
	}

	private HashMap<String,String> initCookie() {
		CookieSyncManager.createInstance(mContext);
		CookieManager cookieManager = CookieManager.getInstance();
		HashMap<String,String> map = new HashMap<String, String>();
		CookieStore mCookie = RequstManager.getInstance().getCookie();
		if (mCookie == null) {

			File cookieFile = new File(LocalPath.intance().cacheBasePath + "co");
			String res = "";
			try {
				FileInputStream fin = new FileInputStream(cookieFile);

				int length = fin.available();

				byte[] buffer = new byte[length];
				fin.read(buffer);

				res = EncodingUtils.getString(buffer, "UTF-8");
				fin.close();
				if (res != null && !"".equals(res.trim())) {

					JSONArray array = new JSONArray(res);

					for (int i = 0; i < array.length(); i++) {
						JSONObject o = array.getJSONObject(i);
						String name = o.optString("name");
						String value = o.optString("value");
						if (name != null && value != null) {
							String str = "";

							str += name + "=" + value + ";";
							str += "comment=" + o.optString("comment") + ";";
							str += "domain=" + o.optString("domain") + ";";
							str += "path=" + o.optString("path") + ";";
							str += "version=" + o.optInt("version") + ";";

							long expire = new Date().getTime() + 12 * 3600000;
							str += "expires=" + expire + "";

							cookieManager
									.setCookie(UserAccount.mSeverHost, str);
							if(name.toUpperCase().contains("__PLATFORM_SESSION_ID_KEY")){
								map.put("cookie", str);
							}
						}

					}

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		} else {
			List<Cookie> cookies = mCookie.getCookies();

			for (Cookie cok : cookies) {
				String str = "" + cok.getName() + "=" + cok.getValue() + ";";
				str += "comment=" + cok.getComment() + ";";
				str += "domain=" + cok.getDomain() + ";";
				str += "path=" + cok.getPath() + ";";
				str += "version=" + cok.getVersion() + ";";
			

				long expire = new Date().getTime() + 12 * 3600000;
				str += "expires=" + expire + "";

				cookieManager.setCookie(UserAccount.mSeverHost, str);
				if(cok.getName().toUpperCase().contains("__PLATFORM_SESSION_ID_KEY")){
					map.put("cookie", str);
				}
			}

		}

		CookieSyncManager.getInstance().sync();
		return map;
	}

	private void initWebView() {
		mWebChromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(mWebChromeClient);

		mWebView.setWebViewClient(new MyWebViewClient());
		
		// Configure the webview
		WebSettings s = mWebView.getSettings();
		s.setBuiltInZoomControls(false);
		s.setDefaultTextEncodingName("gbk");
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);
		
		s.setCacheMode(WebSettings.LOAD_NO_CACHE);
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		s.setDomStorageEnabled(true);
		
		mWebView.addJavascriptInterface(new Object(){
			public void back(String msg){
			
				backAction = msg;
			}
		}, "enterprice");

	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			titleButton.setText(title);
			if(mWebView.getUrl()!=null)
			title_map.put(mWebView.getUrl(), title);
		}
		
		/**
		 * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
		 */
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("对话框").setMessage(message)
					.setPositiveButton("确定", null);

			// 不需要绑定按键事件
			// 屏蔽keycode等于84之类的按键
			builder.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
					return true;
				}
			});
			// 禁止响应按back键的事件
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
			return true;

		}

		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			return super.onJsBeforeUnload(view, url, message, result);
		}

		/**
		 * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
		 */
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("对话框")
					.setMessage(message)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm();
								}
							})
					.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					result.cancel();
				}
			});

			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
			builder.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsConfirm", "keyCode==" + keyCode + "event="
							+ event);
					return true;
				}
			});
			// 禁止响应按back键的事件
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;

		}

		/**
		 * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
		 * window.prompt('请输入您的域名地址', '618119.com');
		 */
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, final JsPromptResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());

			builder.setTitle("对话框").setMessage(message);

			final EditText et = new EditText(view.getContext());
			et.setSingleLine();
			et.setText(defaultValue);
			builder.setView(et)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.confirm(et.getText().toString());
								}

							})
					.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									result.cancel();
								}
							});

			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
			builder.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					Log.v("onJsPrompt", "keyCode==" + keyCode + "event="
							+ event);
					return true;
				}
			});

			// 禁止响应按back键的事件
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;

		}

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
			
			if(url.contains(mContext.getString(R.string.enterprice_url))){
				rightButton.setVisibility(View.VISIBLE);
			}else{
				rightButton.setVisibility(View.INVISIBLE);
			}
		}
		
	

		@Override
		public void onPageFinished(WebView view, String url) {
			
			super.onPageFinished(view, url);
			if(title_map.get(url)!=null){
				titleButton.setText(title_map.get(url));
			}
			mWebView.loadUrl("javascript:window.enterprice.back(getBack());");
		}


		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {

			handler.proceed();
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			
			if (url.contains("navcodecall.call")) {
				new CommandCall(mContext,url.substring(url.lastIndexOf("?")+1)).call();
				return true;
			}
			
			view.loadUrl(url);
			return false;
		}
	}

	

}
