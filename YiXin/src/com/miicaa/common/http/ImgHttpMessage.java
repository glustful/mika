package com.miicaa.common.http;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 13-10-28.
 */
public class ImgHttpMessage implements Serializable
{
    private static final long serialVersionUID = -7060210544600464481L;

    public int mResCode;
    public String mResMsg;
    public double mProgress;
    public long mSize;
    public InputStream mStream = null;
}
