package com.miicaa.common.base;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.pay.PayUtils;

/**
 * Created by LM on 14-9-4.
 */
public  class Utils {
    /*
    广播接收器注册
     */
    public final static String UPDATE_ACTION = "updateAction";

    public final static int APPROVAL_USER_NEXT = 0X10;
    public final static int APPROVAL_USER_CHANGE = 0x11;
    public final static int CCAL_USER_ADD = 0x12;
    public final static int RECEIVER_CHANGE = 0X13;

    public static  void requestUsual(HashMap<String,String> paramMap,final String url,final CallBackListener
                                     callBackListener){
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                callBackListener.callBack(data);
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam(paramMap)
                .notifyRequest();
    }
    public interface CallBackListener{
        public void callBack(ResponseData data);
        void callbackNull();
        public void callBackJson(JSONArray jsonArray);
    }
    public static void hiddenSoftBorad(Context context) {
        try {
           ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    public static void approvalUseData(final Context context,String id,final CallBackListener listener){
        String url = "/home/phone/thing/getcurrentapprover";
        new RequestAdpater(){

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess){
                    JSONArray jsonArray = data.getJsonArray();
                    if (jsonArray == null || jsonArray.length() == 0){
                        listener.callbackNull();
                    }else {
                       listener.callBackJson(jsonArray);
                    }
                }else{
                    Log.i("what_msg_about_approval",data.getMsg());
                    Toast.makeText(context,"网络错误"+data.getMsg(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("id",id)
                .notifyRequest();
    }
    
    public static void setKindsImage(TextView mHeadImg, String ext) {
		String optString = ext.toLowerCase();
		if (optString.equals("txt")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_txt, 0, 0, 0);
		} else if (optString.equals("pdf")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_pdf, 0, 0, 0);
		} else if (optString.equals("doc")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("docx")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("zip")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("rar")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("ppt") || optString.equals("pptx")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_ppt, 0, 0, 0);
		} else if (optString.equals("xlsx") || optString.equals("xls")) {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_execl, 0, 0, 0);
		} else {

			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.accessory_file_ico_normal, 0, 0, 0);
		}

	}
    
    /*
     * 过滤QQ号
     */
    public static String stringFilter(String str){      
        // 只允数字        
        String   regEx  =  "[^0-9]";                      
        Pattern   p   =   Pattern.compile(regEx);      
        Matcher   m   =   p.matcher(str);      
        return   m.replaceAll("").trim();      
    }   
    
    public static void QQFilter(final EditText et){
    	et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    	et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				 String editable = et.getText().toString();   
			        String str = stringFilter(editable.toString()); 
			        if(!editable.equals(str)){ 
			        	
			        	et.setText(str); 
			        	
			        	et.setSelection(str.length()); 
			        } 
			        if(str.length()>11){
		        		PayUtils.showToast(((View)et).getContext(), "长度不能超过11", 3000);
		        		str = str.substring(0, 11);
		        	
		        		et.setText(str); 
		        	
		        		et.setSelection(str.length()); 
			        }
			}
		});
    }
    
    public static void countLimit(final TextView tv,final int count){
    	tv.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				 String str = s.toString();   
			       
			        if(str.length()>count){
		        		PayUtils.showToast(((View)tv).getContext(), "长度不能超过"+count, 3000);
		        		str = str.substring(0, count);
		        	
		        		tv.setText(str); 
		        		if(tv instanceof EditText)
		        		((EditText)tv).setSelection(str.length()); 
			        }
				
			}
		});
    }
}
