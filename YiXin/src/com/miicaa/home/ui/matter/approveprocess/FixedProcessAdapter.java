package com.miicaa.home.ui.matter.approveprocess;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.ui.menu.SelectPersonInfo;
import com.miicaa.utils.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FixedProcessAdapter extends BaseExpandableListAdapter{

	private static String TAG = "FixedProcessAdapter";
	
	Context mContext;
	LayoutInflater mInflater;
	List<FixProcessPersonGroup> mGroupList;
	FixProcessPersonGroup mPersonGroup;
	public int selectStep = -1;
	public int selectGroupPosition = -1;
	
	public FixedProcessAdapter(Context context){
		mContext = context;
		mGroupList = new ArrayList<FixProcessPersonGroup>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void refresh(List<FixProcessPersonGroup> group){
		mGroupList = group;
		notifyDataSetChanged();
	}
	
	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		Log.d(TAG, "getChildrenCount :"+mGroupList.get(groupPosition).children.size());
		return mGroupList.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mGroupList.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
//		return Long.parseLong( mGroupList.get(groupPosition).groupId.toString());
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
//		return Long.parseLong(mGroupList.get(groupPosition).children.get(childPosition).mCode.toString());
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final FixProcessPersonGroup group = mGroupList.get(groupPosition);
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.fixd_process_groupview, null);
		CheckBox groupCheckBox = ViewHolder.get(convertView, R.id.groupCheckBox);
		TextView groupTextView = ViewHolder.get(convertView, R.id.groupTextView);
		groupTextView.setSelected(!isExpanded);
		groupTextView.setText(group.groupName);
		groupCheckBox.setTag(groupPosition);
		groupCheckBox.setChecked(group.isSelect);
		groupCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox meCheckBox = (CheckBox)v;
				group.isSelect = meCheckBox.isChecked();
				selectAllChildren(group);
				notifyDataSetChanged();
			}
		});
		groupCheckBox.setOnCheckedChangeListener(mCheckChanedListener);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final FixProcessPersonGroup group = mGroupList.get(groupPosition);
		final SelectPersonInfo child = group.children.get(childPosition);
		Log.d(TAG, "getChildView child:"+child);
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.fixed_process_chidview, null);
		
		ImageView headImageView = ViewHolder.get(convertView, R.id.headImageView);
		CheckBox processCheckBox = ViewHolder.get(convertView, R.id.childCheckBox);
		processCheckBox.setTag(groupPosition);
		Tools.setHeadImg(child.mCode, headImageView);
		processCheckBox.setChecked(child.isSelect);
		processCheckBox.setEnabled(child.checkEnable);
		if(child.isSelect && mPersonGroup == null)
			mPersonGroup = group;
		processCheckBox.setText(child.mName);
		processCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox meCheckBox = (CheckBox)v;
				child.isSelect = meCheckBox.isChecked();
				if(child.isSelect)
					group.childrenSelectCount++;
				else
					group.childrenSelectCount--;
				
				if(group.childrenSelectCount == group.children.size()){
					group.isSelect = true;
				}else{
					group.isSelect = false;
				}
			}
		});
		
		processCheckBox.setOnCheckedChangeListener(mCheckChanedListener);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return mGroupList.get(groupPosition).children.get(childPosition).isSelect;
	}
	
	private void selectAllChildren(FixProcessPersonGroup groupValue){
		if(groupValue == null)
			return;
		for(SelectPersonInfo child : groupValue.children){
			child.isSelect = groupValue.isSelect;
		}
	}
	
	private OnCheckedChangeListener mCheckChanedListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				int tag = (Integer) buttonView.getTag();
				Log.d(TAG, "onCheckedChanged tag:"+tag + "selectGroupPosition:"+selectGroupPosition);
				if(tag != selectGroupPosition){
					if(selectGroupPosition != -1){
					mGroupList.get(selectGroupPosition).isSelect = false;
					selectAllChildren(mGroupList.get(selectGroupPosition));
					notifyDataSetChanged();
					}
					selectGroupPosition = tag;
					mPersonGroup = mGroupList.get(selectGroupPosition);
				}
			
		}
		}
	};
	

}
