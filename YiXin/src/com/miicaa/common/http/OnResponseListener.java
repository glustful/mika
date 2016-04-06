package com.miicaa.common.http;

/**
 * Created by Administrator on 13-9-13.
 */
public interface OnResponseListener
{
    public void OnResponse(RequestPackage reqPackage, HttpMessage msg);
}
