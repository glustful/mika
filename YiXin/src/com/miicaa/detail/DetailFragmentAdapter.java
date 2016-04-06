package com.miicaa.detail;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.miicaa.home.data.business.matter.MatterInfo;
import com.miicaa.home.ui.home.FragmentInfo;

public class DetailFragmentAdapter extends FragmentStatePagerAdapter implements OnTabCountListener{

	ArrayList<FragmentInfo> infos;
	OnAdapterListener listener;
	MatterInfo mInfo;
	public static DetailFragmentAdapter instance;
//	ArrayList<Fragment> infos;
	Context context;
	public DetailFragmentAdapter(Context context,FragmentManager fm) {
		super(fm);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}
	
	public void refresh(ArrayList<FragmentInfo> infos){
		this.infos = infos;
		this.notifyDataSetChanged();
	}

	public static DetailFragmentAdapter getInstance(){
		return instance;
	}
	void putMatterInfo(MatterInfo mInfo){
		this.mInfo = mInfo;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.d("hehe", infos.get(arg0).class_.getName()+"........"+infos.get(arg0).arg.getString("type"));
		Fragment f = new Fragment();
//		Fragment f =  Fragment.instantiate(context,infos.get(arg0).class_.getName(),infos.get(arg0).arg);
//		Fragment f= new DetailApprovalFragment(arg);
		switch (arg0) {
		case 0:
			if(infos.get(arg0).class_.getName().equals(DetailProgressFragment_.class.getName())){
				 DetailProgressFragment_ pf = new DetailProgressFragment_();
				 if(mInfo != null)
					 infos.get(arg0).arg.putSerializable("mInfo", mInfo);
				 pf.setArguments(infos.get(arg0).arg);
				 pf.setProgressCountListener(this);
//				 if(mInfo != null)
//					 pf.putMatterInfo(mInfo);
				 f =  pf;
			}else{
//				DetailApprovalFragment_.getInstance().setApprovalCountListener(this);
				DetailApprovalFragment_ af = new DetailApprovalFragment_();
				 if(mInfo != null)
					 infos.get(arg0).arg.putSerializable("mInfo", mInfo);
				af.setArguments(infos.get(arg0).arg);
				af.setApprovalCountListener(this);
//				if(mInfo != null)
//					af.putMatterInfo(mInfo);
				f =  af;
			}
			break;
		case 1:
//			DetailDiscussFragment_.getInstance().setDisscussCount(this);
			DetailDiscussFragment_ df = new DetailDiscussFragment_();
			df.setArguments(infos.get(arg0).arg);
			df.setDisscussCount(this);
			f =  df;
			break;
		case 2:
//			DetailDiscussFragment_.getInstance().setDisscussCount(this);
			DetailDiscussFragment_ tf = new DetailDiscussFragment_();
			tf.setArguments(infos.get(arg0).arg);
			tf.setDisscussCount(this);
			f = tf;
			break;
		default:
			break;
		
		}
		return f;
//		return infos.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos!=null?infos.size():0;
	}
	
	
	public interface OnAdapterListener{
		void oninitProgress(int count);
		void oninitApprove(int count);
		void oninitDiscuss(int count);
		void oninitTrends(int count);
	}
	
	public void setOnAdapterListener(OnAdapterListener listener){
	this.listener = listener;	
	
	}


	@Override
	public void progressCount(int count) {
		// TODO Auto-generated method stub
		listener.oninitProgress(count);
	}

	@Override
	public void approveCount(int count) {
		// TODO Auto-generated method stub
		listener.oninitApprove(count);
	}

	@Override
	public void disCussCount(int count) {
		// TODO Auto-generated method stub
		listener.oninitDiscuss(count);
	}

	@Override
	public void trendsCount(int count) {
		// TODO Auto-generated method stub
		listener.oninitTrends(count);
	}
	

}
