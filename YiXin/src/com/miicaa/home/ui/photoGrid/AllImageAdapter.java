//package com.miicaa.home.ui.photoGrid;
//
//import java.util.List;
//
//import com.miicaa.home.R;
//import com.miicaa.utils.ViewHolder;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class AllImageAdapter extends BaseAdapter{
//
//	DisplayImageOptions displayImageOptions ;
//	List<ImageItem> imageItems;
//	LayoutInflater inflater;
//	int mCount = 0;
//	
//	 public AllImageAdapter(Context context,List<ImageItem> imageItems) {
//		 inflater = LayoutInflater.from(context);
//		 this.imageItems = imageItems;
//		 initDisplayImageOptions();
//	}
//	
//	private void initDisplayImageOptions(){
//   	 displayImageOptions = new DisplayImageOptions.Builder()
//			.showImageForEmptyUri(R.drawable.ic_empty)
//			.showImageOnFail(R.drawable.ic_error)
//			.resetViewBeforeLoading(true)
//			.cacheInMemory(false)
//			.cacheOnDisk(true)
//			.imageScaleType(ImageScaleType.EXACTLY)
//			.bitmapConfig(Bitmap.Config.RGB_565)
//			.considerExifParams(true)
//			.displayer(new FadeInBitmapDisplayer(300))
//			.build();
//   }
//	
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public Object getItem(int arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//	        if (convertView == null){
//	            convertView = inflater.inflate(R.layout.photo_gird_list_view,null);
//	            
//	        }
//	        final CheckBox checkView = ViewHolder.get(convertView, R.id.photo_grid_list_checkview);
//            ImageView imageView = ViewHolder.get(convertView, R.id.photo_grid_list_image);
//            TextView countView = ViewHolder.get(convertView, R.id.photo_grid_list_imgCount);
//            TextView nameView = ViewHolder.get(convertView, R.id.photo_grid_list_imgName);
//	        countView.setVisibility(View.GONE);
//	        nameView.setVisibility(View.GONE);
//	        final ImageItem imageItem = imageItems.get(position);
//	        final String thumbnailPath = imageItem.thumbnailPath;
//	        final String imagePath = imageItem.image_path;
//	        String lastPath = thumbnailPath != null ? thumbnailPath : imagePath;
//	        if(lastPath != null){
//	        	
//	        	lastPath  = Scheme.FILE.wrap(lastPath);
//	        	ImageLoader.getInstance().displayImage(lastPath, imageView);
//	        }
//	        if (imageItem.isSelected){
//	            checkView.setChecked(true);
//	        }else{
//	            checkView.setChecked(false);
//	        }
//	        checkView.setOnClickListener(new View.OnClickListener() {
//	            @Override
//	            public void onClick(View view) {
//	                if (Bimp.drr.size() +Bimp.waiMap.size() + mCount < 9){
//	                    imageItem.isSelected = !imageItem.isSelected;
//	                    if (imageItem.isSelected){
//	                        mCount++;
//	                        checkView.setChecked(true);
//	                        onImgChangeListener.contentImgCallback(mCount);
//	                        imgMap.put(imagePath,imagePath);
//	                    }else {
//	                        mCount --;
//	                        holder.checkView.setChecked(false);
//	                        onImgChangeListener.contentImgCallback(mCount);
//	                        imgMap.remove(imagePath);
//	                    }
//	                }else if (Bimp.drr.size()+Bimp.waiMap.size() + mCount >= 9){
//	                    imageItem.isSelected = !imageItem.isSelected;
//	                    if (imageItem.isSelected){
//	                        imageItem.isSelected = !imageItem.isSelected;
//	                        Message msg = Message.obtain(mHandler,1);
//	                        msg.sendToTarget();
//	                    }else {
//	                        mCount--;
//	                        holder.checkView.setChecked(false);
//	                        imgMap.remove(imagePath);
//	                        onImgChangeListener.contentImgCallback(mCount);
//	                    }
//	                }
//	            }
//	        });
//
//	        return view;
//	}
//
//}
