package com.miicaa.common.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 13-9-13.
 */
public class HttpTaskItem implements Runnable
{
    private static final int sGET = 0;
    private static final int sPOST = 1;

    private static final int mRevBufferSize = 1024*2;
    private boolean mStopRunning;
    private int mMethod = sGET;
    private RequestPackage mReqPackage;
    private DefaultHttpClient mHttpClient;
    private HttpContext mHttpContext = null;
    private Handler mRevedHander;
    public String mId;

    public HttpTaskItem (RequestPackage reqPackage,Handler revedHandler)
    {
        this.mRevedHander = revedHandler;

        this.mReqPackage = reqPackage;

        this.mStopRunning = false;

        if(mReqPackage.mMethod.equals("POST"))
            this.mMethod = sPOST;
        else if(mReqPackage.mMethod.equals("GET"))
            this.mMethod = sGET;
        mHttpContext = new BasicHttpContext();
    }

    public String getIdentifier()
    {
        return mReqPackage.mReqMd5;
    }

    public void cancelHttpOp()
    {
        this.mStopRunning = true;
        mHttpClient.getConnectionManager().shutdown();
    }

    @Override
    public void run()
    {
        if(!mReqPackage.mIsValidReq)
        {
            return ;
        }

        String url = mReqPackage.mHost + mReqPackage.mUrl;


        try
        {
            CookieStore cookie = PhoneHttpRequest.getInstance().getCookie();
            mHttpClient =  getHttpClient();
            if(cookie != null)
            {
                mHttpClient.setCookieStore(cookie);
            }

            HttpResponse httpResponse = null;

            switch (mMethod)
            {
                case sGET:
                {
                    if((null != mReqPackage.mParams) && (mReqPackage.mParams.size()>0))
                    {
                        url += "?";
                        for(Map.Entry<String,String> entry:mReqPackage.mParams.entrySet())
                        {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if(value instanceof String)
                            {
                                url += key + "=" + value;
                                url += "&";
                            }
                        }
                        url = url.substring(0,url.length() - 1);
                    }
                    httpResponse = mHttpClient.execute(new HttpGet(url),mHttpContext);
                    int statusCode = this.getHttpResponseStatusCode(httpResponse);
                    if((statusCode > 199) && (statusCode < 400))
                    {
                        if(mReqPackage.mIsLogin && mReqPackage.mIsValidReq)
                        {
                            cookie = mHttpClient.getCookieStore();
                            if(null != cookie)
                            {
                                PhoneHttpRequest.getInstance().setCookie(cookie);
                            }

                        }
                        if(mReqPackage.mDownup)
                        {
                            HttpEntity entity = httpResponse.getEntity();
                            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                            InputStream is = bufHttpEntity.getContent();
                            downloadFileForPost(bufHttpEntity.getContent(),mReqPackage.mLocalDir + mReqPackage.mFileName);

//                            Header header = httpResponse.getFirstHeader("Content-Length");
//                            if(null != header)
//                            {
//                                long fileSize = Integer.valueOf(header.getValue());
//                                downloadFile(httpResponse.getEntity().getContent(),fileSize,mReqPackage.mLocalDir + mReqPackage.mFileName);
//                            }
                        }
                        else
                        {
                            HttpMessage msg = new HttpMessage();
                            msg.mResCode = MessageId.HTTP_RESPONSE_RECEIVE_END;
                            msg.mResMsg = "获取数据成功";
                            msg.mSize = 0;
                            msg.mProgress = 1.0;
                            msg.receivedData = EntityUtils.toByteArray(httpResponse.getEntity());
                            sendRsponseMessage(msg);
                        }
                    }
                    else
                    {
                        HttpMessage msg = new HttpMessage();
                        msg.mResCode = statusCode;
                        msg.mResMsg = "请求:" + url + "失败,statusCode:" + statusCode;
                        msg.mSize = 0;
                        msg.mProgress = 1.0;
                        msg.receivedData = null;
                        sendRsponseMessage(msg);
                    }
                    break;
                }
                case sPOST:
                {
                    HttpPost httpPost = new HttpPost(url);
                    if((null != mReqPackage.mParams) && (!mReqPackage.mDownup)) //文件上传的参数不能在此处设置
                    {
                        List<NameValuePair> reqPar = new ArrayList<NameValuePair>();
                        for(Map.Entry<String,String> entry:mReqPackage.mParams.entrySet())
                        {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            reqPar.add(new BasicNameValuePair(key,value));
                        }
                        httpPost.setEntity(new UrlEncodedFormEntity(reqPar,"UTF-8"));
                    }
                    httpResponse = mHttpClient.execute(httpPost);
                    int statusCode = this.getHttpResponseStatusCode(httpResponse);
                    if((statusCode > 199) && (statusCode < 400) )
                    {
                        if(mReqPackage.mIsLogin && mReqPackage.mIsValidReq)
                        {
                            cookie = mHttpClient.getCookieStore();
                            if(null != cookie)
                            {
                                PhoneHttpRequest.getInstance().setCookie(cookie);
                            }
                        }
                        if(mReqPackage.mDownup)
                        {
                            HttpEntity entity = httpResponse.getEntity();
                            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                            InputStream is = bufHttpEntity.getContent();
                            downloadFileForPost(bufHttpEntity.getContent(),mReqPackage.mLocalDir + mReqPackage.mFileName);

//                            Header header = httpResponse.getFirstHeader("Content-Length");
//                            if(null != header)
//                            {
//                                long fileSize = Integer.valueOf(header.getValue()); // 总字节数据
//                                downloadFile(httpResponse.getEntity().getContent(),fileSize,mReqPackage.mLocalDir + mReqPackage.mFileName);
//                            }
                        }
                        else
                        {
                            HttpMessage msg = new HttpMessage();
                            msg.mResCode = MessageId.HTTP_RESPONSE_RECEIVE_END;
                            msg.mResMsg = "获取数据成功";
                            msg.mSize = 0;
                            msg.mProgress = 1.0;
                            msg.receivedData = EntityUtils.toByteArray(httpResponse.getEntity());
                            sendRsponseMessage(msg);
                        }
                    }
                    else
                    {
                        HttpMessage msg = new HttpMessage();
                        msg.mResCode = statusCode;
                        msg.mResMsg = "请求:" + url + "失败,statusCode:" + statusCode;
                        msg.mSize = 0;
                        msg.mProgress = 1.0;
                        msg.receivedData = null;
                        sendRsponseMessage(msg);
                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            HttpMessage msg = new HttpMessage();
            msg.mResCode = MessageId.FILE_DOWNLOAD_ERORR;
            msg.mResMsg = "请求异常";
            msg.mSize = 0;
            msg.mProgress = 1.0;
            msg.receivedData = null;
            sendRsponseMessage(msg);
        }

    }

    private boolean downloadFileForPost(InputStream is,String filePath)
    {
        boolean rc = false;

        int fileSize = 0;
        try {
            fileSize = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        int readCount = 0;
        try
        {
            out = new FileOutputStream(filePath,false);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            byte[] buf = new byte[4096];
            int bytesRead = 0;
            while (bytesRead >= 0)
            {
                long now = System.currentTimeMillis();
                try
                {
                    bytesRead = bufferedInputStream.read(buf);
                    bufferedOutputStream.write(buf, 0, bytesRead);
                    readCount += bytesRead;

                    HttpMessage msg = new HttpMessage();
                    msg.mResCode = MessageId.FILE_DOWNLOADING;
                    msg.mResMsg = new String("下载文件成功");
                    msg.mSize = fileSize;
                    msg.mProgress = ((double)readCount/(double)fileSize)*100;
                    Log.d("download-------",String.valueOf(msg.mProgress));
//                    Log.d("download-------",String.valueOf(fileSize));
                    sendRsponseMessage(msg);
                    if (readCount == fileSize){
                        break;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                is.close();
                bufferedInputStream.close();
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        HttpMessage msg = new HttpMessage();
        msg.mResCode = MessageId.FILE_DOWNLOAD_END;
        msg.mResMsg = new String("下载文件:" + filePath + "成功");
        msg.mSize = 100;//readBytes;
        msg.mProgress = 100;//(double)((readBytes*1.0)/fileSize);
        sendRsponseMessage(msg);

        //ResponsePackage responsePackage = getResponsePackage();

//        responsePackage.mIssucceed = true;
//        responsePackage.mFileSize = 0;
//        responsePackage.mResMsg = filePath;
//        responsePackage.mProgress = 100;
//        responsePackage.mLocalPath = filePath;
//        sendMessage(MessageIdDefine.HTTP_RESPONSE_RECEIVE_END,responsePackage);






        return rc;
    }

    private boolean downloadFile(InputStream is,long fileSize,String filePath)
    {
        boolean rc = false;
        mReqPackage.mLocalPath = filePath;
        try
        {
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            byte[] byteArr = new byte[mRevBufferSize];
            int readCount = is.read(byteArr);
            long readBytes = readCount;
            while (readCount != -1 && (!mStopRunning))
            {
                fos.write(byteArr, 0, readCount);
                readCount = is.read(byteArr);
                if(readCount != -1)
                {
                    readBytes += readCount;
                    HttpMessage msg = new HttpMessage();
                    msg.mResCode = MessageId.FILE_DOWNLOADING;
                    msg.mResMsg = new String("共读到:"+String.valueOf(readBytes)+"字节");
                    msg.mSize = readBytes;
                    msg.mProgress = (readBytes*1.0)/fileSize;
                    sendRsponseMessage(msg);
                }
            }
            is.close();
            fos.flush();
            fos.close();
            if(fileSize == readBytes)
            {
                if(!mStopRunning)
                {
                    HttpMessage msg = new HttpMessage();
                    msg.mResCode = MessageId.FILE_DOWNLOAD_END;
                    msg.mResMsg = new String("下载文件:" + filePath + "成功");
                    msg.mSize = readBytes;
                    msg.mProgress = (readBytes*1.0)/fileSize;
                    sendRsponseMessage(msg);
                }
            }
            else
            {
                if(!mStopRunning)
                {
                    HttpMessage msg = new HttpMessage();
                    msg.mResCode = MessageId.FILE_DOWNLOAD_ERORR;
                    msg.mResMsg = new String("文件:"+filePath+"有误,请重新下载");
                    msg.mSize =  0;
                    msg.mProgress = 0.0;
                    sendRsponseMessage(msg);
                }
            }
        }
        catch (IOException e)
        {
            HttpMessage msg = new HttpMessage();
            msg.mResCode = MessageId.FILE_DOWNLOAD_ERORR;
            msg.mResMsg = e.getMessage();
            msg.mSize =  0;
            msg.mProgress = 0.0;
            sendRsponseMessage(msg);
        }
        return rc;
    }

    private DefaultHttpClient getHttpClient()
    {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, mReqPackage.mReqTimeout * 1000);
        HttpConnectionParams.setSoTimeout(httpParams,mReqPackage.mReqTimeout * 1000); //此处的单位是毫秒
        HttpConnectionParams.setSocketBufferSize(httpParams,mRevBufferSize*2);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
        return httpClient;
    }

    private int getHttpResponseStatusCode(HttpResponse response)
    {
        return response.getStatusLine().getStatusCode();
    }

    private void sendRsponseMessage(HttpMessage msg)
    {
        Message hadnelMsg = mRevedHander.obtainMessage();
        Bundle bund = new Bundle();
        bund.putSerializable("msg",msg);
        bund.putSerializable("package",mReqPackage);
        hadnelMsg.setData(bund);
        hadnelMsg.sendToTarget();

    }
}
