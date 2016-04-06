package com.miicaa.home.ui.picture;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import com.miicaa.home.R;
import com.miicaa.home.data.old.UserAccount;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by LM on 14-4-4.
 */
@EActivity(R.layout.picture_group_view)
public class BorwsePicture extends Activity {
    PictureGridViewAdapter pictureGridViewAdapter;
    DisplayImageOptions displayImageOptions;
    String[] bigPictureUrls;
    @ViewById(R.id.pictureGrid)
    GridView pictureGridView;
    @ViewById(R.id.picture_group_view_back_button)
    Button backButton;
    
    @Extra
    String mId;
    @Extra
    ArrayList<String> fileIds;
    
    @AfterInject
    void AfterInject(){
    	String url = "/docbase_srv/staticfile/resize/downLoad";
    	setLittlePictureUri(url, fileIds);
    	pictureGridViewAdapter = new PictureGridViewAdapter(this);
    }
    
    @AfterViews
    void afterViews(){
    	displayImageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(false)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
//    	helper.getAttachement();
    	pictureGridView.setAdapter(pictureGridViewAdapter);
    	}
    
    
    @Click(R.id.picture_group_view_back_button)
    void  backButtonClick(){
    	finish();
    	
    }
    
    @ItemClick(R.id.pictureGrid)
    void pictureGridViewClick(int position){
    	 PhotoCheck_.intent(this).name(fileIds.get(position))
    	.names(fileIds)
    	.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

        @Override
        public void finish(){
            super.finish();
            overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
        }
        
        String[] fileUrls;
        void setLittlePictureUri(String url,ArrayList<String> fileIds){
        	fileUrls = new String[fileIds.size()];
        	for(int i = 0;i <fileIds.size();i++){
        		String fileId = fileIds.get(i);
        	    String fileUrl = url + "?fid="+fileId;
        	    fileUrl =  UserAccount.getSeverHost()+fileUrl;
        		fileUrls[i] = fileUrl ;
         	}
        	refreshGridView(fileUrls);
        }
        
        @UiThread
        void refreshGridView(String[] fileUrls){
        	pictureGridViewAdapter.refresh(fileUrls, displayImageOptions);
        }
}



 