package com.miicaa.home.ui.photoGrid;


import java.util.ArrayList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.ui.photoGrid.PictureSelectView.SelectPictureChangeListener;
import com.miicaa.home.ui.picture.PictureShowAtivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@EActivity(R.layout.activity_photo_check)
public class SelectPictureActivity extends PictureShowAtivity{
	
	static String TAG = "SelectPictureActivity";
	
@ViewById(R.id.big_picture_back_button)
Button backButton;
@ViewById(R.id.title)
TextView titleText;
@ViewById(R.id.save_big_picture)	
Button saveButton;
@ViewById(R.id.photo_check_pager)
ViewPager viewPager;
@ViewById(R.id.selectView)
PictureSelectView selectView;
@ViewById(R.id.headView)
RelativeLayout headView;


@Extra
ArrayList<ImageItem> imageItems;
@Extra
ImageItem imageItem;
@Extra
Boolean isYulan;
@Extra
boolean noOperation;
@Extra
Boolean isSave;
@Extra
int mCount;

ArrayList<String> selectPath;

String[] bigPictureUrls;
int pageNum;
SeletPictureAdapter adapter;
DisplayImageOptions displayImageOptions;

	@Override
	protected void hiddenViews() {
		
	}

	@Override
	protected void okInOptions(DisplayImageOptions options) {
		displayImageOptions = options;
		Log.d(TAG, "displayimageOptions :"+displayImageOptions);
	}
	
	@AfterInject
	void afterInject(){
		selectPath = new ArrayList<String>();
		adapter = new SeletPictureAdapter();
		 getBigPictureUriFromFile();
		
	}
	
	@AfterViews
	void aferView(){
		selectView.setChecked(imageItem.isSelected);
		if(isYulan != null && isYulan){
			titleText.setText(1 + "/" + imageItems.size());
		}
		saveButton.setVisibility(View.VISIBLE);
		saveButton.setText("完成"+"("+mCount+")");
		saveButton.setEnabled(mCount > 0 ? true : false);
		selectView.setVisibility(View.VISIBLE);
		selectView.setSelectPictureChangeListener(new SelectPictureChangeListener() {
			
			@Override
			public void slectPictureChange(Boolean isCheck) {
			 if(isCheck){
				  if (Bimp.drr.size()  + mCount >= 9){
					  Toast.makeText(SelectPictureActivity.this, "您最多只能选择9张图片", Toast.LENGTH_SHORT).show();
					  selectView.setChecked(!isCheck);
					  return;
				  }
				  selectPath.add(imageItems.get(pageNum).image_path);
					  mCount++;
					  saveButton.setEnabled(true);
			 }else{
				 selectPath.remove(imageItems.get(pageNum).image_path);
				 mCount --;
				 if(mCount == 0){
					 saveButton.setEnabled(false);
				 }
			 }
				imageItems.get(pageNum).isSelected = isCheck;
				saveButton.setText("完成"+"("+mCount+")");
			}
		});
		if(noOperation){
			saveButton.setVisibility(View.INVISIBLE);
			selectView.setVisibility(View.INVISIBLE);
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {
                if(isYulan != null && isYulan){
                	titleText.setText(position+1 + "/" + imageItems.size());
                }
				Boolean isCheck = imageItems.get(position).isSelected;
				Log.d(TAG, "photo is checked :"+"at position ="+position+isCheck);
				selectView.setChecked(isCheck);
//			    if(isCheck)
//			    	selectPath.add(imageItems.get(position).image_path);
				pageNum = position;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setAdapter(adapter);
//		viewPager.setCurrentItem(pageNum);
		}
	
	@Click(R.id.big_picture_back_button)
	void backClick(){
		finish();
	}
	@Click(R.id.save_big_picture)
	void saveClick(View v){
		Intent data = new Intent();
		data.putExtra("data", imageItems);
		data.putExtra("select", selectPath);
		setResult(RESULT_OK,data);
		finish();
	}
	
	@Background
	  void getBigPictureUriFromFile(){
		ArrayList<String> paths = new ArrayList<String>();
		for(int i = 0 ; i < imageItems.size();i++){
			ImageItem imageItem = imageItems.get(i);
			paths.add(imageItem.image_path);
			if(imageItem.isSelected){
				selectPath.add(imageItem.image_path);
			}
			if(imageItem.image_path.equals(this.imageItem.image_path)){
				pageNum = i;
			}
		}
		if(imageItem == null){
			pageNum = 0;
		}
		System.out.println("pagenum="+pageNum);
		System.out.println("currentimag="+imageItem);
		bigPictureUrls =  getBigPictureUriFromFile(paths);
//		Log.d(TAG, "getBigPictureUriFromFile"+bigPictureUrls);
		refreshThis();
	}
	
	
	private String[] getBigPictureUriFromFile(ArrayList<String> paths){
		String[] bigPictureUrls = new String[paths.size()];
		for(int i = 0 ; i < paths.size(); i++){
			String path = paths.get(i);
			String uri = "";
	 if(path.startsWith(UserAccount.mSeverHost))
		 uri = path;
	 else{
	     uri = Scheme.FILE.wrap(path);
	 }
	 bigPictureUrls[i] = uri;
		}
		return bigPictureUrls;
	}
	
	@UiThread
	void refreshThis(){
		Log.d(TAG, "pageNum is :"+pageNum);
		if(adapter != null)
		adapter.refresh();
		viewPager.setCurrentItem(pageNum);
	}
	
	class SeletPictureAdapter extends PagerAdapter{
		LayoutInflater inflater;
		Context context;
		
		public SeletPictureAdapter(){
			context = SelectPictureActivity.this;
			inflater = LayoutInflater.from(context);
			ImageLoader.getInstance().clearDiskCache();
			ImageLoader.getInstance().clearMemoryCache();
		}
		
		public void refresh(){
			if(getCount() > 0)
			this.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return bigPictureUrls != null ? bigPictureUrls.length : 0 ;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			  return view == object;
		}
		
		 @Override
	        public Object instantiateItem(final ViewGroup container, final int position) {
	        	View imageLayout = inflater.inflate(R.layout.picture_bigimage_view, container, false);
				assert imageLayout != null;
				assert displayImageOptions != null;
				final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.pictureBigImageView);
				final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
//				Log.d(TAG, "imageLayout and displayImageOptions is not null, so"
//						+ "you can continue do this !"+bigPictureUrls[position]);
				ImageLoader.getInstance().displayImage(bigPictureUrls[position], imageView,displayImageOptions, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
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
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
	
	
	
}
