package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.checkwork.CheckWorkPersonDetailView.OnCheckWorkPersonDetailChangeListener;
import com.miicaa.home.ui.checkwork.EditSignBeizhuActivity.OnResponseListener;
import com.miicaa.home.ui.home.TestActivity_;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.ViewHolder;

@EActivity(R.layout.activity_checkwork_detail_list)
public class CheckWorkPersonDetailActivity extends MainActionBarActivity{
	
	@ViewById(R.id.listView)
	ListView listView;
	
	final static int START_FOR_SIGNINREMARK = 1;
	final static int START_FOR_SIGNOUTREMARK = 2;
	
//	@Extra
//	ArrayList<CheckWorkDetailContent> detailCotnets;
	
	CheckWorkPersonDetailContentAdapter adapter_;
	
	@Extra
	CheckWorkDetailContent detailContent;
	@Extra
	Long nowDate;
	@Extra
	String leftTitleText;
	@Extra
	String myUserCode;
	
	String nowDateStr;
	
	@AfterInject
	void afterInject(){
		
	}
	
	@AfterViews
	void afterView(){
		nowDateStr = AllUtils.getYearTime(nowDate);
		adapter_ = new CheckWorkPersonDetailContentAdapter(true);
		listView.setAdapter(adapter_);
		
	}
	
	@Override
	public String showBackButtonStr() {
		return "考勤";
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		return "考勤详情";
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public Boolean showRightButton() {
		return false;
	}

	@Override
	public String showRightButtonStr() {
		return null;
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
	
	
	class CheckWorkPersonDetailContentAdapter extends BaseAdapter{

		LayoutInflater inflater;
		Context mContext;
		
		ArrayList<CheckWorkDetailContent> contents;
		public  CheckWorkPersonDetailContentAdapter(Boolean isSingle) {
			contents = new ArrayList<CheckWorkDetailContent>();
			if(detailContent != null){
				contents.add(detailContent);
			}
			mContext = CheckWorkPersonDetailActivity.this;
			inflater = LayoutInflater.from(mContext);
		}
		
		 public CheckWorkPersonDetailContentAdapter(){
			 
		 }
			
		
		public void refresh(){
			contents.clear();
			contents.add(detailContent);
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return contents.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.checkwork_detail_view, null);
			}
			final CheckWorkDetailContent content = contents.get(position);
			TextView titleView = ViewHolder.get(convertView, R.id.titleView);
			titleView.setHint(content.name+content.dateStr);
			CheckWorkPersonDetailView signInView = ViewHolder.get(convertView, R.id.signInView);
			signInView.setWhereTextViewLeftDrawable(R.drawable.sign_in);
			signInView.setPhotoIds(content.singInFileIdList, content.mId);
			if(myUserCode == null || !myUserCode.equals(content.dataUserCode)){
				signInView.setBeizhuVisiablity(false);
			}
			signInView.setOnCheckWorkPersonDetailChangeListener(new OnCheckWorkPersonDetailChangeListener() {
				
				@Override
				public void beizhuClickListener(View v) {
					if(myUserCode == null || !myUserCode.equals(content.dataUserCode)){
						return;
					}
					Boolean isAdd = (content.signInBeizhu == null ||
							content.signInBeizhu.length()==0) ? true : false;
					addBeizhuPopMenu(isAdd, EditSignBeizhuActivity.SIGNIN,START_FOR_SIGNINREMARK,detailContent.signInBeizhu);
				}

				@Override
				public void LocationClickListener(View v) {
					if(content.signInLongitude != null && content.signInLatitude != null){
						Double longitude = Double.parseDouble(content.signInLongitude);
						Double latitude = Double.parseDouble(content.signInLatitude);
						TestActivity_.intent(mContext)
						.latitude(latitude)
						.longitude(longitude)
						.locationStr(content.signInWhere)
						.start();
					}
				}
			});
			String strIn = null;
			if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
				strIn = "";
			}else {
				strIn = "未签到";
			}
			signInView.setText(content.signInDate, content.signInWhere,content.signInStatusStr, strIn,content.signInBeizhu);
			CheckWorkPersonDetailView signOutView = ViewHolder.get(convertView, R.id.signOutView);
			signOutView.setWhereTextViewLeftDrawable(R.drawable.sigin_out);
			signOutView.setPhotoIds(content.singOutFileIdList, content.mId);
			if(myUserCode == null || !myUserCode.equals(content.dataUserCode)){
				signOutView.setBeizhuVisiablity(false);
			}
			String strOut = null;
			if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
				strOut = "";
			}else{
				strOut = "未签退";
			}
			signOutView.setText(content.signOutDate, content.signOutWhere,content.signOutStatusStr, strOut, content.signOutBeizhu);
			signOutView.setOnCheckWorkPersonDetailChangeListener(new OnCheckWorkPersonDetailChangeListener() {
				
				@Override
				public void beizhuClickListener(View v) {
					
					Boolean isAdd = (content.signOutBeizhu == null ||
							content.signOutBeizhu.length()==0) ? true : false;
					addBeizhuPopMenu(isAdd, EditSignBeizhuActivity.SIGNOUT,START_FOR_SIGNOUTREMARK,
							detailContent.signOutBeizhu);
				}

				@Override
				public void LocationClickListener(View v) {
					if(content.signOutLongitude != null && content.signOutLatitude != null){
						Double longitude = Double.parseDouble(content.signOutLongitude);
						Double latitude = Double.parseDouble(content.signOutLatitude);
						TestActivity_.intent(mContext)
						.latitude(latitude)
						.longitude(longitude)
						.locationStr(content.signOutWhere)
						.start();
					}
				}
			});
			return convertView;
		}
		
	}
	
	ArrayList<PopupItem> popupItems = new ArrayList<PopupItem>();
	
	private void addBeizhuPopMenu(final Boolean isAdd,final String signInOrSignOutStr,
			final int requestCode,final String huixian){
		popupItems.clear();
		final String content = isAdd ? "添加备注" :"编辑备注";
		PopupItem topItem = new PopupItem(content, "add");
		popupItems.add(topItem);
		
		if(!isAdd)
		popupItems.add(new PopupItem("删除备注", "clear"));
		
		popupItems.add(new PopupItem("取消", "cancel"));
		BottomScreenPopup.builder(CheckWorkPersonDetailActivity.this)
		.setItems(popupItems)
		.setOnMessageListener(new OnMessageListener() {
			
			@Override
			public void onClick(PopupItem msg) {
			   if(msg.mCode.equals("add")){
					EditSignBeizhuActivity_.intent(CheckWorkPersonDetailActivity.this)
					.titleText(content)
					.signInOrSignOutStr(signInOrSignOutStr)
					.mId(detailContent.mId)
					.huixian(huixian)
					.startForResult(requestCode);
				}else if(msg.mCode.equals("clear")){
					EditSignBeizhuActivity.requestEditBeizhu(signInOrSignOutStr, "", 
							detailContent.mId, 
							new OnResponseListener() {
								
								@Override
								public void onResponse(ResponseData data) {
									if(data.getResultState() == ResultState.eSuccess){
										if(requestCode == START_FOR_SIGNINREMARK){
											detailContent.signInBeizhu = null;
										}else if(requestCode == START_FOR_SIGNOUTREMARK){
											detailContent.signOutBeizhu = null;
										}
										adapter_.refresh();
										Toast.makeText(CheckWorkPersonDetailActivity.this,
												"删除成功", Toast.LENGTH_SHORT).show();
									}else{
										Toast.makeText(CheckWorkPersonDetailActivity.this,
												"删除失败", Toast.LENGTH_SHORT).show();
									}
								}
							});
				}
				
			}
		}).show();
	}
	
	@OnActivityResult(START_FOR_SIGNINREMARK)
	void signInResult(int resultCode,Intent data){
		if(resultCode != RESULT_OK){
			return;
		}
		String remark = data.getStringExtra("remark");
		detailContent.signInBeizhu = remark;
		adapter_.refresh();
	}
	
	@OnActivityResult(START_FOR_SIGNOUTREMARK)
	void signOutResult(int resultCode,Intent data){
		if(resultCode != RESULT_OK){
			return;
		}
		String remark = data.getStringExtra("remark");
		detailContent.signOutBeizhu = remark;
		adapter_.refresh();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}
	
	

}
