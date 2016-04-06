package com.miicaa.home.data.business;

import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-12-17.
 */
public interface OnBusinessResponse
{
    public void onProgress(ProgressMessage msg);
    public void onResponse(ResponseData data);
}
