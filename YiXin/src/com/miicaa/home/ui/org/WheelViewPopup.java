package com.miicaa.home.ui.org;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.miicaa.common.base.WheelView;
import com.miicaa.common.base.WheelView.WheelAdapter;
import com.miicaa.home.R;
//import com.miicaa.home.ui.report.WeekEntity;
//import com.miicaa.home.ui.report.WorkReportActivity.TextAdapter;
import com.miicaa.home.ui.report.WeekEntity;
import com.miicaa.home.ui.report.WorkReportActivity.TextAdapter;

/**
 * Created by Administrator on 13-12-24.
 */
public class WheelViewPopup
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;

   
	LinearLayout root = null;
	ArrayList<String> items;
	String selected;

	private static WheelViewMenuBuilder mBuilder;

    public static WheelViewMenuBuilder builder(Context context)
    {
        mBuilder = new WheelViewMenuBuilder(context);
        return mBuilder;
    }

    private WheelViewPopup(Context context)
    {
        this.mContext = context;
    }

   

    public void show()
    {
        Activity activity = (Activity)mContext;
        drawContent();
      
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);


    }
    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            
        }
    };
	private int textSize;
	private WheelAdapter adapter;
	private OnItemChangeListener listener;
	 WheelView mWheelView;
	private int mCurrentItem = 0;

    public int getmCurrentItem() {
		return mCurrentItem;
	}

	public void setmCurrentItem(int mCurrentItem) {
		this.mCurrentItem = mCurrentItem;
	}

	private void drawContent()
    {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        root=(LinearLayout)inflater.inflate(R.layout.wheelview_layout, null);

        root.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				
			}
		});
        root.findViewById(R.id.commit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					if(adapter instanceof TextAdapter)
					listener.onChange(((TextAdapter)adapter).getWeekEntity(mWheelView.getCurrentItem()));
					
				}
				mPopupWindow.dismiss();
			}
		});
       
       mWheelView = (WheelView) root.findViewById(R.id.mWheelView);
       
       
       
       mWheelView.TEXT_SIZE = textSize;
       mWheelView.setAdapter(adapter);
       
       
       mWheelView.setCyclic(true);
       mWheelView.setCurrentItem(mCurrentItem , false);
        root.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_arange_bottom));
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        mPopupWindow = new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(onDismissListener);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }
    
    

   


	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public WheelAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(WheelAdapter adapter) {
		this.adapter = adapter;
	}





	public static class WheelViewMenuBuilder
    {
        WheelViewPopup mMenu;
       // ArrayList<String> items;
        public WheelViewMenuBuilder (Context context)
        {
            mMenu = new WheelViewPopup(context);
        }

        public WheelViewMenuBuilder setAdapter(WheelAdapter adapter){
        	mMenu.setAdapter(adapter);
        	return this;
        }
        
        public WheelViewMenuBuilder setOnItemChangeListener(OnItemChangeListener l){
        	mMenu.setOnItemChangeListener(l);
        	return this;
        }
        
        public WheelViewMenuBuilder setCurrentItem(int index){
        	mMenu.setmCurrentItem(index);
        	return this;
        }

        public void show()
        {
            mMenu.show();
        }

		public WheelViewMenuBuilder setTextSize(int i) {
			mMenu.setTextSize(i);
			return this;
		}
    }


	public interface OnItemChangeListener{
		void onChange(WeekEntity item);
	}


	public void setOnItemChangeListener(OnItemChangeListener l) {
		this.listener = l;
		
	}


  
}
