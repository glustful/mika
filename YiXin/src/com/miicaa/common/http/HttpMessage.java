package com.miicaa.common.http;

import java.io.Serializable;

/**
 * Created by Administrator on 13-9-13.
 */
public class HttpMessage implements Serializable
{
    private static final long serialVersionUID = -7060210544600464481L;

    public int mResCode;
    public String mResMsg;
    public double mProgress;
    public long mSize;
    public byte[] receivedData = null;
}
