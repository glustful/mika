package com.miicaa.base.share.city;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-24.
 */
public class ScrollerPickerPopup
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;

   
	LinearLayout root = null;
	LinearLayout contentView = null;
	ArrayList<String> items;
	String selected;

	private static ScrollerPickerBuilder mBuilder;

    public static ScrollerPickerBuilder builder(Context context,LinearLayout contentView)
    {
        mBuilder = new ScrollerPickerBuilder(context,contentView);
        return mBuilder;
    }

    private ScrollerPickerPopup(Context context,LinearLayout contentView)
    {
        this.mContext = context;
        this.contentView = contentView;
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
	
	private OnButtonListener listener;
	
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
        root.removeViewAt(1);
        root.addView(contentView);
        root.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onClick(Dialog.BUTTON_NEGATIVE);
				}
				mPopupWindow.dismiss();
				
			}
		});
        root.findViewById(R.id.commit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onClick(Dialog.BUTTON_POSITIVE);
				}
				mPopupWindow.dismiss();
			}
		});
       
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
    
    

   


	
	public static class ScrollerPickerBuilder
    {
        ScrollerPickerPopup mMenu;
       // ArrayList<String> items;
        public ScrollerPickerBuilder (Context context,LinearLayout contentView)
        {
            mMenu = new ScrollerPickerPopup(context,contentView);
        }

        public ScrollerPickerBuilder setOnButtonListener(OnButtonListener l){
        	mMenu.setOnButtonListener(l);
        	return this;
        }
        
        public ScrollerPickerBuilder setCurrentItem(int index){
        	mMenu.setmCurrentItem(index);
        	return this;
        }

        public void show()
        {
            mMenu.show();
        }

    }


	public interface OnButtonListener{
		void onClick(int code);
	}


	public void setOnButtonListener(OnButtonListener l) {
		this.listener = l;
		
	}


  
}
