package com.miicaa.home.ui.frame;

import android.view.View;

/**
 * Created by Administrator on 13-11-25.
 */
public interface IFrameChild
{
    public View getRootView();
    public void refresh();
    public void setMsg(String msg);
}
