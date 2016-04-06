package com.miicaa.common.http;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.client.CookieStore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Created by Administrator on 13-9-12.
 */
public class PhoneHttpRequest
{
    private static PhoneHttpRequest mRequest = null;
    private ReentrantLock mLock;
    private CookieStore mCookieStore;
    private BlockingQueue<Runnable> mBlockingQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private HashMap<String,HttpTaskItem> mTaskQueue;

    private PhoneHttpRequest()
    {

    }

    public static PhoneHttpRequest getInstance()
    {
        if (mRequest == null)
        {
            synchronized (PhoneHttpRequest.class)
            {
                if (mRequest == null)
                {
                    mRequest = new PhoneHttpRequest();
                    mRequest.init();
                }
            }
        }
        return mRequest;
    }

    private void init()
    {
        mLock = new ReentrantLock();
        clearCookie();

        int threadPoolSize = Runtime.getRuntime().availableProcessors()*2+2;

        mBlockingQueue = new LinkedBlockingQueue<Runnable>();
        mThreadPoolExecutor = new HttpThreadPoolExecutor(threadPoolSize,threadPoolSize*2,30, TimeUnit.SECONDS,mBlockingQueue);
        mTaskQueue = new HashMap<String, HttpTaskItem>();
    }

    //设置cookie
    public void setCookie(CookieStore cookie)
    {
        mCookieStore = cookie;
    }

    //获取cookie
    public CookieStore getCookie()
    {
        return mCookieStore;
    }

    //清楚cookie
    public void clearCookie()
    {
        mCookieStore = null;
    }

    //添加请求任务
    private void runRequestTask(RequestPackage reqPack)
    {
        HttpTaskItem task = new HttpTaskItem(reqPack,revedHander);
        mThreadPoolExecutor.execute(task);
        mTaskQueue.put(reqPack.mReqMd5,task);
    }

    //发送请求
    public boolean sendRequest(RequestPackage reqPackage)
    {
        boolean res = false;
        if (reqPackage.mDownup && reqPackage.mFileName != null && reqPackage.mFileName.length()>0){
            reqPackage.mLocalPath = reqPackage.mLocalDir +reqPackage.mFileName;
        }
        if(!reqPackage.checkParameter())
            return res;
        runRequestTask(reqPackage);

        return res;
    }

    public boolean cancelTask(String reqMd5)
    {
        boolean rc = false;
        if (mLock.tryLock())
        {
            HttpTaskItem task = mTaskQueue.get(reqMd5);
            if (null != task)
            {
                task.cancelHttpOp();
                mTaskQueue.remove(reqMd5);
                rc = true;
            }
            mLock.unlock();
        }

        return rc;
    }

    private Handler revedHander = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle = msg.getData();
            RequestPackage requstPackage = (RequestPackage)bundle.getSerializable("package");
            HttpMessage httpMessage = (HttpMessage)bundle.getSerializable("msg");
            if(httpMessage == null || requstPackage == null)
                return;

            OnResponseListener onResponseListener = requstPackage.getOnResponseListener();
            if(onResponseListener != null)
            {
                onResponseListener.OnResponse(requstPackage,httpMessage);
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

            HttpTaskItem taskItem = (HttpTaskItem)r;
            String reqMd5 = taskItem.getIdentifier();
            mTaskQueue.remove(reqMd5);
            mLock.unlock();
            super.afterExecute(r, t);
        }
    }








}
