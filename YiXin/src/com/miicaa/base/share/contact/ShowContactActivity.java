package com.miicaa.base.share.contact;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.view.LabelEditView;

@EActivity(R.layout.add_contact)
public class ShowContactActivity extends HiddenSoftActivity {
	@ViewById(R.id.pay_cancleButton)
	Button cancel;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById
	LinearLayout rootView;
	ArrayList<Contact> mContacts;
	@Click(R.id.pay_cancleButton)
	void cancel(){
		Intent data = new Intent();
		data.putExtra("data", mContacts);
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	@Click(R.id.pay_commitButton)
	void commit(){
		AddContactActivity_.intent(this)
		.startForResult(1);
	}
	@AfterViews
	void initUI(){
		title.setText("联系人");
		commit.setText("新建联系人");
		commit.setVisibility(View.VISIBLE);
	}
	
	@OnActivityResult(1)
	void onResult(int resultCode,Intent data){
		if(resultCode != Activity.RESULT_OK)
			return;
		Contact c = (Contact) data.getSerializableExtra("data");
		addContact(c);
	}
	private void addContact(Contact c) {
		LinearLayout item = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.show_single_contact, null);
		TextView name = (TextView) item.findViewById(R.id.name);
		name.setText(c.getmName());
		LabelEditView le = (LabelEditView) item.findViewById(R.id.phone);
		le.setContent(c.getPhone());
		le = (LabelEditView) item.findViewById(R.id.email);
		le.setContent(c.getemail());
		le = (LabelEditView) item.findViewById(R.id.qq);
		le.setContent(c.getQq());
		le = (LabelEditView) item.findViewById(R.id.industry);
		le.setContent(c.getIndustry());
		rootView.addView(item, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		if(mContacts == null){
			mContacts = new ArrayList<Contact>();
		}
		mContacts.add(c);
	}
}
