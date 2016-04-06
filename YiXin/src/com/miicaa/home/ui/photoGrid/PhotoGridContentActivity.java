package com.miicaa.home.ui.photoGrid;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.ui.business.file.UploadPicActivity;
import com.miicaa.home.ui.photoGrid.ContentDataHelper.OnLoadOverImageListener;

/**
 * Created by LM on 14-8-13.
 */
public class PhotoGridContentActivity extends Activity {
	
	public final static int SELECT_PICTURE = 0X10;
	public final static int TAKE_PHOTO = 0X11;
	
	public final static int allowNormalUser = 8;
	public final static int allowPayForUser = 20;
	static String tag = "PhotoGridContentActivity";
	
	Toast mToast;
    GridView mGridView;
    Button completeButton;
    ArrayList<ImageItem> imageList;
    ContentImageAdapter imgAdapter;
    MoreOptionsAdapter moreOptionsAdapter;
    TextView backView;
    TextView headTitle;
    TextView cancleView;
    Button morePictrueButton;
    Button yulanButton;
    RelativeLayout selectLayout;
    LinearLayout selectPicLayout;
    ContentDataHelper helper;
    List<BucketData> buckDataList;
    int mBitMapCount;
    int state;
    String photoPath;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                	mToast.setText("您最多只能选择9张图片");
                	mToast.show();
                    break;
                case 2:
                	mToast.setText("您是非付费用户，只能上传小于"+allowNormalUser+"MB的图片");
                	mToast.show();
                	break;
                case 3:
                	mToast.setText("最大上传限制为："+allowPayForUser+"MB");
                	mToast.show();
                	break;
                case 4:
                	completeButton.setEnabled(true);
                	break;
                case 5:
                	completeButton.setEnabled(false);
                	break;
                default:
                    break;
            }
        }
    };
    
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mBitMapCount = getIntent().getIntExtra("bitmapCount", 0);
        dialog = new ProgressDialog(this);
        dialog.show();
        buckDataList = new ArrayList<BucketData>();
        imageList = new ArrayList<ImageItem>();
        helper = new ContentDataHelper();
        helper.init(this);
        helper.setOnLoadOverImageListener(new OnLoadOverImageListener() {
			
			@Override
			public void loadComplete(List<BucketData> bucketDatas) {
				buckDataList = bucketDatas;
				initAllImages(buckDataList);
				if(imgAdapter != null){
					Collections.sort(imageList, new UpdateTimeComparator());
					imgAdapter.refresh(imageList, ALL,mBitMapCount);
				}if(moreOptionsAdapter != null){
					moreOptionsAdapter.refresh(buckDataList);
				}
				dialog.dismiss();
			}
		});
        helper.getBucketImageList(true);
        initAllImages(buckDataList);
        setContentView(R.layout.photo_grid_activity);
        headTitle = (TextView)findViewById(R.id.photo_grid_title);
        mGridView = (GridView)findViewById(R.id.photo_grid_view_layout);
        completeButton = (Button)findViewById(R.id.photo_grid_cancle);
        RelativeLayout.LayoutParams p = (LayoutParams) completeButton.getLayoutParams();
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        completeButton.setLayoutParams(p);
        completeButton.setText("完成");
        completeButton.setEnabled(false);
        morePictrueButton = (Button)findViewById(R.id.moreOptionBtn);
        morePictrueButton.setOnClickListener(morePictureClickListener);
        yulanButton = (Button)findViewById(R.id.yulanBtn);
        backView = (TextView)findViewById(R.id.photo_grid_back);
        backView.setVisibility(View.VISIBLE);
        yulanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<ImageItem> items = getYulanPic();
				if(items.size() == 0){
					return;
				}
				SelectPictureActivity_.intent(PhotoGridContentActivity.this)
				.imageItem(items.get(0))
				.imageItems(items)
				.mCount(items.size())
				.isYulan(true)
				.startForResult(SELECT_PICTURE);;
			}
		});
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        completeButton.setVisibility(View.VISIBLE);
        moreOptionsAdapter = new MoreOptionsAdapter(this, buckDataList);
        imgAdapter = new ContentImageAdapter(PhotoGridContentActivity.this,imageList,handler,ALL);
        imgAdapter.setImgChangeListener(new ContentImageAdapter.OnImgChangeListener() {
            @Override
            public void contentImgCallback(int count) {
            	if(count > 0)
            	{
                completeButton.setText("完成"+"("+count+")");
                yulanButton.setText("预览"+"("+count+")");
            	}else{
            		completeButton.setText("完成");
                    yulanButton.setText("预览");
            	}
            }
        });
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               completeSelect();
            }
        });
        
        
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	Log.d(tag, "onItemClick position :" + i);
            	
            	if(i == 0 && imgAdapter.state == ALL){
            		 Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            		 photoPath = Environment.getExternalStorageDirectory() + "/miicaa/cache/photoCache/";
                     File mPhotoDir = new File(photoPath);
                     if(!mPhotoDir.exists())
                     mPhotoDir.mkdir();
                     photoPath += getPhotoFileName();
                     File photoFile = new File(photoPath);
                     Uri saveUri = Uri.fromFile(photoFile);
                     Log.d(tag, " photoPath uri is :"+saveUri);
                     intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
                     startActivityForResult(intent, TAKE_PHOTO);
            	}else{
            		if(ALL == imgAdapter.state){
            			i = i - 1;
            		}
            		System.out.println("onitemclick="+imageList.get(i));
            		SelectPictureActivity_.intent(PhotoGridContentActivity.this)
            		.mCount(imgAdapter.getSelectCount())
            		.imageItem(imageList.get(i))
            		.imageItems(imageList)
            		.isSave(false)
            		.startForResult(SELECT_PICTURE);
            	}
            }
        });
        mGridView.setAdapter(imgAdapter);
        initPopwindow();
    }
    
    private void initAllImages(List<BucketData> bucketDatas){
    	for(int i = 0 ; i < bucketDatas.size();i++){
    		imageList.addAll(bucketDatas.get(i).imageList);
    	}
    }
    
    private PopupWindow moreOptionWindow;
    LayoutInflater inflater;
    View windowView;
    private void initPopwindow(){
    	inflater = LayoutInflater.from(this);
    	windowView = inflater.inflate(R.layout.photo_grid_list,null);
    	moreOptionWindow = new PopupWindow(windowView,android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	moreOptionWindow.setAnimationStyle(R.style.MorePictureAminStyle);
    	moreOptionWindow.setBackgroundDrawable(new BitmapDrawable());
    	moreOptionWindow.setFocusable(true);
    	moreOptionWindow.setOutsideTouchable(true);
    	int pingmuHeight = getWindowManager().getDefaultDisplay().getHeight();
    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
    			pingmuHeight/2);
    	ListView listView = (ListView)windowView.findViewById(R.id.listView);
    	listView.setLayoutParams(layoutParams);
    	listView.setAdapter(moreOptionsAdapter);
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int i,
					long l) {
				if(i == 0){
					imageList.clear();
					initAllImages(buckDataList);
				    Collections.sort(imageList, new UpdateTimeComparator());
					imgAdapter.refresh(imageList, ALL,mBitMapCount);
					headTitle.setText("全部");
					morePictrueButton.setText("全部");
				}else{
				imageList.clear();
				imageList.addAll(buckDataList.get(i-1).imageList);
				imgAdapter.refresh(imageList, SIMPLE,mBitMapCount);
				headTitle.setText(buckDataList.get(i-1).name);
				morePictrueButton.setText(buckDataList.get(i-1).name);
				}
				moreOptionsAdapter.refresh(buckDataList, i);
				moreOptionWindow.dismiss();
			}
    		
	});
    }
    
        
        OnClickListener morePictureClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(buckDataList.size() == 0){
				return;
			}
			if(!moreOptionWindow.isShowing()){
				int height = morePictrueButton.getMeasuredHeight();
				moreOptionWindow.showAtLocation(morePictrueButton, Gravity.LEFT|Gravity.BOTTOM, 0, height);
			  }else{
				  moreOptionWindow.dismiss();
			  }
			}
		};
		
		
		ArrayList<ImageItem> getYulanPic(){
			ArrayList<ImageItem> itmes = new ArrayList<ImageItem>();
			for(int i = 0 ; i < imageList.size(); i ++){
				ImageItem imageItem = imageList.get(i);
				if(imageItem.isSelected){
					itmes.add(imageItem);
				}
			}
			return itmes;
		}
		
       private void completeSelect(){
    	   for (Map.Entry<String, String> imgPath : imgAdapter.imgMap.entrySet()) {
               Bimp.drr.add(imgPath.getValue());
           }
           setResult(RESULT_OK);
           if(getIntent().getBooleanExtra("upload", false)){
           	Intent intent = new Intent(PhotoGridContentActivity.this,UploadPicActivity.class);
           	startActivity(intent);
           }
           finish();
        }
       private void completeSelect(ArrayList<String> paths){
    	   for (String path: paths) {
               Bimp.drr.add(path);
           }
           setResult(RESULT_OK);
           if(getIntent().getBooleanExtra("upload", false)){
           	Intent intent = new Intent(PhotoGridContentActivity.this,UploadPicActivity.class);
           	startActivity(intent);
           }
           finish();
        }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case SELECT_PICTURE:
				@SuppressWarnings("unchecked")
				ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) data.getSerializableExtra("data");
				assert imageItems != null;
				imageList = imageItems;
				ArrayList<String> imagePaths = data.getStringArrayListExtra("select");
				completeSelect(imagePaths);
//				imgAdapter.refresh(imageItems,imgAdapter.state);
				break;
			case TAKE_PHOTO:
				Bimp.drr.add(photoPath);
				setResult(RESULT_OK);
				if(getIntent().getBooleanExtra("upload", false)){
                	Intent intent = new Intent(PhotoGridContentActivity.this,UploadPicActivity.class);
                	startActivity(intent);
                }
				finish();
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";

    }
	
	public  static int ALL = 0X11;
	public  static int SIMPLE = 0x22; 
    
	
	   /*照片排序规则*/
    private class UpdateTimeComparator implements Comparator<ImageItem>{

		@Override
		public int compare(ImageItem lhs, ImageItem rhs) {
			return lhs.updateTime < rhs.updateTime ? 1 : 0;
		}
    	
    }


	@Override
	public void finish() {
		super.finish();
	}
    
    



}
