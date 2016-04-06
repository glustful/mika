package com.miicaa.home.ui.org;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-19.
 */
public class DiscussList
{
    Context mContext;
    ScrollView mScroll;
    LinearLayout mChildrenLayout;
    LayoutInflater mInflater;
    int count = 0;

    public DiscussList(Context context, ScrollView scroll, LinearLayout childrenLayout)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mScroll = scroll;
        mChildrenLayout = childrenLayout;
    }

    public void appendContent(String content)
    {
        Boolean right = false;
        if(count%2 == 0)
        {
            right = true;
        }
        View view =mInflater.inflate(R.layout.discuss_cell_right_view,null);
        ImageView leftImg = (ImageView) view.findViewById(R.id.discuss_cell_id_left_head);
        ImageView rightImg = (ImageView) view.findViewById(R.id.discuss_cell_id_right_head);
        TextView contentText = (TextView)view.findViewById(R.id.discuss_cell_id_content);

        Date date = new Date();
        content = content + "(" + new SimpleDateFormat("yyyy-MM-dd HH:MM").format(date.getTime()) + ")";
        contentText.setText(content);
        if(right)
        {
            leftImg.setVisibility(View.GONE);
        }
        else
        {
            rightImg.setVisibility(View.GONE);
        }

        mChildrenLayout.addView(view);
        count++;
    }

}
