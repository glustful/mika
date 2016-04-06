package com.miicaa.home.ui.matter;

import android.view.View;

/**
 * Created by Administrator on 13-12-4.
 */
public interface IMatterHome
{
    public View getRootView();
    public void refresh();
    public void setMsg(String msg);
    public void setMatterHead(MatterHead head);
}
