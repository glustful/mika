package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.ViewHolder;

public class MyCheckWorkAdapter extends BaseAdapter{

	static String TAG = "MyCheckWorkAdapter";
	
	final static int SIGN = 0;
	final static int TONGJI = 1;
	final static int DETAIL = 2;
	
	public  static String TODAYSIGINDATE = "todaysignInDate";
	public  static String TODAYSIGNOUTDATE = "todaysignOutDate";
	public  static String TODAYSIGNINREMIND = "todaysignInRemindDate";
	public  static String TODAYSIGNOUTREMIND = "todaysignOutRemindDate";
	public  static String TODAYWORKTIMEBEGIN = "todayWorkTimeBegin";
	public  static String TODAYWORKTIMEEND = "todayWorkTimeEnd";
	
	Context mContext;
	LayoutInflater inflater;
	MyCheckWorkTongjiAdapter tongjiAdapter;
//	HashMap<String, String> tongjiMaps;
	CheckWorkTongjiContents tongjiContent;
	HashMap<String, String> todayContents;
	ArrayList<CheckWorkDetailContent> checkWorkDetailContents;
	Long nowDate;
	String nowDateStr;
	public MyCheckWorkAdapter(){
		checkWorkDetailContents = new ArrayList<CheckWorkDetailContent>();
	}
	
	public CheckWorkTongjiContents getTongjiContents(){
		return tongjiContent;
	}
	
	public MyCheckWorkAdapter(Context context){
		checkWorkDetailContents = new ArrayList<CheckWorkDetailContent>();
		todayContents = new HashMap<String, String>();
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
	}
	
	public void refresh(HashMap<String, String> todayContents,
			CheckWorkTongjiContents tongjiContets,
			ArrayList<CheckWorkDetailContent> contents,
			Long nowDateStr){
		this.nowDate = nowDateStr;
		this.nowDateStr = AllUtils.getYearTime(nowDate);
		this.todayContents.clear();
		this.todayContents.putAll(todayContents);
		this.tongjiContent = tongjiContets;
		this.checkWorkDetailContents.clear();
		this.checkWorkDetailContents.addAll(contents);
		notifyDataSetChanged();
		if(this.tongjiAdapter != null){
			tongjiAdapter.notifyDataSetChanged();
		}
	}
	
	public void tongjiAdapterRefresh(CheckWorkTongjiContents tongjiContents){
		if(tongjiAdapter != null){
			this.tongjiContent = tongjiContents;
			notifyDataSetChanged();
//			tongjiAdapter.notifyDataSetChanged();
		}
	}
	
	
	public void addMore(HashMap<String, String> todayContents,CheckWorkTongjiContents tongjiContets,
			ArrayList<CheckWorkDetailContent> contents){
		this.checkWorkDetailContents.addAll(contents);
		notifyDataSetChanged();
		if(this.tongjiAdapter != null){
			tongjiAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return checkWorkDetailContents.size() + 2;
	}
	
	

	@Override
	public int getItemViewType(int position) {
		int state_;
		switch (position) {
		case 0:
			state_ =  SIGN;
			break;
		case 1:
			state_ = TONGJI;
			break;
		default:
			state_ = DETAIL;
			break;
		}
		return state_;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return checkWorkDetailContents.get(position -2);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int state_ = getItemViewType(position);
		switch (state_) {
		case SIGN:
			if(convertView == null){
				convertView = SignLayout_.build(mContext);
			}
			((SignLayout)convertView).setSignInContent(todayContents.get(TODAYSIGINDATE), 
					todayContents.get(TODAYSIGNINREMIND),todayContents.get(TODAYWORKTIMEBEGIN))
					.setSignOutContent(todayContents.get(TODAYSIGNOUTDATE),
					todayContents.get(TODAYSIGNOUTREMIND),todayContents.get(TODAYWORKTIMEEND))
					.addNowDate(nowDate);
			break;
		case TONGJI:
			if(convertView == null){
				convertView = inflater.inflate(R.layout.checkwork_tongji_layout, null);
			}
			Button timeButton = ViewHolder.get(convertView, R.id.timeTextBtn);
			if(tongjiContent != null){
			if(tongjiContent.timeType != null){
				timeButton.setText(tongjiContent.timeType.mValue);
				timeButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						tongjiContent.OnDateButtonClick();
					}
				});
			}else{
				timeButton.setText("本月");
			}
			}
			GridView gridView = ViewHolder.get(convertView, R.id.gridView);
			if(tongjiAdapter == null){
				tongjiAdapter = new MyCheckWorkTongjiAdapter();
			}
			gridView.setAdapter(tongjiAdapter);
			break;
		case DETAIL:
			if(convertView == null){
				convertView = inflater.inflate(R.layout.checkwork_content_view, null);
			}
			final CheckWorkDetailContent detailContent = checkWorkDetailContents.get(position-2);
			TextView title = ViewHolder.get(convertView, R.id.title);
			TextView signInTextView = ViewHolder.get(convertView, R.id.signInText);
			TextView signInWhereTextView = ViewHolder.get(convertView, R.id.signInWhereText);
			TextView signOutTextView = ViewHolder.get(convertView, R.id.signOutText);
			TextView signOutWhereTextView = ViewHolder.get(convertView, R.id.signOutWhereText);
			title.setText(detailContent.name + "\r"+detailContent.dateStr);
			if(detailContent.signInDate == null || detailContent.signInDate.length() == 0){
				if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
					signInTextView.setText("");
				}else{
				signInTextView.setText("未签到");
				signInTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
				}
			}else if(detailContent.signInStatusStr != null && 
					detailContent.signInStatusStr.length() > 0){
				SpannableStringBuilder sb = new SpannableStringBuilder();
				sb.append(detailContent.signInDate);
				String status =  detailContent.signInStatusStr;
				sb.append(status);
				int nosignColor = mContext.getResources().getColor(R.color.checkwork_nosign_color);
				sb.setSpan(new ForegroundColorSpan(nosignColor) , 
						detailContent.signInDate.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				signInTextView.setText(sb);
			}else{
				signInTextView.setTextColor(Color.BLACK);
			signInTextView.setText(detailContent.signInDate);
			}
			if(detailContent.WhereIsIp(detailContent.signInWhere)){
				setTextDrawableNull(signInWhereTextView);
				signInWhereTextView.setText(detailContent.signInWhere);
			}else if(detailContent.signInWhere == null || detailContent.signInWhere.length() == 0){
				setTextDrawableNull(signInWhereTextView);
				signInWhereTextView.setText("");
			}else {
				setTextDrawableWithLocation(signInWhereTextView,mContext);
				signInWhereTextView.setText(detailContent.signInWhere);
			}
			
			if(detailContent.signOutDate == null || detailContent.signOutDate.length() == 0){
				if(nowDateStr != null && detailContent.date.equals(nowDateStr)){
					signOutTextView.setText("");
				}else{
				signOutTextView.setText("未签退");
				signOutTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
				}
			}else if(detailContent.signOutStatusStr != null && 
					detailContent.signOutStatusStr.length() > 0){
				SpannableStringBuilder sb = new SpannableStringBuilder();
				sb.append(detailContent.signOutDate);
				String status = detailContent.signOutStatusStr;
				sb.append(status);
				int nosignColor = mContext.getResources().getColor(R.color.checkwork_nosign_color);
				sb.setSpan(new ForegroundColorSpan(nosignColor) , 
						detailContent.signOutDate.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				signOutTextView.setText(sb);
//				signOutTextView.setTextColor(mContext.getResources().getColor(R.color.checkwork_nosign_color));
			}else{
			signOutTextView.setText(detailContent.signOutDate);
			signOutTextView.setTextColor(Color.BLACK);
			}
			if(detailContent.WhereIsIp(detailContent.signOutWhere)){
				setTextDrawableNull(signOutWhereTextView);
				signOutWhereTextView.setText(detailContent.signOutWhere);
			}else if(detailContent.signOutWhere == null || detailContent.signOutWhere.length() == 0){
				setTextDrawableNull(signOutWhereTextView);
				signOutWhereTextView.setText("");
			}else{
				setTextDrawableWithLocation(signOutWhereTextView,mContext);
				signOutWhereTextView.setText(detailContent.signOutWhere);
			}
			
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class MyCheckWorkTongjiAdapter extends BaseAdapter{

//		Context context;
//		public MyCheckWorkTongjiAdapter(){
//			contentMap = new HashMap<String, String>();
//		}
		 public MyCheckWorkTongjiAdapter() {
//			 super();
		}
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.checkwork_tongji_view, null);
			}
			TextView mainTextView = ViewHolder.get(convertView, R.id.mainTextView);
			TextView bottomTextView = ViewHolder.get(convertView, R.id.bottomTextView);
				int bottomnoneIcoId = R.drawable.check_none_ico;
				int bottomLeftIcoId = -1;
			switch (position) {
			case 0:
				String lateText = null;
				if(tongjiContent != null){
				lateText = tongjiContent.later;
				}
				if(lateText == null || lateText.length() == 0 || lateText.equals("0")){
					lateText = "0";
					bottomLeftIcoId = bottomnoneIcoId;
				}else{
					bottomLeftIcoId = R.drawable.check_later_ico;
				}
				mainTextView.setText(lateText);
				setBottomText(bottomTextView, "迟到", bottomLeftIcoId,R.drawable.check_later);
				break;
			case 1:
				String leavedText = null;
				if(tongjiContent != null){
				leavedText = tongjiContent.leaved;
				}
				if(leavedText == null || leavedText.length() == 0 || leavedText.equals("0")){
					leavedText = "0";
					bottomLeftIcoId = bottomnoneIcoId;
				}else{
					bottomLeftIcoId = R.drawable.check_later_ico;
				}
				mainTextView.setText(leavedText);
				setBottomText(bottomTextView, "早退", bottomLeftIcoId, R.drawable.check_leaved);
				break;
			case 2:
				String nosignInText = null;
				if(tongjiContent != null){
				nosignInText = tongjiContent.nosignInCount;
				}
				if(nosignInText == null || nosignInText.length() == 0 || nosignInText.equals("0")){
					nosignInText = "0";
					bottomLeftIcoId = bottomnoneIcoId;
				}else{
					bottomLeftIcoId = R.drawable.check_later_ico;
				}
				mainTextView.setText(nosignInText);
				setBottomText(bottomTextView, "未签到", bottomLeftIcoId, R.drawable.check_no_signin);
				break;
			case 3:
				String nosignOutText = null;
				if(tongjiContent != null){
				nosignOutText = tongjiContent.nosignOutCount;
				}
				if(nosignOutText == null || nosignOutText.length() == 0 || nosignOutText.equals("0")){
					nosignOutText = "0";
					bottomLeftIcoId = bottomnoneIcoId;
				}else{
					bottomLeftIcoId = R.drawable.check_later_ico;
				}
				mainTextView.setText(nosignOutText);
				setBottomText(bottomTextView, "未签退", bottomLeftIcoId, R.drawable.check_no_sign_out);
				break;
			default:
				break;
			}
			return convertView;
		}
		
		private void setBottomText(TextView textView,String str,int rightId,int bottomId){
			textView.setText(str);
			Drawable rightDrawable= mContext.getResources().getDrawable(rightId);
			/// 这一步必须要做,否则不会显示.
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
			Drawable bottomDrawable = mContext.getResources().getDrawable(bottomId);
			bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(), bottomDrawable.getMinimumHeight());
			textView.setCompoundDrawables(null, null, rightDrawable, bottomDrawable);
		}
		
	}
	
	public static void setTextDrawableNull(TextView textView){
		textView.setCompoundDrawables(null, null, null, null);
	}
	
	public static void setTextDrawableWithLocation(TextView textView,Context mContext){
		Drawable leftDrawable= mContext.getResources().getDrawable(R.drawable.location);
		/// 这一步必须要做,否则不会显示.
		leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
		textView.setCompoundDrawables(leftDrawable, null, null, null);
	}
	
	public static void setLeftTextDrawable(TextView textView,Context mContext,int resId){
		Drawable leftDrawable= mContext.getResources().getDrawable(resId);
		/// 这一步必须要做,否则不会显示.
		leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
		textView.setCompoundDrawables(leftDrawable, null, null, null);
	}
	
	
	public static  Boolean WhereIsIp(String where){
		//正则式判断是否为一个ip地址
		 String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])"
		 		+ "(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	      
	      Pattern pat = Pattern.compile(rexp);  
	      
	      Matcher mat = pat.matcher(where);  
	      
	      boolean ipAddress = mat.find();

	      return ipAddress;
	}
	
}
