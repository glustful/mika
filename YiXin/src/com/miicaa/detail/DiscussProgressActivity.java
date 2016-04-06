package com.miicaa.detail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PicturButton;
import com.miicaa.common.base.PictureLayout;
import com.miicaa.common.base.PopupItem;
import com.miicaa.common.base.Utils;
import com.miicaa.home.R;
import com.miicaa.home.ui.home.MatterCell;
import com.miicaa.home.ui.org.MatterHttp;
import com.miicaa.home.ui.org.MatterHttp.OnMatterResult;
import com.miicaa.home.ui.org.StyleDialog;
import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.photoGrid.ImageItem;
import com.miicaa.home.ui.photoGrid.PhotoGridContentActivity;
import com.miicaa.home.ui.photoGrid.SelectPictureActivity_;
import com.miicaa.home.ui.widget.UploadWidget;
import com.miicaa.home.ui.widget.UploadWidget.UploadResponse;
import com.miicaa.utils.AllUtils;
@EActivity(R.layout.discuss_progress)
public class DiscussProgressActivity extends Activity{
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(mUploadWidget!=null)
			mUploadWidget.onActivityResult(requestCode, resultCode, data);
	}










	String mPhotoPath;
	String status = "0" ;
	String content ;
	StyleDialog dialog;
    final static int PHOTO_CARMAR = 1;
	final static int PHOTO_LOCAL = 2;
	public final static String PROGRESS = "0";
	public final static String FINISH = "1";
	
@ViewById(R.id.writeCancle)
Button cancle;
@ViewById(R.id.writeTitle)
TextView title;
@ViewById(R.id.writeCommit)
Button commit;
@ViewById(R.id.progressEdit)
EditText editText;
@ViewById(R.id.checkbox)
CheckBox checkbox;
@ViewById(R.id.already)
TextView alReady;

@ViewById(R.id.totalcount)
TextView totalCount;
@ViewById(R.id.upload_widget)
LinearLayout uploadWidget;
@Extra
String dataid;
@Extra
String arrangeType;
@Extra
String type;
UploadWidget mUploadWidget;
@AfterInject
void afterInject(){
	dialog = new StyleDialog(this);
	dialog.setCanceledOnTouchOutside(false);
	mUploadWidget = new UploadWidget(this);
}

@AfterViews
void afterViews(){
	//pictureLayout.setOnPictureListener(onPictureListener);
	InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(1000);
    editText.setFilters(new InputFilter[]{lengthFilter});
    if(type!=null&&type.equals(MatterCell.WORKREPORTTYPE)){
    	title.setText("填写点评意见");
    	checkbox.setVisibility(View.GONE);
    	alReady.setVisibility(View.GONE);
    }
    if(DetailProgressFragment.SECRET_ARRANGE.equals(arrangeType))
    {
    	checkbox.setVisibility(View.GONE);
    	alReady.setVisibility(View.GONE);
    }
    uploadWidget.addView(mUploadWidget.getRootView());
    mUploadWidget.setUploadResponse(new UploadResponse() {
		
		@Override
		public void response(int result) {
			/*
			 * 已完成
			 */
			if("1".equals(status)&&MatterDetailAcrtivity.getInstance()!=null)
				MatterDetailAcrtivity.getInstance().iWasComplete();
			if(type!=null&&type.equals(MatterCell.WORKREPORTTYPE)){
				
				setResult(Activity.RESULT_OK);
			}else{
				if(MatterDetailAcrtivity.getInstance()!=null)
			MatterDetailAcrtivity.getInstance().refreshthis();
			}
			finish();
			
		}
	});
//    editText.setText("没有填写进展");
}

@CheckedChange(R.id.checkbox)
void change(CompoundButton button,Boolean ischecked){
	if(ischecked){
		status = "1";
	}else
		status = "0";
}

@Click(R.id.writeCancle)
void cancle(){
	delBmp();
	finish();
}
@TextChange(R.id.progressEdit)
void onTextChangesOnHelloTextView(CharSequence text, TextView hello, int before, int start, int count) {
    // Something Here
	totalCount.setText(String.valueOf(editText.getText().toString().length()));
 }




@Click(R.id.writeCommit)
void commitClick(){
	Utils.hiddenSoftBorad(this);
	dialog.show();
      content = editText.getText().toString();
      if(content == null || "".equals(content.trim())){
    	  Toast.makeText(this, "内容不能为空！", Toast.LENGTH_SHORT).show();
    	  dialog.dismiss();
    	  return;
      }
      
      MatterHttp.requestArrageProgress(new OnMatterResult() {
		
		@Override
		public void onSuccess(String msg, Object obj) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			delBmp();

			
			/*
			 * 已完成
			 */
			if("1".equals(status)&&MatterDetailAcrtivity.getInstance()!=null)
				MatterDetailAcrtivity.getInstance().iWasComplete();
			if(type!=null&&type.equals(MatterCell.WORKREPORTTYPE)){
				
				setResult(Activity.RESULT_OK);
			}else{
				if(MatterDetailAcrtivity.getInstance()!=null)
			MatterDetailAcrtivity.getInstance().refreshthis();
			}
			finish();
		}
		
		@Override
		public void onFailure(String msg) {
			// TODO Auto-generated method stub
			//delBmp();
			Toast.makeText(DiscussProgressActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}
	}, mUploadWidget, dataid, content, status, "0",type);
      
}


@OnActivityResult(PHOTO_LOCAL)
void onResultlc(int resultCode, Intent data){
	 // pictureLayout.removeAllViews();
     // initBmpCheck();
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	 
    if (keyCode == KeyEvent.KEYCODE_BACK
             && event.getRepeatCount() == 0) {
        //do something...
    	delBmp();
    	finish();
         return true;
     }
     return super.onKeyDown(keyCode, event);
 }










//图片上传
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
		
		SelectPictureActivity_.intent(DiscussProgressActivity.this)
		.imageItems(items)
		.imageItem(item)
		.noOperation(true)
		.start();
    }

    @Override
    public void onAddPictureClick() {
        ArrayList<PopupItem> items = new ArrayList<PopupItem>();
        AllUtils.hiddenSoftBorad(DiscussProgressActivity.this);
        items.add(new PopupItem("从手机相册选择", "local"));
        items.add(new PopupItem("取消", "cancel"));
        BottomScreenPopup.builder(DiscussProgressActivity.this)
                .setItems(items)
                .setDrawable(R.drawable.white_color_selector)
                 
                 .setMargin(false)
                .setOnMessageListener(new OnMessageListener() {
                    @Override
                    public void onClick(PopupItem msg) {
                    	if("cancel".equals(msg.mCode)){
                    		
                    	}else{
                    	Intent i = new Intent(DiscussProgressActivity.this, PhotoGridContentActivity.class);
                        startActivityForResult(i, PHOTO_LOCAL);
                    	}
                    }
                })
                .show();

    }
};

 String getPhotoFileName() {
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "'IMG'_yyyyMMdd_HHmmss");
    return dateFormat.format(date) + ".jpg";

}
 
 //得到图片
 /*void getPhoto(Intent data){
	 Uri originalUri = null;
     String fileLocal = "";
     if (data != null) {
         originalUri = data.getData();
         if (originalUri.getScheme().toString().compareTo("content") == 0)//以//content://开头的URI，共享数据
         {
             String[] filePathColumn = {MediaColumns.DATA};
             Cursor cursor = getContentResolver().query(originalUri,
                     filePathColumn, null, null, null);
             cursor.moveToFirst();

             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             fileLocal = cursor.getString(columnIndex);
             cursor.close();
         }
     } else {
         originalUri = Uri.fromFile(new File(mPhotoPath));
         fileLocal = originalUri.toString().replace("file://", "");
     }
     ContentResolver cr = this.getContentResolver();
     try {
         InputStream is = cr.openInputStream(originalUri);
         Bitmap bitmap = decodeSampledBitmapFromStream(is,
                 pictureLayout.getChildrenHeight(),
                 pictureLayout.getChildrenHeight());
         if (bitmap != null) {
//             PicturButton button = new PicturButton(this, null, fileLocal);
//             mAddPhotoLayout.addPictrue(button,null,fileLocal,null);
             Bimp.drr.add(fileLocal);
             pictureLayout.removeAllViews();
             pictureLayout.removeAllViews();
             initBmpCheck();
         }
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     }
 }*/
 
 
 
 @Override
protected void onDestroy() {
	 delBmp();
	super.onDestroy();
}

 
 Bitmap decodeSampledBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {

     try {
         byte[] bytesIs = readStream(is);
         final BitmapFactory.Options options = new BitmapFactory.Options();
         options.inJustDecodeBounds = true;
         BitmapFactory.decodeByteArray(bytesIs, 0, bytesIs.length, options);
         options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
         options.inJustDecodeBounds = false;
         return BitmapFactory.decodeByteArray(bytesIs, 0, bytesIs.length, options);

     } catch (Exception e) {
         e.printStackTrace();
     }
     return null;
 }
 
 public static byte[] readStream(InputStream in) throws Exception {
     byte[] buffer = new byte[1024];
     int len = -1;
     ByteArrayOutputStream outStream = new ByteArrayOutputStream();

     while ((len = in.read(buffer)) != -1) {
         outStream.write(buffer, 0, len);
     }
     byte[] data = outStream.toByteArray();
     outStream.close();
     in.close();
     return data;
 }
 
 public static int calculateInSampleSize(
         BitmapFactory.Options options, int reqWidth, int reqHeight) {
     final int height = options.outHeight;
     final int width = options.outWidth;
     int inSampleSize = 1;

     if (height > reqHeight || width > reqWidth) {
         final int heightRatio = Math.round((float) height / (float) reqHeight);
         final int widthRatio = Math.round((float) width / (float) reqWidth);
         inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
     }

     return inSampleSize;
 }
 /*
  * 清除选择了的图片
  */
  void delBmp(){
     Bimp.clearMap();
 }

}
