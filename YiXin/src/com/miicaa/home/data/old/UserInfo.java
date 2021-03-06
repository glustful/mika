package com.miicaa.home.data.old;

/**
 * Created by Administrator on 13-11-22.
 */
public class UserInfo
{
    private String mUserName;
    private String mUserCode;
    private OrgInfo mOrg;

    public UserInfo(String name, String code)
    {
        mUserName = name;
        mUserCode = code;
        mOrg = null;
    }

    public void setOrg(OrgInfo org)
    {
        mOrg = org;
    }

    public String getName()
    {
        return mUserName;
    }

    public String getCode()
    {
        return mUserCode;
    }

    public OrgInfo getOrg()
    {
        return mOrg;
    }
}
