package com.miicaa.home.ui.matter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.miicaa.base.request.MRequest;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.home.data.net.SocketUploadFileTask;

public class MatterCommitRequest extends MRequest{

	public String appUn;
	public List<String> fileNames;
	public String infoId;
	
//	public MatterCommitRequest(String url){
//		super(url);
//	}
	
	public MatterCommitRequest(){
		fileNames = new ArrayList<String>();  
	}
	
	SocketUploadFileTask uploadFileTask;
	public void setUploadFileTask(SocketUploadFileTask uploadFileTask){
		this.uploadFileTask = uploadFileTask;
	}
	
	public void saveParam(){
		
	}
	
	public void saveUploadFileParam(){
		HashMap<String, String> uploadParam = new HashMap<String, String>();
		uploadParam.put("appUn", appUn);
		uploadParam.put("infoId", infoId);
		for(int i = 0; i < mFilePathList.size() ;i++){
		uploadParam.put("fileName", getFileName(mFilePathList.get(i)));
		uploadFileList.add(new UploadFileItem(mFilePathList.get(i), uploadParam));
		}
	}
	
	
	//从文件路径中得到文件名
    private  String getFileName(String path){
        int start = path.lastIndexOf("/");
        if(start != -1 ){
            return path.substring(start+1);
        }
        return "";
    }
	}
