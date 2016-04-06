package com.miicaa.home.ui.discover.crm;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.base.share.city.CityMain;
import com.miicaa.base.share.contact.AddContactMain;
import com.miicaa.base.share.contact.ContactMain;
import com.miicaa.base.share.industry.IndustryMain;
import com.miicaa.base.share.round.RoundKinds;
import com.miicaa.base.share.round.RoundMain;
import com.miicaa.base.share.select.SelectKindsMain;
import com.miicaa.common.base.Utils;
import com.miicaa.home.HiddenSoftActivity;
import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.widget.UploadWidget;
import com.miicaa.home.view.DescriptView;
import com.miicaa.home.view.LabelEditView;

@EActivity(R.layout.crm_new_custom)
public class CRMNewCustomActivity extends HiddenSoftActivity {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case UploadWidget.GRID_FILE_CHECK:
		case UploadWidget.GRID_PHOTO_CHECK:
			mUpload.onActivityResult(requestCode, resultCode, data);
			break;
		case RoundMain.REQUEST_CODE:
			mRoundMain.onActivityResult(requestCode, resultCode, data);
			break;
		case SelectKindsMain.REQUEST_CODE:
			mKindsMain.onActivityResult(requestCode, resultCode, data);
			break;
		case ContactMain.REQUEST_CODE:
			mContactMain.onActivityResult(requestCode, resultCode, data);
			break;
		case IndustryMain.REQUEST_CODE:
			mIndustryMain.onActivityResult(requestCode, resultCode, data);
			break;
		case AddContactMain.REQUEST_CODE:
			mAddContactMain.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	UploadWidget mUpload;//文件上传
	RoundMain mRoundMain;//范围选择
	SelectKindsMain mKindsMain;//客户类型选择
	ContactMain mContactMain;//客户负责人单选
	CityMain mCityMain;//国家级别选择
	CityMain mDetailCityMain;//省市级别选择
	IndustryMain mIndustryMain;//行业选择
	AddContactMain mAddContactMain;//添加联系人
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById
	LinearLayout photoWidget;
	@ViewById(R.id.scanRound)
	LabelEditView scanRound;
	@ViewById(R.id.customKind)
	LabelEditView customKind;
	@ViewById(R.id.customReponser)
	LabelEditView customReponser;
	@ViewById
	LabelEditView customIndustry;
	@ViewById(R.id.bigCity)
	TextView bigCity;
	@ViewById(R.id.city)
	TextView city;
	@ViewById(R.id.addContact)
	LabelEditView addContact;
	@ViewById(R.id.qq)
	LabelEditView qq;
	@ViewById
	LabelEditView phone;
	@ViewById
	LabelEditView email;
	@ViewById
	LabelEditView fax;
	@ViewById
	EditText address;
	@ViewById
	EditText webAddress;
	@ViewById
	DescriptView descript;
	@ViewById(R.id.customName)
	EditText customName;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		this.finish();
		
	}
	@Click(R.id.pay_commitButton)
	void commit(){
		if(invalide()){
			
		}
		
	}
	@AfterInject
	void initData() {
		mUpload = new UploadWidget(this);
		ArrayList<RoundKinds> rks = new ArrayList<RoundKinds>();
		rks.add(RoundKinds.PUBLIC);
		rks.add(RoundKinds.PEOPLE);
		mRoundMain = new RoundMain(this, rks);
		mKindsMain = new SelectKindsMain(this);
		mContactMain =new ContactMain(this);
		mCityMain = new CityMain(this);
		mDetailCityMain = new CityMain(this);
		mIndustryMain = new IndustryMain(this);
		mAddContactMain = new AddContactMain(this);
	}

	@AfterViews
	void initUI() {
		title.setText("新建客户");
		commit.setText("提交");
		commit.setVisibility(View.VISIBLE);
		Utils.countLimit(customName, 35);
		photoWidget.addView(mUpload.getRootView());
		mRoundMain.setRootView(scanRound);
		mKindsMain.setRootView(customKind);
		mContactMain.setRootView(customReponser);
		initCity();
		initCustom();
		mIndustryMain.setRootView(customIndustry);
		mAddContactMain.setRootView(addContact);
		Utils.QQFilter(qq.getEditor());
	}
	private void initCustom() {
		phone.getEditor().setInputType(InputType.TYPE_CLASS_PHONE);
		email.getEditor().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}
	private void initCity() {
		mCityMain.setRootView(city);
		mCityMain.setLayers(1);
		mCityMain.setmCityinfos(CityMain.initBigCity());
		mDetailCityMain.setRootView(bigCity);
		mDetailCityMain.setLayers(1);
		mDetailCityMain.setmCityinfos(CityMain.initBigCity());
	}
	
	private boolean invalide(){
		if(customName.getText().toString().trim().equals("")){
			PayUtils.showToast(this, "客户名称不能为空", 3000);
			return false;
		}
		if(!mKindsMain.invalide())
			return false;
		if(!mContactMain.invalide())
			return false;
		if(!phone.invalide())
			return false;
		if(!qq.invalide())
			return false;
		if(!email.invalide())
			return false;
		if(!fax.invalide())
			return false;
		if(!mIndustryMain.invalide())
			return false;
		if(!mCityMain.invalide())
			return false;
		if(!mDetailCityMain.invalide())
			return false;
		if(address.getText().toString().trim().equals("")){
			PayUtils.showToast(this, "客户联系地址不能为空", 3000);
			return false;
		}
		if(webAddress.getText().toString().trim().equals(""))
		{
			PayUtils.showToast(this, "客户网址不能为空", 3000);
			return false;
		}
		if(!descript.invalide())
			return false;
		if(!mAddContactMain.invalide())
			return false;
		if(!mRoundMain.invalide())
			return false;
		return true;
	}
}
