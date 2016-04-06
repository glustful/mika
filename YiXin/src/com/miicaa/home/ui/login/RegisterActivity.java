package com.miicaa.home.ui.login;

import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends MainActionBarActivity{

	@ViewById(R.id.progressBar)
	ProgressBar progressBar;
	@ViewById(R.id.regristerEdit)
	EditText regristerEdit;
	@ViewById(R.id.registerButton)
	Button registerButton;
	@ViewById(R.id.registerLayout)
	LinearLayout registerLayout;
	@ViewById(R.id.regSuccessLayout)
	LinearLayout regSuccessLayout;
	@ViewById(R.id.successTextView)
	TextView successTextView;
	
	Handler mHandler = new Handler();
	
	@SuppressLint({"ShowToast" })
	@AfterViews
	void afterView(){
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		progressBar.setMax(100);
	}
	
	@Click(R.id.registerButton)
	void registerClick(View v){
		if(panduanEdit()){
		v.setEnabled(false);
		registerRequest();
		}
	}
	
	Toast mToast;
	private boolean panduanEdit(){
		Pattern  pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//邮箱匹配正则表达式
		String email = regristerEdit.getText().toString().trim();
		if(email.length() == 0 || !pattern.matcher(email).matches()){
			mToast.setText("请输入正确的邮箱");
			mToast.show();
			return false;
		}
		return true;
	}
	
	private void registerRequest(){
		progressBar.setVisibility(View.VISIBLE);
		final String email = regristerEdit.getText().toString().trim();
		String url = "/home/pc/register/save";
		new RequestAdpater() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -8896154287423702192L;

			@Override
			public void onReponse(ResponseData data) {
				registerButton.setEnabled(true);
				progressBar.setVisibility(View.GONE);
				if(data.getResultState() == ResultState.eSuccess){
					registerLayout.setVisibility(View.GONE);
					String hintString = getResources().getString(R.string.register_success_text,email);
					SpannableStringBuilder emailSb = new SpannableStringBuilder(hintString);
					emailSb.setSpan(new ForegroundColorSpan(Color.BLACK), 
							hintString.indexOf(email), hintString.indexOf(email)+email.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					successTextView.setHint(emailSb);
					regSuccessLayout.setVisibility(View.VISIBLE);
				}else{
					mToast.setText(data.getMsg()+"code:"+data.getCode());
					mToast.show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("email",email)
		.addParam("src", "android")
		.notifyRequest();
	}
	
	class WebBrowseClient extends WebChromeClient{
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setProgress(newProgress);
			if(newProgress == 100){
				progressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
		
	} 
	
	class DefaultWebClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			 view.loadUrl(url);
             return false;
//			return super.shouldOverrideUrlLoading(view, url);
		}
		
	}
	
	@Override
	public String showBackButtonStr() {
		return "返回";
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		return "注册";
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return false;
	}

	@Override
	public String showRightButtonStr() {
		return null;
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	

}
