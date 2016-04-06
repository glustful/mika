package com.miicaa.home;

import android.app.Activity;
import android.view.MotionEvent;

import com.miicaa.common.base.Utils;

public class HiddenSoftActivity extends Activity {

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			Utils.hiddenSoftBorad(this);
		}
		return super.dispatchTouchEvent(ev);
	}


}
