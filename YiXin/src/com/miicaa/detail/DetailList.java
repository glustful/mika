package com.miicaa.detail;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.miicaa.utils.AllUtils;



public class DetailList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4391406791280099952L;
	/**
	 * 
	 */
//    JSONObject detialp;
	String detail;
	public String id;
	public String dataid;
	public String usercode;
	public String username;
	public String content;
	public String createtime;
	public String orgcode;
	public String isfinish;
	int discussnum;
	ArrayList<Document> documents;
	ArrayList<ResourcesArticle> articles;
	ArrayList<ResourcesImage> imgs;
	
	static String TAG = "DetailList";
	
	public DetailList()  {
		// TODO Auto-generated constructor stub
		
//		this.detialp = detialp;
//		documents = new ArrayList<Document>();
//		articles = new ArrayList<ResourcesArticle>();
//		imgs = new ArrayList<ResourcesImage>();
		
	}
	
	@SuppressLint("SimpleDateFormat")
	void save(JSONObject detialp) throws JSONException{
		Log.d(TAG, "detailList json:"+detialp);
		Long time;
		id = detialp.optString("id");
		dataid = detialp.optString("dataId");
		if(dataid !=null)
		usercode = detialp.optString("userCode");
		username = detialp.optString("userName");
		content = detialp.optString("content");
		time = detialp.optLong("createTime");
		orgcode = detialp.optString("orgcode");
		isfinish = detialp.optString("isFinish");
		discussnum = detialp.optInt("discussionNum");
		documents = ProgressListInfo.islistnull(detialp.optJSONArray("documents"))?null:
			savedocumentList(detialp.optJSONArray("documents"));
		articles = ProgressListInfo.islistnull(detialp.optJSONArray("resourcesArticle"))?null:
			savearticleList(detialp.optJSONArray("resourcesArticle"));
		imgs = ProgressListInfo.islistnull(detialp.optJSONArray("resourcesImage"))?null:
			saveimgList(detialp.optJSONArray("resourcesImage"));
		
		createtime = AllUtils.getnormalTime(time);
		
	}
	
	ArrayList<Document> savedocumentList(JSONArray list) throws JSONException{
		ArrayList<Document> detailLists = new ArrayList<Document>();
		
		for(int i = 0; i < list.length(); i++){
			Document dList = new Document();
			dList.save(list.optJSONObject(i));
			detailLists.add(dList);
		}
		return detailLists;
		
	}
	
	ArrayList<ResourcesArticle> savearticleList(JSONArray list) throws JSONException{
		ArrayList<ResourcesArticle> detailLists = new ArrayList<ResourcesArticle>();
		
		for(int i = 0; i < list.length(); i++){
			ResourcesArticle dList = new ResourcesArticle();
			dList.save(list.optJSONObject(i));
			detailLists.add(dList);
		}
		return detailLists;
		
	}
	
	ArrayList<ResourcesImage> saveimgList(JSONArray list) throws JSONException{
		ArrayList<ResourcesImage> detailLists = new ArrayList<ResourcesImage>();
		
		for(int i = 0; i < list.length(); i++){
			ResourcesImage dList = new ResourcesImage();
			dList.save(list.optJSONObject(i));
			detailLists.add(dList);
		}
		return detailLists;
		
	}
	
	
	
	
}
