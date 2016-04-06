package com.miicaa.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.miicaa.home.data.old.UserAccount;

/**
 * Created by Administrator on 13-9-13.
 */
public class RequestPackage implements Serializable
{
    private static final long sSerVerUId = -4903107312403938616L;
    public String mHost;
    public String mUrl;
    public String mMethod;
    public HashMap<String,String> mParams;
    public int mReqTimeout;

    //文件下载相关参数
    public boolean mDownup;
    public String mFileName;
    public String mLocalDir;
    public String mLocalPath;

    public int mTag;
    public String mTagString;
    public String mReqMd5;
    public long mReqTimestamp;
    public Boolean mIsLogin;
    public boolean mIsValidReq;

    public Object mReqTag;

    private OnResponseListener mOnResponseListener;

    public RequestPackage()
    {
        this.mHost = UserAccount.getSeverHost();
        this.mUrl = null;
        this.mMethod = "POST";
        this.mParams = new HashMap<String, String>();
        this.mReqTimeout = 10;

        this.mDownup = false;
        this.mFileName = null;

        //系统目录
        this.mLocalDir = UserAccount.getLocalDir("filecache/");
        this.mLocalPath = null;

        //相关标示
        this.mTag = -1;
        this.mTagString = "";
        this.mReqTimestamp = 0L;
        this.mReqMd5 = null;
        this.mIsValidReq = true;
        this.mIsLogin = false;
        mOnResponseListener = null;
    }

    public void setOnResponseListener(OnResponseListener onResponseListener)
    {
        mOnResponseListener = onResponseListener;
    }

    public OnResponseListener getOnResponseListener()
    {
        return mOnResponseListener;
    }

    public void  setTimestamp()
    {
        this.mReqTimestamp = System.currentTimeMillis()/1000L;
    }

    public boolean checkParameter()
    {
        boolean rc = true;
        try
        {

            if(null == this.mHost)
                throw new StringIndexOutOfBoundsException("host eror");
            if(null == this.mUrl)
                throw new StringIndexOutOfBoundsException("eror");
            if(null == this.mMethod)
                throw new StringIndexOutOfBoundsException("eror");

            if(this.mDownup)
            {
                if(null == this.mFileName)
                {
                    throw new StringIndexOutOfBoundsException("eror");
                }
                if(null == this.mLocalDir)
                {
                    throw new StringIndexOutOfBoundsException("eror");
                }
                if(null == this.mLocalPath)
                {
                    throw new StringIndexOutOfBoundsException("eror");
                }
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            rc = false;
        }

        return rc;
    }

    public String getMd5()
    {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(baos);

            oos.writeObject(mHost);
            oos.writeObject(mUrl);
            oos.writeObject(mMethod);
            oos.writeObject(mParams);
            oos.write(mReqTimeout);
            oos.writeBoolean(mDownup);
            oos.writeObject(mFileName);
            oos.writeObject(mLocalDir);
            oos.writeObject(mLocalPath);
            oos.writeInt(mTag);
            oos.writeObject(mTagString);
            oos.writeLong(mReqTimestamp);
            oos.close();
        }
        catch (IOException e1)
        {

            e1.printStackTrace();

            return result;
        }

        MessageDigest messageDigest;
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(baos.toByteArray());
            result = byte2hex(messageDigest.digest());

            this.mReqMd5 = result;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public String byte2hex(byte[] b)
    {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1)
            {
                hs = hs + "0" + stmp;
            }
            else
            {
                hs = hs + stmp;
            }
        }

        return hs;
    }

    public void AddParam(String key, String value)
    {
        mParams.put(key,value);
    }


}
