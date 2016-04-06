package com.miicaa.home.ui.person;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.org.UserInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.yxst.epic.yixin.activity.ChatActivity_;
import com.yxst.epic.yixin.preference.CachePrefs_;

/**
 * Created by Administrator on 13-11-26.
 */
@EActivity(R.layout.person_home_activity)
public class PersonHome extends Activity
{
	
	static String TAG = "PersonHome";
	
    String mPoneNum = null;
    Boolean mMySelf;
    
    @Pref
	CachePrefs_ mCachePrefs;
    @ViewById(R.id.person_home_id_back)
    Button button;
    @ViewById(R.id.person_home_id_user_title)
    TextView mTitleText;
    @ViewById(R.id.headImageView)
    ImageView mUserHeadImg;
    @ViewById(R.id.nameTextView)
	TextView mUserText;
//    @ViewById(R.id.person_home_id_sex)
//    ImageView mSexImg;
//    @ViewById(R.id.person_home_id_user_depart)
//    TextView mDepartText;
//    @ViewById(R.id.person_home_id_user_group)
//    TextView mGroupText;
//    @ViewById(R.id.person_home_id_detail)
//    Button mDetailButton;
    @ViewById(R.id.person_home_id_dynamic)
    Button mDynamicButton;
    @ViewById(R.id.person_home_id_work)
    Button mWorkButton;
    @ViewById(R.id.personHomeTalk)
    Button talkButton;
    @ViewById(R.id.person_home_id_other_layout)
    LinearLayout mOtherLayout;
    @ViewById(R.id.person_home_id_call_phone)
    Button mCallButton;
    @ViewById(R.id.frame_me_id_send_msg)
    Button mMsgButton;
    @ViewById(R.id.frame_me_id_send_email)
    Button mEmailButton;
    @Extra
    String mUserCode;   
    @Extra
    String titleText;
    String mName;
    String mUid;
    UserInfo info;
    
    @AfterViews
    void crateData(){
        mMySelf = false;
        talkButton.setVisibility(View.GONE);
        if(mUserCode.equals(AccountInfo.instance().getLastUserInfo().getCode()))
        {
            mMySelf = true;
        }
        mTitleText.setText("详细信息");
        initUI();
        selectForUser(mUserCode);
        loadUserHeadImg();
    }
    
    
    @Click(R.id.headImageView)
    void headViewClick(View v){
    	  if(mMySelf)
          {
              Intent intent = new Intent(PersonHome.this, PersonMeDetail.class);
              Bundle bundle = new Bundle();
              bundle.putString("userCode", mUserCode);
              intent.putExtra("bundle", bundle);
              startActivity(intent);
              overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
          }
          else
          {
              Intent intent = new Intent(PersonHome.this, PersonOtherDetail.class);
              Bundle bundle = new Bundle();
              bundle.putString("userCode", mUserCode);
              intent.putExtra("bundle", bundle);
              startActivity(intent);
              overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
          }
    }
    
    @Background
    void selectForUser(String userCode){
    	info = UserInfo.findByCode(userCode);
    	uiThread(info);
    }
    
    @UiThread
    void uiThread(UserInfo info){
//    	if(info == null){
//    		talkButton.setVisibility(View.GONE);
    		requestUserInfo();
//    	}else{
//    	 mUid = String.valueOf(info.getId());
//    	 mName = info.getName();
//         String depart = "";
//         ArrayList<UnitInfo> jDeparts = info.getUnits();
//         if(jDeparts != null)
//         {
//             for(int i=0;i < jDeparts.size();i++)
//             {
//            	 UnitInfo uInfo = jDeparts.get(i);
//                 if(i+1==jDeparts.size())
//                 {
//                     depart += uInfo.getName();
//                 }
//                 else
//                 {
//                     depart += uInfo.getName()+ ";";
//                 }
//             }
//         }
//      
//         String group = "";
//         ArrayList<GroupInfo> jGroups = info.getGroups();
//         if(jGroups != null)
//         {
//             for(int i=0;i < jGroups.size();i++)
//             {
//                 GroupInfo gInfo = jGroups.get(i);
//                 if(i+1==jDeparts.size())
//                 {
//                     group += gInfo.getName();
//                 }
//                 else
//                 {
//                     group += gInfo.getName() + ";";
//                 }
//             }
//         }
//      
//         String sex = info.getGender();
//         String callPhoneNum = "";
//         String phoneNum = "";
//         if(!mMySelf)
//         {
//              callPhoneNum = info.getCellphone();
//              phoneNum = info.getPhone();
//            
//         }
//         setContent(info.getName(), depart, group, sex, callPhoneNum, phoneNum);
//    	}
    }
    
    void setContent(String name ,String unit,String group,String sex,String callPhoneNum,String phoneNum){
//    	if(!mMySelf)
//        {
//            mTitleText.setText(name);
//        }
//        mUserText.setText(name);
//        mDepartText.setText(unit);
//        mGroupText.setText(group);
//        if(("F").equalsIgnoreCase(sex))
//        {
//            mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_f));
//        }
//        else if(("M").equalsIgnoreCase(sex))
//        {
//            mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_m));
//        }
//        else
//        {
//            mSexImg.setImageDrawable(getResources().getDrawable(R.drawable.an_sex_m));
//        }
    	mUserText.setText(name);
        if((callPhoneNum == null || callPhoneNum.length() == 0 || callPhoneNum.equals("null"))
                && (phoneNum == null || phoneNum.length() == 0 || phoneNum.equals("null")))
        {
            mCallButton.setText("拨电话    未填写");
            mMsgButton.setText("发短信    未填写");
        }
        else if((callPhoneNum != null && callPhoneNum.length() > 0 && !callPhoneNum.equals("null"))
                && (phoneNum == null || phoneNum.length() == 0 || phoneNum.equals("null")))
        {
            mCallButton.setText("拨电话    "+callPhoneNum);
            mMsgButton.setText("发短信    "+callPhoneNum);
            mPoneNum = callPhoneNum;
            mCallButton.setClickable(true);
            mMsgButton.setClickable(true);
        }
        else if((phoneNum != null && phoneNum.length() > 0 && !phoneNum.equals("null"))
                && (callPhoneNum == null || callPhoneNum.length() == 0 || callPhoneNum.equals("null")))
        {
            mCallButton.setText("拨电话    "+phoneNum);
            mMsgButton.setText("发短信    "+phoneNum);
            mPoneNum = phoneNum;
            mCallButton.setClickable(true);
            mMsgButton.setClickable(true);
        }
        else
        {
            mCallButton.setText("拨电话    请选择");
            mMsgButton.setText("发短信    请选择");
            mPoneNum = callPhoneNum + ","+phoneNum;
            mCallButton.setClickable(true);
            mMsgButton.setClickable(true);
        }
    }
    

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        requestUserInfo();
        loadUserHeadImg();
    }

    private void initUI()
    {
       
        button.setOnClickListener(onBackClick);  
        mDynamicButton.setVisibility(View.GONE);  
        mWorkButton.setVisibility(View.GONE); 
        mCallButton.setOnClickListener(onCallPhoneClick);
        mMsgButton.setOnClickListener(onSendToPhoneClick);

        if(mMySelf)
        {
            mOtherLayout.setVisibility(View.GONE);
//            mTitleText.setText("我");
        }
        else
        {
//            mTitleText.setText("");
            mCallButton.setClickable(false);
            mMsgButton.setClickable(false);
        }
        if(mUserCode != null ){
        	talkButton.setOnClickListener(miliaoClick);
        }
    }
    
    //侎聊点击
    OnClickListener miliaoClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			SharedPreferences userInfo = getSharedPreferences("CachePrefs", 0);
			if(mMySelf){
				Toast.makeText(PersonHome.this, "不能向自己发送消息", Toast.LENGTH_SHORT).show();
				return;
			}
			String meUserName = mCachePrefs.userName().get();
//		    meUserName += "@user";
//			if(meUserName == null||"".equals(meUserName)){
//				MyApplication.getInstance().relogin();
//			}
//			mUid += "@user";
//			if(mUid == null || "".equals(mUid)){
//				if(info != null){
//					mUid = String.valueOf(info.getId());
//					mName = info.getName();
//				}
//			}
			mUid += "@user";
			Log.d(TAG, "contact to chatactivity :"+meUserName+mUid+mName);
		    ChatActivity_.intent(PersonHome.this)
		    .localUserName(meUserName)
		    .remoteUserName(mUid)
		    .remoteDisplayName(mName).start();
		}
	};

    private void loadUserHeadImg()
    {
        if(mUserCode != null && mUserCode.length() > 0)
        {
            Tools.setHeadImgWithoutClick(mUserCode,mUserHeadImg);
        }
    }

    @UiThread
     void requestUserInfo()
    {
        String url = "/home/phone/personcenter/getpersoninfo";
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    try {
                        JSONObject jData = data.getJsonObject();
                        JSONObject juser = jData.optJSONObject("user");
                        String name = juser.optString("name");
                        mUid  = juser.optString("id");
                        mName = name;
                        talkButton.setVisibility(View.VISIBLE);
                        if(juser != null)
                        {
                           
                            String depart = "";
                            JSONArray jDeparts = juser.optJSONArray("units");
                            if(jDeparts != null)
                            {
                                for(int i=0;i < jDeparts.length();i++)
                                {
                                    JSONObject jDepart = jDeparts.optJSONObject(i);
                                    if(i+1==jDeparts.length())
                                    {
                                        depart += jDepart.optString("name");
                                    }
                                    else
                                    {
                                        depart += jDepart.optString("name") + ";";
                                    }
                                }
                            }
                            String group = "";
                            JSONArray jGroups = juser.optJSONArray("groups");
                            if(jGroups != null)
                            {
                                for(int i=0;i < jGroups.length();i++)
                                {
                                    JSONObject jDepart = jGroups.optJSONObject(i);
                                    if(i+1==jDeparts.length())
                                    {
                                        group += jDepart.optString("name");
                                    }
                                    else
                                    {
                                        group += jDepart.optString("name") + ";";
                                    }
                                }
                            }
                            String sex = juser.optString("gender");
                           
                            
                            String callPhoneNum = "";
                            String phoneNum = "";
                            if(!mMySelf)
                            {
                                 callPhoneNum = juser.optString("cellphone");
                                 phoneNum = juser.optString("phone");
                                if((callPhoneNum == null || callPhoneNum.length() == 0 || callPhoneNum.equals("null"))
                                        && (phoneNum == null || phoneNum.length() == 0 || phoneNum.equals("null")))
                                {
                                    mCallButton.setText("拨电话    未填写");
                                    mMsgButton.setText("发短信    未填写");
                                }
                                else if((callPhoneNum != null && callPhoneNum.length() > 0 && !callPhoneNum.equals("null"))
                                        && (phoneNum == null || phoneNum.length() == 0 || phoneNum.equals("null")))
                                {
                                    mCallButton.setText("拨电话    "+callPhoneNum);
                                    mMsgButton.setText("发短信    "+callPhoneNum);
                                    mPoneNum = callPhoneNum;
                                    mCallButton.setClickable(true);
                                    mMsgButton.setClickable(true);
                                }
                                else if((phoneNum != null && phoneNum.length() > 0 && !phoneNum.equals("null"))
                                        && (callPhoneNum == null || callPhoneNum.length() == 0 || callPhoneNum.equals("null")))
                                {
                                    mCallButton.setText("拨电话    "+phoneNum);
                                    mMsgButton.setText("发短信    "+phoneNum);
                                    mPoneNum = phoneNum;
                                    mCallButton.setClickable(true);
                                    mMsgButton.setClickable(true);
                                }
                                else
                                {
                                    mCallButton.setText("拨电话    请选择");
                                    mMsgButton.setText("发短信    请选择");
                                    mPoneNum = callPhoneNum + ","+phoneNum;
                                    mCallButton.setClickable(true);
                                    mMsgButton.setClickable(true);
                                }
                            }
                            
                            setContent(name, depart, group, sex, callPhoneNum, phoneNum);
                            
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

    View.OnClickListener onBackClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            PersonHome.this.finish();
        }
    };



    View.OnClickListener onCallPhoneClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(mPoneNum == null || mPoneNum.length() == 0)
            {
                return;
            }

            String[] num = mPoneNum.split(",");
            if(num == null || num.length == 0)
            {
                return;
            }
            else if(num.length == 1)
            {
                Intent intent = new Intent(
                        Intent.ACTION_DIAL, Uri.parse("tel:" + num[0]));
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(PersonHome.this, PhoneCall.class);
                Bundle bundle = new Bundle();
                bundle.putString("callPhone", num[0]);
                bundle.putString("phone", num[1]);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
            }
        }
    };

    View.OnClickListener onSendToPhoneClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(mPoneNum == null || mPoneNum.length() == 0)
            {
                return;
            }

            String[] num = mPoneNum.split(",");
            if(num == null || num.length == 0)
            {
                return;
            }
            else if(num.length == 1)
            {
                Intent intent = new Intent(
                        Intent.ACTION_SENDTO, Uri.parse("smsto:"+num[0]));
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(PersonHome.this, PhoneSendMsg.class);
                Bundle bundle = new Bundle();
                bundle.putString("callPhone", num[0]);
                bundle.putString("phone", num[1]);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
            }
        }
    };
}