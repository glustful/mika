package com.miicaa.home.ui.picture;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.business.file.BusinessFileActivity;
import com.miicaa.home.ui.business.file.BusinessFileSearchActivity;
import com.miicaa.home.ui.photoGrid.PictureSelectView;
import com.miicaa.utils.fileselect.BrowseFileFootView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yxst.epic.yixin.utils.ImageUtils;

@EActivity(R.layout.activity_photo_check)
public class PhotoCheck extends PictureShowAtivity {

	
	static String TAG = "PhotoCheck";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK)
			return;
		switch(requestCode){
		case BusinessFileActivity.MOVETOFILE_CODE:
			//BusinessFileActivity.getIntance().isRefresh();
			if(BusinessFileSearchActivity.getIntance()==null)
			BusinessFileActivity.getIntance().setTmpParentId(data.getStringExtra("id"),data.getStringExtra("name"),jsonObjects.get(viewPager.getCurrentItem()));
			deleteItem();
			break;
		case BusinessFileActivity.EDITFILE_CODE:
			isRefresh = true;
			try {
				JSONObject obj = new JSONObject(data.getStringExtra("json")); 
				jsonObjects.set(viewPager.getCurrentItem(), obj);
				footView.setFileInfo(isManager, obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	static String tag = "PhotoCheck";
	boolean isRefresh = false;
	ArrayList<String> bigPictureUrls;
	
	@ViewById(R.id.photo_check_pager)
	ViewPager viewPager;
	@ViewById(R.id.save_big_picture)
	Button saveButton;
	@ViewById(R.id.big_picture_back_button)
	Button backButton;
	@ViewById(R.id.selectView)
	PictureSelectView pictureSlectView;
	@ViewById(R.id.footView)
	BrowseFileFootView footView;
	@ViewById(R.id.title)
	TextView title;
	@ViewById(R.id.headView)
	RelativeLayout headView;
	
	@Extra
	ArrayList<String> names;
	@Extra
	String name;
	
	PopupWindow headWindow;
	PopupWindow bottomWindow;
	
	MyPagerAdapter pagerAdapter;
	DisplayImageOptions displayImageOptions;
	int num;
	Context mContext;
	ProgressDialog mDialog;
	ArrayList<JSONObject> jsonObjects;
	boolean isFooterShow = false;
	boolean isManager = false;
	
	@AfterInject
	void afterInject(){
		mContext = this;
		jsonObjects = new ArrayList<JSONObject>();
		if(names.size()<1){
			isFooterShow = true;
			requestPic();
		}else{
			isFooterShow = false;
			pagerAdapter = new MyPagerAdapter();
			num = getNum();
			bigPictureUrls = getBigPictureUri(names);
		}
	}
	
	private void requestPic() {
		String url = this.getString(R.string.file_all_url);
		HashMap<String,String> map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
		mDialog = ProgressDialog.show(mContext, "Miicaa", "正在请求服务器数据");
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				if(mDialog != null)
					mDialog.dismiss();
				if(data.getResultState() == ResultState.eSuccess || data.getResponseStatus() == 200){
					JSONArray opt = data.getMRootData().optJSONArray("filelist");
					isManager = data.getMRootData().optBoolean(
							"isManager");
					if(opt != null && opt.length()>0){
						names.clear();
						for(int i=0;i<opt.length();i++){
							JSONObject obj = opt.optJSONObject(i);
							
							if(!obj.isNull("ext")&&ImageUtils.isImage(obj.optString("ext"))){
							names.add(obj.optString("fileId"));
							jsonObjects.add(obj);
							}
						}
						pagerAdapter = new MyPagerAdapter();
						num = getNum();
						bigPictureUrls = getBigPictureUri(names);
						viewPager.setAdapter(pagerAdapter);
						viewPager.setCurrentItem(num);
						title.setText((num+1)+"/"+names.size());
						if(isFooterShow){
							footView.setFileInfo(isManager,jsonObjects.get(num));
						}
					}else{
						Toast.makeText(mContext, "数据为空，刷新后重试", 100).show();
					}
				}else{
					Toast.makeText(mContext, "请求出错："+data.getMsg(), 100).show();
				}
				/**/
				
			}
			
			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}.addParam(map)
		.setUrl(url)
		.notifyRequest();
		
	}

	@AfterViews
	void afterViews(){
		 
		 displayImageOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheInMemory(false)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
//		 if(isFooterShow){
//			 footView.setVisibility(View.VISIBLE);
//		 }else{
//			 footView.setVisibility(View.GONE);
//		 }
		 if(names.size()>0){
			viewPager.setAdapter(pagerAdapter);
			viewPager.setCurrentItem(num);
			title.setText((num+1)+"/"+names.size());
			if(isFooterShow){
				footView.setFileInfo(isManager,jsonObjects.get(num));
			}
		 }
		 viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				title.setText((arg0+1)+"/"+names.size());
				if(isFooterShow){
					footView.setFileInfo(isManager,jsonObjects.get(arg0));
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initHeadWindow(){
		View headView = LayoutInflater.from(this).inflate(R.layout.layout_head_view, null);
		headWindow = new PopupWindow(headView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		headWindow.setFocusable(true);
		headWindow.setOutsideTouchable(true);
		
	}
	

	@Click(R.id.big_picture_back_button)
	void backButtonClick(){
		finish();
	}
	@Click(R.id.save_big_picture)
	void saveButtonClick(){
		
	}
	
	private int getNum(){
		for(int i = 0 ;i < names.size();i++){
			String nm = names.get(i);
			if(nm.equals(name)){
				return i;
			}
		}
		return 0;
	}
	
	
	private ArrayList<String> getBigPictureUri(ArrayList<String> fileIds){
		ArrayList<String> bigPictureUrls = new ArrayList<String>();
		String url = UserAccount.getSeverHost()+"/docbase_srv/staticfile/downLoad";
     	for(int i = 0;i <fileIds.size();i++){
     		String fileId = fileIds.get(i);
     	    String fileUrl = url + "?fid="+fileId;
     	    bigPictureUrls.add(fileUrl);
      	}
     	
     	return bigPictureUrls;
     }
	



    @Override
    public void finish(){
    	if(isRefresh){
    	BusinessFileActivity.getIntance().isRefresh();
    	if(BusinessFileSearchActivity.getIntance() != null)
    		BusinessFileSearchActivity.getIntance().isRefresh();
    	}
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

  class MyPagerAdapter extends PagerAdapter {
	    LayoutInflater inflater;
        public MyPagerAdapter(){
        	inflater = LayoutInflater.from(PhotoCheck.this);
        }
        public void refresh(ArrayList<String> str){
        	this.notifyDataSetChanged();
        }
        
        @Override
        public int getCount() {
            return names != null ? names.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
        	View imageLayout = inflater.inflate(R.layout.picture_bigimage_view, container, false);
			assert imageLayout != null;
			final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.pictureBigImageView);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
			ImageLoader.getInstance().displayImage(bigPictureUrls.get(position), imageView,displayImageOptions, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					Log.d(tag, "imageUri in photocheck is :"+imageUri);
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "图片加载失败，请稍后重试。";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(PhotoCheck.this, message, Toast.LENGTH_SHORT).show();
					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				
				}
			});

			container.addView(imageLayout, 0);
			return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        	container.removeView((View)object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
  }
  
  
  class SelectPagerAdapter extends MyPagerAdapter{
	  PictureSelectView selectView;
	  public  SelectPagerAdapter() {
	}
	  public SelectPagerAdapter(PictureSelectView selectView){
		  this.selectView = selectView;
	  }
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		assert selectView != null;
		
		return super.instantiateItem(container, position);
	}
	  
  }
  
  public void deleteItem(){
	  isRefresh = true;
	  int index = viewPager.getCurrentItem();
	  names.remove(index);
	  jsonObjects.remove(index);
	  bigPictureUrls.remove(index);
	  if(names.size()<1){
		  finish();
	  }
	  pagerAdapter.notifyDataSetChanged();
	  footView.setFileInfo(isManager, jsonObjects.get(viewPager.getCurrentItem()));
	  title.setText((viewPager.getCurrentItem()+1)+"/"+names.size());
  }

Boolean bUseFullscreen = true;
@Override
protected void hiddenViews() {
	if(isManager){
	/*隐藏标题栏*/
	 /*if(bUseFullscreen) 
	   { 
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
	    } 
	    else 
	    { 
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	    } */
	 bUseFullscreen = !bUseFullscreen;
	 
	if(headView.isShown()){
//		headView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.headorfoot_gone_view));
		headView.setVisibility(View.GONE);
	}else if(!headView.isShown()){
//		headView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.headorfoot_view_visiable_amin));
		headView.setVisibility(View.VISIBLE);
	}
	if(isFooterShow){
		if(footView.isShown()){
//		footView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.headorfoot_view_visiable_amin));
	footView.setVisibility(View.GONE);
		}else{
//			footView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.headorfoot_gone_view));
			footView.setVisibility(View.VISIBLE);
		}
	}
	}
}
@Override
protected void okInOptions(DisplayImageOptions options) {
	
}

}
