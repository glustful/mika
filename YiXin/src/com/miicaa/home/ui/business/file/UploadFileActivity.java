package com.miicaa.home.ui.business.file;

import java.io.File;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.miicaa.home.data.net.SocketUploadFileTask;
import com.miicaa.home.data.net.SocketHttpRequester.OnCompleteListener;
import com.miicaa.home.data.net.SocketHttpRequester.OnProgressListener;
import com.miicaa.home.ui.contactGet.ContactViewShow;
import com.miicaa.home.ui.contactGet.SelectContacter;
import com.miicaa.home.ui.contactList.ContactUtil;
import com.miicaa.home.ui.contactList.SamUser;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.BigPhotoCheckActivity;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.service.ProgressNotifyService;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.fileselect.FileListActivity_;
import com.miicaa.utils.fileselect.MyFileItem;

public class UploadFileActivity extends Activity implements OnClickListener {

	private  Runnable r = new Runnable() {
        @Override
		public void run() {
        	if(mToast != null)
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		if(requestCode==GRID_PHOTO_CHECK){
			ArrayList<MyFileItem> tmp = (ArrayList<MyFileItem>) data.getSerializableExtra("data");
			if(tmp==null || tmp.size()<1)
				return;
			files.addAll(tmp);
			addFileInfo(tmp);
			return;
		}
		if(requestCode != 1)
			return;
		int result = data.getIntExtra("success", 0);
		mCopyUsers.clear();
		json = "";
		if(result ==2){
			power.setText("公开");
			rightType = "00";
		}else if(result ==3){
			power.setText("仅自己");
			rightType = "10";
		}
		else if (result == 1) {
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
	    public void finish(){
	    Bimp.clearMap();	
	        super.finish();
	        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
	    }

	private void setCopyData(ArrayList<ContactViewShow> data) {
		mCopyUsers.clear();
		//String user = "";
		//String conUser = "";
		json = "[";
		for (int i = 0; i < data.size(); i++) {
			mCopyUsers.add(new SamUser(data.get(i).getCode(), data.get(i)
					.getName()));
			json += "{\"code\":\"" + data.get(i).getCode() + "\",\"name\":\""
					+ data.get(i).getName() + "\"}";
			//user = data.get(i).getName();
			if (i < data.size() - 1) {
				//user += ",";
				json += ",";
			}
			//conUser = conUser + user;
			
		}
		if(data.size()>1){
		power.setText(data.get(0).getName()+"等"+data.size()+"个人");
		}else if(data.size()==1){
			power.setText(data.get(0).getName());
		}else{
			power.setText("");
		}
		json += "]";
	}

	Context mContext;
	Button cancel, commit;
	TextView power, round;
	EditText edit;
	ArrayList<MyFileItem> files;
	String name = "";
	ArrayList<MyFileItem> falieds;
	RelativeLayout power_layout;

	String rightType = "00";
	String json = "";
	String code = "";
	ArrayList<SamUser> mCopyUsers;
	PictureLayout mAddPhotoLayout;
	LinearLayout layout;
	LayoutInflater inflater;
	public final static int GRID_PHOTO_CHECK = 0x09;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDialog = AllUtils.getNormalMiicaaDialog(this);
		setContentView(R.layout.business_file_upload_file);

		this.mContext = this;
		files = new ArrayList<MyFileItem>();
		mCopyUsers = new ArrayList<SamUser>();
		inflater = LayoutInflater.from(mContext);
		initUI();
		try{
		ArrayList<MyFileItem> tmp = (ArrayList<MyFileItem>) getIntent().getSerializableExtra("data");
		if(tmp==null || tmp.size()<1)
			return;
		files.addAll(tmp);
		addFileInfo(tmp);
		}catch(Exception e){
			
		}
	}

	private void addFileInfo(ArrayList<MyFileItem> tmp) {
		for(MyFileItem item:tmp){
			View v = inflater.inflate(R.layout.business_file_upload_fileinfo, null);
			TextView tv = (TextView) v.findViewById(R.id.business_file_file_filename);
			tv.setText(item.name);
			setKindsImage(tv, item.path.substring(item.path.lastIndexOf(".")+1));
			ImageButton delete = (ImageButton) v.findViewById(R.id.business_file_file_delete);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent();
					int index = layout.indexOfChild(parent);
					layout.removeView(parent);
					files.remove(index);
					
				}
			});
			layout.addView(v);
			
		}
	}
	
	private void setKindsImage(TextView mHeadImg,String ext) {
		String optString = ext.toLowerCase();
		if (optString.equals("txt")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_txt, 0, 0, 0);
		} else if (optString.equals("pdf")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_pdf, 0, 0, 0);
		} else if (optString.equals("doc")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("docx")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_word, 0, 0, 0);
		} else if (optString.equals("zip")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("rar")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_rar, 0, 0, 0);
		} else if (optString.equals("ppt") || optString.equals("pptx")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_ppt, 0, 0, 0);
		} else if (optString.equals("xlsx") || optString.equals("xls")) {
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_execl, 0, 0, 0);
		} else {
			
			
			mHeadImg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accessory_file_ico_normal, 0, 0, 0);
		}

	}

	private void initUI() {
		mAddPhotoLayout = (PictureLayout) findViewById(R.id.business_file_file_picture);
		mAddPhotoLayout.setOnPictureListener(onPictureListener);
		mAddPhotoLayout.setVisibility(View.VISIBLE);
		layout = (LinearLayout) findViewById(R.id.upload_file_list);
		power_layout = (RelativeLayout) findViewById(R.id.business_file_file_power);

		cancel = (Button) findViewById(R.id.business_file_file_cancel);
		commit = (Button) findViewById(R.id.business_file_file_commitButton);
		

		edit = (EditText) findViewById(R.id.business_file_file_edit);

		power = (TextView) findViewById(R.id.business_file_file_power_text);
		round = (TextView) findViewById(R.id.business_file_file_power_round);

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

				//commit.setEnabled(true);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Utils.hiddenSoftBorad(mContext);
		switch (v.getId()) {
		case R.id.business_file_file_cancel:
			finish();
			break;
		case R.id.business_file_file_commitButton:
			//commit.setEnabled(false);
			commit();
			break;
		case R.id.business_file_file_power:
			//commit.setEnabled(true);
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
			bundle.putString(SelectContacter.how,
					SelectContacter.ARRANGE);

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
		if (files == null || files.size() < 1) {
			showToast(mContext, "选择好文件后再上传", 1000);
			return;
		}
		String url = mContext.getString(R.string.file_upload_url);
		if (!rightType.equals("10") && !rightType.equals("00")) {
			if(json==null||json.trim().equals("")){
				PayUtils.showToast(mContext, "查看范围不能为空", 2000);
				return;
			}
		}
		name = edit.getText().toString();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("parentId", BusinessFileActivity.getIntance()
				.getTmpParentId());
		map.put("describ", name);
		map.put("rightType", rightType);
		ArrayList<UploadFileItem> fileItemList = new ArrayList<UploadFileItem>();
		List<String> pathList = new ArrayList<String>();
		for(MyFileItem item : files){
			map.put("name", item.name);
			if (!rightType.equals("10") && !rightType.equals("00")) {
				json = json.replaceAll("targetName", "name");
				json = json.replaceAll("targetCode", "code");
				map.put("json", json);
			}
			pathList.add(item.path);
			fileItemList.add(new UploadFileItem(item.path, map));
		}
		
		if(FileUtils.getFilesLength(pathList)/AllUtils.MB > 1){
			Intent intent = new Intent(UploadFileActivity.this, ProgressNotifyService.class);
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
				mToast.setText("上传失败:"+errMsg+errCode);
				mToast.show();
				mDialog.dismiss();
			}
			
			@Override
			public <T> void onReponseComplete(T result) {
				mDialog.dismiss();
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
		})
		.isContinuous(false)
		.execute();
		}

//		falieds = new ArrayList<MyFileItem>();
//		if (!rightType.equals("10") && !rightType.equals("00")) {
//			if(json==null||json.trim().equals("")){
//				PayUtils.showToast(mContext, "查看范围不能为空", 2000);
//				return;
//			}
//		}
//			new UploadTask(url).execute(1000);
		

	}

	// 图片上传
	PicturButton.OnPictureListener onPictureListener = new PicturButton.OnPictureListener() {
		@Override
		public void onPictureClick(String msg,Bitmap b) {
			Intent intent = new Intent(mContext, BigPhotoCheckActivity.class);
		}

		@Override
		public void onAddPictureClick() {

			Intent i = new Intent(mContext, FileListActivity_.class);
			startActivityForResult(i, GRID_PHOTO_CHECK);

		}
	};

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				mDialog.setMessage(msg.obj.toString());
				break;
			}
		}
		
	};
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
			//mDialog.setMessage(filename);
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
			

			BusinessFileActivity.getIntance().isRefresh();
			if(falieds.size()<1){
				
				finish();
			}else{
				String[] items = new String[falieds.size()];
				int i=0;
				//
				for(MyFileItem m:falieds){
					items[i++] = m.name;
					
				}
				ArrayList<MyFileItem> temp = new ArrayList<MyFileItem>();
				for(MyFileItem m:files){
					if(!falieds.contains(m)){
						temp.add(m);
						
						
					}
				}
				for(MyFileItem m:temp){
					int index = files.indexOf(m);
					files.remove(index);
					
					layout.removeViewAt(index);
				}
				temp.clear();
				new AlertDialog.Builder(mContext)
				.setIcon(R.drawable.an_arrange_person_delete)
				.setItems(items, null)
				.setTitle("上传失败")
				.setPositiveButton("确定", null)
				.create()
				.show();
				
			}
			//files.clear();
		}

		@Override
		protected String doInBackground(Integer... params) {
			
			for (int i = 0; i < files.size(); i++) {
				final MyFileItem item = files.get(i);
				path = files.get(i).path;
				
				filename = files.get(i).name;
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = filename;
				handler.sendMessage(msg);
				 map = new HashMap<String, String>();
				map.put("parentId", BusinessFileActivity.getIntance().getTmpParentId());
				map.put("name", filename);
				map.put("describ", name);
				map.put("rightType", rightType);
				if (!rightType.equals("10") && !rightType.equals("00")) {
					json = json.replaceAll("targetName", "name");
					json = json.replaceAll("targetCode", "code");
					map.put("json", json);
				}

			request.post(url, map, new FormFile[] { new FormFile(
					new File(path), "file", null) }, new OnProgressListener() {

				@Override
				public void onProgress(int progress) {
					onProgressUpdate(progress);

				}
			}, new OnCompleteListener() {

				@Override
				public void onSuccess(String json) {
					//files.remove(path);
				}

				@Override
				public void onFailed() {
					falieds.add(item);

				}
			});
			}
			return null;
		}

	}
	
	
}
