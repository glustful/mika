package com.miicaa.base.share.round;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.person.PersonDepartEdit;

@EActivity(R.layout.select_round)
public class SelectRoundActivity extends Activity {
	@Override
	protected void onResume() {
		super.onResume();
		if(isChange()){
			cleanContent();
		}
	}

	private void cleanContent() {
		for(int i=0;i<mRootView.getChildCount();i++){
			if(mRootView.getChildAt(i) instanceof RoundItemView){
				((RoundItemView)mRootView.getChildAt(i)).clean();
			}
		}
		
	}

	private boolean isChange() {
		if(mkind.code.equals(mParam.get("rightType"))){
			return false;
		}
		return true;
	}
	
	@Extra
	HashMap<String, String> mParam;
	@Extra
	ArrayList<RoundKinds> mKinds;
	@Extra
	ArrayList<SamUser> mUsers;
	RoundKinds mkind;
	@ViewById(R.id.rootView)
	LinearLayout mRootView;
	@ViewById(R.id.cancleButton)
	Button cancel;
	@ViewById(R.id.headTitle)
	TextView title;
	@ViewById(R.id.commitButton)
	Button commit;
	Context mContext;
	
	@Click(R.id.cancleButton)
	void cancel(){
		finish();
	}
		
	private void roundSelect(){
		setChecked();
		Intent intent = new Intent();
		intent.putExtra("selected", mkind);
		setResult(Activity.RESULT_OK,intent);
		finish();
	}
		
	void roundPeople(){
		
		Intent intent = new Intent(mContext,
				SelectContacter.class);
		Bundle bundle = new Bundle();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		if (mUsers!=null && mUsers.size() > 0) {
			for (SamUser s : mUsers) {
				name.add(s.getmName());
				code.add(s.getmCode());
			}
			bundle.putStringArrayList("name", name);
			bundle.putStringArrayList("code", code);
		}
		
		bundle.putString(SelectContacter.how, "round");
		intent.putExtra("bundle", bundle);
		startActivityForResult(intent, 4);
	}
	
	@AfterInject
	void initData(){
		this.mContext = this;
		mkind = RoundKinds.getKind(mParam.get("rightType"));
	}
	
	@AfterViews
	void initUi(){
		cancel.setVisibility(View.VISIBLE);
		title.setText("选择查看范围");
		commit.setVisibility(View.INVISIBLE);
		
		for(int i=0;i<mKinds.size();i++){
			RoundKinds item = mKinds.get(i);
			
			if(RoundKinds.getKind(item.code)!=null){
				
				addLayout(item);
			}
		}
		setChecked();	
	}
	
	private void addLayout(RoundKinds item) {
		if(item.onlyOne){
			addRadioButton(item);
		}else{
			addLinearlayout(item);
		}		
	}

	private void addLinearlayout(RoundKinds item) {
		RoundItemView rv = RoundItemView_.build(mContext);
		rv.label.setText(item.content);
		if(item == mkind)
		rv.content.setText(mParam.get("name"));
		rv.setTag(item);
		rv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mkind = (RoundKinds) v.getTag();
				skipActivity();
				
			}
		});
		mRootView.addView(rv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void addRadioButton(RoundKinds item) {
		RadioButton rb = new RadioButton(mContext);
		
		rb.setPadding(8, RoundMain.dip2px(mContext, 9), RoundMain.dip2px(mContext, 2), RoundMain.dip2px(mContext, 9));
		rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		rb.setBackgroundResource(R.drawable.white_icon_selector);
		rb.setText(item.content);
		rb.setTag(item);
		rb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mkind = (RoundKinds) v.getTag();
				roundSelect();
							}
		});
		mRootView.addView(rb, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void setChecked(){
		for(int i=0;i<mRootView.getChildCount();i++){
			View child = mRootView.getChildAt(i);
			if(child.getTag()==mkind){
				setChecked(child,true);
			}else{
				setChecked(child,false);
			}
		}
	}
	
	private void setChecked(View child, boolean b) {
		if(child instanceof RadioButton){
			((RadioButton)child).setChecked(b);
			return;
		}
		if(child instanceof RoundItemView_){
			((RoundItemView)child).setChecked(b);
		}
	}

	private void skipActivity() {
		
		setChecked();
		if(mkind == RoundKinds.PEOPLE){
			roundPeople();
			return;
		}
		Intent intent = new Intent(mContext, PersonDepartEdit.class);
		intent.putExtra("rightType", mkind.code);
		Bundle b = new Bundle();
		if (mkind.code.equals(mParam.get("rightType"))) {
			b.putString("data", mParam.get("json"));
		} else {
			b.putString("data", "[]");
		}
		
		if (mkind==RoundKinds.UNIT) {
			b.putString("type", "unit");
			b.putString("url", "/home/phone/personcenter/getunit");
		} else {
			b.putString("type", "group");
			b.putString("url", "/home/phone/personcenter/getgroup");
		}
		b.putString("code", "");
		intent.putExtra("bundle", b);
		((Activity) mContext).startActivityForResult(intent, 1);
		((Activity) mContext).overridePendingTransition(
				R.anim.my_slide_in_right, R.anim.my_slide_out_left);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		data.putExtra("selected", mkind);
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	@Override
    public void finish(){
    	
        super.finish();
       
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }
}
