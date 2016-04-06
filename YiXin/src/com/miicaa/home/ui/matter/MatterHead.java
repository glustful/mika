package com.miicaa.home.ui.matter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 13-12-4.
 */
public class

        MatterHead
{
    public Button mTitleButton;
    public ImageView mSelImg;
    int mMatterCount;
    Boolean mSelected;
    String mTitle;

    public MatterHead(Button btn,ImageView sel,String title)
    {
        mTitleButton = btn;
        mSelImg = sel;
        mTitle = title;
        setMatterCount(0);
        setSelected(false);
    }

    public void setSelected(Boolean selected)
    {
        mSelected = selected;
        if(selected)
        {
            mTitleButton.setTextColor(Color.parseColor("#0088D6"));
            mSelImg.setVisibility(View.VISIBLE);
        }
        else
        {
            mTitleButton.setTextColor(Color.parseColor("#333333"));
            mSelImg.setVisibility(View.GONE);
        }
    }

    public void setMatterCount(int count)
    {
        mMatterCount = count;
        setTitle(mTitle, mMatterCount);
    }

    private void setTitle(String title, int count)
    {
        String content = title + "(" + String.valueOf(count) + ")";
        mTitleButton.setText(content);
        Context context = mTitleButton.getContext();
        float scale = context.getResources().getDisplayMetrics().density;

        TextView ctext = new TextView(context);
        ctext.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (14.0));
        ctext.setText(content);
        ctext.setLines(1);
        int tempWidth = (int)(ctext.getPaint().measureText(content)+ 10 * scale) ;
        int tempHeight = (int)(4*scale);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tempWidth,tempHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mSelImg.setLayoutParams(params);

    }


}
