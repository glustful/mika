package com.miicaa.home.ui.person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.common.base.LinearMessageBox;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-11-27.
 */
public class PersonOtherDetail extends Activity {
    ImageView mUserHeadImg;
    TextView mUserText;
    ImageView mSexImg;
    TextView mDepartText;
    TextView mGroupText;

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
    String mCallPhone = "";
    String mPhone = "";
    String mQQ;
    String mEmail;
    String mBirthday;
    String mAddress;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_other_detail_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
        initUI();
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
        Button button = (Button) findViewById(R.id.pd_other_id_back);
        button.setOnClickListener(onBackClick);

        mUserHeadImg = (ImageView) findViewById(R.id.pd_other_id_head_img);
        mUserText = (TextView) findViewById(R.id.pd_other_id_user_name);
        mSexImg = (ImageView) findViewById(R.id.pd_other_id_sex);
        mDepartText = (TextView) findViewById(R.id.pd_other_id_depart);
        mGroupText = (TextView) findViewById(R.id.pd_other_id_group);

        mCallPhoneLayout = (LinearLayout) findViewById(R.id.pd_other_id_call_phone_layout);
        mCallPhoneText = (TextView) findViewById(R.id.pd_other_id_call_phone);
        mCallPhoneLayout.setOnLongClickListener(onCallPhoneLongClick);

        mPhoneLayout = (LinearLayout) findViewById(R.id.pd_other_id_phone_layout);
        mPhoneText = (TextView) findViewById(R.id.pd_other_id_phone);
        mPhoneLayout.setOnLongClickListener(onPhoneLongClick);

        mQQLayout = (LinearLayout) findViewById(R.id.pd_other_id_qq_layout);
        mQQText = (TextView) findViewById(R.id.pd_other_id_qq);
        mQQLayout.setOnLongClickListener(onQQLongClick);

        mEmailLayout = (LinearLayout) findViewById(R.id.pd_other_id_email_layout);
        mEmailText = (TextView) findViewById(R.id.pd_other_id_email);
        mEmailLayout.setOnLongClickListener(onEmailLongClick);

        mBirthdayLayout = (LinearLayout) findViewById(R.id.pd_other_id_birthday_layout);
        mBirthdayText = (TextView) findViewById(R.id.pd_other_id_birthday);
        mBirthdayLayout.setOnLongClickListener(onBirthdayClick);

        mAddrLayout = (LinearLayout) findViewById(R.id.pd_other_id_addr_layout);
        mAddrText = (TextView) findViewById(R.id.pd_other_id_addr);
        mAddrLayout.setOnLongClickListener(onAddrLongClick);
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
                        JSONObject jData = data.getJsonObject();
                        JSONObject juser = jData.optJSONObject("user");

                        mCallPhone = "";
                        mPhone = "";
                        mQQ = "";
                        mEmail = "";
                        mBirthday = "";
                        mAddress = "";
                        if (juser != null) {

                            mUserText.setText(juser.optString("name"));
                            String depart = "";
                            JSONArray jDeparts = juser.optJSONArray("units");
                            if (jDeparts != null) {
                                for (int i = 0; i < jDeparts.length(); i++) {
                                    JSONObject jDepart = jDeparts.optJSONObject(i);
                                    if (i + 1 == jDeparts.length()) {
                                        depart += jDepart.optString("name");
                                    } else {
                                        depart += jDepart.optString("name") + ";";
                                    }
                                }
                            }
                            mDepartText.setText(depart);
                            String group = "";
                            JSONArray jGroups = juser.optJSONArray("groups");
                            if (jGroups != null) {
                                for (int i = 0; i < jGroups.length(); i++) {
                                    JSONObject jDepart = jGroups.optJSONObject(i);
                                    if (i + 1 == jDeparts.length()) {
                                        group += jDepart.optString("name");
                                    } else {
                                        group += jDepart.optString("name") + ";";
                                    }
                                }
                            }
                            mGroupText.setText(group);
                            String sex = juser.optString("gender");
                            if (sex.equals("F")) {
                                mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_f));
                            } else if (sex.equals("M")) {
                                mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_m));
                            } else {
                                mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_m));
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
                            mBirthday = formatter.format(birthday);
                            if (mBirthday != null && mBirthday.equals("null")) {
                                mBirthday = "";
                            }
                            mAddress = juser.optString("addr");
                            if (mAddress != null && mAddress.equals("null")) {
                                mAddress = "";
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

                        if (mBirthday == null || mBirthday.length() < 1) {
                            mBirthdayText.setText("未填写");
                        } else {
                            mBirthdayText.setText(mBirthday);
                        }

                        if (mAddress == null || mAddress.length() < 1) {
                            mAddrText.setText("未填写");
                        } else {
                            mAddrText.setText(mAddress);

                        }
                    } catch (Exception e) {

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

    private void loadUserHeadImg() {
        if (mUserCode != null) {
            Tools.setHeadImgWithoutClick(mUserCode, mUserHeadImg);
        }
    }

    public boolean isNumeric(String numberstr) {

        if (numberstr == null || numberstr.length() < 1)
            return false;
        String temp = numberstr;
        temp = temp.replace("-", "0");
        temp = temp.replace("", "");
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(temp);
        if (isNum.matches()) {
            return true;
        }
        return false;
    }


    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonOtherDetail.this.finish();
        }
    };

    View.OnLongClickListener onCallPhoneLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (isNumeric(mCallPhone)) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("拨打电话", "call"));
                items.add(new PopupItem("发短信", "sendTo"));
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "call") {
                                    Intent intent = new Intent(
                                            Intent.ACTION_DIAL, Uri.parse("tel:" + mCallPhone));
                                    startActivity(intent);
                                } else if (msg.mCode == "sendTo") {
                                    Intent intent = new Intent(
                                            Intent.ACTION_SENDTO, Uri.parse("smsto:" + mCallPhone));
                                    startActivity(intent);
                                } else if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                                    ClipData clip = ClipData.newPlainText("simple text", mCallPhone);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }

            return true;
        }
    };

    View.OnLongClickListener onPhoneLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (isNumeric(mPhone)) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("拨打电话", "call"));
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "call") {
                                    Intent intent = new Intent(
                                            Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
                                    startActivity(intent);
                                } else if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("simple text", mPhone);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }
            return true;
        }
    };

    View.OnLongClickListener onQQLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mQQ != null && mQQ.length() > 0) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                                    ClipData clip = ClipData.newPlainText("simple text", mQQ);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }
            return true;
        }
    };

    View.OnLongClickListener onEmailLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mEmail != null && mEmail.length() > 0) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                                    ClipData clip = ClipData.newPlainText("simple text", mEmail);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }
            return true;
        }
    };

    View.OnLongClickListener onBirthdayClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mBirthday != null && mBirthday.length() > 0) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                                    ClipData clip = ClipData.newPlainText("simple text", mBirthday);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }
            return true;
        }
    };

    View.OnLongClickListener onAddrLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mAddress != null && mAddress.length() > 0) {
                ArrayList<PopupItem> items = new ArrayList<PopupItem>();
                items.add(new PopupItem("复制", "copy"));
                LinearMessageBox.builder(PersonOtherDetail.this)
                        .setItems(items)
                        .setOnMessageListener(new OnMessageListener() {
                            @Override
                            public void onClick(PopupItem msg) {
                                if (msg.mCode == "copy") {
                                    ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                                    ClipData clip = ClipData.newPlainText("simple text", mAddress);
                                    cmb.setPrimaryClip(clip);
                                }
                            }
                        })
                        .show();
            }
            return true;
        }
    };
}