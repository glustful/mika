package com.miicaa.home.ui.menu;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;
@EViewGroup(R.layout.approval_screen)
public class Screenview extends RelativeLayout{
	static String TAG = "Screenview";
	
	ScreenClickLinstener listener;
	Context context;
	Boolean isSelect;//选中和取消
	@ViewById(R.id.contentText)
    TextView contentview;
	public Screenview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public Screenview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public Screenview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	void init(){
	}
	
	@AfterInject
	void afterInject(){
		
	}
	@AfterViews
	void afterViews(){
		isSelect = false;
		Log.d(TAG, "selct mode is after init :....." + isSelect);
		contentview.setText("asdjkakj哈哈哈");
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Click(R.id.contentText)
	void scrrenClick(){
		click_();
	}
	
	@SuppressLint("NewApi")
	@UiThread
	void click_(){
		Log.d(TAG, "selct mode is before change :....." + isSelect);
		isSelect = !isSelect;
		Log.d(TAG, "selct mode is:....." + isSelect);
		if(isSelect == true){
			contentview.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_button_select_bg));
		    listener.screenClick();
		    
		}else{
			contentview.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_button_bg));
			listener.sreenCancle();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void setIsSelect(Boolean w){
		if(w)
			contentview.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_button_select_bg));
		else
			contentview.setBackgroundDrawable(getResources().getDrawable(R.drawable.screen_button_bg));
		isSelect = w;
	}
	
	public void setContent(String content){		
		content = content.trim();
		content = content.length() > 12 ? content.substring(0,12)+"..." : content;
		contentview.setText(content);
	}
	
	public interface  ScreenClickLinstener{
		void screenClick();
		void sreenCancle();
	}
	
	public void setScreenClickLinstener(ScreenClickLinstener listener){
		
		this.listener = listener;
		
	}
}
