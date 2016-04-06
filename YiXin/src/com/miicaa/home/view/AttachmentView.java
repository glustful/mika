package com.miicaa.home.view;

import java.util.ArrayList;
import java.util.List;

import com.miicaa.home.R;
import com.miicaa.home.attachment.AttachmentItem;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.common.accessory.AccessoryFileListActivity;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PhotoCheck_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.attachment_s_view)
public class AttachmentView extends LinearLayout{
	
	DisplayImageOptions displayImageOptions;
	Context context;

	public AttachmentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}
	
	

	public AttachmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}



	public AttachmentView(Context context) {
		super(context);
		this.context = context;
	}
	
	@ViewById(R.id.picLayout)
	RelativeLayout picLayout;
	@ViewById(R.id.picImageView)
    ImageView picImageView;
	@ViewById(R.id.picTextView)
	TextView picTextView;
	@ViewById(R.id.fileLayout)
	RelativeLayout fileLayout;
	@ViewById(R.id.fileImageView)
	ImageView fileImageView;
	@ViewById(R.id.fileTextView)
	TextView fileTextView;
	
	private static final String[] bmpBiamingdan = new String[]{
		"bmp","jpg","jpeg","png","gif"
	};
	
	@AfterViews
	void aftetView(){
		attachmentItems = new ArrayList<AttachmentItem>();
		displayImageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	List<AttachmentItem> attachmentItems;
	String mDataId;
	public AttachmentView setShow(List<AttachmentItem> itemList,String dataId){
		this.attachmentItems = itemList;
		this.mDataId = dataId;
		if(attachmentItems == null || dataId == null){
			try {
				throw new Exception("附件id不能为空 或dataId不能为空");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final ArrayList<String> pictureFileId = getPictureFileId();
		if(pictureFileId.size() > 0){
			picLayout.setVisibility(View.VISIBLE);
			String url = context.getString(R.string.little_file_download, UserAccount.mSeverHost,pictureFileId.get(0));
			ImageLoader.getInstance()
			.displayImage(url, picImageView);
			picTextView.setText(pictureFileId.size()+"");
			picLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					BorwsePicture_.intent(context)
//					.fileIds(pictureFileId)
//					.mId(mDataId)
//					.start();
					 PhotoCheck_.intent(context).name(pictureFileId.get(0))
				    	.names(pictureFileId)
				    	.start();
					
				}
			});
		}
		if(attachmentItems.size() > 0){
			fileLayout.setVisibility(View.VISIBLE);
			fileTextView.setText(attachmentItems.size()+"");
			fileLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,AccessoryFileListActivity.class);
		            Bundle bundle = new Bundle();
		            bundle.putString("id",mDataId);
		            intent.putExtra("bundle",bundle);
//		            ArrangementDetailActivity.mSelf.startActivity(intent);
		            context.startActivity(intent);
				}
			});
		}
		return this;
	}
	
	private  ArrayList<String> getPictureFileId(){
		ArrayList<String> bmpFileId = new ArrayList<String>();
		if(attachmentItems == null){
			return null;
		}
		ArrayList<AttachmentItem> atItemList = new ArrayList<AttachmentItem>();
		atItemList.addAll(attachmentItems);
		for(AttachmentItem item : attachmentItems){
			for(int i = 0 ; i < bmpBiamingdan.length; i++){
				if(bmpBiamingdan[i].equals(item.fileExt)){
					bmpFileId.add(item.fileId);
					atItemList.remove(item);
				}
			}
		}
		attachmentItems = atItemList;
		return bmpFileId;
	}
	
	
	private static List<String> getPictureFileId(List<AttachmentItem> itemList){
		List<String> bmpFileId = new ArrayList<String>();
		if(itemList == null){
			return null;
		}
		for(AttachmentItem item : itemList){
			for(int i = 0 ; i < bmpBiamingdan.length; i++){
				if(bmpBiamingdan[i].equals(item.fileExt)){
					bmpFileId.add(item.fileId);
				}
			}
		}
		return bmpFileId;
	}
    
	
	
}
