package com.miicaa.home.ui.login;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.RestErrorHandler;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.base.share.city.Cityinfo;
import com.miicaa.common.base.DatabaseOption;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.business.account.LoginInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.RequestTask;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.storage.LocalPath;
import com.miicaa.home.ui.guidepage.GuidePageActivity;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.service.VersionBroadCaseReceiver;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.NetUtils.OnReloginListener;
import com.yxst.epic.yixin.MyApplication;
import com.yxst.epic.yixin.data.dto.request.BaseRequest;
import com.yxst.epic.yixin.data.dto.request.LoginRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.LoginResponse;
import com.yxst.epic.yixin.data.rest.IMInterface;
import com.yxst.epic.yixin.utils.CacheUtils;
@EActivity(R.layout.home_login_activity)
public class HomeLoginActivity extends Activity implements RestErrorHandler {

	static String TAG = "HomeLoginActivity";
	
	@ViewById(R.id.login_id_err)
    TextView mErrorText;
	@ViewById(R.id.login_id_email)
    EditText mEmailText;
	@ViewById(R.id.login_id_pwd)
    EditText mPwdText;
	@ViewById(R.id.login_id_auto)
    CheckBox mAutoCheck;
	@ViewById(R.id.login_id_login)
    Button mLoginBtn;
	@ViewById(R.id.login_id_loading_layout)
    RelativeLayout mLoadingLayout;
    @ViewById(R.id.delMailBtn)
    ImageButton delMailButton;
    @ViewById(R.id.delPwdBtn)
    ImageButton delPassWordButton;
    @RestService
	IMInterface myRestClient;
    
    private Animation animErrOpen = null;
    private Animation animErrClose = null;
    private final int MSG_HIDE_ERROR = 1;
    private Timer timer = null;
    private Boolean auto = false;

    LoginInfo mLoginInfo = null;
    Context mContext;
    VersionBroadCaseReceiver mReceiver;
    
    @AfterInject
    void afterInject(){
    	this.mContext = this;
    	 HttpComponentsClientHttpRequestFactory schrf =  new HttpComponentsClientHttpRequestFactory();
		schrf.setConnectTimeout(10 * 1000);
		schrf.setReadTimeout(10 * 1000);
		myRestClient.getRestTemplate().setRequestFactory(schrf);
		myRestClient.setRestErrorHandler(this);
    }

    @AfterViews
    void crateData(){
    	mReceiver = new VersionBroadCaseReceiver();
    	 IntentFilter intentFilter = new IntentFilter(AllUtils.version_reciver);
         registerReceiver(mReceiver, intentFilter);
        mLoginInfo = LoginInfo.lastLogin();
        mAutoCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				auto = !auto;
				mAutoCheck.setChecked(auto);
				mLoginInfo.updateLoginAuto(auto);
			}
		});
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle!=null) {
            String exit = bundle.getString("exit");
            if("exit".equals(exit)){
                if (mLoginInfo != null){
//                    mLoginInfo.setLoginAuto(false);
                }
            }
        }
        if (mLoginInfo == null){
            Log.e("mLogininfo",mLoadingLayout.toString());
            mLoginInfo = new LoginInfo();
            mLoginInfo.setUserEmail("");
            mLoginInfo.setUserPassword("");
            mLoginInfo.setLoginAuto(false);
        }else{
        }
        initUI();
        initData();
	}
    
    @TextChange(R.id.login_id_email)
    void mailTextChange(CharSequence text, TextView textView, int before, int start, int count){
    	if(TextUtils.isEmpty(text)){
    		delMailButton.setVisibility(View.GONE);
//    		setEditTextRightDrawable(mEmailText);
    	}else{
    		delMailButton.setVisibility(View.VISIBLE);
//    		setEditTextDrawbleNull(mEmailText);
    	}
    }
    
    @TextChange(R.id.login_id_pwd)
    void passwordTextChange(CharSequence text, TextView textView, int before, int start, int count){
    	if(TextUtils.isEmpty(text)){
    		delPassWordButton.setVisibility(View.GONE);
//    		setEditTextRightDrawable(mPwdText);
    	}else{
    		delPassWordButton.setVisibility(View.VISIBLE);
//    		setEditTextDrawbleNull(mPwdText);
    	}
    }
    
    
    @Click(R.id.delMailBtn)
    void delMailClick(View v){
    	mEmailText.setText("");
    	mPwdText.setText("");
    }
    
    @Click(R.id.delPwdBtn)
    void delPwdClick(View v){
    	mPwdText.setText("");
    	mPwdText.requestFocus();
    }
    
    @Click(R.id.loginRegister)
    void registerClick(View v){
    	 RegisterActivity_.intent(this)
    	 .start();
    }
    
    private void setEditTextRightDrawable(EditText editText){
    	Drawable drawable = getResources().getDrawable(R.drawable.contact_list_clear_button_selector);
    	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    	editText.setCompoundDrawables(null, null, drawable, null);
    }
    
    private void setEditTextDrawbleNull(EditText editText){
    	editText.setCompoundDrawables(null, null, null, null);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

//        initData();
    }
   
    private void initUI()
    {
        animErrOpen = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        animErrClose = AnimationUtils.loadAnimation(this, R.anim.push_top_out);
        mErrorText.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mLoginBtn.setOnClickListener(onLogin);
    }

    private void initData()
    {
        //删除以前记录的cookie信息
        File cookieFile = new File(LocalPath.intance().cacheBasePath + "co");
        if(cookieFile.exists()){
            cookieFile.delete();
        }
        RequestTask.setmCookieStore(null);
		String eMail = mLoginInfo.getUserEmail();
		String pwd = mLoginInfo.getUserPassword();
		auto = mLoginInfo.getLoginAuto();
		auto = (auto == null) ? false :auto;
		if(auto)
		{
			if(pwd != null)
			{
				mEmailText.setText(eMail);
				mPwdText.setText(pwd);
			}
			else
			{
				mEmailText.setText("");
				mPwdText.setText("");
			}
		}
		else
		{
			mPwdText.setText("");
		}
		mAutoCheck.setChecked(auto);
    }

    private void autoLogin()
    {
        if(mLoginInfo.getLoginAuto())
        {
//            requestLogin(mLoginInfo.getUserEmail(),mLoginInfo.getUserPassword(), mLoginInfo.getLoginAuto());
        }
    }

    private Boolean getAutoState()
    {
        String auto = DatabaseOption.getIntance().getValue("userAuto");
        if(auto == null || auto.length() < 1)
        {
            return false;
        }
        else if(auto.equals("yes"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void showError(String msg)
    {
        mErrorText.setText(msg);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.startAnimation(animErrOpen);
        clearError();
    }

    private void hideError()
    {
        mErrorText.setVisibility(View.GONE);
        mErrorText.startAnimation(animErrClose);
    }

    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == MSG_HIDE_ERROR)
            {
                hideError();
            }
        }
    };



    private void clearError()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run()
            {
                Message msg = Message.obtain(handler, MSG_HIDE_ERROR, null);
                msg.sendToTarget();
                if(timer != null)
                {
                    timer.cancel();
                    timer = null;
                }
            }
        };
        if (timer != null)
        {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(task, 3000);
    }
    
    @UiThread
     void requestLogin(final String eMail,final String pwd, Boolean auto)
    {
        MyApplication account = (MyApplication) getApplicationContext();
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoginInfo.setUserEmail(eMail);
        mLoginInfo.setUserPassword(pwd);
		mLoginInfo.setLoginAuto(auto);
        mLoginInfo.login(this,account.getAccountInfo(),new OnFinish() {
            @Override
            public void onSuccess(JSONObject res)
            {
                
               
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                for(int i= 0 ;i<=2;i++) {//登录成功后清除原来的通知
                    nm.cancel(i);
                }
                String code = res.optString("userCode");
                /**
                 * 侎聊登录
                 */
               /* Intent intent = new Intent(HomeLoginActivity.this, FramMainActivity.class);
           	 intent.putExtra("guide", true);
                startActivity(intent);
           	  finish();*/
                test();
                loginYouxin(code, pwd);
                
            }

            @Override
            public void onFailed(String msg) {
                mLoadingLayout.setVisibility(View.GONE);
                showError(msg);
            }
        });
    }
    
    protected void test() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("test1");
		list.add("tesst2");
		System.out.println(list);
		new RequestAdpater(){

			@Override
			public void onReponse(ResponseData data) {
				System.out.println("data="+data.getMRootData());
				
			}

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
			
		}.setUrl("/home/phone/crmtest")
		.addParam(new String("ids[]"), list.get(0).toString())
		.addParam(new String("ids[]"), list.get(1).toString())
		.notifyRequest();
		
	}

	@Background
    void loginYouxin(String name,String pswd){
    
    	BaseRequest baseRequest = CacheUtils.getBaseRequest(this);
		LoginRequest request = new LoginRequest();
		request.BaseRequest = baseRequest;
		request.UserName = name;
		request.Password = pswd;
		request.ApnsToken = "";
		Log.d(TAG, "doInBackground() request:----" + request);
		LoginResponse response = myRestClient.login(request);
		MyApplication.getInstance().putLoginResponse(response);
		loginComplete(response);
    }
    
    
    @Background
    void refreshContact(){
//    	new RefreshContact(this).refreshStart();
    }
    
    @UiThread
    void loginComplete(LoginResponse response){
    	Log.d(TAG, "response:"+response);
    	if (response == null || response.BaseResponse.Ret != BaseResponse.RET_SUCCESS) {
			showError("登录失败，请重新登陆");
			/*
			 * 清楚cookie,防止下次直接进入办理页不会退出到登陆页
			 */
			setNullCookie();
			mLoadingLayout.setVisibility(View.GONE);
			return;
		}

    	mLoadingLayout.setVisibility(View.GONE);
    	 Intent intent = new Intent(HomeLoginActivity.this, FramMainActivity.class);
    	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	 intent.putExtra("guide", true);
         startActivity(intent);
    	  finish();
    }
    
    
    public static void LoginYouxin(IMInterface restClient,Context context,OnReloginListener listener){
    	
    	BaseRequest baseRequest = CacheUtils.getBaseRequest(context);
		LoginRequest request = new LoginRequest();
		request.BaseRequest = baseRequest;
		request.UserName = AccountInfo.instance().getLastUserInfo().getCode();
		request.Password = AccountInfo.instance().getUserPassword();
		request.ApnsToken = "";
		Log.d("reLogin", "doInBackground() request:" + request);
		LoginResponse response = restClient.login(request);
		Log.d("reLogin", "doInBackground() response:" + response);
		if(response == null ){
			listener.failed();
			return;
		}
		if(response.BaseResponse.Ret != BaseResponse.RET_SUCCESS){
			listener.failed();
			return;
		}
		MyApplication.getInstance().putLoginResponse(response);
		Log.d("hsakdfasldfsakhdfsjd", "doInBackground() request:" + request);
		if(listener != null){
		listener.success();
		}
		
    }
    
    




    View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
        	
            String eMail = mEmailText.getText().toString();
            String pwd = mPwdText.getText().toString();
            Boolean auto = mAutoCheck.isChecked();
            if(eMail == null || eMail.length() == 0)
            {
                showError("请输入账户");
                return;
            }

            if(pwd == null || pwd.length() == 0)
            {
                showError("请输入密码");
                return;
            }
            Utils.hiddenSoftBorad(mContext);
            requestLogin(eMail,pwd,auto);
        }
    };

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null){
            if(this.getCurrentFocus() != null && this.getCurrentFocus().getWindowToken() != null){
                return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        }
        return true;
    }

	@Override
	public void onRestClientExceptionThrown(RestClientException arg0) {
		Log.e(TAG,"token错误"+arg0.getMessage());
		errorToLogin();
	}
	
	
	@UiThread
	void errorToLogin(){
		showError("登录失败,请重新登录。");
		//**登陆失败删除cookie下一次重新登陆
		setNullCookie();
//		Toast.makeText(HomeLoginActivity.this, "登录失败,请重新登录。", Toast.LENGTH_SHORT).show();
		mLoadingLayout.setVisibility(View.GONE);
	}

	private void setNullCookie(){
		 File cookieFile = new File(LocalPath.intance().cacheBasePath + "co");
	        if(cookieFile.exists()){
	            cookieFile.delete();
	        }
	        RequestTask.setmCookieStore(null);
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	 @Override
	    protected void onStart() {
	    	 mReceiver = new VersionBroadCaseReceiver();
	         IntentFilter intentFilter = new IntentFilter(AllUtils.version_reciver);
	         registerReceiver(mReceiver, intentFilter);
	        super.onStart();
	    }
	    
	    

	    @Override
		protected void onStop() {
	    	unregisterReceiver(mReceiver);
			super.onStop();
		}
	
	
	
	
}
