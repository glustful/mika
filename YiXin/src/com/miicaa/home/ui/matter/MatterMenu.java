package com.miicaa.home.ui.matter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-11.
 */
public class MatterMenu
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;
    protected ArrayList<PopupItem> mItems = null;
    protected OnMessageListener mOnMessageListener;
    protected PopupWindow.OnDismissListener mOnDismissListener = null;

    private static MatterMenuBuilder mBuilder;

    public static MatterMenuBuilder builder(Context context)
    {
        mBuilder = new MatterMenuBuilder(context);
        return mBuilder;
    }

    private MatterMenu(Context context)
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

    public void show(int x,int y)
    {
        if(mItems != null && mItems.size() > 0)
        {
            drawContent();

            Activity activity = (Activity)mContext;
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(),
                    Gravity.TOP|Gravity.CENTER_HORIZONTAL, x, y);
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
        LinearLayout itemLayout = new LinearLayout(this.mContext);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_frame_matter_type_menu_bg));

        float scale = mContext.getResources().getDisplayMetrics().density;
        int rowHeight = (int)(44*scale);
        float rowWidth = 0;
        for(int i =0; i < mItems.size(); i++)
        {
            PopupItem item = mItems.get(i);
            Button button = new Button(mContext);
            button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_matter_type_menu_item_selector));
            if(item.mIcon > 0)
            {
                button.setCompoundDrawables(mContext.getResources().getDrawable(item.mIcon),null,null,null);
            }
            button.setText(item.mContent);
            button.setTag(item);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            button.setTextColor(Color.WHITE);
            button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            button.setOnClickListener(itemClickListener);

            LinearLayout.LayoutParams buttonParam
                    = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowHeight);
            button.setLayoutParams(buttonParam);

            TextView ctext = new TextView(this.mContext);
            ctext.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            ctext.setText(item.mContent);
            ctext.setLines(1);
            float tempWidth = ctext.getPaint().measureText(item.mContent) + 150 * scale ;
            rowWidth = tempWidth > rowWidth ? tempWidth : rowWidth;
            itemLayout.addView(button);

            if(i < mItems.size() -1)
            {
                ImageView split = new ImageView(mContext);
                LinearLayout.LayoutParams splitParam
                        = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                split.setImageDrawable(mContext.getResources().getDrawable(R.drawable.an_frame_matter_type_menu_split));
                split.setScaleType(ImageView.ScaleType.FIT_XY);
                split.setLayoutParams(splitParam);
                itemLayout.addView(split);
            }
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)rowWidth,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        itemLayout.setLayoutParams(params);


        RelativeLayout rootLayout = new RelativeLayout(this.mContext);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.setBackgroundColor(Color.TRANSPARENT);
        rootLayout.addView(itemLayout);

        mPopupWindow = new PopupWindow(rootLayout, rootLayout.getMeasuredWidth(), rootLayout.getMeasuredHeight(), false);
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(onDismissListener);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    public static class MatterMenuBuilder
    {
        MatterMenu mMenu;
        public MatterMenuBuilder (Context context)
        {
            mMenu = new MatterMenu(context);
        }

        public MatterMenuBuilder setItems(ArrayList<PopupItem> items)
        {
            mMenu.setItems(items);
            return this;
        }

        public MatterMenuBuilder setOnMessageListener(OnMessageListener onMessage)
        {
            mMenu.setOnMessageListener(onMessage);
            return this;
        }

        public MatterMenuBuilder setOnDissmion(PopupWindow.OnDismissListener onDissmion)
        {
            mMenu.setOnDissmion(onDissmion);
            return this;
        }

        public void show(int x, int y)
        {
            mMenu.show(x,y);
        }
    }
}
