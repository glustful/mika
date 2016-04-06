package com.miicaa.home.ui.frame;

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
 * Created by Administrator on 14-2-11.
 */
public class DetectManager
{
    private static DetectManager mManager = null;

    private ReentrantLock mLock;
    private CookieStore mCookieStore;
    private BlockingQueue<Runnable> mBlockingQueue;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private HashMap<String,DetectDataInit> mTaskQueue;

    public static DetectManager getInstance()
    {
        if (mManager == null)
        {
            synchronized (DetectManager.class)
            {
                if (mManager == null)
                {
                    mManager = new DetectManager();
                    mManager.init();
                }
            }
        }
        return mManager;
    }

    private void init()
    {
        mLock = new ReentrantLock();

        int threadPoolSize = Runtime.getRuntime().availableProcessors()*2+2;

        mBlockingQueue = new LinkedBlockingQueue<Runnable>();
        mThreadPoolExecutor = new DetectedThreadPoolExecutor(threadPoolSize,threadPoolSize*2,30, TimeUnit.SECONDS,mBlockingQueue);
        mTaskQueue = new HashMap<String, DetectDataInit>();
    }

    private void execute(DetectAdapter adpater)
    {
        DetectDataInit task = new DetectDataInit(adpater,revedHander);
        mThreadPoolExecutor.execute(task);
        mTaskQueue.put(adpater.getId(),task);
    }

    protected boolean sendDected(DetectAdapter adpater)
    {
        execute(adpater);
        return true;
    }

    private Handler revedHander = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle = msg.getData();

            DetectAdapter adpater = (DetectAdapter)bundle.getSerializable("adpater");

            if(adpater == null)
            {
                return;
            }
            adpater.response();
        }
    };

    public class DetectedThreadPoolExecutor extends ThreadPoolExecutor
    {
        public DetectedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
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

            DetectDataInit taskItem = (DetectDataInit)r;
            String id = taskItem.getId();
            mTaskQueue.remove(id);
            mLock.unlock();
            super.afterExecute(r, t);
        }
    }
}
