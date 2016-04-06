package com.miicaa.home.ui.business.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.base.request.HttpFileUpload;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData;
import com.miicaa.common.base.FormFile;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PictureLayout;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.net.SocketHttpRequester;
import com.miicaa.home.data.net.SocketHttpRequester.OnCompleteListener;
import com.miicaa.home.data.net.SocketHttpRequester.OnProgressListener;
import com.miicaa.home.data.net.SocketUploadFileTask;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.service.ProgressNotifyService;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;

public class UploadPicActivity extends Activity implements OnClickListener {

	@Override
	protected void onDestroy() {

		super.onDestroy();
		Bimp.drr.clear();
	}

	private Runnable r = new Runnable() {
		@Override
		public void run() {
			if (mToast != null)
				mToast.cancel();
		}
	};

	private Handler mhandler = new Handler();

	public void showToast(Context mContext, String text, int duration) {

		mhandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mhandler.postDelayed(r, duration);

		mToast.show();
	}

	Toast mToast;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == GRID_PHOTO_CHECK) {
			mAddPhotoLayout.removeAllViews();

			initBmpCheck();
			return;
		}
		if (requestCode != 1)
			return;
		int result = data.getIntExtra("success", 0);
		mCopyUsers.clear();
		json = "";
		if (result == 2) {
			power.setText("公开");
			rightType = "00";
		} else if (result == 3) {
			power.setText("仅自己");
			rightType = "10";
		} else if (result == 1) {
			power.setText(data.getStringExtra("result"));
			code = data.getStringExtra("code");
			json = data.getStringExtra("json");
			rightType = data.getStringExtra("rightType");
		} else if (result == 4) {
			rightType = "40";
			ArrayList<ContactViewShow> copyDatas = data
					.getParcelableArrayListExtra(ContactUtil.SELECT_BACK);
			if (copyDatas == null || copyDatas.size() == 0) {
				power.setText("");
				mCopyUsers.clear();

				return;
			}
			setCopyData(copyDatas);
		}
	}

	@Override
	public void finish() {
		Bimp.clearMap();
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	private void setCopyData(ArrayList<ContactViewShow> data) {
		mCopyUsers.clear();
		// String user = "";
		// String conUser = "";
		json = "[";
		for (int i = 0; i < data.size(); i++) {
			mCopyUsers.add(new SamUser(data.get(i).getCode(), data.get(i)
					.getName()));
			json += "{\"code\":\"" + data.get(i).getCode() + "\",\"name\":\""
					+ data.get(i).getName() + "\"}";
			// user = data.get(i).getName();
			if (i < data.size() - 1) {
				// user += ",";
				json += ",";
			}
			// conUser = conUser + user;
			// power.setText(conUser.toString());
		}
		json += "]";
		if (data.size() > 1) {
			power.setText(data.get(0).getName() + "等" + data.size() + "个人");
		} else if (data.size() == 1) {
			power.setText(data.get(0).getName());
		} else {
			power.setText("");
		}
	}

	Context mContext;
	Button cancel, commit;
	TextView power, round;
	EditText edit;

	String name = "";
	ArrayList<String> falieds;
	RelativeLayout power_layout;

	String rightType = "00";
	String json = "";
	String code = "";
	ArrayList<SamUser> mCopyUsers;
	PictureLayout mAddPhotoLayout;
	public final static int GRID_PHOTO_CHECK = 0x09;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 3) {
				mDialog.setMessage(msg.obj.toString());
				return;
			}
			Bundle bundle = msg.getData();
			int count = bundle.getInt("count");
			Bitmap bitmap = (Bitmap) msg.obj;
			String fileLocal = Bimp.drr.get(count);
			showSmallPhoto(bitmap, fileLocal);
			switch (msg.what) {
			case 1:

				break;
			case 2:
				PicturButton button = new PicturButton(mContext);
				mAddPhotoLayout.addPictrue(button, null, null, null);
				break;
			case 3:
				mDialog.setMessage(msg.obj.toString());
				break;
			default:
				break;

			}
		}
	};

	private void showSmallPhoto(Bitmap bitmap, String fileLocal) {
		PicturButton picturButton = new PicturButton(mContext, bitmap,
				fileLocal);
		mAddPhotoLayout.addPictrue(picturButton, bitmap, fileLocal, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.business_file_upload_pic);

		this.mContext = this;
		mDialog = AllUtils.getNormalMiicaaDialog(this);

		mCopyUsers = new ArrayList<SamUser>();
		initUI();
		if (Bimp.drr != null && Bimp.drr.size() > 0) {
			mAddPhotoLayout.removeAllViews();
			initBmpCheck();
		}
	}

	private void initUI() {
		mAddPhotoLayout = (PictureLayout) findViewById(R.id.business_file_pic_picture);
		mAddPhotoLayout.setOnPictureListener(onPictureListener);
		mAddPhotoLayout.setVisibility(View.VISIBLE);

		power_layout = (RelativeLayout) findViewById(R.id.business_file_pic_power);

		cancel = (Button) findViewById(R.id.business_file_pic_cancel);
		commit = (Button) findViewById(R.id.business_file_pic_commitButton);
		// commit.setEnabled(true);

		edit = (EditText) findViewById(R.id.business_file_pic_edit);

		power = (TextView) findViewById(R.id.business_file_pic_power_text);
		round = (TextView) findViewById(R.id.business_file_pic_power_round);

		cancel.setOnClickListener(this);
		commit.setOnClickListener(this);
		power_layout.setOnClickListener(this);
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

				// commit.setEnabled(true);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Utils.hiddenSoftBorad(mContext);
		switch (v.getId()) {
		case R.id.business_file_pic_cancel:
			finish();
			break;
		case R.id.business_file_pic_commitButton:
			// commit.setEnabled(false);
			commit();
			break;
		case R.id.business_file_pic_power:
			Intent intent = new Intent(mContext, SelectRoundActivity_.class);
			intent.putExtra("rightType", rightType);
			intent.putExtra("json", json);
			intent.putExtra("name", power.getText().toString());
			Bundle bundle = new Bundle();
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<String> code = new ArrayList<String>();
			if (mCopyUsers.size() > 0) {
				for (SamUser s : mCopyUsers) {
					name.add(s.getmName());
					code.add(s.getmCode());
				}
				bundle.putStringArrayList("name", name);
				bundle.putStringArrayList("code", code);
			}
			bundle.putString(SelectContacter.how, SelectContacter.ARRANGE);

			bundle.putString("contact", "arrange");
			intent.putExtra("bundle", bundle);
			((Activity) mContext).startActivityForResult(intent, 1);
			((Activity) mContext).overridePendingTransition(
					R.anim.my_slide_in_right, R.anim.my_slide_out_left);

			break;
		}

	}

	private void commit() {
		Utils.hiddenSoftBorad(mContext);
		if (Bimp.drr == null || Bimp.drr.size() < 1) {
			showToast(mContext, "选择好图片后再上传", 1000);
			return;
		}
		name = edit.getText().toString();
		
		ArrayList<UploadFileItem> fileItemList = new ArrayList<UploadFileItem>();
		String url = mContext.getString(R.string.file_upload_url);
		if (!rightType.equals("10") && !rightType.equals("00")) {
			if(json==null||json.trim().equals("")){
				PayUtils.showToast(mContext, "查看范围不能为空", 2000);
				return;
			}
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("parentId", BusinessFileActivity.getIntance()
				.getTmpParentId());
		map.put("describ", name);
		map.put("rightType", rightType);
		for(String paht : Bimp.drr){
		map.put("name", FileUtils.getFileName(paht));
		if (!rightType.equals("10") && !rightType.equals("00")) {
			json = json.replaceAll("targetName", "name");
			json = json.replaceAll("targetCode", "code");
			map.put("json", json);
		}
		fileItemList.add(new UploadFileItem(paht, map));
	}
		if(FileUtils.getFilesLength(Bimp.drr)/AllUtils.MB > AllUtils.Service_File_Length){
			Intent intent = new Intent(UploadPicActivity.this, ProgressNotifyService.class);
			intent.putParcelableArrayListExtra("upload", fileItemList);
			intent.putExtra("dataType", "");
			intent.putExtra("title","企业文件");
			intent.putExtra("url", url);
			startService(intent);
			finish();
		}else{
			new HttpFileUpload()
	      .setParam(fileItemList, url, new BaseResopnseData.OnFileResponseListener() {
			
			@Override
			public void onResponseError(String errMsg, String errCode) {
				mToast.setText("上传失败："+errMsg+errCode);
				mToast.show();
				mDialog.dismiss();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				finish();
			}
			
			@Override
			public void onProgress(float progress, int count, String fileName) {
				mDialog.setProgress((int)progress);
			}
			
			@Override
			public void onPreExecute() {
				mDialog = new ProgressDialog(mContext);
				mDialog.setMax(100);
				mDialog.setMessage("正在上传");
				mDialog.setCancelable(false);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mDialog.show();
			}
			
			@Override
			public void onNoneData() {
				
			}
			
			@Override
			public void onFileReponseFile(List<String> filePaths) {
				if(filePaths.size() > 0){
					mToast.setText("上传失败:"+filePaths.size()+"张图片");
					mToast.show();
					mDialog.dismiss();
				}
				
			}
		}).isContinuous(false)
		.execute();
		}

		
//
//		falieds = new ArrayList<String>();
//		new UploadTask(url).execute(1000);

	}

	PicturButton.OnPictureListener onPictureListener = new PicturButton.OnPictureListener() {
		@Override
		public void onPictureClick(String msg,Bitmap b) {
			ArrayList<ImageItem> items = new ArrayList<ImageItem>();
			for(String s:Bimp.drr){
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
			
			SelectPictureActivity_.intent(mContext)
			.imageItems(items)
			.imageItem(item)
			.noOperation(true)
			.start();
		}

		@Override
		public void onAddPictureClick() {

			Intent i = new Intent(mContext, PhotoGridContentActivity.class);
			startActivityForResult(i, GRID_PHOTO_CHECK);

		}
	};

	// 添加图片后处理
	private void initBmpCheck() {

		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < Bimp.drr.size(); i++) {
					try {
						Bitmap bitmap = Bimp.revitionImageSize(Bimp.drr.get(i));
						Message msg = new Message();
						msg.what = 1;
						if (i == Bimp.drr.size() - 1) {
							msg.what = 2;
						}
						Bundle bundle = new Bundle();
						bundle.putInt("count", i);
						msg.obj = bitmap;
						msg.setData(bundle);
						handler.sendMessage(msg);
					} catch (IOException e) {

					}
				}
			}
		}.start();
	}

	ProgressDialog mDialog;

	public class UploadTask extends AsyncTask<Integer, Integer, String> {
		HashMap<String, String> map;

		String url;
		String path;
		String filename;
		SocketHttpRequester request;

		public UploadTask(String url) {

			this.url = url;
			this.request = new SocketHttpRequester();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
			mDialog.setProgress(values[0]);
			
			// mDialog.setMessage(filename);
		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMax(100);
			mDialog.setMessage("正在上传");
			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.show();

		}

		@Override
		protected void onPostExecute(String result) {

			if (mDialog != null)
				mDialog.dismiss();
			// commit.setEnabled(true);

			BusinessFileActivity.getIntance().isRefresh();
			if (falieds.size() < 1) {
				Bimp.drr.clear();
				
				finish();
			} else {
				String[] items = new String[falieds.size()];
				int i = 0;

				for (String m : falieds) {
					items[i++] = m;

				}
				ArrayList<String> temp = new ArrayList<String>();
				for(String m:Bimp.drr){
					if(!falieds.contains(m)){
						temp.add(m);
						
						
					}
				}
				for(String m:temp){
					int index = Bimp.drr.indexOf(m);
					Bimp.drr.remove(index);
					
					mAddPhotoLayout.removeViewAt(index);
				}
				temp.clear();
				new AlertDialog.Builder(mContext)
						.setIcon(R.drawable.an_arrange_person_delete)
						.setItems(items, null).setTitle("上传失败")
						.setPositiveButton("确定", null).create().show();

			}

		}

		@Override
		protected String doInBackground(Integer... params) {

			for (int i = 0; i < Bimp.drr.size(); i++) {

				path = Bimp.drr.get(i);
				filename = path.substring(path.lastIndexOf("/") + 1);
				Message msg = handler.obtainMessage();
				msg.what = 3;
				msg.obj = filename;
				handler.sendMessage(msg);
				map = new HashMap<String, String>();
				map.put("parentId", BusinessFileActivity.getIntance()
						.getTmpParentId());
				map.put("name", filename);
				map.put("describ", name);
				map.put("rightType", rightType);
				if (!rightType.equals("10") && !rightType.equals("00")) {
					json = json.replaceAll("targetName", "name");
					json = json.replaceAll("targetCode", "code");
					map.put("json", json);
				}

				request.post(url, map, new FormFile[] { new FormFile(new File(
						path), "file", null) }, new OnProgressListener() {

					@Override
					public void onProgress(int progress) {
						onProgressUpdate(progress);

					}
				}, new OnCompleteListener() {

					@Override
					public void onSuccess(String json) {
						// Bimp.drr.remove(path);
					}

					@Override
					public void onFailed() {
						falieds.add(path);

					}
				});
			}
			return null;
		}

	}
	


}
