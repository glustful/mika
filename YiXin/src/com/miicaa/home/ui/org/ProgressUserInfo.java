
package com.miicaa.home.ui.org;

import java.util.ArrayList;

/**
 * Created by LM on 14-6-4.
 */
public class ProgressUserInfo {
    private String mId;
    private String mDataId;
    private String mUserCode;
    private String mUserName;
    private String mContent;
    private Long mCreateTime;
    private String mOrgCode;
    private String mIsFinish;
    private int mDisdussionNum;
    private ArrayList<String> mExt;

    public void setId(String id){
        mId = id;
    }
    public String getmId(){
        return mId;
    }
    public void setDataId(String dataId){
        mDataId = dataId;
    }
    public String getDataId(){
        return mDataId;
    }
    public void setUserCode(String userCode){
        mUserCode = userCode;
    }
    public String getUserCode(){
        return mUserCode;
    }
    public void setUserName(String userName){
        mUserName = userName;
    }
    public String getUserName(){
        return mUserName;
    }
    public void setExt(ArrayList ext){
        mExt = ext;
    }
    public ArrayList<String> getmExt(){
        return mExt;
    }
    public void setContent(String content){
        mContent = content;
    }
    public String getContent(){
        return mContent;
    }
    public void setCreateTime(Long createTime){
        mCreateTime = createTime;
    }
    public Long getCreateTime(){
       return  mCreateTime;
    }
    public void setOrgCode(String orgCode){
        mOrgCode = orgCode;
    }
    public String getOrgCode(){
        return mOrgCode;
    }
    public void setIsFinish(String isFinish){
        mIsFinish = isFinish;

    }
    public String getIsFinsh(){
        return mIsFinish;
    }

    public void setdisDussionNum(int disDussionNum){
        mDisdussionNum = disDussionNum;
    }
    public int getDisDussionNum(){
        return mDisdussionNum;
    }
}

