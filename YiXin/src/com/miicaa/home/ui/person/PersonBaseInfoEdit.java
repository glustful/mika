package com.miicaa.home.ui.person;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aps.p;
import com.miicaa.common.base.Utils;
import com.miicaa.common.http.HttpMessage;
import com.miicaa.common.http.MessageId;
import com.miicaa.common.http.OnResponseListener;
import com.miicaa.common.http.RequestPackage;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by Administrator on 13-11-27.
 */
public class PersonBaseInfoEdit extends Activity
{
    EditText mEdit;
    String mUserCode;
    String mUserId;
    String mEditContent;
    String mParam;
    TextView errView;
    Timer timer = null;

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_base_info_edit_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
        mUserId = bundle.getString("userId");
        mEditContent= bundle.getString("content");
        mParam= bundle.getString("param");
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
        TextView titleText = (TextView)findViewById(R.id.person_base_id_title);

        Button button = (Button)findViewById(R.id.person_base_id_back);
        button.setOnClickListener(onBackClick);
        button = (Button)findViewById(R.id.person_base_id_commit);
        button.setOnClickListener(onCommitClick);
        mEdit = (EditText)findViewById(R.id.person_base_id_edit);
        mEdit.setText(mEditContent);
        mEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if("qq".equals(mParam)){
				 String editable = mEdit.getText().toString();   
			        String str = Utils.stringFilter(editable.toString()); 
			        if(!editable.equals(str)){ 
			        	mEdit.setText(str); 
			        	mEdit.setSelection(str.length()); 
			        } 
				}
				if("name".equals(mParam)){
					 if(start > 0 && s.toString().length() == 10){
				        	showErr("长度不能超过10个字！");
				        }
				}else{
			        if(start > 0 && s.toString().length() == 20){
			        	showErr("长度不能超过20个字！");
			        }
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
        mEdit.requestFocus();
        errView = (TextView)findViewById(R.id.person_base_id_err);
        float scale = getResources().getDisplayMetrics().density;
        if(mParam.equals("name"))
        {
            titleText.setText("修改姓名");
            mEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            mEdit.setMinimumHeight((int)(40*scale));
            /**
             * 限制名字十个字
             */
            InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(10);
            mEdit.setFilters(new InputFilter[]{lengthFilter});

        }
        else if(mParam.equals("cellphone"))
        {
            titleText.setText("修改手机号码");
            mEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            mEdit.setMinimumHeight((int)(40*scale));
        }
        else if(mParam.equals("phone"))
        {
            titleText.setText("修改座机号码");
            mEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            mEdit.setMinimumHeight((int)(40*scale));
        }
        else if(mParam.equals("qq"))
        {
            titleText.setText("修改QQ号码");
            mEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            
           mEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 String editable = mEdit.getText().toString();   
			        String str = Utils.stringFilter(editable.toString()); 
			        if(!editable.equals(str)){ 
			        	mEdit.setText(str); 
			            //设置新的光标所在位置 www.2cto.com    
			        	mEdit.setSelection(str.length()); 
			        } 
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
            mEdit.setMinimumHeight((int)(40*scale));
        }
        else if(mParam.equals("email"))
        {
            titleText.setText("修改邮箱号码");
            mEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mEdit.setMinimumHeight((int)(40*scale));
        }
        else if(mParam.equals("addr"))
        {
            titleText.setText("修改住址");
            //mEdit.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            mEdit.setMinimumHeight((int)(100*scale));
           
           	mEdit.setPadding((int)getResources().getDimension(R.dimen.paddingleft), 10, (int)getResources().getDimension(R.dimen.paddingright), 10);
           	
            mEdit.setGravity(Gravity.TOP|Gravity.LEFT);
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            finish();
        }
    };

    private void showErr(String msg){
        errView.setText(msg);
        errView.setVisibility(View.VISIBLE);
        errView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.push_down_in));
        clearError();

    }


    private void clearError(){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
                if (timer != null ){
                    timer.cancel();
                }
            }
        };
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(task,3000);
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    errView.setVisibility(View.GONE);
                    errView.setAnimation(AnimationUtils.loadAnimation(PersonBaseInfoEdit.this,
                            R.anim.push_top_out));
            }
        }
    };

    View.OnClickListener onCommitClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            hiddenKeyBorad();
            if (mEditContent.equalsIgnoreCase(
                    mEdit.getText().toString().trim())){
                showErr("您未做任何修改");
                return;
            }

            final String content = mEdit.getText().toString();
            String mUrl = "/home/phone/personcenter/edituserinfo";

            new RequestAdpater(){
                @Override
                public void onReponse(ResponseData data) {

                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        Intent dataIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("code",mParam);
                        bundle.putString("result",content);
                        dataIntent.putExtra("bundle",bundle);
                        setResult(RESULT_OK,dataIntent);
                        finish();
                    }else {
                        Log.v("why",data.getMsg());
                        showErr(data.getMsg());
                    }
                }@Override
                 public void onProgress(ProgressMessage msg) {

                }
            }.setUrl(mUrl)
                    .addParam("userId", mUserId)
                    .addParam("userCode",mUserCode)
                    .addParam(mParam,content)
                    .notifyRequest();


//            rp.AddParam("userId", mUserId);
//            rp.AddParam("userCode", mUserCode);
//            rp.AddParam(mParam, content);
//
//            rp.mTagString = "personInfoCommit";

//            rp.setOnResponseListener(onResponseListener);
//            PhoneHttpRequest.getInstance().sendRequest(rp);
        }
    };




//        }else if("name".equals(mParam)){
//            Toast.makeText(PersonBaseInfoEdit.this,"名字里不能含有数字，请重新输入！",1).show();
//            mEdit.setText("");
//            return false;
//        }

    private void responseCommit(byte[] revedData)
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

                setResult(RESULT_OK);
                finish();

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
        if(tag.equals("personInfoCommit"))
        {
            responseCommit(revedData);
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

    private void hiddenKeyBorad()
    {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();
        if(isOpen)
        {
            imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    
    
}