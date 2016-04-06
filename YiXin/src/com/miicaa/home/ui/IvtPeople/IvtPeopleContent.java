package com.miicaa.home.ui.IvtPeople;

import java.util.regex.Pattern;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-8-3.
 */
public abstract class IvtPeopleContent {
    private Context mContext;
    private View mRootView;
    private EditText nameText;
    private EditText mailText;
    private TextView mailError;
    private TextView nameError;
    private String nameString;
    private String mailString;
    private int ivtCount = 0;
    private Button commitButton;
    private Boolean isError;
    public IvtPeopleContent(Context context,Button button)
    {
        mContext = context;
        isError = false;
        commitButton = button;
        initView();
    }
    private void initView(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.ivt_people_layout,null);
        nameText = (EditText)mRootView.findViewById(R.id.ivt_people_layout_name);
        nameError = (TextView)mRootView.findViewById(R.id.ivt_people_layout_nameError);
        mailText = (EditText)mRootView.findViewById(R.id.ivt_people_layout_mail);
        mailError = (TextView)mRootView.findViewById(R.id.ivt_people_layout_mailError);

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "/sysconfig/pc/invite/sendInviteMail";
                new RequestAdpater(){

                    @Override
                    public void onReponse(ResponseData data) {
                        Toast toast = new Toast(mContext);
                        if (data.getResultState() == ResponseData.ResultState.eSuccess){
                            toast.setText("您成功邀请了"+ivtCount+"个人");
                            toast.setView(LayoutInflater.from(mContext).
                                    inflate(R.layout.toast_view_layout,null));
                            toast.show();
                            finishActivity();
                        }else{
                            toast.setText("提交失败");
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onProgress(ProgressMessage msg) {

                    }
                }.setUrl(url)
                        .addParam("usernames",nameString)
                        .addParam("emails",mailString)
                        .notifyRequest();
            }
        });
        commitButton.setClickable(false);
        final Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//邮箱匹配正则表达式
        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (isError == true){
//                    nameText.clearFocus();
                    nameText.setFocusable(false);
                    nameText.setFocusableInTouchMode(false);

                }else{
                    nameText.setFocusable(true);
                    nameText.setFocusableInTouchMode(true);

                }
                if (nameText.hasFocus() == false){
                    if ("".equals(nameText.getText().toString().trim())){
                        isError = true;
                        nameError.setText("请输入姓名");
                        nameError.setVisibility(View.VISIBLE);
                        nameText.setFocusable(true);
                        nameText.setFocusableInTouchMode(true);
//                        nameText.requestFocus();
                        commitButton.setClickable(false);
                        return;
                    }checkNameRep(nameText.getText().toString().trim(),
                            nameError,
                            mailText);
                }
            }
        });
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameError.setVisibility(View.GONE);
            }
        });
        mailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (isError == true){
//                    mailText.clearFocus();
                    mailText.setFocusable(false);
                    mailText.setFocusableInTouchMode(false);
                    mailText.setCursorVisible(false);
                    return;

                }else{
                    mailText.setFocusable(true);
                    mailText.setFocusableInTouchMode(true);
                    mailText.setCursorVisible(true);
                }
                if (mailText.hasFocus() == false){
                    if("".equals(mailText.getText().toString().trim())
                            ||!pattern.matcher(mailText.getText().toString().trim()).matches()){
                        mailError.setText("请正确输入邮箱格式！");
                        mailError.setVisibility(View.VISIBLE);
                        mailText.requestFocus();
                        mailText.setFocusable(true);
                        mailText.setFocusableInTouchMode(true);
                        isError = true;
                        commitButton.setClickable(false);
                        return;
                    }checkMailRep(nameText.getText().toString().trim(),
                            mailText.getText().toString().trim(),
                            mailError);
                }
            }
        });
        mailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mailError.setVisibility(View.INVISIBLE);
            }
        });
    }

    private  void checkNameRep(String name,final TextView error,final EditText mailText){
        String url = "/sysconfig/pc/invite/canAccess/isNameExist";
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    mailText.setFocusable(true);
                }else{
                    if (error.isShown()){
                        return;
                    }
//                    nameText.requestFocus();
                    nameText.setFocusable(true);
                    nameText.setFocusableInTouchMode(true);
                    error.setText("名字重复！");
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("name",name)
                .notifyRequest();


    }

    private void checkMailRep(final String name ,final String mail,final TextView error){
        String url = "/sysconfig/pc/invite/canAccess/isEmailExist";
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    String nameStr;
                    String mailStr;
                    ivtCount ++;
                    if (ivtCount != 1){
                        nameString = nameString + "," + name;
                        mailString = mailString + "," + mail;
                    }else{
                        nameString = name;
                        mailString = mail;
                    }
                    isError = false;
                    commitButton.setTextColor(mContext.getResources().getColor(R.color.ok));
                    commitButton.setClickable(true);
                }else{
                    commitButton.setTextColor(mContext.getResources().getColor(R.color.notok));
                    commitButton.setClickable(false);
                    if (error.isShown()) {
                        return;
                    }
                    isError = true;
                    mailText.requestFocus();
                    mailText.setFocusable(true);
                    mailText.setFocusableInTouchMode(true);
                    error.setText("邮箱重复，请更换邮箱！");
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("email",mail)
                .notifyRequest();
    }
    public abstract void finishActivity();

    public View getmRootView(){
        return mRootView;
    }
}
