package com.miicaa.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class PagerScorllView extends ScrollView {

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public PagerScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
//        this.post(new Runnable(){
//                 public void run() {  
//                fullScroll(ScrollView.FOCUS_UP);  
//             }  
//        });
        
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
    
    class YScrollDetector extends SimpleOnGestureListener implements OnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(distanceY!=0&&distanceX!=0){
                        
                }
            if(Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
}
