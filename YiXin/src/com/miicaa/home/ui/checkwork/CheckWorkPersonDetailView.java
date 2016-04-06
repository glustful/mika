package com.miicaa.home.ui.checkwork;

import java.util.ArrayList;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.attachment.AttachmentItem;
import com.miicaa.home.view.AttachmentView;

@EViewGroup(R.layout.checkwork_persondetail_view)
public class CheckWorkPersonDetailView extends LinearLayout{

	@SystemService
	LayoutInflater infalter;
	
	Context mContext;
	
	@ViewById(R.id.signTextView)
	TextView signTextView;
	@ViewById(R.id.signWhereTextBtn)
	Button signWhereTextButton;
	@ViewById(R.id.beizhuBtn)
	Button beizhuButton;
	@ViewById(R.id.attachementView)
	AttachmentView attachmentView;
	
	public CheckWorkPersonDetailView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public CheckWorkPersonDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	

	public CheckWorkPersonDetailView(Context context) {
		super(context);
		mContext = context;
	}
	
	public void setText(String signText,String signWhereText,String statusStr,
			String withNotSignWhereStr,
			String beizhuText){
		int nColor = mContext.getResources().getColor(R.color.checkwork_nosign_color);
		SpannableStringBuilder sb = new SpannableStringBuilder();
		if(signText == null || signText.length() == 0){
//			setSignText(withNotSignWhereStr);
			signTextView.setTextColor(nColor);
			signText = withNotSignWhereStr != null ? withNotSignWhereStr : "";
			sb.append(signText);
		}else if(statusStr != null && statusStr.length() > 0){
			sb.append(signText);
			sb.append(statusStr);
			sb.setSpan(new ForegroundColorSpan(nColor), signText.length(), sb.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}else{
			sb.append(signText);
		}
		setSignText(sb);
		setSignWhereText(signWhereText);
		setBeizhuText(beizhuText);
	}
	
	public void setSignText(String signText){
		signTextView.setText(signText);
	}
	
	public void setSignText(SpannableStringBuilder sb){
		signTextView.setText(sb);
	}
	
	public void setSignWhereText(String signWhereText){
		if(signWhereText == null || signWhereText.length() == 0){
				signWhereText = "";
				MyCheckWorkAdapter.setTextDrawableNull(signWhereTextButton);
		}else if(MyCheckWorkAdapter.WhereIsIp(signWhereText)){
			MyCheckWorkAdapter.setTextDrawableNull(signWhereTextButton);
		}else{
			MyCheckWorkAdapter.setTextDrawableWithLocation(signWhereTextButton, mContext);
			signWhereTextButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener_ != null){
						listener_.LocationClickListener(v);
					}
				}
			});
		}
		signWhereTextButton.setText(signWhereText);
	}
	
	public void setBeizhuText(String beizhuText){
		if(beizhuText == null || beizhuText.length() == 0){
			beizhuText = "添加备注";
		}
		beizhuButton.setText(beizhuText);
	}
	
	@Click(R.id.beizhuBtn)
	void beizhuBtnClick(View v){
		if(listener_ != null){
			listener_.beizhuClickListener(v);
		}
	}
	
	public interface OnCheckWorkPersonDetailChangeListener{
		void beizhuClickListener(View v);
		void LocationClickListener(View v);
	}
	
	OnCheckWorkPersonDetailChangeListener listener_;
	public void setOnCheckWorkPersonDetailChangeListener(OnCheckWorkPersonDetailChangeListener 
			listener){
		this.listener_ = listener;
	}
	
	public void setWhereTextViewLeftDrawable(int resId){
		setTextViewLeftDrawable(signTextView, resId);
	}
	
	public void setTextViewLeftDrawable(TextView textView,int resId){
		Drawable leftDrawable= mContext.getResources().getDrawable(resId);
		/// 这一步必须要做,否则不会显示.
		leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
		textView.setCompoundDrawables(leftDrawable, null, null, null);
	}
	
	public void setPhotoIds(ArrayList<String> photoIds,String dataId){
		if(photoIds == null || dataId == null){
			try {
				throw new IllegalAccessException("photoId 或者 dataId起码不能为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		ArrayList<AttachmentItem> itemList = new ArrayList<AttachmentItem>();
		for(String id : photoIds){
			AttachmentItem item = new AttachmentItem(id, "jpg", "x");
			itemList.add(item);
		}
		attachmentView.setShow(itemList, dataId);
	}
	
	public void setBeizhuVisiablity(Boolean show){
		if(show){
			beizhuButton.setVisibility(View.VISIBLE);
		}else{
			beizhuButton.setVisibility(View.GONE);
		}
	}
	
}
