package com.miicaa.home.ui.picture;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import com.miicaa.home.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public abstract class PictureShowAtivity extends Activity{

	DisplayImageOptions options;
	
	public PictureShowAtivity(){
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheInMemory(false)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.build();
		okInOptions(options);
	}
	
	Long myTime;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			myTime = System.currentTimeMillis();
		}else if(ev.getAction() == MotionEvent.ACTION_UP){
			myTime = System.currentTimeMillis() - myTime;
			if(myTime < 100){
				hiddenViews();
			}
		}
		Log.d("PictureShowActivity","PictureShowActivity dispatchTouchEvent return : ....."+
		super.dispatchTouchEvent(ev));
//		return super.dispatchTouchEvent(ev);
		return true;
	}

	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if(event.getAction() == MotionEvent.ACTION_UP){
//			hiddenViews();
//			return true;
//		}
//		return true;
//	}
	
	protected abstract void hiddenViews();
	protected abstract void okInOptions(DisplayImageOptions options);
	
}
