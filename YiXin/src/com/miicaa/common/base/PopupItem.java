package com.miicaa.common.base;

/**
 * Created by Administrator on 13-11-29.
 */
public class PopupItem
{
    public String mContent;
    public String mCode;
    public int mIcon;
    public Object mTag;

    public PopupItem(String content, String code)
    {
        mContent = content;
        mCode = code;
        mIcon = -1;
        mTag = null;
    }

    public PopupItem(String content, String code,int drawId)
    {
        mContent = content;
        mCode = code;
        mIcon = drawId;
        mTag = null;
    }

    public PopupItem(String content, String code,int drawId,Object tag)
    {
        mContent = content;
        mCode = code;
        mIcon = drawId;
        mTag = tag;
    }
}
