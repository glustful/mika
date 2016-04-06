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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-11-29.
 */
public class LinearMessageBox
{
    protected PopupWindow mPopupWindow = null;
    protected Context mContext = null;
    protected ArrayList<PopupItem> mItems = null;
    protected OnMessageListener mOnMessageListener;

    private static LinearMessageBuilder mBuilder;

    public static LinearMessageBuilder builder(Context context)
    {
        mBuilder = new LinearMessageBuilder(context);
        return mBuilder;
    }

    private LinearMessageBox(Context context)
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

    public void show()
    {
        if(mItems != null && mItems.size() > 0)
        {
            drawContent();

            Activity activity = (Activity)mContext;
            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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

    private void drawContent()
    {
        LinearLayout itemLayout = new LinearLayout(this.mContext);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.an_round_all_normal));

        float scale = mContext.getResources().getDisplayMetrics().density;
        int rowHeight = (int)(40*scale);
        float rowWidth = 0;
        for(int i =0; i < mItems.size(); i++)
        {
            PopupItem item = mItems.get(i);
            Button button = new Button(mContext);
            button.setBackgroundDrawable(null);
            if(item.mIcon > 0)
            {
                button.setCompoundDrawables(mContext.getResources().getDrawable(item.mIcon),null,null,null);
            }
            button.setText(item.mContent);
            button.setTag(item);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            button.setOnClickListener(itemClickListener);
            button.setPadding((int)(10*scale),0,0,0);

            LinearLayout.LayoutParams buttonParam
                    = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, rowHeight);
            button.setLayoutParams(buttonParam);

            TextView ctext = new TextView(this.mContext);
            ctext.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
            ctext.setText(item.mContent);
            ctext.setLines(1);
            float tempWidth = ctext.getPaint().measureText(item.mContent) + 80 * scale ;
            rowWidth = tempWidth > rowWidth ? tempWidth : rowWidth;
            itemLayout.addView(button);

            if(i < mItems.size() -1)
            {
                ImageView split = new ImageView(mContext);
                LinearLayout.LayoutParams splitParam
                        = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
                split.setBackgroundColor(Color.parseColor("#CACACA"));
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
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.parseColor("#75000000"));
        rootLayout.addView(itemLayout);

        mPopupWindow = new PopupWindow(rootLayout, rootLayout.getMeasuredWidth(), rootLayout.getMeasuredHeight(), false);
        mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    public static class LinearMessageBuilder
    {
        LinearMessageBox mMessageBox;
        public LinearMessageBuilder (Context context)
        {
            mMessageBox = new LinearMessageBox(context);
        }

        public LinearMessageBuilder setItems(ArrayList<PopupItem> items)
        {
            mMessageBox.setItems(items);
            return this;
        }

        public LinearMessageBuilder setOnMessageListener(OnMessageListener onMessage)
        {
            mMessageBox.setOnMessageListener(onMessage);
            return this;
        }

        public void show()
        {
            mMessageBox.show();
        }
    }
}
