package com.miicaa.home.ui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avos.avoscloud.LogUtil.log;
import com.miicaa.base.request.HttpFileUpload;
import com.miicaa.base.request.UploadFileItem;
import com.miicaa.base.request.response.BaseResopnseData.OnFileResponseListener;
import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PictureLayout;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.PictureLayout.OndelWebPicListener;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.matter.MatterEditor;
import com.miicaa.home.ui.matter.MatterImgManager;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.home.ui.report.ReportUtils;
import com.miicaa.service.ProgressNotifyService;
import com.miicaa.utils.AllUtils;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.fileselect.FileListActivity_;
import com.miicaa.utils.fileselect.MyFileItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UploadWidget {
	
	private static String TAG = "UploadWidget";
	
	Context mContext;
	LayoutInflater inflater;
	View rootView;
	public final static int GRID_PHOTO_CHECK = 0x09;
	public final static int GRID_FILE_CHECK = 0x10;
	PictureLayout mAddPhotoLayout;
	ArrayList<MyFileItem> files;
	ArrayList<MatterImgManager> imageManagerList;
	LinearLayout fileLayout;
	ArrayList<Object> falieds;
	UploadResponse reponse;
	DisplayImageOptions displayImageOptions;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 202) {
				uploadFinish();
				return;
			}
			Bundle bundle = msg.getData();
			int count = bundle.getInt("count");
			if(msg.obj instanceof Bitmap){
			Bitmap bitmap = (Bitmap) msg.obj;
			if (count == -1) {
				showSmallPhoto(bitmap, null, bundle.getString("fid"));
			} else {
				String fileLocal = Bimp.drr.get(count);
				showSmallPhoto(bitmap, fileLocal);
			}
			}
			switch (msg.what) {
			case 1:
				break;
			case 2:
				PicturButton button = new PicturButton(mContext);
				mAddPhotoLayout.addPictrue(button, null, null, null);
				break;
			case 3:
//				if(msg.obj instanceof String){
				String name = msg.obj.toString();
				progressDialog.setMessage(name);
//				}
				break;
			default:
				break;

			}
		}
	};
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
	case GRID_FILE_CHECK:

		ArrayList<MyFileItem> tmp = (ArrayList<MyFileItem>) data
				.getSerializableExtra("data");
		log.d(TAG, "file select size:"+tmp.size());
		if (tmp == null || tmp.size() < 1)
			return;
		files.addAll(tmp);
		addFileInfo(tmp);
		break;
	case GRID_PHOTO_CHECK:
		mAddPhotoLayout.removeAllViews();
		initBmpCheck();
		break;
		}
	}
	
	public UploadWidget(Context context){
		displayImageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(false)
		.cacheOnDisk(true)
		.considerExifParams(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		this.mContext = context;
		files = new ArrayList<MyFileItem>();
		falieds = new ArrayList<Object>();
		imageManagerList = new ArrayList<MatterImgManager>();
		inflater = LayoutInflater.from(mContext);
		rootView = inflater.inflate(R.layout.upload_widget, null);
		fileLayout = (LinearLayout) findViewById(R.id.upload_file_list);
		mAddPhotoLayout = (PictureLayout) findViewById(R.id.upload_file_picture);
		mAddPhotoLayout.setOnPictureListener(onPictureListener);
		mAddPhotoLayout.setOnDelWebPicListener(new OndelWebPicListener() {

			@Override
			public void onDelWeb(String fid, View delView) {
				delWebPic(fid, delView, 0);
			}
		});
	}
	
	ArrayList<String> pictureList = new ArrayList<String>();
	public void initPhotoOrFile(ArrayList<MyFileItem> tmps,
			ArrayList<String> pictureList){
		if(tmps!=null)
		addFileInfo(tmps);
		if(pictureList!=null&&pictureList.size()>0){
			mAddPhotoLayout.removeAllViews();
			this.pictureList.clear();
			this.pictureList.addAll(pictureList);
			initBmpCheck();
		}
	}
	
	public void setUploadResponse(UploadResponse re){
		this.reponse = re;
	}
	
	private View findViewById(int id){
		return rootView.findViewById(id);
	}
	
	public ArrayList<MyFileItem> getFiles(){
		return files;
	}
	
	public View getRootView(){
		return rootView;
	}
	
		private void initBmpCheck() {

					String url = null;
					for (int i = 0; i < Bimp.drr.size(); i++) {
							url = Scheme.FILE.wrap(Bimp.drr.get(i));
							PicturButton picturButton = new PicturButton(mContext,Bimp.drr.get(i));
							ImageLoader.getInstance().displayImage(url, picturButton.mPicture, displayImageOptions);
							mAddPhotoLayout.addPictrue(picturButton, Bimp.drr.get(i),null );
					}
					for(int i = 0 ; i < pictureList.size();i++){
						url = pictureList.get(i);
						String fid = url.substring(url.indexOf("fid=")+4);
						PicturButton picturButton = new PicturButton(mContext,url);
						ImageLoader.getInstance().displayImage(url, picturButton.mPicture, displayImageOptions);
						log.d(TAG, "fileId:"+fid);
						mAddPhotoLayout.addPictrue(picturButton, null,fid );
					}
					PicturButton picturButton = new PicturButton(mContext);
					mAddPhotoLayout.addPictrue(picturButton, null, null, null);
//					if (imageManagerList != null)
//						for (int i = 0; i < imageManagerList.size(); i++) {
//							try {
//								Bitmap bitmap = imageManagerList.get(i).bitmap;
//								Message msg = new Message();
//								msg.what = 1;
//								if (i == imageManagerList.size() - 1) {
//									msg.what = 2;
//								}
//								Bundle bundle = new Bundle();
//								bundle.putInt("count", -1);
//								bundle.putString("fid", imageManagerList.get(i).fid);
//								msg.obj = bitmap;
//								msg.setData(bundle);
//								handler.sendMessage(msg);
//							} catch (Exception e) {
//
//							}
//						}
		}
	
	private void addFileInfo(ArrayList<MyFileItem> tmp) {
		for (MyFileItem item : tmp) {
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.business_file_upload_fileinfo, null);
			v.setTag(item);
			TextView tv = (TextView) v
					.findViewById(R.id.business_file_file_filename);
			tv.setText(item.name);
			Utils.setKindsImage(tv,
					item.path.substring(item.path.lastIndexOf(".") + 1));
			ImageButton delete = (ImageButton) v
					.findViewById(R.id.business_file_file_delete);
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent();
					Object o = parent.getTag();
					if(o==null)
						return;
					if(!(o instanceof MyFileItem))
						return;
					MyFileItem tmp = (MyFileItem) o;
					if(tmp.fid!=null&&!tmp.fid.equals("")){
						delWebPic(tmp.fid, parent, 1);
					}else{
					fileLayout.removeView(parent);
					files.remove(parent.getTag());
					}
					
				}
			});
			fileLayout.addView(v);

		}
	}
	
	private void showSmallPhoto(Bitmap bitmap, String fileLocal) {
		PicturButton picturButton = new PicturButton(mContext, bitmap,
				fileLocal);
		mAddPhotoLayout.addPictrue(picturButton, bitmap, fileLocal, null);
	}

	private void showSmallPhoto(Bitmap bitmap, String fileLocal, String fid) {
		PicturButton picturButton = new PicturButton(mContext, bitmap,
				fileLocal);
		mAddPhotoLayout.addPictrue(picturButton, bitmap, fileLocal, fid);
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
				for(String s: pictureList){
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

				SelectPictureActivity_.intent(mContext).imageItems(items)
						.imageItem(item).noOperation(true).start();
				// PhotoCheck_.intent(this).name(fileIds.get(position))
			    	//.names(fileIds)
			    	//.start();
			}

			@Override
			public void onAddPictureClick() {
				ArrayList<com.miicaa.common.base.PopupItem> items = new ArrayList<PopupItem>();
				items.add(new PopupItem("选择图片", "pic"));
				items.add(new PopupItem("选择文件", "file"));

				items.add(new PopupItem("取消", "cancel"));
				BottomScreenPopup.builder(mContext).setItems(items)
						.setDrawable(R.drawable.white_color_selector)

						.setMargin(false)
						.setOnMessageListener(new OnMessageListener() {
							@Override
							public void onClick(PopupItem msg) {
								if (msg.mCode.equals("pic")) {
									Intent i = new Intent(mContext,
											PhotoGridContentActivity.class);
									((Activity) mContext).startActivityForResult(i, GRID_PHOTO_CHECK);
									((Activity)mContext).overridePendingTransition(
											R.anim.my_slide_in_right,
											R.anim.my_slide_out_left);
								} else if (msg.mCode.equals("file")) {
									Intent i = new Intent(mContext,
											FileListActivity_.class);
									((Activity) mContext).startActivityForResult(i, GRID_FILE_CHECK);
									((Activity)mContext).overridePendingTransition(
											R.anim.my_slide_in_right,
											R.anim.my_slide_out_left);
								}
							}
						}).show();

			}
		};
		
		private void delWebPic(final String fid, final View v, final int type) {

			if (fid != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
						.setTitle("提示")
						.setMessage("确定删除吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface, int i) {
										String url = "/home/proupload/pc/component/upload/delete";
										PayUtils.showDialog(mContext);
										new RequestAdpater() {

											@Override
											public void onReponse(ResponseData data) {
												PayUtils.closeDialog();

												if (data.getResultState() == ResultState.eSuccess) {
													//isRefresh = true;
													Toast.makeText(mContext,
															"删除成功",
															Toast.LENGTH_SHORT)
															.show();
													if (type == 0) {
														mAddPhotoLayout
																.removeView(v);
														for(String path : pictureList){
															if(path.contains(fid)){
																pictureList.remove(path);
																break;
															}
														}
													} else {
														fileLayout.removeView(v);
														files.remove(v.getTag());
													}
												} else {
													Toast.makeText(mContext,
															"删除失败",
															Toast.LENGTH_SHORT)
															.show();
												}

											}

											@Override
											public void onProgress(
													ProgressMessage msg) {
												// Toast.makeText(mContext, "删除失败",
												// Toast.LENGTH_SHORT).show();
											}
										}.setUrl(url).addParam("id", fid)
												.notifyRequest();
										dialogInterface.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface, int i) {
										dialogInterface.dismiss();
									}
								});
				builder.show();
			}
		}
		
		 ProgressDialog progressDialog;
		@SuppressLint("ShowToast")
		public void upload(String fid,String appun,String dataType){
			progressDialog = new ProgressDialog(mContext);
			progressDialog.setTitle("正在上传");
			final Toast mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(100);
			ArrayList<UploadFileItem> uploadFileItemList = new ArrayList<UploadFileItem>();
			HashMap<String, String> param = new HashMap<String, String>();
			ArrayList<String> pathList = new ArrayList<String>();
			param.put("infoId", fid);
			param.put("appUn", appun);
			for(MyFileItem item : files){
				String path = item.path;
				param.put("fileName", item.name);
				UploadFileItem uploadFileItem = new UploadFileItem(path, param);
				uploadFileItemList.add(uploadFileItem);
				pathList.add(path);
			}
			for(String bimpPath : Bimp.drr){
			     String fileName = FileUtils.getFileName(bimpPath);
			     param.put("fileName", fileName);
			     UploadFileItem uploadFileItem = new UploadFileItem(bimpPath,param);
			     uploadFileItemList.add(uploadFileItem);
			     pathList.add(bimpPath);
			}
			if(FileUtils.getFilesLength(pathList)/AllUtils.MB  > AllUtils.Service_File_Length){
				mToast.setText("附件太大，请看进度条！");
				Intent i = new Intent(mContext, ProgressNotifyService.class);
				i.putExtra("upload", uploadFileItemList);
				i.putExtra("url",  mContext
						.getString(R.string.upload_url));
				i.putExtra("title", "");
				i.putExtra("dataType", dataType);
				mContext.startService(i);
				mToast.show();
			}else{
				new HttpFileUpload()
			.setParam(uploadFileItemList,  mContext
					.getString(R.string.upload_url), 
					new OnFileResponseListener() {
						
						@Override
						public void onResponseError(String errMsg, String errCode) {
							mToast.setText(errMsg+errCode);
							mToast.show();
							progressDialog.dismiss();
						}
						
						@Override
						public <T> void onReponseComplete(T result) {
							progressDialog.dismiss();
							handler.sendEmptyMessage(202);
						}
						
						@Override
						public void onProgress(float progress, int count, String fileName) {
							progressDialog.setProgress((int)progress);
							handler.obtainMessage(3, fileName).sendToTarget();
//							progressDialog.setMessage(fileName);
						}
						
						@Override
						public void onPreExecute() {
							progressDialog.show();
						}
						
						@Override
						public void onNoneData() {
							
						}
						
						@Override
						public void onFileReponseFile(List<String> filePaths) {
							progressDialog.dismiss();
							if(filePaths.size() > 0){
								mToast.setText(filePaths.size()+"个附件上传失败！");
								mToast.show();
							}
						}
					}).isContinuous(false)
					.execute();
			}
		}
		
		
		protected void uploadFinish() {
			if (falieds.size() < 1) {
				Bimp.drr.clear();
				if(reponse!=null){
					reponse.response(UploadResponse.e_success);
				}
			} else {
				String[] items = new String[falieds.size()];
				int i = 0;

				for (Object m : falieds) {
					if (m instanceof MyFileItem)
						items[i++] = ((MyFileItem) m).name;
					else {
						items[i++] = m.toString();
					}
				}
				ArrayList<MyFileItem> temp = new ArrayList<MyFileItem>();
				for (MyFileItem m : files) {
					if (!falieds.contains(m)) {
						temp.add(m);

					}
				}
				for (MyFileItem m : temp) {
					int index = files.indexOf(m);
					files.remove(index);
					for (int k = 0; k < fileLayout.getChildCount(); k++) {
						if (fileLayout.getChildAt(k).getTag() == m) {
							fileLayout.removeViewAt(k);
							break;
						}
					}

				}
				temp.clear();
				ArrayList<String> temp1 = new ArrayList<String>();
				for (String m : Bimp.drr) {
					if (!falieds.contains(m)) {
						temp1.add(m);

					}
				}
				for (String m : temp1) {
					int index = Bimp.drr.indexOf(m);
					Bimp.drr.remove(index);

					mAddPhotoLayout.removeViewAt(index);
				}
				temp1.clear();
				new AlertDialog.Builder(mContext)
						.setIcon(R.drawable.an_arrange_person_delete)
						.setItems(items, null)
						.setTitle("上传失败")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Bimp.drr.clear();
										if(reponse!=null){
											reponse.response(UploadResponse.e_dialog_ok);
										}
									}
								}).create().show();

			}

		}	
		
		public interface UploadResponse{
			public static final int e_success = 0;
			public static final int e_dialog_ok = 1;
			public static final int e_dialog_cancel = 2;
			void response(int result);
		}

		public boolean hasFile() {
			if(Bimp.drr.size()>0)
				return true;
			if(files!=null&&files.size()>0){
				for(MyFileItem item:files){
					if(item.fid!=null&&!item.fid.equals("")){
						continue;
					}else{
						return true;
					}
				}
			}
			return false;
		}
}
