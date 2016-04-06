package com.miicaa.home.ui.person;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miicaa.common.http.HttpMessage;
import com.miicaa.common.http.MessageId;
import com.miicaa.common.http.OnResponseListener;
import com.miicaa.common.http.RequestPackage;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-11-28.
 */
public class PersonSexEdit extends Activity
{
    String mUserCode;
    String mUserId;
    String mSex;

    Button mFButton;
    Button mWButton;
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_sex_edit_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
        mUserId = bundle.getString("userId");
        mSex= bundle.getString("sex");
        initUI();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);
    }

    private void initUI()
    {
        Button button = (Button)findViewById(R.id.person_sex_id_back);
        button.setOnClickListener(onBackClick);

        mFButton = (Button)findViewById(R.id.person_sex_id_women);
        mFButton.setOnClickListener(onFClick);
        mWButton = (Button)findViewById(R.id.person_sex_id_men);
        mWButton.setOnClickListener(onWClick);

        if(mSex.equals("F"))
        {
            mFButton.setSelected(true);
            mWButton.setSelected(false);
        }
        else
        {
            mFButton.setSelected(false);
            mWButton.setSelected(true);
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            finish();
        }
    };

    View.OnClickListener onFClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
//            RequestPackage rp = new RequestPackage();
           String mUrl = "/home/phone/personcenter/edituserinfo";
            new RequestAdpater(){
                @Override
                public void onReponse(ResponseData data) {

                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                           Intent intent = new Intent();
                           Bundle bundle = new Bundle();
                           bundle.putString("sexcode",mFButton.getText().toString());
                           intent.putExtra("bundle",bundle);
                           setResult(RESULT_OK,intent);
                       finish();
                    }else {

                    }
                }@Override
                 public void onProgress(ProgressMessage msg) {

                }
            }.setUrl(mUrl)
                    .addParam("userId",mUserId)
                    .addParam("userCode",mUserCode)
                    .addParam("gender","F")
                    .notifyRequest();
//            rp.AddParam("userId", mUserId);
//            rp.AddParam("userCode", mUserCode);
//            rp.AddParam("gender", "F");

//            rp.mTagString = "SexFCommit";

//            rp.setOnResponseListener(onResponseListener);
//            PhoneHttpRequest.getInstance().sendRequest(rp);
        }
    };

    View.OnClickListener onWClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            String mUrl = "/home/phone/personcenter/edituserinfo";
            new RequestAdpater(){
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("sexcode",mWButton.getText().toString());
                        intent.putExtra("bundle",bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else {

                    }
                }@Override
                 public void onProgress(ProgressMessage msg) {

                }
            }.setUrl(mUrl)
                    .addParam("userId",mUserId)
                    .addParam("userCode",mUserCode)
                    .addParam("gender","M")
                    .notifyRequest();
//            rp.AddParam("userId", mUserId);
//            rp.AddParam("userCode", mUserCode);
//            rp.AddParam("gender", "M");
//
//            rp.mTagString = "SexMCommit";
//            rp.setOnResponseListener(onResponseListener);
//            PhoneHttpRequest.getInstance().sendRequest(rp);
        }
    };

    private void responseFCommit(byte[] revedData)
    {
        if (revedData == null)
        {
            return;
        }
        else
        {
            JSONObject jReuslt = null;
            try
            {
                String content = new String(revedData);
                jReuslt = new JSONObject(new String(revedData));

                mFButton.setSelected(true);
                mWButton.setSelected(false);

            }
            catch (JSONException e)
            {

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }

    private void responseMCommit(byte[] revedData)
    {
        if (revedData == null)
        {
            return;
        }
        else
        {
            JSONObject jReuslt = null;
            try
            {
                String content = new String(revedData);
                jReuslt = new JSONObject(new String(revedData));

                mFButton.setSelected(false);
                mWButton.setSelected(true);

            }
            catch (JSONException e)
            {

            }
            catch (Exception e)
            {
               e.printStackTrace();
            }


        }
    }

    private void handleResponseData(String tag, byte[] revedData)
    {
        if(tag.equals("SexFCommit"))
        {
            responseFCommit(revedData);
        }
        else if(tag.equals("SexMCommit"))
        {
            responseMCommit(revedData);
        }
    }

    OnResponseListener onResponseListener = new OnResponseListener()
    {
        @Override
        public void OnResponse(RequestPackage reqPackage, HttpMessage msg)
        {
            switch (msg.mResCode)
            {
                case MessageId.HTTP_RESPONSE_RECEIVE_END:
                {
                    handleResponseData(reqPackage.mTagString, msg.receivedData);
                    break;
                }
                default:
                {
                    handleResponseData(reqPackage.mTagString, msg.receivedData);
                    break;
                }
            }
        }
    };


}