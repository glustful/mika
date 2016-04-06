package com.yxst.epic.yixin.view;


import com.miicaa.home.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewPartColor extends TextView {
	

	@Override
	public void setText(CharSequence text, BufferType type) {
		
		if(end > start && text.length()>=end){
			Spannable WordtoSpan = new SpannableString(text);   
			
			WordtoSpan.setSpan(new ForegroundColorSpan(partColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			super.setText(WordtoSpan, type);
		}else{
		super.setText(text, type);
		}
	}

	int partColor;
	public int getPartColor() {
		return partColor;
	}

	public void setPartColor(int partColor) {
		this.partColor = partColor;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	int start,end;

	public TextViewPartColor(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextViewPartColor);
		partColor = ta.getColor(R.styleable.TextViewPartColor_partTextColor, Color.RED);
		start = ta.getInteger(R.styleable.TextViewPartColor_colorStart, 0);
		end = ta.getInt(R.styleable.TextViewPartColor_colorEnd, 0);
		setText(getText());
	}
	

}
