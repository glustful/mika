package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.miicaa.home.R;

@EViewGroup(R.layout.select_face_value)
public class SelectFaceValueView extends LinearLayout {

	String money = "";
	
	@ViewById
	RadioButton other;
	@ViewById(R.id.input)
	EditText input;
	@ViewById(R.id.edit)
	LinearLayout edit;
	
	@Click(R.id.small)
	void small(){
		input.setText("");
		money = "100";
		
	}
	
	@Click(R.id.middle)
	void middle(){
		input.setText("");
		money = "250";
		
		
	}
	
	@Click(R.id.big)
	void big(){
		input.setText("");
		money = "500";
		
	}
	
	@Click(R.id.bigger)
	void bigger(){
		input.setText("");
		money = "5000";
		
	}
	
	@Click(R.id.other)
	void other(){
		
	}
	
	@CheckedChange(R.id.other)
	void otherCheck(CompoundButton cb,boolean isChecked){
		
		if(isChecked){
			edit.setBackgroundResource(R.drawable.pay_recharge_facevalue_input);;
		}else{
			edit.setBackgroundResource(R.drawable.pay_recharge_edit_bg);;
		}
	}
	
	@Touch(R.id.input)
	boolean onTouch(MotionEvent event,View view){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
		other.setChecked(true);
		money = input.getText().toString();
		
		}
		return false;
	}
	
	@AfterTextChange(R.id.input)
	void onAfterTextChange(Editable view){
		money = view.toString();
		
	}
	
	public SelectFaceValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public String getMoney(){
		return money;
	}

	public void clean() {
		if(other.isChecked()){
			money = "0";
			input.setText("");
		}
	}

}
