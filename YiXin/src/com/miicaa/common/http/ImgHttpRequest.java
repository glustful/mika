package com.miicaa.common.http;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * Created by Administrator on 13-10-25.
 */
public class ImgHttpRequest
{
    private static ImgHttpRequest mRequest = null;
    private ReentrantLock mLock;
    private BlockingQueue<Runnable> mBlockingQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private HashMap<String,ImgHttpTaskItem> mTaskQueue;
    private HashMap<String, SoftReference<Bitmap>> mImgeCache = null;

    private ImgHttpRequest()
    {

    }

    public static ImgHttpRequest getInstance()
    {
        if (mRequest == null)
        {
            synchronized (ImgHttpRequest.class)
            {
                if (mRequest == null)
                {
                    mRequest = new ImgHttpRequest();
                    mRequest.init();
                }
            }
        }
        return mRequest;
    }

    private void init()
    {
        mLock = new ReentrantLock();
        int threadPoolSize = Runtime.getRuntime().availableProcessors()*2+2;
        mBlockingQueue = new LinkedBlockingQueue<Runnable>();
        mThreadPoolExecutor = new HttpThreadPoolExecutor(threadPoolSize,threadPoolSize*2,30, TimeUnit.SECONDS,mBlockingQueue);
        mTaskQueue = new HashMap<String, ImgHttpTaskItem>();
        mImgeCache = new HashMap<String, SoftReference<Bitmap>>();
    }



    public Bitmap loadBitmap(ImageView imageView,OnImgHttpResponse onResponse, final String userCode)
    {
        mLock.lock();
        Bitmap bitmap = null;
        ImgRequestPackage imgPack = new ImgRequestPackage(userCode);
        imgPack.AddParam("usercode",userCode);
        imgPack.setOnImgHttpResponse(onResponse);
        imgPack.setImageView(imageView);

        bitmap = getBitmap(imgPack);
        if(bitmap != null)
        {
            mLock.unlock();
            return bitmap;
        }

        ImgHttpTaskItem task = new ImgHttpTaskItem(imgPack,revedHander);
        mThreadPoolExecutor.execute(task);
        mTaskQueue.put(imgPack.getMd5(),task);

        mLock.unlock();

        return bitmap;
    }

    public void uploadBitmap(Bitmap bitmap,OnImgHttpResponse onResponse,final String userCode)
    {
        ImgRequestPackage imgPack = new ImgRequestPackage(userCode);
        imgPack.mIsUpload = true;
        imgPack.mBitmap = bitmap;
        imgPack.mMethod = "POST";
        imgPack.setOnImgHttpResponse(onResponse);
        imgPack.mUrl = "/home/phone/personcenter/uploadhead";
        ImgHttpTaskItem task = new ImgHttpTaskItem(imgPack,revedHander);
        mThreadPoolExecutor.execute(task);
        mTaskQueue.put(imgPack.getMd5(), task);
    }

    private Bitmap getBitmap(ImgRequestPackage imgPack)
    {
        if(mImgeCache.containsKey(imgPack.mFileName))
        {

            SoftReference<Bitmap> reference = mImgeCache.get(imgPack.mFileName);
            Bitmap bitmap = reference.get();

            if(bitmap != null)
            {
                return bitmap;
            }
            else
            {
                mImgeCache.remove(imgPack.mFileName);
            }
        }
        else
        {
            File cacheDir = new File(imgPack.mLocalDir);
            File[] cacheFiles = cacheDir.listFiles();
            Boolean find = false;
            for(int i=0; i<cacheFiles.length; i++)
            {
                if(imgPack.mFileName.equals(cacheFiles[i].getName()))
                {
                    find = true;
                    break;
                }
            }
            if(find)
            {
                String filepath = imgPack.mLocalPath;
                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                mImgeCache.put(imgPack.mFileName, new SoftReference<Bitmap>(bitmap));
                return bitmap;
            }
        }

        return null;
    }



    private Handler revedHander = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle = msg.getData();
            ImgRequestPackage requstPackage = (ImgRequestPackage)bundle.getSerializable("package");
            ImgHttpMessage httpMessage = (ImgHttpMessage)bundle.getSerializable("msg");
            if(httpMessage == null || requstPackage == null)
                return;
            if(httpMessage.mResCode == MessageId.FILE_DOWNLOAD_STREAM_GET
                    && httpMessage.mStream != null)
            {
                Bitmap bitmap = BitmapFactory.decodeStream(httpMessage.mStream);
                mImgeCache.put(requstPackage.mFileName, new SoftReference<Bitmap>(bitmap));

                OnImgHttpResponse onImgResponse = requstPackage.getOnImgHttpResponse();

                if(onImgResponse != null)
                {
                    ImageView view = requstPackage.mImageView.get();
                    String tag = requstPackage.mFileName;
                    onImgResponse.onResponse(view,bitmap,tag);
                }
            }
            else if(httpMessage.mResCode == MessageId.HTTP_RESPONSE_UPLOAD_SUCCESS)
            {
                mImgeCache.remove(requstPackage.mFileName);

                File cacheDir = new File(requstPackage.mLocalDir);
                File[] cacheFiles = cacheDir.listFiles();
                Boolean find = false;
                for(int i=0; i<cacheFiles.length; i++)
                {
                    if(requstPackage.mFileName.equals(cacheFiles[i].getName()))
                    {
                        cacheFiles[i].delete();
                        break;
                    }
                }
                OnImgHttpResponse onImgResponse = requstPackage.getOnImgHttpResponse();

                if(onImgResponse != null)
                {
                    onImgResponse.onUpload(httpMessage);
                }

            }
            else if(httpMessage.mResCode == MessageId.HTTP_RESPONSE_UPLOAD_FAILURE)
            {
                OnImgHttpResponse onImgResponse = requstPackage.getOnImgHttpResponse();

                if(onImgResponse != null)
                {
                    onImgResponse.onUpload(httpMessage);
                }
            }
        }
    };

    public class HttpThreadPoolExecutor extends ThreadPoolExecutor
    {
        public HttpThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                      long keepAliveTime, TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue)
        {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
        @Override
        public void beforeExecute(Thread t, Runnable r)
        {
            super.beforeExecute(t, r);
        }

        @Override
        public void afterExecute(Runnable r, Throwable t)
        {
            mLock.lock();
            ImgHttpTaskItem taskItem = (ImgHttpTaskItem)r;
            String reqMd5 = taskItem.getIdentifier();
            mTaskQueue.remove(reqMd5);
            mLock.unlock();
            super.afterExecute(r, t);
        }
    }
}
