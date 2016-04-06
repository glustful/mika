package com.miicaa.detail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.detail.ApprovalFootView.OnApprovalClickLintener;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.org.ArrangementPlan_;
import com.miicaa.home.ui.org.MessageType;
import com.miicaa.utils.AddMoreListView;

@EFragment(R.layout.matter_do_approve)
public class DetailApprovalFragment extends Fragment{
	
	static String TAG = "DetailApprovalFragment";
	
	public static DetailApprovalFragment instance;
	ArrayList<String> tagList;
	ArrayList<DetailApprovalInfo> groupList;
	int apprCount;
	DetailApprovalAdapter adapter;
	OnTabCountListener apprListener;
	ListView list;
	String groupId;
	
	@ViewById(R.id.apprlistview)
	AddMoreListView listview;
	@FragmentArg
	String dataId;
	@FragmentArg
	String operateGroup;
	@FragmentArg
	MatterInfo mInfo;
	@AfterInject
	void beginRequset(){
		instance = this;
		tagList = new ArrayList<String>();
		groupList = new ArrayList<DetailApprovalInfo>();
		adapter = new DetailApprovalAdapter(getActivity());
		request();
		requestgroupid();
	}
	@AfterViews
	void afterView(){
		listview.shownFootView(false);
		listview.setAdapter(adapter);
		listview.setFooterDividersEnabled(false);
	}
	
	@Background
	void jsonToData(JSONArray json){
		if(listview.getTag(R.id.tag_json)==null || !listview.getTag(R.id.tag_json).toString().equals(json.toString())){
			listview.setTag(R.id.tag_json, json.toString());
		}else{
			return;
		}
		LinkedHashMap<String, String> group = new LinkedHashMap<String, String>();
		ArrayList<DetailApprovalInfo> list = new ArrayList<DetailApprovalInfo>();
		for(int i = 0 ; i < json.length();i++){
			DetailApprovalInfo info = new DetailApprovalInfo(json.optJSONObject(i), null).save();
			list.add(info);
			info.flag = info.flag == null ? "null":info.flag;
			group.put(info.flag, info.flag);
		}
		
		apprCount = list.size();//审批数目
		if(group.size() == 1)
			groupList.addAll(list);
		else {
			Iterator<?> iter = group.entrySet().iterator();
			while(iter.hasNext()){
			   @SuppressWarnings("unchecked")
			Map.Entry<String, String> s = (Entry<String, String>) iter.next();
			    tagList.add(s.getValue());//分组
				for(int i = 0 ; i < list.size(); i++){
					if(s.getValue().equals(list.get(i).flag)){
						groupList.add(list.get(i));
					}
				}
				groupList.add(new DetailApprovalInfo(null, s.getValue()));
			}
		if(groupList.size() > 0)
		groupList.remove(groupList.size()-1);
		}
		refresh();
	}
	
	@UiThread
	void refresh(){
		adapter.refresh(groupList,tagList);
		listview.setOpenView(true);
	}
	
	public static DetailApprovalFragment getInstance(){
		return instance;
	}
	
	@SuppressWarnings("serial")
	void request(){
		String url = "/home/phone/thing/getdialogueinfo";
		String type = "flow_record";
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
					jsonToData(data.getJsonArray());
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.addParam("dataId",dataId)
		.addParam("type",type)
		.notifyRequest();
	}
	
	void requestgroupid(){
		  //在同意的时候判断是否要显示下一审批人
        Utils.approvalUseData(getActivity(),dataId,new Utils.CallBackListener() {
            @Override
            public void callBack(ResponseData data) {
            	
            }

            @Override
            public void callBackJson(JSONArray jsonArray) {
                JSONObject whatJson = jsonArray.optJSONObject(0);
                if(whatJson.isNull("groupId")){
                    groupId = null;
                }else {
                    groupId = whatJson.optString("groupId");
                }
            }

			@Override
			public void callbackNull() {
				
			}
        });
	}
	
	public ArrayList<PopupItem> initPopItems(){
		ArrayList<PopupItem> items = new ArrayList<PopupItem>();
		 if ("06".equals(operateGroup) || "81".equals(operateGroup) || "82".equals(operateGroup)) {
             items.add(new PopupItem("同意", MessageType.eAgree.toString()));
             items.add(new PopupItem("不同意", MessageType.eDisAgree.toString()));
         }else if("83".equals(operateGroup) || "87".equals(operateGroup)
                 || "88".equals(operateGroup)){
             items.add(new PopupItem("会签通过",MessageType.eJointlyPass.toString()));
             items.add(new PopupItem("会签不通过",MessageType.eJoinlyMiss.toString()));
         } else if ("84".equals(operateGroup) || "85".equals(operateGroup) ||
                 "86".equals(operateGroup)){
             items.add(new PopupItem("同意", MessageType.eAgree.toString()));
             items.add(new PopupItem("不同意", MessageType.eDisAgree.toString()));
             items.add(new PopupItem("会签通过",MessageType.eJointlyPass.toString()));
             items.add(new PopupItem("会签不通过",MessageType.eJoinlyMiss.toString()));
         }else{
         }
		 return items;
	}
	
	OnApprovalClickLintener listener = new OnApprovalClickLintener() {
		
		
		@Override
		public void planClick() {
			ArrangementPlan_.intent(getActivity()).clientcode(mInfo.getClientName())
			.dataid(dataId)
			.todoid(mInfo.getTodoId())
			.start();
		}

		@Override
		public void approvalClick(PopupItem msg) {
			
		}

		@Override
		public void approvalClickWithOutItems() {
			if("02".equals(operateGroup)||"03".equals(operateGroup)){
				 DiscussApproveActivity_.intent(getActivity()).status("")
	             .groupid(groupId)
	             .dataid(dataId)
	             .start();
			}
		}

		@Override
		public void remindClick() {
			
		}
		
		
	};
	
	public void approvalClick(PopupItem msg,boolean isProcess){
					String status = "";
					 if (msg.mContent.equals("同意")) {
		                 status = DiscussApproveActivity.AGREE;
		             }
		             if (msg.mContent.equals("不同意")) {
		                 status = DiscussApproveActivity.DISAGREE;
		             }if (msg.mContent.equals("会签通过")){
		                 status = DiscussApproveActivity.PASS;
		             }if (msg.mContent.equals("会签不通过")){
		                 status = DiscussApproveActivity.MISS;
		             }if(msg.mContent.equals("取消")){
		            	 MatterDetailAcrtivity.getInstance().approvalFootView.builder.dismiss();
		            	 return;
		             }
		             DiscussApproveActivity_.intent(getActivity())
		             .status(status)
		             .groupid(groupId)
		             .dataid(dataId)
		             .isProcess(isProcess)
		             .start();
	}
	
	
	@UiThread
	public void setApprovalCountListener(OnTabCountListener apprListener){
		this.apprListener = apprListener;
	}
}
