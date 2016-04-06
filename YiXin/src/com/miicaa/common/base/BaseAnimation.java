package com.miicaa.common.base;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/**
 * Created by Administrator on 13-11-25.
 */
public class BaseAnimation extends Animation
{


    View mView;
    InterpolatedTimeListener mListener = null;
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        super.applyTransformation(interpolatedTime, t);
        if(mListener != null)
        {
            mListener.interpolatedTime(interpolatedTime, mView);
        }
    }

    public void setInterpolatedTimeListener(InterpolatedTimeListener listener)
    {
        mListener = listener;
    }

    public void startAnimForView(View view)
    {
        mView = view;
        view.startAnimation(this);
    }

    public static interface InterpolatedTimeListener
    {
        public void interpolatedTime(float interpolatedTime, View view);
    }
}
