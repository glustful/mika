package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.Member;

@EViewGroup(R.layout.grid_item_chat_detail)
public class ChatDetialItem extends RelativeLayout {

	@ViewById(R.id.iv_icon)
	public ImageView mIconView;

	@ViewById(R.id.tv_name)
	public TextView mTextView;

	@ViewById
	public ImageView ivDel;
	
	@DimensionPixelOffsetRes(R.dimen.icon_size_normal)
	int size;
	
	private BitmapUtils mBitmapUtils;

	public ChatDetialItem(Context context) {
		super(context);
	}

	public ChatDetialItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChatDetialItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void bind(Member member, boolean isDelMode) {
		mTextView.setText(member.NickName);
		mIconView.setImageResource(R.drawable.an_user_head_large);

		ivDel.setVisibility(isDelMode ? View.VISIBLE : View.GONE);
		
//		mBitmapUtils.display(mIconView, Utils.getAvataUrl(member.UserName, size));
	}
	
	public void setBitmapUtils(BitmapUtils bitmapUtils) {
		this.mBitmapUtils = bitmapUtils;
	}
	public void setHeadImg(String userCode){
		Tools.setHeadImgWithoutClick(userCode, mIconView);
	}
}
