package com.miicaa.common.base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-5.
 */
public class BottomScreenPopup
{
    protected PopupWindow mPopupWindow = null;
    protected LinearLayout mRoot = null;
    protected Context mContext = null;
    protected ArrayList<PopupItem> mItems = null;
    protected String mTitle;
    protected OnMessageListener mOnMessageListener;
    protected Boolean mDrawTitle = false;
    private int drawable = R.drawable.white_color_selector;
    private int cancelDrawablee = R.drawable.bottom_menu_cancel_selector;
    private boolean isMargin = true;

    public boolean isMargin() {
		return isMargin;
	}
	public void setMargin(boolean isMargin) {
		this.isMargin = isMargin;
	}

	private static BottomBuilder mBuilder;
    public static BottomBuilder builder(Context context)
    {
        mBuilder = new BottomBuilder(context);
        return mBuilder;
    }
    public int getDrawable() {
		return drawable;
	}
	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}
	public int getCancelDrawablee() {
		return cancelDrawablee;
	}
	public void setCancelDrawablee(int cancelDrawablee) {
		this.cancelDrawablee = cancelDrawablee;
	}
	private BottomScreenPopup(Context context)
    {
        this.mContext = context;
    }

    private void setTitle(String title)
    {
        mTitle = title;
        mDrawTitle = true;
    }

    private void setItems(ArrayList<PopupItem> items)
    {
        mItems = items;
    }

    private void setOnMessageListener(OnMessageListener onMessage)
    {
        mOnMessageListener = onMessage;
    }
    @SuppressWarnings("deprecation")
	private void DrawContent()
    {
        LinearLayout itemLayout = new LinearLayout(mContext);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        itemLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.white_icon_pressed));
        float scale = mContext.getResources().getDisplayMetrics().density;
        if(mDrawTitle)
        {
            RelativeLayout titleLayout = new RelativeLayout(mContext);
            LinearLayout.LayoutParams titleLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int)(40*scale));
            titleLayout.setLayoutParams(titleLayoutParam);
            itemLayout.addView(titleLayout);

            TextView titleText = new TextView(mContext);
            RelativeLayout.LayoutParams titleTextParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            titleTextParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
            titleTextParam.addRule(RelativeLayout.CENTER_VERTICAL);
            titleText.setLayoutParams(titleLayoutParam);
            titleText.setTextColor(Color.parseColor("#333333"));
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            titleText.setText(mTitle);
            titleLayout.addView(titleText);
        }

        
        int buttonHeight = 0;
        for(int i = 0; i < mItems.size(); i++)
        {
            Button button = new Button(mContext);
            LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int)(40*scale));
           
            	 buttonParam.setMargins(0,(int)(1*scale),0,0);
            
            button.setLayoutParams(buttonParam);
            button.setBackgroundDrawable(mContext.getResources().getDrawable(drawable));
            button.setTextColor(Color.parseColor("#333333"));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            button.setText(mItems.get(i).mContent);
            button.setTag(mItems.get(i));
            button.setOnClickListener(itemClickListener);
            if(mItems.get(i).mCode == "cancel")
            {
            	
            		buttonParam.setMargins(0,(int)(8*scale),0,0);
            	
            	button.setTextColor(Color.WHITE);
                button.setBackgroundDrawable(mContext.getResources().getDrawable(cancelDrawablee));
            }
            buttonHeight  += button.getMeasuredHeight();
            itemLayout.addView(button);
        }
       
        RelativeLayout rootLayout = new RelativeLayout(mContext);
        ViewGroup.LayoutParams rootLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams itemLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        itemLayout.setLayoutParams(itemLayoutParam);

        rootLayout.setBackgroundColor(Color.parseColor("#A6000000"));
        rootLayout.addView(itemLayout);
        rootLayout.setLayoutParams(rootLayoutParam);

        mPopupWindow = new PopupWindow(rootLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    View.OnClickListener itemClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            mPopupWindow.dismiss();
            PopupItem item = (PopupItem) view.getTag();

            if(mOnMessageListener != null)
            {
                mOnMessageListener.onClick(item);
            }
            mBuilder = null;
        }
    };

    public void show()
    {
        DrawContent();
        Activity activity = (Activity)mContext;
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void dismiss(){
    	mPopupWindow.dismiss();
    }

    public static class BottomBuilder
    {
        BottomScreenPopup mPopup;
        public BottomBuilder (Context context)
        {
            mPopup = new BottomScreenPopup(context);
        }

        public BottomBuilder setTitle(String title)
        {
            mPopup.setTitle(title);
            return this;
        }
        
        public BottomBuilder setMargin(boolean isMargin)
        {
            mPopup.setMargin(isMargin);
            return this;
        }
        
        public BottomBuilder setDrawable(int id)
        {
            mPopup.setDrawable(id);
            return this;
        }
        
        public BottomBuilder setCancelDrawable(int drawableId)
        {
            mPopup.setCancelDrawablee(drawableId);
            return this;
        }

        public BottomBuilder setItems(ArrayList<PopupItem> items)
        {
            mPopup.setItems(items);
            return this;
        }

        public BottomBuilder setOnMessageListener(OnMessageListener onMessage)
        {
            mPopup.setOnMessageListener(onMessage);
            return this;
        }



        public void show()
        {
            mPopup.show();
        }
        
        public void dismiss(){
        	mPopup.dismiss();
        }
    }
}
