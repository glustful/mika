package com.miicaa.home.ui.checkwork;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.miicaa.base.request.HttpFileUpload;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.base.request.response.BaseResopnseData.OnFileResponseListener;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PictureLayout;
import com.miicaa.home.MainActionBarActivity;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.SocketUploadFileTask;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.home.TestActivity_;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.fileselect.MyFileItem;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;

@EActivity(R.layout.activity_checkwork_sign)
public class CheckWorkSignActivity extends MainActionBarActivity implements
		AMapLocationListener, Runnable {
	
	Toast mToast;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Bimp.drr.clear();
		deleteFiles();
	}

	private void deleteFiles() {
		try{
		File dir = new File(photoDir);
		if(dir.exists()&&dir.isDirectory()){
			File[] fs = dir.listFiles();
			if(fs==null||fs.length<1)
				return;
			for(int i=0;i<fs.length;i++){
				fs[i].delete();
			}
		}
		}catch(Exception e){
			
		}
		
	}

	protected static final int TAKE_PICTURE = 0;

	static String TAG = "CheckWorkSignActivity";

	static int zishu = 140;

	public static String SIGNIN = "SignIn";
	public static String SIGNOUT = "SignOut";
	String photoPath;
	String photoDir = Environment.getExternalStorageDirectory() + "/miicaa/checkwork/photoCache/";
	LocationManagerProxy aMapLocManager;
	AMapLocation aMapLocation;
	HttpFileUpload mHttpFileUpload;
	
	String locationSite;
	String latNLongude;
	String remarks;
	String titleMsg;
	Double longutide;
	Double latutide;
	ProgressDialog progressDialog;

	

	@Extra
	String signState;
	@Extra
	Long nowTime;
	@Extra
	String xiabanTimeStr;

	Long xiabanTime;

	@ViewById(R.id.editText)
	EditText editText;
	@ViewById(R.id.progressLayout)
	LinearLayout progressLayout;
	@ViewById(R.id.progressBar)
	ProgressBar progressBar;
	@ViewById(R.id.progressText)
	TextView progressText;
	@ViewById(R.id.locationBtn)
	Button locationButton;
	@ViewById(R.id.zishuTextView)
	TextView zishuTextView;
	@ViewById(R.id.photo)
	PictureLayout photo;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			
			
			Bitmap bitmap = (Bitmap) msg.obj;
			
			showSmallPhoto(bitmap, msg.getData().getString("path"));
			
			switch (msg.what) {
			case 1:

				break;
			case 2:
				PicturButton button = new PicturButton(CheckWorkSignActivity.this,R.drawable.photo_icon,true);
				photo.addPictrue(button, null, null, null);
				break;

			default:
				break;

			}
		}
		
	};
	@OnActivityResult(TAKE_PICTURE)
	void takePicture(int resultCode,Intent data) {
		if(resultCode != RESULT_OK){
			return;
		}
			Bimp.drr.add(photoPath);
			photo.removeViewAt(photo.getChildCount()-1);
			initBmpCheck();
		
	}

	@AfterInject
	void afterInject() {
		
		
		progressDialog = AllUtils
				.getNormalMiicaaDialog(CheckWorkSignActivity.this);
		titleMsg = "";
		if (signState.equals(SIGNIN)) {
			titleMsg = "签到";
		} else if (signState.equals(SIGNOUT)) {
			titleMsg = "签退";
		}
		progressDialog.setMessage("正在" + titleMsg + ",请稍后");
		
	}

	private void initPhotoWidget() {
		
		photo.setOnPictureListener(onPictureListener);
		
		photo.removeAllViews();
		photo.addPictrue(new PicturButton(this, R.drawable.photo_icon, true), null, null, null);
	}

	@SuppressWarnings("deprecation")
	@AfterViews
	void afterView() {
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(
				zishu);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		editText.setFilters(new InputFilter[] { lengthFilter });
		if (xiabanTimeStr != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date date = null;
			try {
				date = sdf.parse(xiabanTimeStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			xiabanTime = date.getTime();
		}
		setRightButtonClickable(false);
		aMapLocManager = LocationManagerProxy.getInstance(this);
		/*
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		aMapLocManager.setGpsEnable(true);
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位
		initPhotoWidget();
	}

	@TextChange(R.id.editText)
	void onTextChangesOnEditText(CharSequence text, TextView hello, int before,
			int start, int count) {
		// Something Here
		String content = String.valueOf(editText.getText().toString().length())
				+ "/140";
		zishuTextView.setText(content);
	}

	@Override
	public String showBackButtonStr() {
		return "我的考勤";
	}

	@Override
	public Boolean showBackButton() {
		return true;
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public Boolean showTitleButton() {
		return true;
	}

	@Override
	public String showTitleButtonStr() {
		String title = null;
		if (SIGNIN.equals(signState)) {
			title = "今日签到";
		} else if (SIGNOUT.equals(signState)) {
			title = "今日签退";
		}
		return title;
	}

	@Override
	public void titleButtonClick(View v) {

	}

	@Override
	public Boolean showRightButton() {
		return true;
	}

	@Override
	public String showRightButtonStr() {
		if (signState.equals(SIGNIN)) {
			return "签到";
		} else if (signState.equals(SIGNOUT)) {
			return "签退";
		}
		return null;
	}

	@Override
	public void rightButtonClick(View v) {

		panduanLeaved();
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			progressBar.setVisibility(View.GONE);
			progressText.setText("定位失败，请尝试开启gps或网络重新定位。");
			stopLocation();// 销毁掉定位
		}
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		aMapLocation = location;
		progressLayout.setVisibility(View.GONE);
		latutide = location.getLatitude();// 纬度
		longutide = location.getLongitude();// 经度
		String latitudeStr = String.valueOf(latutide);
		String longitudeStr = String.valueOf(longutide);
		latNLongude = longitudeStr + "," + latitudeStr;
		locationSite = location.getAddress();
		
		locationButton.setText(locationSite);
		locationButton.setVisibility(View.VISIBLE);
		setRightButtonClickable(true);
	}

	/**
	 * 销毁定位
	 */
	@SuppressWarnings("deprecation")
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	private void panduanLeaved() {

		if (nowTime == null || xiabanTime == null || signState.equals(SIGNIN)) {
//			progressDialog.show();
			requestPhoto();
//			requestSign();
			return;
		}
		String nowTimeStr = AllUtils.getTime(nowTime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = sdf.parse(nowTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		nowTime = date.getTime();

		if (nowTime < xiabanTime) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					CheckWorkSignActivity.this);
			builder.setTitle("提示");
			builder.setMessage("您现在签退算是早退,您确定要签退吗?");
			builder.setNegativeButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					requestPhoto();
//					progressDialog.show();
//					requestSign();
				}
			}).setPositiveButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		} else {
			progressDialog.show();
			requestPhoto();
//			requestSign();
		}
	}

	@Click(R.id.locationBtn)
	void locationBtnClick() {
		TestActivity_.intent(this).latitude(latutide).longitude(longutide)
				.locationStr(locationSite).start();
	}
	
	private void requestPhoto(){
		ArrayList<UploadFileItem> itemList = new ArrayList<UploadFileItem>();
		 for(String path : Bimp.drr){
			 UploadFileItem item = new UploadFileItem(path);
			 itemList.add(item);
		 }
		 Log.d(TAG, "requestPhoto path length:"+itemList.size());
		 mHttpFileUpload = new HttpFileUpload()
		 .setParam(itemList, "/attendance/phone/attend/uploadPhoto", new BaseResopnseData.OnFileResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				Log.d(TAG, "uploadFile error:"+errMsg+"errorCode"+errCode);
				mToast.setText("失败,原因:"+errMsg+","+errCode);
				mToast.show();
				progressDialog.dismiss();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				if(result == null){
					requestSign("");
					return;
				}else{
//				Log.d(TAG, "upLoadFile complete data:"+result);
				if(result instanceof String){
					JSONObject json;
					try {
						json = new JSONObject((String)result);
						
						if(!json.optBoolean("succeed",false)){
							mToast.setText("签到失败，原因："+json.optString("errMsg"));
							mToast.show();
							progressDialog.dismiss();
							return;
						}
					
					String fids = "";
					JSONArray fileArray = json.optJSONArray("fileList");
					if(fileArray == null)
						return;
					for(int i = 0; i < fileArray.length(); i++){
						 JSONObject fileJson = fileArray.optJSONObject(i);
						 String id = fileJson.optString("fileid");
						 if(i < fileArray.length()-1){
							fids += id+","; 
						 }else{
							 fids += id;
						 }
					}
					
					requestSign(fids);
				}
			    catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
					
				}
			}
			
			@Override
			public void onProgress(float progress, int count, String fileName) {
				Log.d(TAG, "progress:"+progress);
				if (signState.equals(SIGNIN)) {
					 progressDialog.setMessage("正在签到"+progress + "%");
				} else if (signState.equals(SIGNOUT)) {
					 progressDialog.setMessage("正在签退"+progress + "%");
				}
			 	
			}
			
			@Override
			public void onPreExecute() {
				progressDialog.show();
				progressDialog.setMessage("正在上传照片...");
			}
			
			@Override
			public void onNoneData() {
				
			}
			
			@Override
			public void onFileReponseFile(List<String> filePaths) {
				
			}
		})
		.isContinuous(true);
		 mHttpFileUpload.addParam("location", locationSite);
		 mHttpFileUpload.execute();
		 
	}

	private void requestSign(String fids) {
		if (aMapLocation == null) {
			return;
		}
		progressDialog.show();
		HashMap<String, String> parMap = new HashMap<String, String>();
		remarks = editText.getText().toString();
		parMap.put("site", locationSite);
		parMap.put("coordinate", latNLongude);
		parMap.put("remarks", remarks);
		parMap.put("action", signState);
		if(fids.trim().length() > 0){
			Log.d(TAG, "fid:"+fids);
			parMap.put("photoIds", fids);
		}
		String url = "/attendance/phone/attend/clocking";
		// 描述
		Log.d(TAG, "param:"+parMap);
		
		new RequestAdpater() {

			private static final long serialVersionUID = 3478621980489736979L;

			@Override
			public void onReponse(ResponseData data) {
				progressDialog.dismiss();
				System.out.println("result="+data.getMRootData());
				if (data.getResultState().equals(ResultState.eSuccess)) {
					setResult(RESULT_OK);
					finish();
				} else {
					Log.d(TAG,
							"RequestAdpater onReponse( error " + data.getCode()
									+ "..." + data.getMsg() + "...");
					Toast.makeText(CheckWorkSignActivity.this,
							titleMsg + "失败" + data.getMsg(), Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onProgress(ProgressMessage msg) {

			}
		}.setUrl(url)
		 .addParam(parMap)
		 .notifyRequest();
	}

	// 图片上传
	PicturButton.OnPictureListener onPictureListener = new PicturButton.OnPictureListener() {
		@Override
		public void onPictureClick(String msg,Bitmap b) {
			ArrayList<ImageItem> items = new ArrayList<ImageItem>();
			for (String s : Bimp.drr) {
				ImageItem item = new ImageItem();
				item.iamge_id = "";
				item.image_path = s;
				item.thumbnailPath = "";
				items.add(item);
			}
			ImageItem item = new ImageItem();
			item.iamge_id = "";
			item.image_path = msg;
			item.thumbnailPath = "";

			SelectPictureActivity_.intent(CheckWorkSignActivity.this).imageItems(items)
					.imageItem(item).noOperation(true).start();
		}

		@Override
		public void onAddPictureClick() {
			if(Bimp.drr.size()>=9){
				PayUtils.showToast(CheckWorkSignActivity.this, "最多允许上传9张", 3000);
				return;
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
   		
            File mPhotoDir = new File(photoDir);
            if(!mPhotoDir.exists())
            mPhotoDir.mkdirs();
            photoPath = photoDir + getPhotoFileName();
            File photoFile = new File(photoPath);
            Uri saveUri = Uri.fromFile(photoFile);
            
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
            startActivityForResult(intent, TAKE_PICTURE);
		}
	};
	
	private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";

    }
	
	private void showSmallPhoto(Bitmap bitmap, String fileLocal) {
		PicturButton picturButton = new PicturButton(this, bitmap,
				fileLocal);
		photo.addPictrue(picturButton, bitmap, fileLocal, null);
	}
	
			private void initBmpCheck() {

				new Thread() {
					@Override
					public void run() {
								Bitmap bitmap;
								try {
									bitmap = Bimp.getimage(photoPath);
								bitmap = Bimp.revitionImageSize(photoPath);
								Message msg = new Message();
									msg.what = 2;
								Bundle bundle = new Bundle();
								bundle.putString("path", photoPath);
								msg.obj = bitmap;
								msg.setData(bundle);
								handler.sendMessage(msg);
								} catch (Exception e) {
									e.printStackTrace();
								}
						
						
					}
				}.start();
			}
			
			
			
}
