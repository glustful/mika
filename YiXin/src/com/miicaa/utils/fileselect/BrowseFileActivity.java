package com.miicaa.utils.fileselect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.business.file.BusinessFileActivity;
import com.miicaa.home.ui.business.file.BusinessFileSearchActivity;
import com.miicaa.home.ui.picture.PictureShowAtivity;
import com.miicaa.utils.FileUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

@EActivity(R.layout.activity_browse_file)
public class BrowseFileActivity extends PictureShowAtivity{

	@Override
	public void finish() {
		if(isRefresh){
	    	BusinessFileActivity.getIntance().isRefresh();
	    	if(BusinessFileSearchActivity.getIntance() != null)
	    		BusinessFileSearchActivity.getIntance().isRefresh();
	    	}
		
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK)
			return;
		switch(requestCode){
		case BusinessFileActivity.MOVETOFILE_CODE:
			isRefresh = true;
			//BusinessFileActivity.getIntance().isRefresh();
			if(BusinessFileSearchActivity.getIntance()==null){
			BusinessFileActivity.getIntance().setTmpParentId(data.getStringExtra("id"),data.getStringExtra("name"),jsonObject);
			}
			finish();
			break;
		case BusinessFileActivity.EDITFILE_CODE:
			isRefresh = true;
			try {
				jsonObject =  new JSONObject(data.getStringExtra("json"));
				footView.setFileInfo(isAdmin, jsonObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	static String TAG = "BrowseFileActivity";
	boolean isRefresh = false;
	ProgressDialog dialog;
	
	@ViewById(R.id.headView)
	RelativeLayout headView;
	@ViewById(R.id.progressBar)
	ProgressBar progressBar;
	@ViewById(R.id.webView)
	WebView webView;
	@ViewById(R.id.footView)
	BrowseFileFootView footView;
	@ViewById
	TextView textView;
	
	@Extra
	String mId ;
	@Extra 
	String name;
	@Extra
	Boolean isAdmin;
	@Extra 
	String json;
	@Extra 
	String fileType;
	@Extra
	boolean isAttachment;
	
	String uncodeName;
	
	JSONObject jsonObject;
	
	String ext;
	
	String url = "http://docview.miicaa.com/op/view.aspx?"
			+ "src=https://www.miicaa.com/prodocument_srv/docView";
	
	Handler mHandler;
	
	@AfterInject
	void afterInject(){
		isAdmin = isAdmin != null ? isAdmin : false;
		dialog = new ProgressDialog(this);
		if(json != null){ 
			try {
				jsonObject = new JSONObject(json);
			} catch (JSONException e) {
				Log.d(TAG, "json is not a json value");
				e.printStackTrace();
			}
		}
			Log.d(TAG, "document names is "+"..."+name);
			try {
				uncodeName = URLEncoder.encode(name,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		
		url += "/"+mId+"/"+uncodeName;
		
		 ext = name.substring(name.lastIndexOf(".")+1,name.length());
		mHandler = new Handler();
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@AfterViews
	void afterView(){

		if(isAttachment)
			footView.setVisibility(View.GONE);
			
		String titleName = name.length()>10?name.substring(0, 10)+"...":name;
		((TextView)headView.findViewById(R.id.headTitle)).setText(titleName);
		headView.findViewById(R.id.commitButton).setVisibility(View.INVISIBLE);
		Button backButton = (Button)headView.findViewById(R.id.cancleButton);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if(jsonObject != null)
		footView.setFileInfo(isAdmin, jsonObject);
		
		progressBar.setMax(100);
		webView.setWebChromeClient(new WebBrowseClient());
		webView.setWebViewClient(new DefaultWebClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new Object(){
			public void clickOnAndroid() {       
				               mHandler.post(new Runnable() {       
				                    @Override
									public void run() {       
				                         webView.loadUrl("javascript:wave()");       
				                    }       
				                });       
				           }

		}, "test");
		 if("20".equals(fileType)){
			String vUrl = "";
			if(mId.contains("youku.com")){
			 vUrl = "http://v.youku.com/v_show/id_";
			String vId = mId.substring(mId.indexOf("sid/")+4,mId.lastIndexOf("/"));
			vUrl += vId + ".html";
			}else if(mId.contains("tudou.com")){
				vUrl = "http://www.tudou.com/programs/view/";
				String vId = mId.substring(mId.indexOf("/v/")+3,mId.length());
				vUrl += vId;
			}else if(mId.contains("ku6.com")){
				vUrl = "http://v.ku6.com/show/";
				String vId = mId.substring(mId.indexOf("refer/")+6,mId.lastIndexOf("/"));
				vUrl += vId + ".html";
			}else{
				Toast.makeText(this, "目前暂不支持此地址播放", Toast.LENGTH_SHORT).show();
				return;
			}
//			webView.loadUrl(vUrl);
			Uri uri = Uri.parse(vUrl);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}else if("ppt".equals(ext)||"doc".equals(ext)||
				"xls".equals(ext)||"pptx".equals(ext)||"docx".equals(ext)
				||"xlsx".equals(ext)){
			Log.d(TAG, "url at office docment:"+url);
		webView.loadUrl(url);
		}else{
				downLoadFile();
		}
		
	}

	class WebBrowseClient extends WebChromeClient{
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setProgress(newProgress);
			if(newProgress == 100){
				progressBar.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
		
	} 
	
	class DefaultWebClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			 view.loadUrl(url);
             return false;
//			return super.shouldOverrideUrlLoading(view, url);
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {       
            webView.goBack();       
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }  
	
	
	private void downLoadFile(){
		String url = "/home/proupload/pc/component/upload/download";
		final String path = FileUtils.geInstance().getDownLoadFileStringPath() + name;
		final File file = new File(path);
		loading();
		if(file.exists()){
//			txtShow(path);
			openOffice(file);
		}else{
		new RequestAdpater() {
			@Override
			public void onReponse(ResponseData data) {
				if(data.getResultState() == ResultState.eSuccess){
//					txtShow(path);
//					File f = new File(path);
					openOffice(file);
				}else{
					Toast.makeText(BrowseFileActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
			}
		}.setUrl(url)
		.setRequestMethod(RequestAdpater.RequestMethod.eGet)
        .setRequestType(RequestAdpater.RequestType.eFileDown)
        .addParam("id", mId)
        .setDownLoadDir()
        .setFileName(name)
        .notifyRequest();
		}
	}

	void openOffice(File file){
		loaded();
		Intent intent = new Intent("android.intent.action.VIEW");      
		intent.addCategory("android.intent.category.DEFAULT");      
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      
		Uri uri = Uri.fromFile(file);      
		//获取文件mmie类型
		String type = getMIMEType(file);
		intent.setDataAndType(uri, type);
		try{
		startActivity(intent);
		}catch(Exception e){
			Log.d(TAG, "Activity Not Found!");
			intent.setDataAndType(uri, "*/*");
			startActivity(intent);
		}
		finish();
		
	}
	
	
	@Background
	 void txtShow(String  path){
		loading();
		  try {
			String content  = getTxtFileChar(path);
			Log.d(TAG, "txt file content : ...."+content);
			showTxt(content);
//			webView.loadDataWithBaseURL("",content.toString(), "txt/html", "utf-8","");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@UiThread
	void loading(){
		dialog.show();
	}
	
	@UiThread
	void loaded(){
		dialog.dismiss();
	}
	
	
	@UiThread
	void update(int num){
		dialog.setProgress(num);
	}
	
	@UiThread
	void showTxt(String content){
		progressBar.setVisibility(View.GONE);
		webView.setVisibility(View.GONE);
		textView.setMovementMethod(new ScrollingMovementMethod());
		textView.setVisibility(View.VISIBLE);
		textView.setText(content);
		dialog.dismiss();
	}
	
	private String getTxtFileChar(String path) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer("");  
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}  
		// 必须设置成GBK，否则将出现乱码  
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,  
		"GBK"));  
		try {  
		String line = "";  
		while ((line = reader.readLine()) != null) {  
		sb.append(line + "\r");  
		}  
		} catch (FileNotFoundException e) {  
		e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return sb.toString().trim(); 
	}


	Boolean bUseFullscreen = true;
	@Override
	protected void hiddenViews() {
		/*隐藏标题栏*/
		if(!isAttachment && isAdmin){
		 if(bUseFullscreen) 
		   { 
		        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
		    } 
		    else 
		    { 
		        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
		        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		    } 
		 if(headView.isShown()){
			 headView.setVisibility(View.GONE);
		 }else{
			 headView.setVisibility(View.VISIBLE);
		 }
		 if(footView.isShown()){
			 footView.setVisibility(View.GONE);
		 }else{
			 footView.setVisibility(View.VISIBLE);
		 }
		 bUseFullscreen = !bUseFullscreen;
		}
	}


	@Override
	protected void okInOptions(DisplayImageOptions options) {
	}
	
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * @param file
	 */ 
	private String getMIMEType(File file) { 
	     
	    String type="*/*"; 
	    String fName = file.getName(); 
	    //获取后缀名前的分隔符"."在fName中的位置。 
	    int dotIndex = fName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    /* 获取文件的后缀名*/ 
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase(); 
	    if(end=="")return type; 
	    //在MIME和文件类型的匹配表中找到对应的MIME类型。 
	    for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??
	        if(end.equals(MIME_MapTable[i][0])) 
	            type = MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 
	
	
	
	private final String[][] MIME_MapTable={ 
            //{后缀名，MIME类型} 
            {".3gp",    "video/3gpp"}, 
            {".apk",    "application/vnd.android.package-archive"}, 
            {".asf",    "video/x-ms-asf"}, 
            {".avi",    "video/x-msvideo"}, 
            {".bin",    "application/octet-stream"}, 
            {".bmp",    "image/bmp"}, 
            {".c",  "text/plain"}, 
            {".class",  "application/octet-stream"}, 
            {".conf",   "text/plain"}, 
            {".cpp",    "text/plain"}, 
            {".doc",    "application/msword"}, 
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
            {".xls",    "application/vnd.ms-excel"},  
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
            {".exe",    "application/octet-stream"}, 
            {".gif",    "image/gif"}, 
            {".gtar",   "application/x-gtar"}, 
            {".gz", "application/x-gzip"}, 
            {".h",  "text/plain"}, 
            {".htm",    "text/html"}, 
            {".html",   "text/html"}, 
            {".jar",    "application/java-archive"}, 
            {".java",   "text/plain"}, 
            {".jpeg",   "image/jpeg"}, 
            {".jpg",    "image/jpeg"}, 
            {".js", "application/x-javascript"}, 
            {".log",    "text/plain"}, 
            {".m3u",    "audio/x-mpegurl"}, 
            {".m4a",    "audio/mp4a-latm"}, 
            {".m4b",    "audio/mp4a-latm"}, 
            {".m4p",    "audio/mp4a-latm"}, 
            {".m4u",    "video/vnd.mpegurl"}, 
            {".m4v",    "video/x-m4v"},  
            {".mov",    "video/quicktime"}, 
            {".mp2",    "audio/x-mpeg"}, 
            {".mp3",    "audio/x-mpeg"}, 
            {".mp4",    "video/mp4"}, 
            {".mpc",    "application/vnd.mpohun.certificate"},        
            {".mpe",    "video/mpeg"},   
            {".mpeg",   "video/mpeg"},   
            {".mpg",    "video/mpeg"},   
            {".mpg4",   "video/mp4"},    
            {".mpga",   "audio/mpeg"}, 
            {".msg",    "application/vnd.ms-outlook"}, 
            {".ogg",    "audio/ogg"}, 
            {".pdf",    "application/pdf"}, 
            {".png",    "image/png"}, 
            {".pps",    "application/vnd.ms-powerpoint"}, 
            {".ppt",    "application/vnd.ms-powerpoint"}, 
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, 
            {".prop",   "text/plain"}, 
            {".rc", "text/plain"}, 
            {".rmvb",   "audio/x-pn-realaudio"}, 
            {".rtf",    "application/rtf"}, 
            {".sh", "text/plain"}, 
            {".tar",    "application/x-tar"},    
            {".tgz",    "application/x-compressed"},  
            {".txt",    "text/plain"}, 
            {".wav",    "audio/x-wav"}, 
            {".wma",    "audio/x-ms-wma"}, 
            {".wmv",    "audio/x-ms-wmv"}, 
            {".wps",    "application/vnd.ms-works"}, 
            {".xml",    "text/plain"}, 
            {".z",  "application/x-compress"}, 
            {".zip",    "application/x-zip-compressed"}, 
            {"",        "*/*"}   
        }; 
 
	
}
