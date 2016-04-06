package com.miicaa.common.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 13-10-25.
 */
public class ImgHttpTaskItem implements Runnable
{
    private static final int sGET = 0;
    private static final int sPOST = 1;
    private static final int mRevBufferSize = 1024*2;
    private boolean mStopRunning;
    private int mMethod = sGET;
    private ImgRequestPackage mReqPackage;
    private DefaultHttpClient mHttpClient;
    private HttpContext mHttpContext = null;
    private Handler mRevedHander;

    public ImgHttpTaskItem (ImgRequestPackage reqPackage,Handler revedHandler)
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
                    httpResponse = mHttpClient.execute(new HttpGet(url), mHttpContext);
                    int statusCode = this.getHttpResponseStatusCode(httpResponse);
                    if((statusCode > 199) && (statusCode < 400))
                    {
                        HttpEntity entity = httpResponse.getEntity();
                        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                        InputStream is = bufHttpEntity.getContent();


                        ImgHttpMessage msg = new ImgHttpMessage();
                        msg.mResCode = MessageId.FILE_DOWNLOAD_STREAM_GET;
                        msg.mResMsg = new String("下载文件成功");
                        msg.mSize = 100;
                        msg.mProgress = 100;
                        msg.mStream = bufHttpEntity.getContent();
                        sendRsponseMessage(msg);

                        is = bufHttpEntity.getContent();
                        downloadFileForPost(is,mReqPackage.mLocalPath);
                    }
                    break;
                }
                case sPOST:
                {
                    if(mReqPackage.mIsUpload && mReqPackage.mBitmap != null)
                    {
                        try
                        {
                            Bitmap bitmap = mReqPackage.mBitmap;
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                            HttpEntity entity = new ByteArrayEntity(bao.toByteArray());
                            HttpPost httpPost = new HttpPost(url);
                            httpPost.setEntity(entity);
                            httpResponse = mHttpClient.execute(httpPost);
                            int statusCode = this.getHttpResponseStatusCode(httpResponse);
                            if((statusCode > 199) && (statusCode < 400))
                            {

                                ImgHttpMessage msg = new ImgHttpMessage();
                                msg.mResCode = MessageId.HTTP_RESPONSE_UPLOAD_SUCCESS;
                                msg.mResMsg = new String("文件上传成功");
                                msg.mSize = 100;
                                msg.mProgress = 100;
                                sendRsponseMessage(msg);
                            }
                            else
                            {
                                ImgHttpMessage msg = new ImgHttpMessage();
                                msg.mResCode = MessageId.HTTP_RESPONSE_UPLOAD_FAILURE;
                                msg.mResMsg = new String("文件上传失败");
                                msg.mSize = 100;
                                msg.mProgress = 100;
                                sendRsponseMessage(msg);
                            }
                        }
                        catch (Exception e)
                        {
                            ImgHttpMessage msg = new ImgHttpMessage();
                            msg.mResCode = MessageId.HTTP_RESPONSE_UPLOAD_FAILURE;
                            msg.mResMsg = new String("文件上传失败");
                            msg.mSize = 100;
                            msg.mProgress = 100;
                            sendRsponseMessage(msg);
                        }

                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            ImgHttpMessage msg = new ImgHttpMessage();
            msg.mResCode = MessageId.FILE_DOWNLOAD_ERORR;
            msg.mResMsg = new String("下载文件异常");
            msg.mSize = 100;
            msg.mProgress = 100;
            sendRsponseMessage(msg);
        }
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
                    bufferedOutputStream.write(buf, 0, bytesRead);
                    bytesRead = bufferedInputStream.read(buf);
                    readCount += bytesRead;

                    ImgHttpMessage msg = new ImgHttpMessage();
                    msg.mResCode = MessageId.FILE_DOWNLOADING;
                    msg.mResMsg = new String("下载文件成功");
                    msg.mSize = fileSize;
                    msg.mProgress = readCount/fileSize;
                    msg.mStream = null;
                    sendRsponseMessage(msg);
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
        return rc;
    }

    private void sendRsponseMessage(ImgHttpMessage msg)
    {
        Message hadnelMsg = mRevedHander.obtainMessage();
        Bundle bund = new Bundle();
        bund.putSerializable("msg",msg);
        bund.putSerializable("package",mReqPackage);
        hadnelMsg.setData(bund);
        hadnelMsg.sendToTarget();

    }


}
