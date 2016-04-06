package com.miicaa.home.ui.photoGrid;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class MoreOptionsAdapter extends BaseAdapter{
	
	private static String TAG = "MoreOptionsAdapter";
	
	List<BucketData> bucketImageList;
	LayoutInflater inflater;
	DisplayImageOptions displayImageOptions;
	int mPosition;
	public MoreOptionsAdapter(Context context,List<BucketData> bucketDatas)  {
		inflater = LayoutInflater.from(context);
		bucketImageList = bucketDatas;
		initDisplayImageOptions();
	}
	
	public void refresh(List<BucketData> bucketDatas){
		bucketImageList.clear();
		bucketImageList.addAll(bucketDatas);
		this.notifyDataSetChanged();
	}
	
	public void refresh(List<BucketData> bucketDatas,int position){
		this.mPosition = position;
		refresh(bucketDatas);
	}
	
	private void initDisplayImageOptions(){
	   	 displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.accessory_file_ico_normal)
				.resetViewBeforeLoading(true)
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();
	   }
	
	@Override
	public int getCount() {
		return bucketImageList != null ? bucketImageList.size()+1 : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.picture_item_view, null);
		}
		
		ImageView imageView = ViewHolder.get(convertView, R.id.imageView);
		TextView nameText = ViewHolder.get(convertView, R.id.name);
		CheckBox checkBox = ViewHolder.get(convertView, R.id.checkBox);
		checkBox.setVisibility(mPosition >= 0 && mPosition == position ? View.VISIBLE : View.GONE);
		String name = null;
		ImageItem imageItem = null;
		if(position == 0){
			name = "全部";
//		    Log.d(TAG, "bucketImageList size:"+bucketImageList.size() 
//		    		+"imageList size:"+bucketImageList.get(position).imageList.size());
		    if(bucketImageList.get(position).imageList.size() > 0)
			imageItem =  bucketImageList.get(position).imageList.get(0);
		}else{
//			Log.d(TAG, "getView position:"+position+"bucketImageList size:"+bucketImageList.size());
//			Log.d(TAG,"imageList size:"+bucketImageList.get(position-1).imageList.size());
			if(bucketImageList.get(position-1).imageList.size() > 0)
			imageItem = bucketImageList.get(position-1).imageList.get(0);
			name = bucketImageList.get(position-1).name;
		}
		String imagePath = imageItem != null ? imageItem.image_path : "";
		imagePath = Scheme.FILE.wrap(imagePath);
		ImageLoader.getInstance().displayImage(imagePath, imageView,displayImageOptions);
		nameText.setText(name);
		return convertView;
	}

}
