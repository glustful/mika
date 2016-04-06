package com.miicaa.home.ui.announcement;

import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.miicaa.base.RoundImage.CircularImage;
import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.pay.PayUtils;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.home.ui.picture.PictureHelper.OnFirstPicureListener;
import com.miicaa.utils.FileUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.pay.PayUtils;

@EActivity(R.layout.announcement_detail_view)
public class AnnouncementDetailActivity extends Activity {

	@Override
	public void onBackPressed() {
		
		back();
	}

	@Extra
	String jsonStr;
	@ViewById(R.id.pay_cancleButton)
	Button back;
	@ViewById(R.id.symbol)
	TextView symbol;

	@ViewById(R.id.pay_headTitle)
	TextView htitle;
	@ViewById(R.id.pay_commitButton)
	Button commit;
	@ViewById(R.id.headimg)
	CircularImage headImg;
	@ViewById(R.id.name)
	TextView name;
	@ViewById(R.id.title)
	TextView title;
	@ViewById(R.id.content)
	TextView content;
	@ViewById(R.id.unit)
	TextView unit;
	@ViewById(R.id.pubtime)
	TextView time;
	@ViewById(R.id.keepDays)
	TextView keepDays;
	@ViewById(R.id.attachment_layout)
	LinearLayout mAttachmentLayout;
	@ViewById(R.id.photo_layout)
	RelativeLayout mPhotoLayout;
	@ViewById(R.id.file_layout)
	RelativeLayout mFileLayout;
	@ViewById(R.id.picture)
	ImageView littlePhoto;
	@ViewById(R.id.photo_count)
	TextView mPhotoCountText;
	@ViewById(R.id.file_count)
	TextView mFileCountText;
	
	@Click(R.id.pay_cancleButton)
	void back(){
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	Context mContext;
	JSONObject jsonObj;
	ArrayList<String> pictureFids;
	ArrayList<AccessoryInfo> mAccDatas = null;
	@AfterInject
	void initData(){
		this.mContext = this;
		try {
			jsonObj = new JSONObject(jsonStr);
		} catch (JSONException e) {
			
			e.printStackTrace();
			PayUtils.showToast(mContext, "数据解析出错！", 3000);
			finish();
		}
	}
	@AfterViews
	void initUI(){
		back.setText("公告");
		htitle.setText("查看公告");
		commit.setVisibility(View.INVISIBLE);
		mAttachmentLayout.setVisibility(View.GONE);
		mFileLayout.setVisibility(View.GONE);
		mPhotoLayout.setVisibility(View.GONE);
		Tools.setHeadImgWithoutClick(jsonObj.optString("publishUserCode"), headImg);
		name.setText(jsonObj.optString("publishUserName"));
		title.setText(jsonObj.optString("title"));
		unit.setText(jsonObj.optString("publishUnitName"));
		time.setText(PayUtils.formatData("yyyy-MM-dd", jsonObj.optLong("publishDate", 0)));
		keepDays.setText(""+jsonObj.optInt("keepDays", 0));
		content.setText(jsonObj.optString("content"));
		 long start = jsonObj.optLong("publishDate");
         long end = jsonObj.optLong("endDate", 0);
         long current = new Date().getTime();
         if(current>start&&current<end){
        	 symbol.setVisibility(View.VISIBLE); 
         }else{
        	 symbol.setVisibility(View.GONE);
         }
		getDetailAttechment(jsonObj.optJSONArray("attachList"));
		appendAttachment();
		if(jsonObj.isNull("readedId"))
		requestMarker();
	}
	
	private void requestMarker() {
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				
				
			}
		}.setUrl(mContext.getString(R.string.announcement_markread_url))
		.addParam("id", jsonObj.optString("id"))
		.notifyRequest()
		;
		
	}
	private void appendAttachment()
    {
        if(mAccDatas==null || mAccDatas.size() == 0)
        {
        	mAttachmentLayout.setVisibility(View.GONE);
            return;
        }
        mAttachmentLayout.setVisibility(View.VISIBLE);//如果有附件就将附件的布局显示
        
        int pngCount = PictureHelper.getPictureCount(mAccDatas, new OnFirstPicureListener() {
			
			@Override
			public void beginLoadPic( ArrayList<String> fids) {
				final String fid = fids.get(0);
				pictureFids = fids;
				Bitmap bitmap = FileUtils.geInstance().getLittleImg(fid);
			
				if(bitmap != null){
					
					littlePhoto.setImageBitmap(bitmap);
					mPhotoLayout.setVisibility(View.VISIBLE);
				}
				else{
				PictureHelper.requestFirstPic(fid, new FirstPictureLoadListener() {
					@Override
					public void loadPic(Bitmap map) {
						if(map != null){
							FileUtils.geInstance().saveLittleBmp(map, fid);
							littlePhoto.setImageBitmap(map);
							mPhotoLayout.setVisibility(View.VISIBLE);
							}
						}
					});
				}
				}
			});
        
        if(pngCount > 0)
        {
        	mPhotoLayout.setVisibility(View.VISIBLE);
            mPhotoCountText.setText(String.valueOf(pngCount));
            mPhotoLayout.setOnClickListener(mPhotoListener);
        }

        if((mAccDatas.size() - pngCount) > 0)
        {
            mFileLayout.setVisibility(View.VISIBLE);
            mFileCountText.setText(String.valueOf(mAccDatas.size() - pngCount));
            mFileLayout.setOnClickListener(mFileListener);

        }
    }
	
	 private void getDetailAttechment(JSONArray jDataArray) {//获取附件
	        if (jDataArray == null) {
	            return;
	        }
	        mAccDatas = new ArrayList<AccessoryInfo>();
	        
	        for (int i = 0; i < jDataArray.length(); i++) {
	            JSONObject jrow = jDataArray.optJSONObject(i);
	            AccessoryInfo info = new AccessoryInfo();
	            info.setId(jrow.optString("id"));
	            info.setTitle(jrow.optString("title"));
	            info.setExt(jrow.optString("ext"));
	            info.setSize(jrow.optLong("size"));
	            info.setInfoId(jrow.optString("infoId"));
	            info.setFileId(jrow.optString("fileId"));
	            info.setUserCode(jrow.optString("userCode"));
	            info.setUserName(jrow.optString("userName"));
	          
	            mAccDatas.add(info);
	        }
	        
	    }
	
	 View.OnClickListener mPhotoListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            BorwsePicture_.intent(mContext)
	            .fileIds(pictureFids)
	            .mId(jsonObj.optString("id"))
	            .start();
	        }
	    };
	    View.OnClickListener mFileListener = new View.OnClickListener(){
	        @Override
	        public void onClick(View view){
	            Intent intent = new Intent(view.getContext(),AccessoryFileListActivity.class);
	            Bundle bundle = new Bundle();
	            bundle.putString("id",jsonObj.optString("id"));
	            intent.putExtra("bundle",bundle);

	            mContext.startActivity(intent);

	        }
	    };
}
