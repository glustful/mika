package com.miicaa.home.ui.business.file;

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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.person.PersonDepartEdit;

@EActivity(R.layout.business_file_select_round)
public class SelectRoundActivity extends Activity {

	
	@Override
	protected void onResume() {
		
		super.onResume();
		if(isChange()){
			round_group_text.setText("");
			round_peopel_text.setText("");
			round_unit_text.setText("");
		}
	}

	private boolean isChange() {
		if(round_unit.isChecked()&&rightType.equals("20"))
			return false;
		if(round_group.isChecked()&&rightType.equals("30"))
			return false;
		if(round_peopel.isChecked()&&rightType.equals("40"))
			return false;
		if(round_public.isChecked()&&rightType.equals("00"))
			return false;
		if(round_private.isChecked()&&rightType.equals("10"))
			return false;
		return true;
	}

	@Extra
	String rightType;
	@Extra
	String json;
	@Extra
	String name;
	@Extra
	String privateTitle;
	
	@ViewById(R.id.cancleButton)
	Button cancel;
	@ViewById(R.id.headTitle)
	TextView title;
	@ViewById(R.id.commitButton)
	Button commit;
	
	@ViewById(R.id.round_public)
	RadioButton round_public;
	
	@ViewById(R.id.round_private)
	RadioButton round_private;
	
	@ViewById(R.id.round_unit)
	RadioButton round_unit;
	
	@ViewById(R.id.round_group)
	RadioButton round_group;
	
	@ViewById(R.id.round_peopel)
	RadioButton round_peopel;
	
	
	
	@ViewById(R.id.round_unit_text)
	TextView round_unit_text;
	
	@ViewById(R.id.round_group_text)
	TextView round_group_text;
	
	@ViewById(R.id.round_peopel_text)
	TextView round_peopel_text;
	
	Context mContext;
	
	@Click(R.id.cancleButton)
	void cancel(){
		finish();
	}
	
	@Click(R.id.round_public)
	void roundPublic(){
		setChecked(round_public);
		Intent intent = new Intent();
		intent.putExtra("success", 2);
		setResult(Activity.RESULT_OK,intent);
		finish();
	}
	
	@Click(R.id.round_private)
	void roundPrivate(){
		setChecked(round_private);
		Intent intent = new Intent();
		intent.putExtra("success", 3);
		setResult(Activity.RESULT_OK,intent);
		finish();
	}
	
	@Click(R.id.round_unit_layout)
	void roundUnit(){
		setChecked(round_unit);
		skipActivity("20");
	}
	
	@Click(R.id.round_group_layout)
	void roundGroup(){
		setChecked(round_group);
		skipActivity("30");
	}
	
	@Click(R.id.round_peopel_layout)
	void roundPeople(){
		setChecked(round_peopel);
		Intent intent = new Intent(mContext,
				SelectContacter.class);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		bundle.putString(SelectContacter.how, "round");
		intent.putExtra("bundle", bundle);
		startActivityForResult(intent, 4);
	}
	
	@AfterInject
	void initData(){
		this.mContext = this;
	}
	
	@AfterViews
	void initUi(){
		cancel.setVisibility(View.VISIBLE);
		title.setText("选择查看范围");
		commit.setVisibility(View.INVISIBLE);
		if(privateTitle==null||privateTitle.equals(""))
		{
			privateTitle = "仅自己";
			
		}else{
			((View)round_group.getParent()).setVisibility(View.GONE);
			((View)round_unit.getParent()).setVisibility(View.GONE);
			((View)round_peopel.getParent()).setVisibility(View.GONE);
		}
		round_private.setText(privateTitle);
		if(rightType.equals("00")){
			
			setChecked(round_public);
		}else if(rightType.equals("10")){
			
			setChecked(round_private);
		}else if(rightType.equals("20")){
			
			setChecked(round_unit);
			round_unit_text.setText(name);
		}else if(rightType.equals("30")){
			
			setChecked(round_group);
			round_group_text.setText(name);
		}else if(rightType.equals("40")){
			setChecked(round_peopel);
			round_peopel_text.setText(name);
			
		}
	}
	
	private void setChecked(RadioButton rb){
		round_group.setChecked(false);
		round_peopel.setChecked(false);
		round_private.setChecked(false);
		round_unit.setChecked(false);
		round_public.setChecked(false);
		rb.setChecked(true);
	}
	
	private void skipActivity(String type) {
		Intent intent = new Intent(mContext, PersonDepartEdit.class);
		intent.putExtra("rightType", type);
		Bundle b = new Bundle();
		if (type.equals(rightType)) {
			b.putString("data", json);
		} else {
			b.putString("data", "[]");
		}
		
		if (type.equals("20")) {
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		data.putExtra("success", requestCode);
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	@Override
    public void finish(){
    	
        super.finish();
       
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }
}
