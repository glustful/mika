package com.miicaa.home.ui;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.miicaa.home.R;

@EViewGroup(R.layout.search_fouction_view)
public class SearchFunctionView extends LinearLayout{
	
	static String TAG = "SearchFunctionView";

	
	Context mContext;
	@ViewById(R.id.searchEditText)
	EditText editText;
	@ViewById(R.id.screenImageBtn)
	ImageButton screenImageButton;
	@ViewById(R.id.delSearchImgeBtn)
	ImageButton delSearchImageButton;
	
	public SearchFunctionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public SearchFunctionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public SearchFunctionView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	@AfterInject
	void afterInject(){
		
	}
	
	@AfterViews
	void afterView(){
		editText.setImeActionLabel("搜索" , EditorInfo.IME_ACTION_SEARCH);
		editText.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				 switch(actionId ){
                 case  EditorInfo.IME_ACTION_SEARCH: 
                	 String searText = editText.getText().toString();
                	 if(onSearchFounctionViewListener != null)
                		 onSearchFounctionViewListener.onSearchClick(searText);
                        break;
               }                          
                  return false ;
			}});
		editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		editText.setSingleLine();

	}
	
	@TextChange(R.id.searchEditText)
	void onTextChangesOnSearchEditTextView(CharSequence text, TextView textView, int before, int start, int count) {
		if(TextUtils.isEmpty(text))
			delSearchImageButton.setVisibility(View.GONE);
		else
			delSearchImageButton.setVisibility(View.VISIBLE);
		
		Log.d(TAG, "onTextChangesOnSearchEditTextView :"+"before:"+before
				+"start:"+start);
		if(before > start){
			if(onSearchFounctionViewListener != null)
				onSearchFounctionViewListener.onDelSearchTextClick(text);
		}
	 }
	
	@Click(R.id.screenImageBtn)
	void screenBtnClick(View v){
		if(onSearchFounctionViewListener != null)
			onSearchFounctionViewListener.onScreenClick();
	}
	
	@Click(R.id.delSearchImgeBtn)
	void delImageButtonClick(View v){
		v.setVisibility(View.GONE);
		editText.setText("");
		if(onSearchFounctionViewListener != null)
			onSearchFounctionViewListener.onDelSearchTextClick("");
	}
	
	
	public interface OnSearchFounctionViewListener{
		void onDelSearchTextClick(CharSequence text);
		void onScreenClick();
		void onSearchClick(CharSequence text);
	}
	
	OnSearchFounctionViewListener onSearchFounctionViewListener;
	public void setOnSearchFunctionViewListener(OnSearchFounctionViewListener listener){
		this.onSearchFounctionViewListener = listener;
	}
	
	public void clearFocous(){
		editText.clearFocus();
	}
	
	public void setSearHint(CharSequence str){
		editText.setHint(str);
	}
	
}
