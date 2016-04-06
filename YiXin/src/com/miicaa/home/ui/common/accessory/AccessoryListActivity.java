package com.miicaa.home.ui.common.accessory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miicaa.common.base.Tools;
import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.picture.BorwsePicture_;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.miicaa.utils.FileUtils;

/**
 * Created by apple on 13-12-30.
 */
public class AccessoryListActivity extends Activity {
	
	static String TAG = "AccessoryListActivity";
	
    private Context mContext;
    private AccessoryData accessoryData;
    private LinearLayout contentLayout;
    private String mId;
    private ResponseData mData;
    private ArrayList<AccessoryInfo> pngInfos;
    private ArrayList<AccessoryInfo> fileInfos;
    
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessory_list_activity);
        contentLayout = (LinearLayout) findViewById(R.id.accessory_list_activity_content_layout);
        mContext = AccessoryListActivity.this;
        Intent intent = getIntent();
        pngInfos = (ArrayList<AccessoryInfo>) intent.getSerializableExtra("png");
        fileInfos = (ArrayList<AccessoryInfo>)intent.getSerializableExtra("file");
        Log.d(TAG, "pngInfo + fileInfo ----" + pngInfos.size() +"---"+ fileInfos.size());
        Bundle bundle = intent.getBundleExtra("bundle");
        mId = bundle.getString("dataId");
        loadView();
        Button returnButton = (Button) findViewById(R.id.accessory_list_activity_back_button);
        returnButton.setOnClickListener(returnButtonClick);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

  

    private void loadView() {
       accessoryData = new AccessoryData();
       loadAccessory(true, pngInfos);
       loadAccessory(false, fileInfos);
       viewCreate();

    }
    
    void loadAccessory(Boolean isPng ,ArrayList<AccessoryInfo> infos){
    	assert isPng !=null;
    	
    	if(infos == null)
   		 return;
    	
    	 for(AccessoryInfo i : infos){
    		 Log.d(TAG, "accressoryInfo"+i.getExt() + "----"+i.getFileId()+"-----------"+i.getUserCode());
    	 PersonAccessory pData = accessoryData.getData().get(i.getUserCode());
    	 if (pData == null) {
             pData = new PersonAccessory();
             pData.setUserCode(i.getUserCode());
             pData.setUserName(i.getUserName());
             accessoryData.getData().put(i.getUserCode(), pData);
         }
       SingleDataAccessory singleDataAccessory = pData.getAccessory().get(i.getInfoId());
       if (singleDataAccessory == null) {
           singleDataAccessory = new SingleDataAccessory();
           Long uploadTime = i.getUpLoadTime() != null  ? i.getUpLoadTime() : 0;
           if (uploadTime > 0) {
               singleDataAccessory.setUploadTime(new Date(uploadTime));
           }
           pData.getAccessory().put(i.getInfoId(), singleDataAccessory);
       }
       if(isPng)
    	   singleDataAccessory.getImageArrayList().add(i);
    	   else
    		   singleDataAccessory.getFileArrayList().add(i);
    		 
    	 }
    }
    
    private ArrayList<String> getPngIds(SingleDataAccessory singleDataAccessory){
    	ArrayList<String> fileIds = new ArrayList<String>();
    	for(AccessoryInfo data : singleDataAccessory.imageArrayList){
    		fileIds.add(data.getFileId());
    	}
    	return fileIds;
    }
   
    

    private void viewCreate() {
        contentLayout.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Map.Entry<String,PersonAccessory> u : accessoryData.getData().entrySet()) {
            PersonAccessory personAccessory = u.getValue();
            String uCode = personAccessory.getUserCode();
            View itemView = layoutInflater.inflate(R.layout.accessory_list_cell, null);
            ImageView headView = (ImageView) itemView.findViewById(R.id.accessory_list_cell_head_image);
            Tools.setHeadImg(uCode, headView);
            TextView nameTextView = (TextView) itemView.findViewById(R.id.accessory_list_cell_name_textview);
            if(personAccessory.getUserName()!=null && !"".equals(personAccessory.getUserName().trim())) {
                nameTextView.setText(personAccessory.getUserName());
            }
           
            LinearLayout subContentLayout = (LinearLayout) itemView.findViewById(R.id.accessory_list_cell_content_layout);
            for (final String infoId : personAccessory.getAccessory().keySet()) {
                final SingleDataAccessory singleDataAccessory = personAccessory.getAccessory().get(infoId);
                List<AccessoryInfo> imageArrayList = singleDataAccessory.getImageArrayList();
                List<AccessoryInfo> fileArrayList = singleDataAccessory.getFileArrayList();
                View subItemView = layoutInflater.inflate(R.layout.accessory_list_sub_cell, null);
                TextView sendTimeTextView = (TextView) subItemView.findViewById(R.id.accessory_list_sub_cell_time_textview);
                if (singleDataAccessory.getUploadTime() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sendTimeTextView.setText(dateFormat.format(singleDataAccessory.getUploadTime()));
                }
                RelativeLayout imageLayout = (RelativeLayout) subItemView.findViewById(R.id.accessory_list_sub_cell_image_layout);
                RelativeLayout fileLayout = (RelativeLayout) subItemView.findViewById(R.id.accessory_list_sub_cell_file_layout);

                TextView imageCountTextView = (TextView) subItemView.findViewById(R.id.accessory_list_sub_cell_image_textview);
                imageCountTextView.setText(String.valueOf(imageArrayList.size()));
                TextView fileCountTextView = (TextView) subItemView.findViewById(R.id.accessory_list_sub_cell_file_textview);
                fileCountTextView.setText(String.valueOf(fileArrayList.size()));
                final ImageView imageButton = (ImageView) subItemView.findViewById(R.id.accessory_list_sub_cell_image_button);
                final String fid = singleDataAccessory.imageArrayList.size() > 0 ? 
                		singleDataAccessory.imageArrayList.get(0).getFileId()
                		:null;
                if(fid != null){
                Bitmap bitmap = FileUtils.geInstance().getLittleImg(fid);
				Log.d(TAG, "out of the bitmap is "+bitmap);
				if(bitmap != null){
					Log.d(TAG, "bitmap is"+ bitmap);
					imageButton.setImageBitmap(bitmap);
				}
                }
				else{
				PictureHelper.requestFirstPic(fid, new FirstPictureLoadListener() {
					@Override
					public void loadPic(Bitmap map) {
						if(map != null){
							FileUtils.geInstance().saveLittleBmp(map, fid);
							imageButton.setImageBitmap(map);
							}
						}
					});
				}
                View.OnClickListener imageButtonClick = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BorwsePicture_.intent(AccessoryListActivity.this)
                        .fileIds(getPngIds(singleDataAccessory))
                        .mId(mId)
                        .start();
                        overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
                    }
                };
                imageButton.setOnClickListener(imageButtonClick);
                ImageView fileButton = (ImageView) subItemView.findViewById(R.id.accessory_list_sub_cell_file_button);
                View.OnClickListener fileButtonClick = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, AccessoryFileListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", infoId);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
                    }
                };
                fileButton.setOnClickListener(fileButtonClick);
                if (imageArrayList.size() < 1) {
                    imageLayout.setVisibility(View.GONE);
                }
                if (fileArrayList.size() < 1) {
                    fileLayout.setVisibility(View.GONE);
                }

                subContentLayout.addView(subItemView);
            }
            contentLayout.addView(itemView);
        }

    }

    View.OnClickListener returnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    
    
    private ArrayList<String> getPngIds(ArrayList<AccessoryInfo> infos){
    	ArrayList<String> ids = new ArrayList<String>();
    	if(infos == null){
    		return ids;
    	}
    	for(AccessoryInfo info : infos){
    		ids.add(info.getFileId());
    	}
    	return ids;
    }
}
