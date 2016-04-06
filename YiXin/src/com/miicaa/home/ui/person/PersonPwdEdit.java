package com.miicaa.home.ui.person;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.DatabaseOption;
import com.miicaa.common.http.HttpMessage;
import com.miicaa.common.http.MessageId;
import com.miicaa.common.http.OnResponseListener;
import com.miicaa.common.http.PhoneHttpRequest;
import com.miicaa.common.http.RequestPackage;
import com.miicaa.home.R;
import com.miicaa.home.cast.PushMessage;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.login.HomeLoginActivity_;

/**
 * Created by Administrator on 13-11-27.
 */
public class PersonPwdEdit extends Activity
{
    String mUserCode;
    String mUserId;

    EditText mSrcPwdEdit;
    EditText mNewPwdEdit;
    EditText mNewPwdRepeatEdit;
    private TextView errText = null;
    private Animation animErrOpen = null;
    private Animation animErrClose = null;
    public static Handler handler = null;
    public static int MSG_HIDE_ERROR = 1;
    private Timer timer = null;

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_pwd_edit_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mUserCode = bundle.getString("userCode");
        mUserId = bundle.getString("userId");
        handler = new Handler()
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
        animErrOpen = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        animErrClose = AnimationUtils.loadAnimation(this, R.anim.push_top_out);

        Button button = (Button)findViewById(R.id.person_pwd_id_back);
        button.setOnClickListener(onBackDown);
        button = (Button)findViewById(R.id.person_pwd_id_commit);
        button.setOnClickListener(onOverDown);

        mSrcPwdEdit = (EditText)findViewById(R.id.person_pwd_id_src);
        mNewPwdEdit = (EditText)findViewById(R.id.person_pwd_id_new);
        mNewPwdRepeatEdit = (EditText)findViewById(R.id.person_pwd_id_new_repeat);

        errText = (TextView) findViewById(R.id.person_pwd_id_err);
    }

    View.OnClickListener onBackDown = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            finish();
        }
    };

    View.OnClickListener onOverDown = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
           if(!checkError()){
               return;
           }
            conDialog();
        }
    };

    protected void conDialog() {
          AlertDialog.Builder builder = new AlertDialog.Builder(PersonPwdEdit.this);
          builder.setMessage("修改密码后请用新密码重新登录");
          builder.setTitle("提示");
          builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
          });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                checkSecry();
            }
        });
        builder.create().show();
    }

    private  Boolean checkError(){
        String newPwd = mNewPwdEdit.getText().toString();
        String newPwdRepeat = mNewPwdRepeatEdit.getText().toString();
        String srcSecry = mSrcPwdEdit.getText().toString();
        if(!AccountInfo.instance().getUserPassword().equals(srcSecry))
        {
        	showError("原始密码输入错误！");
        	return false;
        }else if(newPwd == null || newPwd.length() < 1)
        {
            showError("请输入修改密码！");
            return false;
        }else if(newPwd.length()<6||newPwd.length()>14){
            showError("密码长度6~14位，字母区分大小写!");
            return false;
        }
        else if(!newPwd.equals(newPwdRepeat))
        {
            showError("两次输入的新密码不一致！");
            return false;
        }if (newPwd.equals(srcSecry)){
            showError("新密码不能和原始密码一样！");
            return false;
        }
        return  true;
    }

    private void checkSecry()
    {
        String srcSecry = mSrcPwdEdit.getText().toString();
//        RequestPackage rp = new RequestPackage();
        String mUrl = "/home/phone/personcenter/checkpsw";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    modifyPwd();
                }else {
                    showError("校验失败");
                    return;
                }
            }@Override
             public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(mUrl)
                .addParam("email",AccountInfo.instance().getUserEmail())
                .addParam("psw",srcSecry)
                .notifyRequest();
//        rp.AddParam("email", AccountInfo.instance().getUserEmail());
//        rp.AddParam("psw", srcSecry);
//        rp.mTagString = "checkpsw";
//        rp.setOnResponseListener(onResponseListener);
//        PhoneHttpRequest.getInstance().sendRequest(rp);
    }

    private void modifyPwd()
    {
       String newPwd = mNewPwdEdit.getText().toString();
        String srcSecry = mSrcPwdEdit.getText().toString();
        RequestPackage rp = new RequestPackage();
        String mUrl = "/home/phone/personcenter/changepsw";
        new RequestAdpater(){
            @Override
            public void onReponse(ResponseData data) {

                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    Toast.makeText(PersonPwdEdit.this,"密码修改成功",1).show();
                    Context context = PersonPwdEdit.this;
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(context, HomeLoginActivity_.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("exit", "exit");
                    intent.putExtra("bundle", bundle);
                    context.startActivity(intent);
                    PushMessage.stopPushMessage(context);//停止消息推送服务
//                    activity.finish();
                    finish();
                }else {
//                    showError("新密码未上传成功！");
                    showError("原始密码错误！");
                }
            }@Override
             public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(mUrl)
                .addParam("email",AccountInfo.instance().getUserEmail())
                .addParam("oldPsw",srcSecry)
                .addParam("newPsw",newPwd)
                .notifyRequest();

//        rp.AddParam("email", AccountInfo.instance().getUserEmail());
//        rp.AddParam("oldPsw", srcSecry);
//        rp.AddParam("newPsw", newPwd);
//        rp.mTagString = "changepsw";
        rp.setOnResponseListener(onResponseListener);
        PhoneHttpRequest.getInstance().sendRequest(rp);
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

    private void checkPwdRespone(byte[] revedData)
    {
        if (revedData == null)
        {
            showError("当前密码错误，请重新输入");
            return;
        }
        else
        {
            JSONObject jReuslt = null;
            try
            {
                jReuslt = new JSONObject(new String(revedData));

                Boolean succeed = jReuslt.optBoolean("succeed");
                if (!succeed)
                {
                    showError("当前密码错误，请重新输入");
                    return;
                }
                if(jReuslt.optBoolean("data"))
                {
                    modifyPwd();
                    return;
                }

            }
            catch (JSONException e)
            {

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }

        showError("当前密码错误，请重新输入");
    }

    private void handleResponseData(String tag, byte[] revedData)
    {
        if(tag.equals("checkpsw"))
        {
            checkPwdRespone(revedData);
        }
        else if(tag.equals("changepsw"))
        {
            changePwdResponse(revedData);
            getAutoState();
            this.finish();
        }

    }

    private void changePwdResponse(byte[] revedData)
    {
        if (revedData == null)
        {
            showError("网络异常，请重试");
            return;
        }
        else
        {
            JSONObject jReuslt = null;
            try
            {
                jReuslt = new JSONObject(new String(revedData));

                Boolean succeed = jReuslt.optBoolean("succeed");
                if (!succeed)
                {
                    showError("修改密码不成功");
                    return;
                }
                else {
                    if (getAutoState()) {
                        DatabaseOption.getIntance().setValue("userPwd", mNewPwdEdit.getText().toString());
                        finish();
                        return;
                    }
                }

            }
            catch (JSONException e)
            {
                showError("网络异常，请重试");
            }



        }
    }

    private void showError(String msg) {
        errText.setText(msg);
        errText.setVisibility(View.VISIBLE);
        errText.startAnimation(animErrOpen);
        clearError();
    }

    private void hideError()
    {
        errText.setVisibility(View.INVISIBLE);
        errText.startAnimation(animErrClose);
    }

    private void clearError()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run()
            {
                Message msg = Message.obtain(PersonPwdEdit.handler, PersonPwdEdit.MSG_HIDE_ERROR, null);
                msg.sendToTarget();

                timer.cancel();
                timer = null;
            }
        };
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(task, 3000);
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
}