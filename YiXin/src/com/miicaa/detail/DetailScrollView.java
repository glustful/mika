package com.miicaa.detail;



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;
/** 
 * @blog 
 *  
 * @author 
 * 
 */  
public class DetailScrollView extends ScrollView {  
	static String TAG = "DetailScrollView";
    private OnScrollListener onScrollListener;  
      
    public DetailScrollView(Context context) {  
        this(context, null);  
    }  
      
    public DetailScrollView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
        
    }  
  
    public DetailScrollView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    /** 
     * 设置滚动接口 
     * @param onScrollListener 
     */  
    public void setOnScrollListener(OnScrollListener onScrollListener) {  
        this.onScrollListener = onScrollListener;  
    }  
      
      
    @Override  
    public int computeVerticalScrollRange() {  
        return super.computeVerticalScrollRange();  
    }  
      
  
    @Override  
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {  
        super.onScrollChanged(l, t, oldl, oldt);  
        if(onScrollListener != null){  
        	Log.d(TAG, "onscrollListener is let :"+t);
            onScrollListener.onScroll(t);  
        }  
    }  
  
  
  
    /** 
     *  
     * 滚动的回调接口 
     *  
     * @author 
     * 
     */  
    public interface OnScrollListener{  
        /** 
         * 回调方法， 返回MyScrollView滑动的Y方向距离 
         * @param scrollY 
         *              、 
         */  
        public void onScroll(int scrollY);  
    }  
      
      
  
} 
