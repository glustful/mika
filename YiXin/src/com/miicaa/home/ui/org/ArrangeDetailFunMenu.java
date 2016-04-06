package com.miicaa.home.ui.org;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-20.
 */
public class ArrangeDetailFunMenu
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;
    protected ArrayList<PopupItem> mItems = null;
    protected OnMessageListener mOnMessageListener;
    protected PopupWindow.OnDismissListener mOnDismissListener = null;

    private static ArrangDetailFunMenuBuilder mBuilder;

    public static ArrangDetailFunMenuBuilder builder(Context context)
    {
        mBuilder = new ArrangDetailFunMenuBuilder(context);
        return mBuilder;
    }

    private ArrangeDetailFunMenu(Context context)
    {
        this.mContext = context;
    }

    private void setItems(ArrayList<PopupItem> items)
    {
        mItems = items;
    }

    private void setOnMessageListener(OnMessageListener onMessage)
    {
        mOnMessageListener = onMessage;
    }

    private void setOnDissmion(PopupWindow.OnDismissListener onDissmion)
    {
        mOnDismissListener = onDissmion;
    }

    public void show(View view, int x,int y)
    {
        if(mItems != null && mItems.size() > 0)
        {
            drawContent();
            mPopupWindow.showAsDropDown(view,x, y);
        }
    }

    public void show(int x,int y)
    {
        if(mItems != null && mItems.size() > 0)
        {
            Activity activity = (Activity)mContext;
            drawContent();
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(),//window中最顶层视图，根视图
                    Gravity.BOTTOM|Gravity.LEFT,x, y);
        }
    }

    View.OnClickListener itemClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            // TODO Auto-generated method stub
            mPopupWindow.dismiss();
            PopupItem item = (PopupItem) view.getTag();

            if(mOnMessageListener != null)
            {
                mOnMessageListener.onClick(item);
            }
            mBuilder = null;
        }
    };

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            if(mOnDismissListener != null)
            {
                mOnDismissListener.onDismiss();
            }
        }
    };

    private void drawContent()
    {
//        LinearLayout itemLayout = new LinearLayout(this.mContext);
//        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
//        itemLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_window_top));

        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_menu_layout,null);
        LinearLayout itemLayout = (LinearLayout)view.findViewById(R.id.detail_menu_top);
        float scale = mContext.getResources().getDisplayMetrics().density;
        int rowHeight = (int)(40*scale);
        float rowWidth = 0;
        for(int i =0; i < mItems.size(); i++)
        {
            int margin = (int)(4*scale);
            int rMargin = (int)(2*scale);
            PopupItem item = mItems.get(i);
            Button button = new Button(mContext);
            button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.save_selector));

            button.setText(item.mContent);
            button.setTag(item);
            button.setLines(1);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            button.setTextColor(Color.parseColor("#333333"));
            button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            button.setOnClickListener(itemClickListener);
            LinearLayout.LayoutParams buttonParam
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rowHeight);
            button.setLayoutParams(buttonParam);
            buttonParam.setMargins(rMargin,margin,margin,margin);
            itemLayout.addView(button);
        }

//        RelativeLayout.LayoutParams params =
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0,0,0,0);
//        itemLayout.setLayoutParams(params);
//        RelativeLayout bg = new RelativeLayout(mContext);
//        bg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_window_top));
//        bg.addView(itemLayout);
//        LinearLayout rootLayout = new LinearLayout(this.mContext);
//        RelativeLayout.LayoutParams rootParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//        rootLayout.setLayoutParams(rootParam);
//
//        rootLayout.setBackgroundColor(Color.TRANSPARENT);
//        rootLayout.setOrientation(LinearLayout.VERTICAL);
//        ImageView popBottomView = new ImageView(mContext);
//        LinearLayout.LayoutParams bottomParam = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        bottomParam.setMargins(100,0,0,0);
//        popBottomView.setLayoutParams(bottomParam);
//        popBottomView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.pop_window_bottom_y));
//
//        rootLayout.addView(bg);
//        rootLayout.addView(popBottomView);

        mPopupWindow = new PopupWindow(view, view.getMeasuredWidth(), view.getMeasuredHeight(), false);
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(onDismissListener);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    public static class ArrangDetailFunMenuBuilder
    {
        ArrangeDetailFunMenu mMenu;
        public ArrangDetailFunMenuBuilder (Context context)
        {
            mMenu = new ArrangeDetailFunMenu(context);
        }

        public ArrangDetailFunMenuBuilder setItems(ArrayList<PopupItem> items)
        {
            mMenu.setItems(items);
            return this;
        }

        public ArrangDetailFunMenuBuilder setOnMessageListener(OnMessageListener onMessage)
        {
            mMenu.setOnMessageListener(onMessage);
            return this;
        }

        public ArrangDetailFunMenuBuilder setOnDissmion(PopupWindow.OnDismissListener onDissmion)
        {
            mMenu.setOnDissmion(onDissmion);
            return this;
        }

        public void show(View v,int x, int y)
        {
            mMenu.show(v,x,y);
        }

        public void show(int x, int y)
        {
            mMenu.show(x,y);
        }


    }
}
