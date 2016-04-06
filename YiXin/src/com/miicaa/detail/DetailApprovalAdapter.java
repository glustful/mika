package com.miicaa.detail;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;

public class DetailApprovalAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> titlelist;
	ArrayList<DetailApprovalInfo> infos;
	
	static int GROUP = 0;
	static int MEMBER = 1;
	
	 public DetailApprovalAdapter(Context context) {
		// TODO Auto-generated constructor stub
		 this.context = context;
		 titlelist = new ArrayList<String>();
		 infos = new ArrayList<DetailApprovalInfo>();
	}
	 
	 public void refresh(ArrayList<DetailApprovalInfo> infos ,ArrayList<String > titlelist){
		 this.infos.clear();
		 this.infos.addAll(infos);
		 this.titlelist.clear();
		 this.titlelist.addAll(titlelist);
		 this.notifyDataSetChanged();
	 }
	
	 
	 
	@Override
	public int getItemViewType(int position) {
		int type;
		DetailApprovalInfo info = infos.get(position);
		if(titlelist.contains(info.group)){
			type = GROUP;
		}else{
			type = MEMBER;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos != null ? infos.size() :0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0).group;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		DetailApprovalInfo info = infos.get(position);
		int type = getItemViewType(position);
//		if(position == 0 ){
//			return convertview = new  View(context);
//		}
			if(type == GROUP){
				if(convertview == null)
				convertview = LayoutInflater.from(context).inflate(R.layout.arrangement_personnel_group_view, null);
				
			}
		    else if(type == MEMBER){
			if(convertview == null)
			convertview = LayoutInflater.from(context).inflate(R.layout.matter_narmal_detail_app, null);
			
			ImageView headimg = ViewHolder.get(convertview, R.id.head_img);
			TextView  headname = ViewHolder.get(convertview, R.id.head_name);
			ImageView approvalstate = ViewHolder.get(convertview, R.id.approvalState);
			ImageView talk = ViewHolder.get(convertview, R.id.normalTalk);
			ImageView hLine = ViewHolder.get(convertview, R.id.horizenLine);
			talk.setVisibility(View.GONE);
			approvalstate.setVisibility(View.VISIBLE);
			TextView content = ViewHolder.get(convertview, android.R.id.content);
			TextView time = ViewHolder.get(convertview, R.id.time);
			TextView from = ViewHolder.get(convertview, R.id.from);
			Tools.setHeadImg(info.usercode, headimg);
			headname.setText(info.username!=null?info.username:"");
//			setcontent
			setContent(content, info, approvalstate);
			if(info.endtime != null){
				time.setVisibility(View.VISIBLE);
				time.setText(info.endtime);
			}
			else{
				time.setVisibility(View.GONE);
			}
			if(position < infos.size() -1 && getItemViewType(position+1) == GROUP){
				hLine.setVisibility(View.GONE);
			}else{
				hLine.setVisibility(View.VISIBLE);
			}
			}
		return convertview;
	}
	
	
	void setContent(TextView content,DetailApprovalInfo info,ImageView apprview){
		  if ("1".equals(info.status)) {
			  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.dis_agree));
              content.setText( info.content);
          } else if ("2".equals(info.status)) {
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.agree));
        	  content.setText(info.content);
          } else if ("0".equals(info.status)) {
              String contentstr = "";
              apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.wait_approve));
                  contentstr = "已提交，等待" + info.username + "审批";
              content.setText(contentstr);
          }else if("3".equals(info.status)){
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.no_approve));
              String contentstr = info.username + "未审批";
              content.setText(contentstr);
          }else if ("4".equals(info.status)){
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.wait_meet));
              String contentstr = "已提交，等待"+info.username+"会签";
              content.setText(contentstr);

          }
          else if ("5".equals(info.status)){
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.pass_meet));
              content.setText(info.content);
          }else if ("6".equals(info.status)){
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.miss_meet));
              content.setText(info.content);
          }else if("7".equals(info.status)){
        	  apprview.setImageDrawable(content.getResources().getDrawable(R.drawable.summa_meet));
              content.setText(info.content);
          }
      
	}

}
