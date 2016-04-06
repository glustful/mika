package com.yxst.epic.yixin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.avos.avoscloud.LogUtil.log;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.view.ChatDetialItem;
import com.yxst.epic.yixin.view.ChatDetialItem_;

@EBean
public class ChatDetailAdapter extends BaseAdapter {

	private static String TAG  = "ChatDetailAdapter";
	
	@RootContext
	Context context;

	private List<Member> list;
	private List<String> userCodes;

	private boolean isShowAdd = false;
	private boolean isShowMinus = false;

	private boolean isDelMode = false;

	public void changeData(List<Member> list) {
		this.list = list;
		selectUserCodes(list);
	}

	@Override
	public int getCount() {
		if (getRealCount() > 0) {
			return getRealCount() + (isShowAdd ? 1 : 0) + (isShowMinus ? 1 : 0);
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		log.d(TAG, "getView position:"+position);
		ChatDetialItem view;
		
		if (convertView == null) {
			view = ChatDetialItem_.build(context);
		} else {
			view = (ChatDetialItem) convertView;
		}

		if (!isPositionAdd(position) && !isPositionMinus(position)) {
			final Member member = (Member) getItem(position);
			
			String userCode = userCodes.get(position);
			view.bind(member,
					this.isDelMode && !member.UserName.equals(localUserName));
			view.setHeadImg(userCode);
			view.ivDel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mOnDelMemberListenr != null) {
						mOnDelMemberListenr.onDelMember(member);
					}
				}
			});
		} else if (isPositionAdd(position)) {
			// 没想到吧 这里存在有线程问题 2015/5/21 
			ImageLoader.getInstance().cancelDisplayTask(view.mIconView);
//			view.mIconView.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_dotline_add_bg));
			view.mIconView.setImageResource(R.drawable.avatar_dotline_add_bg);
			view.mTextView.setText(null);
			view.ivDel.setVisibility(View.GONE);
		} else if (isPositionMinus(position)) {
			// 没想到吧 这里存在有线程问题 2015/5/21 
			ImageLoader.getInstance().cancelDisplayTask(view.mIconView);
//			view.mIconView.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_dotline_minus_bg));
			view.mIconView.setImageResource(R.drawable.avatar_dotline_minus_bg);
			view.mTextView.setText(null);
			view.ivDel.setVisibility(View.GONE);
		}

		return view;
	}

	public boolean isPositionAdd(int position) {
		if (position >= getRealCount()) {
			if (isShowAdd) {
				if (position == getRealCount()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isPositionMinus(int position) {
		if (position >= getRealCount()) {
			if (isShowMinus) {
				if (isShowAdd && position == getRealCount() + 1) {
					return true;
				} else if (!isShowAdd && position == getRealCount()) {
					return true;
				}
			}
		}
		return false;
	}

	public int getRealCount() {
		return list == null ? 0 : list.size();
	}

	public List<Member> getMembers() {
		return this.list;
	}

	public void setDelMode(boolean isDelMode) {
		this.isDelMode = isDelMode;
		this.notifyDataSetChanged();
	}

	public boolean isDelMode() {
		return this.isDelMode;
	}

	private String localUserName;

	public void setLocalUserName(String localUserName) {
		this.localUserName = localUserName;
		this.notifyDataSetChanged();
	}
	
	private OnDelMemberListenr mOnDelMemberListenr;
	
	public void setOnDelMemberListenr(OnDelMemberListenr l) {
		mOnDelMemberListenr = l;
	}
	
	public static interface OnDelMemberListenr{
		void onDelMember(Member member);
	}
	
	public void setShowMinus(boolean isShowMinus) {
		this.isShowMinus = isShowMinus;
		this.notifyDataSetChanged();
	}
	
	public void setShowAdd(boolean isShowAdd) {
		this.isShowAdd = isShowAdd;
		this.notifyDataSetChanged();
	}
	
	@Background
	void selectUserCodes(List<Member> members){
		this.userCodes = new ArrayList<String>();
		for(Member m : members){
			String s = m.Uid;
			Long u = Long.parseLong(s);
			String userCode = UserInfo.findByIdForUserCode(u);
			userCode = userCode != null ? userCode:"null";
			userCodes.add(userCode);
		}
		refresh();
	}
	
	@UiThread
	void refresh(){
		this.notifyDataSetChanged();
	}
}
