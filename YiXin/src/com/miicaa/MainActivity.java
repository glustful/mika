package com.miicaa;

import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

@EActivity
public class MainActivity extends Activity{

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d("FramMainActionBarActivity", "dispatchTouchEvent ev :"+ev.getAction());
		if(ev.getAction() == MotionEvent.ACTION_MOVE ){
			Log.d("FramMainActionBarActivity", "dispatchTouchEvent actionmove touchY:"+ev.getY());
			activityYMove();
//			touchY = (int) ev.getY();
		}
//		}else if( ev.getAction() == MotionEvent.ACTION_UP){
//			Log.d("FramMainActionBarActivity", "dispatchTouchEvent actionUp touchY:"+ev.getY());
//			activityYMove();
////		Log.d("PictureShowActivity","PictureShowActivity dispatchTouchEvent return : ....."+
//		}
//		super.dispatchTouchEvent(ev));
		return super.dispatchTouchEvent(ev);
	}
	
	protected void activityYMove(){
		
	}
}
