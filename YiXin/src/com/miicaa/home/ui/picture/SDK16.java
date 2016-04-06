package com.miicaa.home.ui.picture;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by Administrator on 13-12-10.
 */
public class SDK16
{
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void postOnAnimation(View view, Runnable r)
    {
        view.postOnAnimation(r);
    }
}
