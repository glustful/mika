package com.miicaa.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.org.StyleDialog;

public class AllUtils {
	
//	public final static String dataType = "dataType";
//	public final static String dataId = "dataId";
//	public final static String status = "status";
//	public final static String operateGroup = "operateGroup";
	
	public static  AlertDialog.Builder mVersionBuilder = null;
	public final static int PAYFOR_USER = 0x000001;
	public final static int NORMAL_User = 0x000002;
	public final static int MB = 1024*1024;
	public final static String version_reciver = "com.miicaa.version";
	public static long Service_File_Length = 30;
	
	public final static String arrangeType = "1";
	public final static String approvalType = "2";
	public final static String reporteType = "9";
	public final static String discuss = "discuss";
	public final static String trends ="trends";
	public final static String AGREE = "2";
	public final static String DISAGREE = "1";
	public final static String PASS = "5";
	public final static String MISS = "6";

	@SuppressLint("SimpleDateFormat")
	public static String getnormalTime(Long time){
		if(time == null || time == 0){
			return "";
		}
		Date date = new Date(time);
		SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		nformat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return time != null ? nformat.format(date):null;
	}
	
	
	public static String getYearTime(Long time){
		if(time == null || time == 0){
			return "";
			}
		Date date = new Date(time);
		SimpleDateFormat nformat = new SimpleDateFormat("yyyy-MM-dd");
		return time != null ? nformat.format(date):null;
	}
	
	public static String getTime(Long time){
		if(time == null || time == 0){
			return "";
			}
		Date date = new Date(time);
		SimpleDateFormat nformat = new SimpleDateFormat("HH:mm");
		return time != null ? nformat.format(date):null;
	}
	public static String listToString(ArrayList<String> strlist){
		
		if(strlist != null && strlist.size() > 0){
			String str = "";
			for (String s : strlist){
				str += s+",";
			}
			return str.substring(0, str.length()-1);
		}
		return null;
		
	}
	
	 public static View getTabIndicator(Context context,String str){
			View v = LayoutInflater.from(context).inflate(R.layout.matter_deatail_tab_view, null);
			final TextView tv = (TextView)v.findViewById(R.id.textv);
			return v;
	 }
	 
	 public static void hiddenSoftBorad(Context context) {
	        try {
	            ((InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE))
	                    .hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
	                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	        } catch (Exception e) {

	        }
	    }
	 
	 public static void netIsNotGeiLi(Context context){
		 Toast.makeText(context, "网络不给力！", Toast.LENGTH_SHORT).show();
	 }
	 
	 @SuppressWarnings("serial")
	public static void refreshClient(final String clientId) {
	        String oldClientId = AccountInfo.instance().getClientId();
	        if (oldClientId == null || oldClientId.length() == 0) {
	            oldClientId = clientId;
	        }
	        if(AccountInfo.instance().getLastUserInfo() == null || AccountInfo.instance().getLastOrgInfo() == null){
	        	return;
	        }
	        String deviceType = Build.MANUFACTURER;//
	        String deviceVersion = Build.MODEL;
	        String osVersion = Build.VERSION.RELEASE;
	        String sysType = "ANDROID";

	        new RequestAdpater() {
	            @Override
	            public void onReponse(ResponseData data) {
	                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
	                    AccountInfo.instance().setClientId(clientId);
	                }
	            }

	            @Override
	            public void onProgress(ProgressMessage msg) {
	            }
	        }.setUrl("/mobile/mobile/devicereg")
	                .addParam("clientCode", oldClientId)
	                .addParam("userCode", AccountInfo.instance().getLastUserInfo().getCode())
	                .addParam("clientId", clientId)
	                .addParam("deviceType", deviceType)
	                .addParam("deviceVersion", deviceVersion)
	                .addParam("orgCode", AccountInfo.instance().getLastOrgInfo().getCode())
	                .addParam("osVersion", osVersion)
	                .addParam("sysType", sysType)
	                .notifyRequest();

	    }
	 
	 /*
	  * 获取版本号
	  */
	 public static  String getVersionName(Context context) throws Exception
	   {
	           // 获取packagemanager的实例
	           PackageManager packageManager = context.getPackageManager();
	           // getPackageName()是你当前类的包名，0代表是获取版本信息
	           PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
	           String version = packInfo.versionName;
	           return version;
	   }
	 
	 
	 @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static View getLabelView(Context context,String label){
		 ArrayList<Integer> roundD = new ArrayList<Integer>();
		 roundD.add(R.drawable.label_1);
		 roundD.add(R.drawable.label_2);
		 roundD.add(R.drawable.label_3);
		 int rN = (int) Math.round(Math.random()*2);
		 int dId = roundD.get(rN);
		 LinearLayout linearLayout = new LinearLayout(context);
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//	                        params.setMargins(0,8,0,0);
	        linearLayout.setLayoutParams(params);
//	        linearLayout.setMinimumHeight(35);
	        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
	        TextView textView = new TextView(context);
	        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
	                LayoutParams.WRAP_CONTENT);
	        tParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        tParams.setMargins(0, 0, 5, 0);
	        textView.setLayoutParams(tParams);
	        String str = label;
	        textView.setText(str);
	        textView.setTextSize(10);
	        textView.setTextColor(context.getResources().getColor(R.color.alittle_white));
	        TextPaint paint = textView.getPaint();
	        int w = (int)paint.measureText(str);
	        w += context.getResources().getDimension(R.dimen.labelW);
	        RelativeLayout sRelativeLayout = new RelativeLayout(context);
	        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(w,
	     		   LayoutParams.WRAP_CONTENT);
	        sRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(dId));
	        sRelativeLayout.setLayoutParams(rParams);
	        sRelativeLayout.addView(textView);
	        
	        RelativeLayout relativeLayout = new RelativeLayout(context);
	        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(5, ViewGroup.LayoutParams.WRAP_CONTENT));
	        linearLayout.addView(sRelativeLayout);
	        linearLayout.addView(relativeLayout);
	        return linearLayout;
		 }
	
	 
	 public static ProgressDialog getMaualStyleDialog(Context context){
		 StyleDialog dialog = new StyleDialog(context);
	        dialog.setCanceledOnTouchOutside(false);
	        return dialog;
	 }
	 
	 public static ProgressDialog getNormalMiicaaDialog(Context context){
		 ProgressDialog dialog = new ProgressDialog(context);
		 dialog.setTitle("miicaa");
		 dialog.setCanceledOnTouchOutside(true);
		 return dialog;
	 }
	 
	 
	 public static void panduanVersionCode(Context context ,
			 final OnAllUtilsListener listener_){
		 String url = "/home/phone/login/validate";
		 String versionCode = null;
		 try {
			versionCode = getVersionName(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 new RequestAdpater() {
			private static final long serialVersionUID = -6926512029835043349L;

			@Override
			public void onReponse(ResponseData data) {
				if(listener_ != null){
					listener_.onResponse(data);
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		 .addParam("type","android")
		.addParam("version", versionCode)
		.notifyRequest();
	 }
	 
	 public interface OnAllUtilsListener{
		 void onResponse(ResponseData data);
		 void onFailed(String errCode,String errMsg);
	 }
	 
	 public static void cancelAllNotification(Context context){
		 NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		 nm.cancelAll();
	 }
	 
	 public static void showVersionDialog(final Context context){
//		 if(mVersionBuilder == null){
			 mVersionBuilder = new android.app.
						AlertDialog.Builder(context);
				
			 mVersionBuilder.setCancelable(false);
			 mVersionBuilder.setTitle("提示")
					.setMessage("您的版本低于当前可运行版本，请移步至侎佧官网，app商城进行下载")
					.setPositiveButton("确定", new AlertDialog.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent();
							i.setAction("android.intent.action.VIEW");    
					        Uri uri = Uri.parse("http://m.vcooline.com/55850/channel/333813#mp.weixin.qq.com");
					        i.setData(uri);  
					        context.startActivity(i);
					        ((Activity)context).finish();
						}
					});
//				}
		 if(isActivityTaskRunning(context));
		 mVersionBuilder.show();
	 }
	 
	 /**
	  * 判断activity是否有task运行
	  * @param context
	  * @return
	  */
	 public static boolean isActivityTaskRunning(Context context){
		 ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		 if(manager == null){
			 try {
				throw new Exception("is your Context is a Activity context?");
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 List<RunningTaskInfo> infoList = manager.getRunningTasks(1);
		 if(infoList.size() > 0){
			 return true;
		 }
		 return false;
	 }
}

