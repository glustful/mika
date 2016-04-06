package com.miicaa.home.ui.photoGrid;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;

@EViewGroup(R.layout.picture_select_foot)
public class PictureSelectView extends RelativeLayout{

	@ViewById(R.id.checkBox)
	CheckBox checkBox;
	@ViewById(R.id.text)
	TextView textView;
	
	
	Boolean isCheck;
	
	public PictureSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PictureSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PictureSelectView(Context context) {
		super(context);
	}
	
	
	@AfterViews
	void afterViews(){
		
	}
	
	@Click(R.id.checkBox)
	void checkBoxClick(){
		if(isCheck == null){
			isCheck = true;
		}else{
			isCheck = !isCheck;
		}
		checkBox.setChecked(isCheck);
		if(selectPictureChangeListener != null){
			selectPictureChangeListener.slectPictureChange(isCheck);
		}
	}
	
	public void setChecked(Boolean isCheck){
		this.isCheck = isCheck;
		checkBox.setChecked(isCheck);
	}
	
	
	public interface SelectPictureChangeListener{
		void slectPictureChange(Boolean isCheck);
	}
	
	SelectPictureChangeListener selectPictureChangeListener;
	public void setSelectPictureChangeListener(SelectPictureChangeListener listener){
		selectPictureChangeListener = listener;
	}

}
