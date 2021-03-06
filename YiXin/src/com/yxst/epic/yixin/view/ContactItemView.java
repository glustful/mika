package com.yxst.epic.yixin.view;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.miicaa.home.R;
import com.yxst.epic.yixin.data.dto.model.Member;
import com.yxst.epic.yixin.listener.OnMemberCheckedChangedListener;
import com.yxst.epic.yixin.utils.Utils;

@EViewGroup(R.layout.list_item_contact)
public class ContactItemView extends RelativeLayout {

	private static final String TAG = "ContactItemView";

	@ViewById
	public View viewDivider;

	@ViewById(R.id.iv_icon)
	public ImageView mIconIV;

	@ViewById(R.id.tv_name)
	public TextView mNameTV;

	@ViewById
	TextView tvOrgName;
	
	@ViewById(R.id.cb)
	public CheckBox mCheckBox;
	
	@ViewById
	View ivRight;
	
//	private BitmapUtils mBitmapUtils;
	
	@DimensionPixelOffsetRes(R.dimen.icon_size_small)
	int size;

	public ContactItemView(Context context) {
		super(context);
	}

	public ContactItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContactItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void bind(int imageResId, int textResId) {
		mIconIV.setImageResource(imageResId);
		mNameTV.setText(textResId);
	}
	
	public void bind(int imageResId, String text) {
		mIconIV.setImageResource(imageResId);
		mNameTV.setText(text);
	}

	public void bind(Member member) {
		bind(member, false, false, false);
	}
	
	public void bind(Member member, boolean isSelectMode, boolean isSelected, boolean isLocked) {
		this.member = member;

		//Log.d(TAG, "bind() member:" + member);
		tvOrgName.setText(member.OrgName);
		
		if (member.isTypeUser()) {
			mIconIV.setImageResource(R.drawable.default_avatar);
			mNameTV.setText(member.NickName);
			ivRight.setVisibility(View.GONE);
			
//			mBitmapUtils.display(mIconIV, Utils.getAvataUrl(member.UserName, size));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_default_avata_mini)
				.showImageForEmptyUri(R.drawable.ic_default_avata_mini)
				.showImageOnFail(R.drawable.ic_default_avata_mini)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer())
				.build();
//			ImageLoader.getInstance().displayImage(Utils.getAvataUrl(member.UserName, size), mIconIV, options);
			ImageLoader.getInstance().displayImage(Utils.getImgUrl(member.Avatar, size), mIconIV, options);
		} else if (member.isTypeApp()) {
//			mIconIV.setImageResource(R.drawable.ic_default_app_mini);
			mNameTV.setText(member.NickName);
			ivRight.setVisibility(View.GONE);
			
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_app_mini)
//				.showImageForEmptyUri(R.drawable.ic_default_app_mini)
//				.showImageOnFail(R.drawable.ic_default_app_mini)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.build();
//			ImageLoader.getInstance().displayImage(Utils.getAvataUrl(member.UserName, size), mIconIV, options);
//			ImageLoader.getInstance().displayImage(Utils.getImgUrl(member.Avatar, size), mIconIV, options);
		} else if (member.isTypeOrg()){
//			mIconIV.setImageResource(R.drawable.ic_default_org_mini);
			mNameTV.setText(member.NickName);
			ivRight.setVisibility(View.VISIBLE);
			
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_org_mini)
//				.showImageForEmptyUri(R.drawable.ic_default_org_mini)
//				.showImageOnFail(R.drawable.ic_default_org_mini)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.build();
//			ImageLoader.getInstance().displayImage(Utils.getAvataUrl(member.UserName, size), mIconIV, options);
//			ImageLoader.getInstance().displayImage(Utils.getImgUrl(member.Avatar, size), mIconIV, options);
		} else if (member.isTypeTenant()){
//			mIconIV.setImageResource(R.drawable.ic_default_tenant_mini);
			mNameTV.setText(member.NickName);
			ivRight.setVisibility(View.VISIBLE);
			
//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_tenant_mini)
//				.showImageForEmptyUri(R.drawable.ic_default_tenant_mini)
//				.showImageOnFail(R.drawable.ic_default_tenant_mini)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.build();
//			ImageLoader.getInstance().displayImage(Utils.getAvataUrl(member.UserName, size), mIconIV, options);
//			ImageLoader.getInstance().displayImage(Utils.getImgUrl(member.Avatar, size), mIconIV, options);
		}
		
		mCheckBox
				.setVisibility(isSelectMode && member.isTypeUser() ? View.VISIBLE
						: View.GONE);
		
		mCheckBox.setChecked(isSelected || isLocked);
		
		mCheckBox.setEnabled(!isLocked);
	}

	public void setDividerVisible(boolean b) {
		viewDivider.setVisibility(b ? View.VISIBLE : View.GONE);
	}

	@CheckedChange(R.id.cb)
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mOnMemberCheckedChangedListener != null) {
			mOnMemberCheckedChangedListener.onMemberCheckedChanged(member,
					isChecked);
		}
	}

	private Member member;
	private OnMemberCheckedChangedListener mOnMemberCheckedChangedListener;

	public void setOnMemberCheckedChangedListener(
			OnMemberCheckedChangedListener l) {
		mOnMemberCheckedChangedListener = l;
	}
	
	public void setBitmapUtils(BitmapUtils bitmapUtils) {
//		this.mBitmapUtils = bitmapUtils;
	}
}
