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
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.miicaa.home.BaseKeyVaule;
import com.miicaa.home.R;

@EViewGroup(R.layout.approval_screen)
public class BaseScreenView extends RelativeLayout{
	
	private static String TAG = "BaseScreenView";

	OnBaseScreenClickLinstener listener;
	Context context;
	Boolean isSelect;//选中和取消
	@ViewById(R.id.contentText)
    CheckBox contentview;
	public BaseScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public BaseScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public BaseScreenView(Context context) {
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
	void scrrenClick(View v){
		click_(v);
	}
	
	@SuppressLint("NewApi")
	@UiThread
	void click_(View v){
		Log.d(TAG, "selct mode is before change :....." + isSelect);
		BaseKeyVaule type = (BaseKeyVaule)v.getTag();
		type.isSelect = !type.isSelect;
		Log.d(TAG, "selct mode is:....." + isSelect);
		if(type.isSelect){
		    listener.screenClick(type);
		}else{
			listener.sreenCancle(type);
		}
		
	}
	
	public void changeViewWithType(){
		BaseKeyVaule type = (BaseKeyVaule) contentview.getTag();
		Log.d(TAG, "changeViewWithType isSelect:"+type.isSelect);
		setIsSelect(type.isSelect);
		
	}
	
	@SuppressWarnings("deprecation")
	public void setIsSelect(Boolean w){
		contentview.setChecked(w);
	}
	
	public BaseScreenView setContent(String content){
		content = content.trim();
		content = content.length() > 12 ? content.substring(0,12)+"..." : content;
		contentview.setText(content);
		return this;
	}
	
	public interface  OnBaseScreenClickLinstener{
		<T extends BaseKeyVaule> void screenClick(T type);
		<T extends BaseKeyVaule> void sreenCancle(T type);
	}
	
	public BaseScreenView setBaseScreenClickLinstener(OnBaseScreenClickLinstener listener){
		this.listener = listener;
		return this;
		
	}
	
	public <T extends BaseKeyVaule> BaseScreenView setTag(T tag){
		contentview.setTag(tag);
		return this;
	}
	
	public Object getContentTag(){
		return contentview.getTag();
	}
}
