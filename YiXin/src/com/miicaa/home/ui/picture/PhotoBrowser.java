package com.miicaa.home.ui.picture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LM on 14-4-10.
 */
public class PhotoBrowser extends ViewGroup{

    public PhotoBrowser(Context context){
        super(context);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        for(int i = 0 ; i<getChildCount();i++){
            View v = getChildAt(i);
            v.layout(l,t,r,b);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
