package com.miicaa.base.share.contact;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.view.LabelEditView;
@EActivity(R.layout.add_single_contact)
public class AddContactActivity extends HiddenSoftActivity {
	@ViewById(R.id.pay_cancleButton)
	Button cancel;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById
	EditText name;
	@ViewById
	LabelEditView phone;
	@ViewById
	LabelEditView email;
	@ViewById
	LabelEditView qq;
	@ViewById
	LabelEditView industry;
	@Click(R.id.pay_cancleButton)
	
	void cancel(){
		finish();
	}
	@Click(R.id.pay_commitButton)
	void commit(){
		if(name.getText().toString().trim().equals("")){
			PayUtils.showToast(this, "联系人姓名不能为空", 3000);
			return;
		}
		if(phone.isNull()&&email.isNull()&&qq.isNull()){
			PayUtils.showToast(this, "电话、邮箱、QQ至少有一个不为空", 3000);
			return;
		}
		Intent i = new Intent();
		i.putExtra("data", createContact());
		setResult(Activity.RESULT_OK, i);
		finish();
	}
	private Contact createContact() {
		Contact c = new Contact("-1", name.getText().toString(), phone.getText(), email.getText(), qq.getText(), industry.getText());
		return c;
	}
	@AfterViews
	void initUI(){
		title.setText("添加联系人");
		commit.setText("提交");
		commit.setVisibility(View.VISIBLE);
	}
}
