package com.miicaa.home.ui.contactList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apple on 13-11-27.
 */
public class ContactIndexListView extends View {
	
	

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public String[] letterStrings = {"★", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public ContactIndexListView(Context context, AttributeSet attrs, int defStyle) {
    	
        super(context, attrs, defStyle);

    }

    public ContactIndexListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ContactIndexListView(Context context) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showBkg) {
            canvas.drawColor(Color.parseColor("#20888888"));
        }
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / letterStrings.length;
        for (int i = 0; i < letterStrings.length; i++) {
            paint.setColor(Color.parseColor("#999999"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(18);
            if (i == choose) {
                paint.setColor(Color.parseColor("#ff0000"));
                paint.setFakeBoldText(true);
            }
            //xpos居中的意思
            float xPos = width / 2 - paint.measureText(letterStrings[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letterStrings[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        float x_d = 0;
        float x_u = 0;
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * letterStrings.length);
        
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            	x_d  = event.getX();
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < letterStrings.length) {
                        listener.onTouchingLetterChanged(letterStrings[c], true);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
            	
            	 Log.d("ContactIndexListView", "dispatchTouchEvent c:"+c+"");
            	
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < letterStrings.length) {
                    	
                        listener.onTouchingLetterChanged(letterStrings[c], true);
                        choose = c;
                    	}
                        invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            	x_u = event.getX();
            	if(x_d != x_u && c >= 0 && c < letterStrings.length){
            		listener.onTouchingLetterChanged(letterStrings[c], false);
            		choose = -1;
            	}
                showBkg = false;
                choose = -1;
                listener.onTouchingLetterChanged("", false);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s, boolean isShow);
    }

}