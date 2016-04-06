package com.miicaa.detail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.miicaa.home.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

@EViewGroup(R.layout.approval_process_type_view)
public class ApprovalProcessTypeView extends RelativeLayout implements OnClickListener{
	
	private static String TAG = "ApprovalProcessTypeView";
	
	public final static int FIXED  = 0x1;
	public final static int FREE  = 0x2;
	
	Context mContext;

	public ApprovalProcessTypeView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public ApprovalProcessTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ApprovalProcessTypeView(Context context) {
		super(context);
		mContext = context;
	}
	
	@ViewById(R.id.freeProcessButton)
	CheckBox freeProcessButton;
	@ViewById(R.id.fixedProcessButton)
	CheckBox fixedProcessButton;
	
	@AfterViews
	void afterView(){
		freeProcessButton.setTag(FREE);
		fixedProcessButton.setTag(FIXED);
		fixedProcessButton.setChecked(true);
		fixedProcessButton.setClickable(false);
		freeProcessButton.setOnClickListener(this);
		fixedProcessButton.setOnClickListener(this);
		freeProcessButton.setOnCheckedChangeListener(onCheckedChangeListener);
		fixedProcessButton.setOnCheckedChangeListener(onCheckedChangeListener);
//		freeProcessButton.setOnKeyListener(mTabKeyListener);
//		fixedProcessButton.setOnKeyListener(mTabKeyListener);
//		freeProcessButton.setOnClickListener(onButtonClickListener);
//		fixedProcessButton.setOnClickListener(onButtonClickListener);
	}
	
	OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int tag = (Integer) buttonView.getTag();
			if(tag == FREE)
			{
				if(onProcessChangeListener != null)
					onProcessChangeListener.onProcessChange(FREE);
			}else if(tag == FIXED)
			{
				if(onProcessChangeListener != null)
					onProcessChangeListener.onProcessChange(FIXED);
			}
		}
	};
	
//	@OnFocusChange(R.id.freeProcessButton)
//	void freeProcessChange(View v,boolean hasFocus){
//		Log.d(TAG, "freeProcessChange focus:"+hasFocus);
//		if(hasFocus && onProcessChangeListener != null)
//			onProcessChangeListener.onProcessChange(0);
//			
//	}
//	
//	@OnFocusChange(R.id.fixedProcessButton)
//	void fixedProcessChange(View v,boolean hasFocus){
//		Log.d(TAG, "fixedProcessChange focus:"+hasFocus);
//		if(hasFocus && onProcessChangeListener != null){
//			onProcessChangeListener.onProcessChange(1);
//		}
//	}
	
	
	
//	private OnKeyListener mTabKeyListener = new OnKeyListener() {
//        public boolean onKey(View v, int keyCode, KeyEvent event) {
//        	Log.d(TAG,"onKeyListener is listening!");
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_DPAD_CENTER:
//                case KeyEvent.KEYCODE_DPAD_LEFT:
//                case KeyEvent.KEYCODE_DPAD_RIGHT:
//                case KeyEvent.KEYCODE_DPAD_UP:
//                case KeyEvent.KEYCODE_DPAD_DOWN:
//                case KeyEvent.KEYCODE_ENTER:
//                    return false;
//
//            }
////            v.requestFocus(View.FOCUS_FORWARD);
//            v.requestFocus();
//            return v.dispatchKeyEvent(event);
//        }
//
//    };
	
    
    public interface OnProcessChangeListener{
    	void onProcessChange(int key);
    }
    
    OnProcessChangeListener onProcessChangeListener;
    public void setOnProcessChangeListener(OnProcessChangeListener listener){
    	this.onProcessChangeListener = listener;
    }

	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		v.setClickable(false);
		Boolean isChecked = ((CheckBox)v).isChecked();
		Log.d(TAG, "onClick isChecked:"+isChecked);
		if(tag == FIXED){
			freeProcessButton.setClickable(true);
			freeProcessButton.setChecked(!isChecked);
		}else if(tag == FREE){
			fixedProcessButton.setClickable(true);
			fixedProcessButton.setChecked(!isChecked);
		}
		
	}

}
