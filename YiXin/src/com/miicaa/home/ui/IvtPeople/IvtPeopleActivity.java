package com.miicaa.home.ui.IvtPeople;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;

/**
 * Created by LM on 14-7-31.
 */
public class IvtPeopleActivity extends Activity {

    LinearLayout mLinearLayout;
    //    View mRootView;
    int ivtCount;
    Button commitButton;
    String nameString;
    String mailString;
    LinearLayout addMoreLayout;
    Button backButton;
    ProgressDialog dialog;
    ArrayList<EditText> nameTexts;
    ArrayList<EditText> mailTexts;
    ArrayList<TextView> nameErrors;
    ArrayList<TextView> mailErrors;
    ArrayList<TextView> nameRepErrors = new ArrayList<TextView>();
    ArrayList<TextView> mailRepErrors = new ArrayList<TextView>();
    HashMap<Integer,Boolean> nameHash = new HashMap<Integer, Boolean>();
    HashMap<Integer,Boolean> mailHash = new HashMap<Integer, Boolean>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> mails = new ArrayList<String>();
    final static int NAME_SELF = 0X0;
    final static int MAIL_SELF = 0X1;
    HashMap<Integer, Boolean> notNameSelf = new HashMap<Integer, Boolean>();
    HashMap<Integer, Boolean> notMailSelf = new HashMap<Integer, Boolean>();
    HashMap<Integer,Boolean> nameMap = new HashMap<Integer, Boolean>();
    HashMap<Integer,Boolean>mailMap = new HashMap<Integer, Boolean>();
    Pattern pattern ;
    int nameNum;
    int maileNum;
    Boolean isError = true;

    InputKeyMode keyMode = InputKeyMode.iFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                    .build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ivt_people_activity);
        pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//邮箱匹配正则表达式
        mLinearLayout = (LinearLayout) findViewById(R.id.ivt_people_list);
//        mRootView = LayoutInflater.from(IvtPeopleActivity.this).inflate(R.layout.ivt_people_layout,null);
        commitButton = (Button) findViewById(R.id.ivt_people_save_button);
        addMoreLayout = (LinearLayout)findViewById(R.id.ivt_people_more_layout);

        backButton = (Button)findViewById(R.id.ivt_people_contactList_button);
        nameTexts = new ArrayList<EditText>();
        nameErrors = new ArrayList<TextView>();
        mailTexts  = new ArrayList<EditText>();
        mailErrors = new ArrayList<TextView>();
        dialog = new ProgressDialog(IvtPeopleActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("miicaa");
        dialog.setMessage("正在提交");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameString = "";
        mailString = "";
        ivtCount = 0;
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
                verContent();
            }
        });

        initList();
    }

    private void initList() {
        for (int i = 0; i < 3; i++) {
            int count = i;
            View mRootView = getmView(count);
            mLinearLayout.addView(mRootView);
            mLinearLayout.setFocusable(true);
            mLinearLayout.setFocusableInTouchMode(true);

        }

    }


    private View getmView(final int count) {
        View mRootView = LayoutInflater.from(IvtPeopleActivity.this).inflate(R.layout.ivt_people_layout, null);
        final EditText nameText = (EditText) mRootView.findViewById(R.id.ivt_people_layout_name);
        final TextView nameError = (TextView) mRootView.findViewById(R.id.ivt_people_layout_nameError);
        final EditText mailText = (EditText) mRootView.findViewById(R.id.ivt_people_layout_mail);
        final TextView mailError = (TextView) mRootView.findViewById(R.id.ivt_people_layout_mailError);
        nameTexts.add(nameText);
        mailTexts.add(mailText);
        nameErrors.add(nameError);
        mailErrors.add(mailError);
        notNameSelf.put(count,true);
        notMailSelf.put(count,true);

        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                String name = nameText.getText().toString().trim();
                String mail = mailText.getText().toString().trim();
                    if ("".equalsIgnoreCase(name)&&"".equalsIgnoreCase(mail)){
                        nameError.setVisibility(View.GONE);
                        mailError.setVisibility(View.INVISIBLE);
                        return;
                    }else if ("".equals(name)) {
                        nameError.setText("请输入姓名");
                        nameError.setVisibility(View.VISIBLE);
                        return;
                    }else if ("".equalsIgnoreCase(mail)){
                        if (!mailText.hasFocus()) {
                            mailError.setText("请输入邮箱");
                            mailError.setVisibility(View.VISIBLE);
                        }
                    }
                    checkNameRep(nameText.getText().toString().trim(),
                            nameError,
                            mailText,
                            nameText,
                            count);
                }
        });
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                if ("".equalsIgnoreCase(nameText.getText().toString().trim())){
//                    return;
//                }
//                if (keyMode == InputKeyMode.iWait){
//                    nameText.setText("");
//                    isError = false;
//
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                nameError.setVisibility(View.GONE);
                notNameSelf.put(count,true);
//                resetInitEditTexts();
            }
        });
        mailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                    String name = nameText.getText().toString().trim();
                    String mail = mailText.getText().toString().trim();
                    if (!mailText.hasFocus()){

                    if ("".equalsIgnoreCase(name)&&"".equalsIgnoreCase(mail)){
                        nameError.setVisibility(View.GONE);
                        mailError.setVisibility(View.INVISIBLE);
                        return;
                    }else if ("".equalsIgnoreCase(mail)){
                        mailError.setText("请输入邮箱");
                        mailError.setVisibility(View.VISIBLE);
                        return;
                    }else if ("".equalsIgnoreCase(name)){
                        if (!nameText.hasFocus()) {
                            nameError.setText("请输入姓名");
                            nameError.setVisibility(View.VISIBLE);
                        }
                    }
                        if (!pattern.matcher(mail).matches()){
                            mailError.setText("请正确输入邮箱格式");
                            mailError.setVisibility(View.VISIBLE);
                            return;
                        }
                    checkMailRep(nameText.getText().toString().trim(),
                            mailText.getText().toString().trim(),
                            mailError,
                            mailText,
                            count
                    );
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
                notMailSelf.put(count,true);
//                resetInitEditTexts();


            }
        });

        return mRootView;
    }

    private  void checkNameRep(String name,final TextView error,final EditText mailText,final EditText nameText,final int position){
        String url = "/sysconfig/pc/invite/canAccess/isNameExist";
        keyMode = InputKeyMode.iWait;
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    JSONObject jsonObject = data.getMRootData();
                    if (jsonObject.optBoolean("data")){
                        error.setText("名字重复！");
                        error.setVisibility(View.VISIBLE);
                    }
//                   keyMode = InputKeyMode.iBegin;
                }else{
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("name",name)
                .notifyRequest();


    }

    private void checkMailRep(final String name ,final String mail,final TextView error,final EditText mailText,final  int position){
        String url = "/sysconfig/pc/invite/canAccess/isEmailExist";
        keyMode = InputKeyMode.iWait;
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    JSONObject jsonObject = data.getMRootData();
                    if (jsonObject.optBoolean("data")){
                        error.setText("邮箱重复，请更换邮箱！");
                        error.setVisibility(View.VISIBLE);
                    }

                }else{


                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("email",mail)
                .notifyRequest();
    }

    private void verContent(){
    	dialog.show();
    	commitButton.setClickable(false);
        isError = true;
        nameNum = 0;
        maileNum = 0;
        names.clear();
        mails.clear();
        nameRepErrors.clear();
        nameHash.clear();
        mailHash.clear();
        mailRepErrors.clear();
        int count = 0;
        for (int i = 0 ; i < nameTexts.size(); i++){
            String name = nameTexts.get(i).getText().toString();
            String mail = mailTexts.get(i).getText().toString();
            nameErrors.get(i).setVisibility(View.GONE);
            mailErrors.get(i).setVisibility(View.INVISIBLE);
            String errorText ="";
            if ("".equalsIgnoreCase(name.trim())&&!"".equalsIgnoreCase(mail.trim())){
                errorText = "请输入姓名";
                nameErrors.get(i).setText(errorText);
                nameErrors.get(i).setVisibility(View.VISIBLE);
                restString();
                if (!pattern.matcher(mail).matches()){
                    mailErrors.get(i).setText("请正确输入邮箱格式");
                    mailErrors.get(i).setVisibility(View.VISIBLE);
                    continue;
                }
                mails.add(mail);
                mailRepErrors.add(mailErrors.get(i));

            }else if (!"".equalsIgnoreCase(name.trim())&&"".equalsIgnoreCase(mail.trim())){
                errorText = "请输入邮箱";
                mailErrors.get(i).setText(errorText);
                mailErrors.get(i).setVisibility(View.VISIBLE);
                restString();
                names.add(name);
                nameRepErrors.add(nameErrors.get(i));
            }else if ("".equalsIgnoreCase(name.trim())&&"".equalsIgnoreCase(mail.trim())){


                if (count == nameTexts.size()-1){
                    nameErrors.get(0).setText("请输入姓名");
                    nameErrors.get(0).setVisibility(View.VISIBLE);
                }
                count ++;

            }
            else{
               if (!pattern.matcher(mail.trim()).matches()) {
                   mailErrors.get(i).setText("请正确输入邮箱格式");
                   mailErrors.get(i).setVisibility(View.VISIBLE);
                   names.add(name);
                   nameRepErrors.add(nameErrors.get(i));
                   continue;
               }names.add(name);
                mails.add(mail);
                nameRepErrors.add(nameErrors.get(i));
                mailRepErrors.add(mailErrors.get(i));
            }

        }
        localVerRep();

    }

    private void localVerRep(){
        for (int i = 0;i < names.size(); i++){
            String name = names.get(i);
            for (int n = i+1; n <names.size();n++){
                if (name.equalsIgnoreCase(names.get(n))){
                    nameRepErrors.get(i).setText("名字重复");
                    nameRepErrors.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
        for (int i = 0;i < mails.size(); i++){
            String mail = mails.get(i);
            for (int n = i+1; n < mails.size();n++){
                if (mail.equalsIgnoreCase(mails.get(n))){
                    mailRepErrors.get(i).setText("邮箱重复");
                    mailRepErrors.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
        webVerRep();
    }

    private void webVerRep(){
    	
        final String urlName = "/sysconfig/pc/invite/canAccess/isNameExist";
        final String urlMail = "/sysconfig/pc/invite/canAccess/isEmailExist";
        if (names.size() > 0){
            new RequestAdpater(){

                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                        JSONObject jsonObject = data.getMRootData();
                        if (jsonObject.optBoolean("data")){
                            nameRepErrors.get(0).setText("名字重复");
                            nameRepErrors.get(0).setVisibility(View.VISIBLE);
                        }else{
                            nameNum++;
                        }
                        for (int i = 1;i < names.size(); i++){

                            final int n = i;
                            new RequestAdpater(){
                                @Override
                                public void onReponse(ResponseData data) {
                                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                                        JSONObject jsonObject = data.getMRootData();
                                        if (jsonObject.optBoolean("data")){
                                            nameRepErrors.get(n).setText("名字重复");
                                            nameRepErrors.get(n).setVisibility(View.VISIBLE);
                                        }

                                    }else{
                                        nameNum++;
                                    }
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {

                                }
                            }.setUrl(urlName)
                                    .setRequestMode(RequestMode.eSync)
                                    .addParam("name",names.get(i))
                                    .notifyRequest();
                        }
                        for (int s = 0 ; s < mails.size();s++){
                            final int n = s;

                            new RequestAdpater(){
                                @Override
                                public void onReponse(ResponseData data) {
                                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                                        JSONObject jsonObject = data.getMRootData();
                                        if (jsonObject.optBoolean("data")){
                                            if (n == mails.size()-1) {
                                                isError = true;
                                            }

                                            mailRepErrors.get(n).setText("邮件重复！");
                                            mailRepErrors.get(n).setVisibility(View.VISIBLE);
                                        }else
                                        {
                                            maileNum ++;
                                            if (n == mails.size()-1) {
                                                isError = false;
                                            }
                                        }

                                    }
                                    else {
                                        Toast toast = Toast.makeText(IvtPeopleActivity.this,"cuowu",1);
                                        toast.show();

                                    }
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {

                                }
                            }.setUrl(urlMail)
                                    .setRequestMode(RequestMode.eSync)
                                    .addParam("email",mails.get(s))
                                    .notifyRequest();
                        }
//                        if (!isError)
//                        {
                        for (int s = 0; s <nameTexts.size(); s++){
                            String name = nameTexts.get(s).getText().toString().trim();
                            String mail = mailTexts.get(s).getText().toString().trim();
                            if(isReset(name, mail)){
                            	reset();
                            	return;
                            	
                            }
                        }if (names.size() != mails.size()){
                        	reset();
                            return;
                        }

                        commitContent();

                    }else {

                    }

                }

                @Override
                public void onProgress(ProgressMessage msg) {

                }
            }.setUrl(urlName)
                    .addParam("name",names.get(0))
                    .notifyRequest();
        }else if (mails.size() > 0){
            new RequestAdpater(){

                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                        JSONObject jsonObject = data.getMRootData();
                        if (jsonObject.optBoolean("data")){

                            mailRepErrors.get(0).setText("邮件重复！");
                            mailRepErrors.get(0).setVisibility(View.VISIBLE);
                        }else{
                            maileNum++;
                        }
                        for (int i = 0;i < names.size(); i++){

                            final int n = i;
                            new RequestAdpater(){

                                @Override
                                public void onReponse(ResponseData data) {
                                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                                        JSONObject jsonObject = data.getMRootData();
                                        if (jsonObject.optBoolean("data")){
                                            nameRepErrors.get(n).setText("名字重复");
                                            nameRepErrors.get(n).setVisibility(View.VISIBLE);
                                        }
                                    }else{
                                       nameNum++;

                                    }
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {

                                }
                            }.setUrl(urlName)
                                    .setRequestMode(RequestMode.eSync)
                                    .addParam("name",names.get(i))
                                    .notifyRequest();
                        }
                        for (int i = 1 ; i < mails.size();i++){
                            final int n = i;

                            new RequestAdpater(){
                                @Override
                                public void onReponse(ResponseData data) {
                                    if (data.getResultState() == ResponseData.ResultState.eSuccess){
                                        JSONObject jsonObject = data.getMRootData();
                                        if (jsonObject.optBoolean("data")){
                                            if (n == mails.size()-1) {
                                                isError = true;
                                            }
                                            mailRepErrors.get(n).setText("邮件重复！");
                                            mailRepErrors.get(n).setVisibility(View.VISIBLE);
                                        }else{
                                            maileNum++;
                                            if (n == mails.size() -1) {
                                                isError = false;
                                            }
                                        }
                                    }else {

                                    }
                                }

                                @Override
                                public void onProgress(ProgressMessage msg) {

                                }
                            }.setUrl(urlMail)
                                    .setRequestMode(RequestMode.eSync)
                                    .addParam("email",mails.get(i))
                                    .notifyRequest();

                        }
//                        if(!isError) {
                        for (int s = 0; s <nameTexts.size(); s++){
                            String name = nameTexts.get(s).getText().toString().trim();
                            String mail = mailTexts.get(s).getText().toString().trim();
                           if(isReset(name, mail)){
                        	   reset();
                        	   return;
                           }

                        }
                        if (names.size() != mails.size()){
                        	reset();
                            return;
                        }

                            commitContent();

//                        }

                    }else {

                    }

                }

                @Override
                public void onProgress(ProgressMessage msg) {

                }
            }.setUrl(urlMail)
                    .addParam("email",mails.get(0))
                    .notifyRequest();
        }else{
        	reset();
        }
    }
    
    
    void reset(){
    	dialog.dismiss();
    	commitButton.setClickable(true);
    }

    Boolean isReset(String name , String mail){
    	return (!"".equals(name.trim())&&"".equals(mail.trim()))||("".equals(name.trim())&&!"".equals(mail.trim()));
    }
    
    private void commitContent(){
        ivtCount = 0;
        String url = "/sysconfig/pc/invite/sendInviteMail";
        String nameStr = "";
        String mailStr = "";
        for (int i = 0 ; i < names.size(); i++){
            if (i > 0){
                nameStr = nameStr +","+ names.get(i);
            }else{
                nameStr = names.get(i);
            }
            ivtCount ++;
        }
        for (int i = 0; i < mails.size();i++){
            if (i > 0){
                mailStr = mailStr + "," + mails.get(i);
            }else {
                mailStr = mails.get(i);
            }
        }
        final String name = nameStr;
        final String mail = mailStr;
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    Toast toast = Toast.makeText(IvtPeopleActivity.this,"您成功邀请了" + ivtCount + "个人",
                            Toast.LENGTH_SHORT);

//                            toast.setView(LayoutInflater.from(IvtPeopleActivity.this).
//                                    inflate(R.layout.toast_view_layout, null));
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                    commitButton.setClickable(true);
                    Toast toast = Toast.makeText(IvtPeopleActivity.this,"提交失败:"+data.getMsg(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("usernames", name)
                .addParam("emails", mail)
                .notifyRequest();
    }

    private void restString(){
        nameString = "";
        mailString = "";
    }

    public enum InputKeyMode{
        iFirst,
        iBegin,
        iWait;
    }


}
