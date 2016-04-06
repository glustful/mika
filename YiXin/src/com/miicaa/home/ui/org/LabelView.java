package com.miicaa.home.ui.org;

import java.util.ArrayList;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;

@EViewGroup
public class LabelView extends RelativeLayout{
	
	static String TAG = "LabelView";
	
	TextView textView;
	 RelativeLayout sRelativeLayout;
	 

	public LabelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public LabelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public LabelView(Context context) {
		super(context);
		Log.d(TAG, "context ---- "+context.toString());
		init(context);
		// TODO Auto-generated constructor stub
	}
	
	

	
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	void init(Context context){
//		this.setLayoutParams(new  AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		 sRelativeLayout = new RelativeLayout(context);
		 sRelativeLayout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
//	        linearLayout.setMinimumHeight(35);
	        textView = new TextView(context);
	        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
	                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	        tParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        tParams.setMargins(0, 0, 5, 0);
	        textView.setLayoutParams(tParams);
	        textView.setTextSize(10);
	        sRelativeLayout.addView(textView);
	        this.addView(sRelativeLayout);
//	        this.addView(sRelativeLayout);
	        
	}
	
	public void ChangeText(Context context,String text){
		ArrayList<Integer> roundD = new ArrayList<Integer>();
		 TextPaint paint = textView.getPaint();
		 roundD.add(R.drawable.label_1);
		 roundD.add(R.drawable.label_2);
		 roundD.add(R.drawable.label_3);
		 int rN = (int) Math.round(Math.random()*2);
		 int dId = roundD.get(rN);
		 sRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(dId));
		 textView.setText(text);
		 int w = (int)paint.measureText(text);
         w += context.getResources().getDimension(R.dimen.labelW);
         
         LayoutParams rParams = new LayoutParams(w,
     		   android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        sRelativeLayout.setLayoutParams(rParams);
        Log.d(TAG, "text ----"+text);
	}

	

}
