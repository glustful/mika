package com.miicaa.home.ui.frame;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Administrator on 14-2-11.
 */
public class DetectAdapter implements Serializable {

    private static final long sSerVerUId = -4903107312403938616L;
    DetectRespones mOnResponse = null;

    protected String mId;

    final protected String getId()
    {
        return mId;
    }

    final protected void setId(String id)
    {
        mId = id;
    }

    public DetectAdapter setResponse(DetectRespones onResponse)
    {
        mOnResponse = onResponse;
        return this;
    }

    public DetectAdapter()
    {
        setId(UUID.randomUUID().toString());
    }

    public void response()
    {
        if(mOnResponse != null)
        {
            mOnResponse.onResponse();
        }
    }

    final public Boolean notifyRequest()
    {
        return DetectManager.getInstance().sendDected(this);
    }
}
