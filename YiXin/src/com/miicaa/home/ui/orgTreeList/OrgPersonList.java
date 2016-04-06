package com.miicaa.home.ui.orgTreeList;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.BaseExpandableListView;
import com.miicaa.home.R;
import com.miicaa.home.data.business.org.OrgTreeElement;
import com.miicaa.home.ui.person.PersonHome;

/**
 * Created by apple on 13-11-22.
 * 组织机构树
 * 1、是个view而整个模块是个activity
 * 2、view中的点击能够放回到上次activity调用
 */
public class OrgPersonList {
	private Activity activity;
	private View mRootView = null;
	private BaseExpandableListView orgListView;
	ArrayList<OrgTreeElement> contentArray;
	private OrgPersonListActivity.OrgType orgType;
	private OrgPersonListActivity.SelectType selectType;

	public OrgPersonList(Activity activity,
						 ArrayList<OrgTreeElement> dataArrayParam,
						 OrgPersonListActivity.OrgType orgType,
						 OrgPersonListActivity.SelectType selectType) {
		this.activity = activity;
		contentArray = dataArrayParam;
		this.orgType = orgType;
		this.selectType = selectType;
		init();
	}

	public View getRootView() {
		if (mRootView == null) {
			init();
		}
		return mRootView;
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(activity);
		mRootView = inflater.inflate(R.layout.org_person_list_view, null);
		orgListView = (BaseExpandableListView) mRootView.findViewById(R.id.org_person_list);
		ExpandableAdapter expandableAdapter = new ExpandableAdapter();
		orgListView.setAdapter(expandableAdapter);
		for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
			orgListView.expandGroup(i);
		}
	}

	private class ExpandableAdapter extends BaseExpandableListAdapter {
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			OrgTreeElement cellMap = contentArray.get(groupPosition);
			return cellMap.getChildren().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
								 boolean isLastChild, View convertView, ViewGroup parent) {
			LinearLayout layout = new LinearLayout(activity);
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = layoutInflater.inflate(R.layout.org_person_list_content_cell, null);

			// 数据
			OrgTreeElement cellMap = contentArray.get(groupPosition);
			ArrayList<OrgTreeElement> cellArray = cellMap.getChildren();
			OrgTreeElement subCellMap = cellArray.get(childPosition);

			// title
			TextView contentTextView = (TextView) itemView.findViewById(R.id.org_person_list_content_cell_content_textview);
			contentTextView.setText(subCellMap.getName());

			//头像的图片
			ImageView headImage = (ImageView) itemView.findViewById(R.id.org_person_list_content_cell_left_head_image);
			RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(0, 0);
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.org_person_list_content_cell_left_image);
			if (selectType != OrgPersonListActivity.SelectType.NONE) {
				mLayoutParams.leftMargin = 10;
			} else {
				mLayoutParams.leftMargin = 40;
			}
			if (cellMap.getType() == OrgTreeElement.Type.UNIT) {
				headImage.setLayoutParams(mLayoutParams);
			}

			ImageView rightJiantouImage = (ImageView) itemView.findViewById(R.id.org_person_list_content_cell_right_image);

			Button cellButton = (Button) itemView.findViewById(R.id.org_person_list_content_cell_button);
			cellButton.setTag(R.id.tag_group, String.valueOf(groupPosition));
			cellButton.setTag(R.id.tag_child, String.valueOf(childPosition));
			if (subCellMap.hasChild()) {
				cellButton.setTag(R.id.tag_child_is_into, Boolean.valueOf(true));//有分组
			} else {
				cellButton.setTag(R.id.tag_child_is_into, Boolean.valueOf(false));//已经无分组
				rightJiantouImage.setVisibility(View.GONE);
			}
			cellButton.setOnClickListener(cellButtonClick);

			Button selectCellButton = (Button) itemView.findViewById(R.id.org_person_list_content_cell_select_button);
			selectCellButton.setTag(R.id.tag_group, String.valueOf(groupPosition));
			selectCellButton.setTag(R.id.tag_child, String.valueOf(childPosition));
			selectCellButton.setOnClickListener(selectCellButtonClick);

			ImageView selectImage = (ImageView) itemView.findViewById(R.id.org_person_list_content_cell_left_image);
			if (subCellMap.isSelect()) {
				selectImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.org_person_select));
			} else {
				selectImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.org_person_no_select));
			}

			if (selectType == OrgPersonListActivity.SelectType.NONE) {
				selectCellButton.setVisibility(View.GONE);
				selectImage.setVisibility(View.GONE);
			}
			layout.addView(itemView);

			if (subCellMap.getType() == OrgTreeElement.Type.USER) {
				itemView.setBackgroundColor(Color.parseColor("#9999ff"));
			}

			return layout;
		}

		View.OnClickListener cellButtonClick = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int groupPositionIndex = Integer.parseInt(view.getTag(R.id.tag_group).toString());
				int childPositionIndex = Integer.parseInt(view.getTag(R.id.tag_child).toString());
				Boolean isHaveChildren = Boolean.valueOf(view.getTag(R.id.tag_child_is_into).toString());

				OrgTreeElement cellMap = contentArray.get(groupPositionIndex);
				ArrayList<OrgTreeElement> cellArray = cellMap.getChildren();
				OrgTreeElement subCellMap = cellArray.get(childPositionIndex);

				if (isHaveChildren) {
					Intent itent = new Intent(activity, OrgPersonListActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putString("title", subCellMap.getName());
					mBundle.putInt("org_type", orgType.ordinal());
					mBundle.putInt("select_type", selectType.ordinal());
					mBundle.putString("id", subCellMap.getId());
					mBundle.putInt("level", subCellMap.getLevel());
					itent.putExtras(mBundle);
					activity.startActivityForResult(itent, subCellMap.getLevel());
				} else {
					Log.d("cellButtonClick----Group:", view.getTag(R.id.tag_group).toString() + "---Child:" + view.getTag(R.id.tag_child).toString());
					if (cellMap.getType() == OrgTreeElement.Type.USER && selectType == OrgPersonListActivity.SelectType.NONE) {
						Intent intent = new Intent(activity, PersonHome.class);
						Bundle bundle = new Bundle();
						bundle.putString("userCode", "U=o07c5d12");
						intent.putExtra("bundle", bundle);
						activity.startActivity(intent);
						activity.overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
					}
				}
			}
		};
		View.OnClickListener selectCellButtonClick = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int groupPositionIndex = Integer.parseInt(view.getTag(R.id.tag_group).toString());
				int childPositionIndex = Integer.parseInt(view.getTag(R.id.tag_child).toString());

                if(selectType.equals(OrgPersonListActivity.SelectType.SINGLE)) {//单选
                    for(int i = 0; i< contentArray.size(); i++) {
                        OrgTreeElement cellMap = contentArray.get(i);
                        ArrayList<OrgTreeElement> cellArray = cellMap.getChildren();
                        for(int j= 0;j<cellArray.size();j++) {
                            OrgTreeElement subCellMap = cellArray.get(j);
                            subCellMap.setSelect(false);
                        }
                    }
                }

				OrgTreeElement cellMap = contentArray.get(groupPositionIndex);
				ArrayList<OrgTreeElement> cellArray = cellMap.getChildren();
//                if (cellArray.size()>1){
//                    Toast toast = Toast.makeText(OrgPersonListActivity.meT,"您只能选择一个审批人",Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER,0,0);
//                    toast.show();
//                    return;
//                }
				OrgTreeElement subCellMap = cellArray.get(childPositionIndex);
				if (subCellMap.isSelect()) {
					subCellMap.setSelect(false);
				} else {
					subCellMap.setSelect(true);
				}
				notifyDataSetChanged();
			}
		};

		@Override
		public int getChildrenCount(int groupPosition) {
			OrgTreeElement cellMap = contentArray.get(groupPosition);
			return cellMap.getChildren().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return contentArray.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return contentArray.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
								 View convertView, ViewGroup parent) {

			LinearLayout layout = new LinearLayout(activity);
			LayoutInflater layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = layoutInflater.inflate(
					R.layout.org_person_list_head_cell, null);
			// ///
			//数据
			OrgTreeElement cellMap = contentArray.get(groupPosition);

			//title
			TextView contentTextView = (TextView) itemView.findViewById(R.id.org_person_list_head_cell_content_textview);
			contentTextView.setText(cellMap.getName());

			//头像
			ImageView headImage = (ImageView) itemView.findViewById(R.id.org_person_list_head_cell_left_head_image);
			RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(0, 0);
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.org_person_list_head_cell_left_image);
			mLayoutParams.leftMargin = 10;
			if (cellMap.getType() == OrgTreeElement.Type.UNIT) {
				headImage.setLayoutParams(mLayoutParams);
			}

			ImageView imgIndicator = (ImageView) itemView.findViewById(R.id.org_person_list_head_cell_right_image);
			RelativeLayout bgLayout = (RelativeLayout) itemView.findViewById(R.id.org_person_list_head_view_bg);
			Button cellContentButton = (Button) itemView.findViewById(R.id.org_person_list_head_cell_button);
			cellContentButton.setTag(groupPosition);
			cellContentButton.setOnClickListener(headContentButtonClick);

			if (cellMap.hasChild()) {
				bgLayout.setBackgroundColor(Color.parseColor("#f6f6f6"));
				if (isExpanded) {
					imgIndicator.setImageDrawable(activity.getResources().getDrawable(
							R.drawable.an_right_up));
				} else {
					imgIndicator.setImageDrawable(activity.getResources().getDrawable(
							R.drawable.an_right_down));
				}
			} else {
				bgLayout.setBackgroundColor(Color.parseColor("#ffffff"));
				imgIndicator.setVisibility(View.GONE);
				cellContentButton.setVisibility(View.VISIBLE);
			}

			ImageView selectImage = (ImageView) itemView.findViewById(R.id.org_person_list_head_cell_left_image);
			if (cellMap.isSelect()) {
				selectImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.org_person_select));
			} else {
				selectImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.org_person_no_select));
			}
			Button cellSelectButton = (Button) itemView.findViewById(R.id.org_person_list_head_cell_select_button);
			cellSelectButton.setTag(groupPosition);
			cellSelectButton.setOnClickListener(headSelectButtonClick);
			if (selectType == OrgPersonListActivity.SelectType.NONE) {
				cellSelectButton.setVisibility(View.GONE);
				selectImage.setVisibility(View.GONE);
			}
			layout.addView(itemView);

			if (cellMap.getType() == OrgTreeElement.Type.USER) {
				bgLayout.setBackgroundColor(Color.parseColor("#9999ff"));
			}

			return layout;
		}

		View.OnClickListener headSelectButtonClick = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int groupPositionIndex = Integer.parseInt(view.getTag().toString());
				OrgTreeElement cellMap = contentArray.get(groupPositionIndex);
				if (cellMap.isSelect()) {
					cellMap.setSelect(false);
				} else {
					cellMap.setSelect(true);
				}
				notifyDataSetChanged();
			}
		};
		View.OnClickListener headContentButtonClick = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int groupPositionIndex = Integer.parseInt(view.getTag().toString());
				Log.d("headContentButtonClick----", view.getTag().toString());
				OrgTreeElement cellMap = contentArray.get(groupPositionIndex);
				if (cellMap.getType() == OrgTreeElement.Type.USER && selectType == OrgPersonListActivity.SelectType.NONE) {
					Intent intent = new Intent(activity, PersonHome.class);
					Bundle bundle = new Bundle();
					bundle.putString("userCode", "U=o07c5d12");
					intent.putExtra("bundle", bundle);
					activity.startActivity(intent);
					activity.overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
				}
			}
		};

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

//        // 控制列表只能展开一个组
//        @Override
//        public void onGroupExpanded(int groupPosition) {
//            for (int i = 0; i < getGroupCount(); i++) {
//                // ensure only one expanded Group exists at every time
//                if (groupPosition != i
//                        && orgListView.isGroupExpanded(groupPosition)) {
//                    orgListView.collapseGroup(i);
//                }
//            }
//
//        }
	}
}
