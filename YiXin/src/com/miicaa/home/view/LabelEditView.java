package com.miicaa.home.view;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.label_editview)
public class LabelEditView extends LinearLayout {

	@ViewById
	TextView label;
	@ViewById
	EditText content;
	String mLabel, mContent, mHint;
	int gravity = Gravity.RIGHT;
	boolean enable = true;
	int drawableRight;

	@AfterInject
	void initData() {

	}

	@AfterViews
	void initUI() {

		label.setText(mLabel);
		content.setHint(mHint);
		content.setText(mContent);
		content.setGravity(gravity);
		
		content.setEnabled(enable);
		content.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);
		
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!enable) {
			this.onTouchEvent(ev);
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public LabelEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public LabelEditView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.LabelTextView);
		mLabel = ta.getString(R.styleable.LabelTextView_label);
		mContent = ta.getString(R.styleable.LabelTextView_text);
		mHint = ta.getString(R.styleable.LabelTextView_hint);
		gravity = ta.getInt(R.styleable.LabelTextView_gravity, Gravity.RIGHT);
		enable = ta.getBoolean(R.styleable.LabelTextView_enable, true);
		
		drawableRight = ta.getResourceId(
				R.styleable.LabelTextView_drawableRight, 0);
		ta.recycle();

	}

	public LabelEditView(Context context) {
		super(context);

	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public void setLabel(int rid) {
		this.label.setText(rid);
	}

	public void setContent(String label) {
		this.content.setText(label);
	}

	public void setContent(int rid) {
		this.content.setText(rid);
	}

	public void setHint(String label) {
		this.content.setHint(label);
	}

	public void setHint(int rid) {
		this.content.setHint(rid);
	}

	public String getText() {
		// TODO Auto-generated method stub
		return this.content.getText().toString();
	}

	public boolean isNull() {
		if(content.getText().toString().trim().equals(""))
			return true;
		return false;
	}

	public EditText getEditor() {
		
		return this.content;
	}

	public boolean invalide() {
		if(this.content.getText().toString().trim().equals("")){
			PayUtils.showToast(getContext(), this.label.getText()+"不能为空", 3000);
			return false;
		}
		return true;
	}

}
