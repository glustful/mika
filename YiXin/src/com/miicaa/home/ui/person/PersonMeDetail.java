package com.miicaa.home.ui.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.DateHelper;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.org.StyleDialog;

/**
 * Created by Administrator on 13-11-27.
 */
public class PersonMeDetail extends Activity {
    final static int BASE_CODE = 0;
    final static int HEAD_CODE = 1;
    final static int SEX_CODE = 2;
    final static int DEPART_CODE = 3;

    StyleDialog dialog;

    LinearLayout mHeadLayout;
    ImageView mHeadImg;

    LinearLayout mUserLayout;
    TextView mUserText;

    LinearLayout mPwdLayout;

    LinearLayout mDepartLayout;
    TextView mDepartText;

    LinearLayout mGroupLayout;
    TextView mGroupText;

    LinearLayout mSexLayout;
    TextView mSexText;

    LinearLayout mCallPhoneLayout;
    TextView mCallPhoneText;

    LinearLayout mPhoneLayout;
    TextView mPhoneText;

    LinearLayout mQQLayout;
    TextView mQQText;

    LinearLayout mEmailLayout;
    TextView mEmailText;

    LinearLayout mBirthdayLayout;
    TextView mBirthdayText;

    LinearLayout mAddrLayout;
    TextView mAddrText;

    String mUserCode;
    String mUserId = "";
    Calendar mBirthday = DateHelper.getToday();

    String mUserName;
    String mPwdStr;
    String mDepart;
    String mGroup;
    String mSex;
    String mCallPhone;
    String mPhone;
    String mQQ;
    String mEmail;
    String mBirthdayStr;
    String mAddress;
    
    JSONArray units;
    JSONArray groups;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_me_detail_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
         dialog = new StyleDialog(PersonMeDetail.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        initUI();
        editAuth();
        requestUserInfo();
        loadUserHeadImg();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestUserInfo();
        loadUserHeadImg();
    }

    private void initUI() {
        Button button = (Button) findViewById(R.id.pd_me_id_back);
        button.setOnClickListener(onBackClick);

        mHeadLayout = (LinearLayout) findViewById(R.id.pd_me_id_head_layout);
        mHeadImg = (ImageView) findViewById(R.id.pd_me_id_head_img);
        mHeadLayout.setOnClickListener(onHeadClick);

        mUserLayout = (LinearLayout) findViewById(R.id.pd_me_id_user_layout);
        mUserText = (TextView) findViewById(R.id.pd_me_id_user);
        mUserLayout.setOnClickListener(onUserClick);

        mPwdLayout = (LinearLayout) findViewById(R.id.pd_me_id_pwd_layout);
        mPwdLayout.setOnClickListener(onPwdClick);

        mDepartLayout = (LinearLayout) findViewById(R.id.pd_me_id_depart_layout);
        mDepartText = (TextView) findViewById(R.id.pd_me_id_depart);
        mDepartLayout.setOnClickListener(onDepartClick);

        mGroupLayout = (LinearLayout) findViewById(R.id.pd_me_id_group_layout);
        mGroupText = (TextView) findViewById(R.id.pd_me_id_group);
        mGroupLayout.setOnClickListener(onGroupClick);

        mSexLayout = (LinearLayout) findViewById(R.id.pd_me_id_sex_layout);
        mSexText = (TextView) findViewById(R.id.pd_me_id_sex);
        mSexLayout.setOnClickListener(onSexClick);

        mCallPhoneLayout = (LinearLayout) findViewById(R.id.pd_me_id_call_phone_layout);
        mCallPhoneText = (TextView) findViewById(R.id.pd_me_id_call_phone);
        mCallPhoneLayout.setOnClickListener(onCallPhoneClick);

        mPhoneLayout = (LinearLayout) findViewById(R.id.pd_me_id_phone_layout);
        mPhoneText = (TextView) findViewById(R.id.pd_me_id_phone);
        mPhoneLayout.setOnClickListener(onPhoneClick);

        mQQLayout = (LinearLayout) findViewById(R.id.pd_me_id_qq_layout);
        mQQText = (TextView) findViewById(R.id.pd_me_id_qq);
        mQQLayout.setOnClickListener(onQQClick);

        mEmailLayout = (LinearLayout) findViewById(R.id.pd_me_id_email_layout);
        mEmailText = (TextView) findViewById(R.id.pd_me_id_email);
        //mEmailLayout.setOnClickListener(onEmailClick);

        mBirthdayLayout = (LinearLayout) findViewById(R.id.pd_me_id_birthday_layout);
        mBirthdayText = (TextView) findViewById(R.id.pd_me_id_birthday);
        mBirthdayLayout.setOnClickListener(onBirthdayClick);

        mAddrLayout = (LinearLayout) findViewById(R.id.pd_me_id_addr_layout);
        mAddrText = (TextView) findViewById(R.id.pd_me_id_addr);
        mAddrLayout.setOnClickListener(onAddrClick);
    }

    private void requestUserInfo() {
//        RequestPackage rp = new RequestPackage();
//        rp.mUrl = "/home/phone/personcenter/getpersoninfo";
//        rp.AddParam("userCode", mUserCode);
//        rp.mTagString = "getpersoninfo";
//
//        rp.setOnResponseListener(onResponseListener);
//        PhoneHttpRequest.getInstance().sendRequest(rp);
        String url = "/home/phone/personcenter/getpersoninfo";
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    try {
                        mUserName = "";
                        mPwdStr = "";
                        mDepart = "";
                        mGroup = "";
                        mSex = "";
                        mCallPhone = "";
                        mPhone = "";
                        mQQ = "";
                        mEmail = "";
                        mBirthdayStr = "";
                        mAddress = "";
                        JSONObject jData = data.getJsonObject();
                        JSONObject juser = jData.optJSONObject("user");
                        if (juser != null) {
                            mUserId = juser.optString("id");
                            mUserText.setText(juser.optString("name"));
                            String depart = "";
                            JSONArray jDeparts = juser.optJSONArray("units");
                           
                            if (jDeparts != null) {
                            	units = jDeparts;
                                for (int i = 0; i < jDeparts.length(); i++) {
                                    JSONObject jDepart = jDeparts.optJSONObject(i);
                                   
                                    String name = jDepart.optString("name");
                                  
                                    if (i + 1 == jDeparts.length()) {
                                        depart += name;
                                    } else {
                                        depart += name + ";";
                                    }
                                   
                                }
                            }
                            mDepartText.setText(depart);
                            String group = "";
                            JSONArray jGroups = juser.optJSONArray("groups");
                           
                            if (jGroups != null) {
                            	groups = jGroups;
                                for (int i = 0; i < jGroups.length(); i++) {
                                    JSONObject jDepart = jGroups.optJSONObject(i);
                                    
                                    String name = jDepart.optString("name");
                                   
                                    if ((i + 1) == jGroups.length()) {
                                        group += name;
                                       
                                    } else {
                                        group += name + ";";
                                       
                                    }
                                    
                                }
                                
                            }
                            mGroupText.setText(group);
                            mUserName = juser.optString("name");
                            if (mUserName != null && mUserName.equals("null")) {
                                mUserName = "";
                            }
                            mDepart = depart;
                            mGroup = group;
                            
                            mSex = juser.optString("gender");
                            if (mSex != null && mSex.equals("null")) {
                                mSex = "";
                            }
                            mPhone = juser.optString("phone");
                            if (mPhone != null && mPhone.equals("null")) {
                                mPhone = "";
                            }
                            mCallPhone = juser.optString("cellphone");
                            if (mCallPhone != null && mCallPhone.equals("null")) {
                                mCallPhone = "";
                            }
                            mQQ = juser.optString("qq");
                            if (mQQ != null && mQQ.equals("null")) {
                                mQQ = "";
                            }
                            mEmail = juser.optString("email");
                            if (mEmail != null && mEmail.equals("null")) {
                                mEmail = "";
                            }
                            Date birthday = new Date(juser.optLong("birthday"));
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            mBirthdayStr = formatter.format(birthday);
                            if (mBirthdayStr != null && mBirthdayStr.equals("null")) {
                                mBirthdayStr = "";
                            }
                            mAddress = juser.optString("addr");
                            if (mAddress != null && mAddress.equals("null")) {
                                mAddress = "";
                            }
                        }
                        if (mUserName == null || mUserName.length() < 1) {
                            mUserText.setText("未填写");
                        } else {
                            mUserText.setText(mUserName);
                        }

                        if (mDepart == null || mDepart.length() < 1) {
                            mDepartText.setText("未填写");
                        } else {
                            mDepartText.setText(mDepart);
                        }

                        if (mGroup == null || mGroup.length() < 1) {
                            mGroupText.setText("未填写");
                        } else {
                            mGroupText.setText(mGroup);
                        }

                        if (mSex == null || mSex.length() < 1) {
                            mSexText.setText("未填写");
                        } else {
                            if (mSex.equals("F")) {
                                mSexText.setText("女");
                            } else {
                                mSexText.setText("男");
                            }
                        }


                        if (mCallPhone == null || mCallPhone.length() < 1) {
                            mCallPhoneText.setText("未填写");
                        } else {
                            mCallPhoneText.setText(mCallPhone);
                        }

                        if (mPhone == null || mPhone.length() < 1) {
                            mPhoneText.setText("未填写");
                        } else {
                            mPhoneText.setText(mPhone);
                        }

                        if (mQQ == null || mQQ.length() < 1) {
                            mQQText.setText("未填写");
                        } else {
                            mQQText.setText(mQQ);
                        }

                        if (mEmail == null || mEmail.length() < 1) {
                            mEmailText.setText("未填写");
                        } else {
                            mEmailText.setText(mEmail);
                        }

                        if (mBirthdayStr == null || mBirthdayStr.length() < 1) {
                            mBirthdayText.setText("未填写");
                        } else {
                            mBirthdayText.setText(mBirthdayStr);
                        }

                        if (mAddress == null || mAddress.length() < 1) {
                            mAddrText.setText("未填写");
                        } else {
                            mAddrText.setText(mAddress);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //TODO:失败了要弹通知
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("userCode", mUserCode)
                .notifyRequest();
    }


    private void editAuth(){
        String url = "/home/phone/personcenter/editfields";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    try {
                        JSONObject authData = data.getMRootData().getJSONObject("data").getJSONObject("editField");
                        String  sBirthday = authData.getString("birthday");
                        String sUnit = authData.getString("unit");
                        String sPhone = authData.getString("phone");
                        String sName = authData.getString("name");
                        String sCellPhone = authData.getString("cellPhone");
                        String sGender = authData.getString("gender");
                        String sGroup = authData.getString("group");
                        String sQQ = authData.getString("qq");

                    if(sBirthday.equals( "false")){
                        mBirthdayLayout.setClickable(false);
                    }if (sUnit.equals("false")){
                        mDepartLayout.setClickable(false);
                    }if (sPhone.equals("false")){
                        mPhoneLayout.setClickable(false);
                    }if (sName.equals("false")){
                        mUserLayout.setClickable(false);
                    }if (sCellPhone.equals("false")){
                        mCallPhoneLayout.setClickable(false);
                    }if (sGender.equals("false")){
                        mSexLayout.setClickable(false);
                    }if (sGroup.equals( "false")){
                        mGroupLayout.setClickable(false);
                    }if (sQQ.equals("false")){
                        mQQLayout.setClickable(false);
                    }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }else{
                    Toast.makeText(PersonMeDetail.this,data.getMsg()+"",1).show();
                    dialog.dismiss();
                    finish();

                }

            }
            @Override
        public  void onProgress(ProgressMessage msg){

            }
        }.setUrl(url)
                .notifyRequest();
    }

    private void loadUserHeadImg() {
        if (mUserCode != null) {
            Tools.setHeadImgWithoutClick(mUserCode, mHeadImg);
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    View.OnClickListener onHeadClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonHeadEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, HEAD_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };
    View.OnClickListener onUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mUserName);
            bundle.putString("param", "name");
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onPwdClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonPwdEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onDepartClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	Intent intent = new Intent(PersonMeDetail.this, PersonDepartEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("url", "/home/phone/personcenter/getunit");
            bundle.putString("type", "unit");
            bundle.putString("code", "");
            if(units != null)
            bundle.putString("data", units.toString());
           
            intent.putExtra("bundle", bundle);
         
            startActivityForResult(intent, DEPART_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onGroupClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        	Intent intent = new Intent(PersonMeDetail.this, PersonDepartEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("url", "/home/phone/personcenter/getgroup");
            bundle.putString("type", "group");
            bundle.putString("code", "");
            if(groups != null)
            bundle.putString("data", groups.toString());
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, DEPART_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onSexClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonSexEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("sex", mSex);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, SEX_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onCallPhoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mCallPhone);
            bundle.putString("param", "cellphone");
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onPhoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mPhone);
            bundle.putString("param", "phone");
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onQQClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mQQ);
            bundle.putString("param", "qq");
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onEmailClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mEmail);
            bundle.putString("param", "email");
            intent.putExtra("bundle", bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    View.OnClickListener onBirthdayClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBirthday = DateHelper.getToday();
            try {
                Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(mBirthdayText.getText().toString());
                mBirthday.setTime(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            new DatePickerDialog(PersonMeDetail.this, new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    mBirthday = Calendar.getInstance();
                    mBirthday.set(i, i2, i3);
                    upDateBirthday();
                }
            }, mBirthday.get(Calendar.YEAR), mBirthday.get(Calendar.MONTH), mBirthday.get(Calendar.DAY_OF_MONTH))
                    .show();
        }
    };

    View.OnClickListener onAddrClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(PersonMeDetail.this, PersonBaseInfoEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("userCode", mUserCode);
            bundle.putString("userId", mUserId);
            bundle.putString("content", mAddress);
            bundle.putString("param", "addr");
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, BASE_CODE);
            overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
        }
    };

    private void upDateBirthday() {
        final String birthday = new SimpleDateFormat("yyyy-MM-dd").format(mBirthday.getTime());
        String mUrl = "/home/phone/personcenter/edituserinfo";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    mBirthdayText.setText(birthday);
                }else {

                }
            }@Override
             public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(mUrl)
                .addParam("userId",mUserId)
                .addParam("userCode",mUserCode)
                .addParam("birthday",birthday)
                .notifyRequest();
//
//        RequestPackage rp = new RequestPackage();
//        rp.mUrl = "/home/phone/personcenter/edituserinfo";
//        rp.AddParam("userId", mUserId);
//        rp.AddParam("userCode", mUserCode);
//        rp.AddParam("birthday", birthday);
       //TODO:待实现

    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BASE_CODE:
            if (resultCode == RESULT_OK && data !=null) {
                Bundle bundle = data.getBundleExtra("bundle");
                String code = bundle.getString("code");
                String result = bundle.getString("result");
                if (code.equals("name")) {
                    mUserText.setText(result);
                    AccountInfo.instance().getLastUserInfo().setName(result);
                } else if (code.equals("cellphone")) {
                    mCallPhoneText.setText(result);
                } else if (code.equals("phone")) {
                    mPhoneText.setText(result);
                } else if (code.equals("qq")) {
                    mQQText.setText(result);
                } else if (code.equals("email")) {
                    mEmailText.setText(result);
                } else if (code.equals("addr")) {
                    mAddrText.setText(result);
                }
            }break;
            case HEAD_CODE:
                if (resultCode == RESULT_OK) {
                    loadUserHeadImg();
                }
                break;
            case SEX_CODE:
                if (resultCode == RESULT_OK && data != null){
                    Bundle bundle = data.getBundleExtra("bundle");
                    String code = bundle.getString("sexcode");
                    mSexText.setText(code);
                }
            default:
                break;
        }
    }


}