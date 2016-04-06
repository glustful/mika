package com.miicaa.home.ui.org;

import java.util.ArrayList;

import com.miicaa.home.data.business.org.UserInfo;

/**
 * Created by Administrator on 14-1-8.
 */
public class MatterMessage {
    public MessageType mType;
    public String mStatus;
    public ArrayList<UserInfo> mAppvoralArray;
    public ArrayList<UserInfo> mCcArray;
    public CharSequence mMsgContent;
    public ArrayList<String> mPaths;

    public MatterMessage()
    {
        mType = null;
        mMsgContent = null;
        mStatus = null;
        mAppvoralArray = new ArrayList<UserInfo>();
        mCcArray = new ArrayList<UserInfo>();
        mPaths = new ArrayList<String>();
    }

    final public void setType(MessageType type)
    {
        mType = type;
    }

    final public MessageType getType()
    {
        return mType;
    }

    final public void addAppvoral(UserInfo userInfo)
    {
        mAppvoralArray.add(userInfo);
    }

    final public int appvoralSize()
    {
        return mAppvoralArray.size();
    }

    final public UserInfo getAppvoral(int i)
    {
        if(mAppvoralArray == null || mAppvoralArray.size() == 0)
        {
            return null;
        }
        return mAppvoralArray.get(i);
    }

    final public void addCc(UserInfo userInfo)
    {
        mCcArray.add(userInfo);
    }

    final public int CcSize()
    {
        return mCcArray.size();
    }

    final public UserInfo getCc(int i)
    {
        return mCcArray.get(i);
    }

    final public void setMsg(CharSequence msg)
    {
        mMsgContent = msg;
    }

    final public CharSequence getMsg()
    {
        return mMsgContent;
    }

    final public void addPath(String path)
    {
        mPaths.add(path);
    }

    final public int pathSize()
    {
        return mPaths.size();
    }

    final public String getPath(int i)
    {
        return mPaths.get(i);
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmStatus() {
        return mStatus;
    }
}
