package com.miicaa.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.miicaa.home.data.old.UserAccount;

/**
 * Created by Administrator on 13-10-25.
 */
public class ImgRequestPackage implements Serializable
{
    private static final long sSerVerUId = -4903107312403938616L;
    public String mHost;
    public String mUrl;
    public String mMethod;
    public HashMap<String,String> mParams;
    public int mReqTimeout;

    public String mFileName;
    public String mLocalDir;
    public String mLocalPath;

    public String mTagString;
    public String mReqMd5;
    public long mReqTimestamp;
    public Boolean mIsUpload;
    Bitmap mBitmap;

    public boolean mIsValidReq;
    SoftReference<ImageView> mImageView;

    private OnImgHttpResponse mResponse = null;

    public ImgRequestPackage(String fileName)
    {
        this.mHost = UserAccount.getSeverHost();
        this.mUrl = "/home/phone/personcenter/showhead";
        this.mMethod = "GET";
        this.mParams = new HashMap<String, String>();
        this.mReqTimeout = 10;
        this.mFileName = fileName;
        this.mLocalDir = UserAccount.getLocalDir("imgCache/");
        this.mLocalPath = mLocalDir +mFileName;
        this.mIsUpload = false;
        this.mBitmap = null;
        //相关标示
        this.mTagString = "";
        this.mReqTimestamp = 0L;
        this.mReqMd5 = null;
        this.mIsValidReq = true;
        mResponse = null;
        this.mReqMd5 = getMd5() ;
    }

    public void setOnImgHttpResponse(OnImgHttpResponse onResponse)
    {
        mResponse = onResponse;
    }

    public OnImgHttpResponse getOnImgHttpResponse()
    {
        return mResponse;
    }

    public void setImageView(ImageView img)
    {
        mImageView = new SoftReference<ImageView>(img);
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
            oos.writeObject(mFileName);
            oos.writeObject(mLocalDir);
            oos.writeObject(mLocalPath);
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
