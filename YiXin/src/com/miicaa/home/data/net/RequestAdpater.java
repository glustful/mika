package com.miicaa.home.data.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import android.graphics.Bitmap;
import android.util.Log;

import com.miicaa.home.data.old.UserAccount;

/**
 * 作者：李燕华
 * 时间：13-12-16.
 * 作用：网络请求适配器
 */
public abstract class RequestAdpater implements Serializable
{
    @SuppressWarnings("unused")
	private static final long sSerVerUId = -4903107312403938616L;

	public static ResponseAspect responseAspect = new ResponseAspect();

    protected int mTimeoutLimit;

    protected Boolean mSaveSession;

    protected String mHost;
    protected String mUrl;
    protected String mUnityUrl;

    protected String mFileName;
    protected String mAttPath;
    protected String mLocalDir;
    protected String mLocalPath;
    protected IdentityHashMap<String, String> mParams;

    protected RequestType mType;
    protected RequestMode mHowSyncMode;
    protected CallMethod mCallMethod;
    protected youXinLogin loginMode;
    protected RequestMethod mMethod;
    protected String mId;

    protected Bitmap mBitmap;


    public RequestAdpater()
    {
        mId = UUID.randomUUID().toString();//产生ID
        mParams = new IdentityHashMap<String, String>();
        setHost(UserAccount.getSeverHost());//得到网络地址
        setUrl("");//不传入Url
        setTimeOutLimit(30);//延时时间
        setRequestType(RequestType.eGeneral);
        setRequestMode(RequestMode.eAntsynC);
        setRequestMethod(RequestMethod.ePost);//请求方法
        setLoginMethod(youXinLogin.eMiicaa);
        setCallMethod(CallMethod.eDispersion);
        setFileName("temp");
        setLocalDir(UserAccount.getLocalDir("filecache/"));//本地路径
        setSaveSession(false);
    }

    final protected String getHost()
    {
        return mHost;
    }

    final protected String getUrl()
    {
        return mUrl;
    }

    final protected int getTimeOutLimit()
    {
        return mTimeoutLimit;
    }

    final protected RequestType getRequestType()
    {
        return mType;
    }

    final protected RequestMethod getRequestMethod()
    {
        return mMethod;
    }
    final protected RequestMode getRequestMode(){
        return mHowSyncMode;
    }

    final protected CallMethod getCallMethod()
    {
        return mCallMethod;

    }

    final protected  String getmAttPath(){
        return mAttPath;
    }

    final protected String getFileName()
    {
        return mFileName;
    }

    final protected String getLocalDir()
    {
        return mLocalDir;
    }

    final protected String getLocalPath()
    {
        return mLocalPath;
    }

    final protected Bitmap getmBitmap(){
        return mBitmap;
    }

    final protected Boolean getSaveSession()
    {
        return mSaveSession;
    }

    final private CallMethod setCallMethod()
    {
        return mCallMethod;
    }

    final protected String getUnityUrl()
    {
        return mUnityUrl;
    }

    final protected IdentityHashMap<String, String> getParams()
    {
        return mParams;
    }

    final protected String getId()
    {
        return mId;
    }

    final protected void setId(String id)
    {
        mId = id;
    }

    final public RequestAdpater addParam(HashMap<String,String> map)
    {
        mParams.putAll(map);
        return this;
    }


    final public RequestAdpater addParam(String param, String value)
    {
        mParams.put(param,value);
        return this;
    }


    final public RequestAdpater setHost(String host)
    {
        mHost = host;
        return this;
    }

    final public RequestAdpater setUrl(String url)
    {
        mUrl = url;
        return this;
    }

    final public RequestAdpater setTimeOutLimit(int limit)
    {
        mTimeoutLimit = limit;
        return this;
    }

    final public RequestAdpater setRequestType(RequestType type)
    {
        mType = type;
        return this;
    }

    final public RequestAdpater setRequestMethod(RequestMethod method)
    {
        mMethod = method;
        return this;
    }
    
    final public RequestAdpater setLoginMethod(youXinLogin loginMethod){
    	loginMode = loginMethod;
    	return this;
    }
    
   final protected youXinLogin getLoginMethod(){
	   return loginMode;
   }

    final public RequestAdpater setRequestMode(RequestMode mode){
        mHowSyncMode = mode;
        return this;
    }
    final public RequestAdpater setFileName(String fileName)
    {
        mFileName = fileName;
        setLocalPath(getLocalDir() + getFileName());
        return this;
    }

    final  public RequestAdpater setAttPath(String attPath){
        mAttPath = attPath;
        return  this;
    }

    final public RequestAdpater setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        return this;
    }



    final public RequestAdpater setLocalDir(String localDir)
    {
        mLocalDir = localDir;
//        File file = new File(mLocalDir);
//        if(!file.exists()){
//        	file.mkdir();
//        }
        setLocalPath(getLocalDir() + getFileName());
        return this;
    }
    
    final public RequestAdpater setDownLoadDir(){
    	mLocalDir = UserAccount.getLocalDir("download/");
    	setLocalPath(getLocalDir()+getFileName());
    	return this;
    }

    final public RequestAdpater setLocalPath(String localPath)
    {
        mLocalPath = localPath;
        Log.d("requsetAdapter", "mLocal path is "+mLocalPath);
        return this;
    }

    final public RequestAdpater setSaveSession(Boolean save)
    {
        mSaveSession = save;
        return this;
    }

    final private void setCallMethod(CallMethod mothod)
    {
        mCallMethod = mothod;
        if(mCallMethod == CallMethod.eUnity)
        {
            mUnityUrl = UserAccount.getSeverHost();
        }
        else
        {
            mUnityUrl = null;
        }
    }

    final protected Boolean checkParameter()
    {
        if(getCallMethod() == CallMethod.eUnity)
        {
            if(mUnityUrl == null || mUnityUrl.length() == 0)
            {
                return false;
            }
        }
        else
        {
            if(mHost == null || mUrl == null || mHost.length() == 0 || mUrl.length() == 0)
            {
                return false;
            }
        }

        if(mType == RequestType.eFileUp || mType == RequestType.eFileDown)
        {
            if(mLocalPath == null || mLocalPath.length() == 0)
            {
                return false;
            }
        }


        return true;
    }

    final protected void handleProgresMsg(ProgressMessage msg)
    {
        onProgress(msg);
    }

    final protected void handleResponeMsg(ResponseMessage msg)
    {
        ResponseData data = new ResponseData();
        data.setCode(msg.getCode());
        data.setMsg(msg.getMsg());
        data.setEntityData(msg.getData());
        data.setInstream(msg.getmIs());
        if(mType == RequestType.eFileDown || mType == RequestType.eFileUp)
        {
            data.setStringData(mLocalPath);
        }
		if (!responseAspect.onResponse(data, this)) {
			onReponse(data);
		}
    }

    final public Boolean notifyRequest()
    {
        return RequstManager.getInstance().sendRequest(this);
    }

    final public RequestAdpater changeParam(String key, String value)
    {
        mParams.put(key,value);
        return this;
    }

    public abstract void onReponse(ResponseData data);
    public abstract void onProgress(ProgressMessage msg);


    protected enum CallMethod
    {
        eUnity,
        eDispersion,
    }

    public enum RequestMethod
    {
        ePost,
        eGet,
        ePost1,

    }

    public enum RequestType
    {
        eGeneral,
        eFileDown,
        eFileUp,
        eFileStream,
    }

    public enum RequestMode{
        eSync,
        eAntsynC;
    }
    public enum youXinLogin{
    	eYouxin,
    	eMiicaa;
    }

}
