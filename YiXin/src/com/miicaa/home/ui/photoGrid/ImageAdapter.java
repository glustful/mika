package com.miicaa.home.ui.photoGrid;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by LM on 14-8-12.
 */
public class ImageAdapter extends BaseAdapter {
	
	private DisplayImageOptions displayImageOptions;
    Context mContext;
    List<BucketData> mBuckList;
    String TAG = getClass().getSimpleName();
    public ImageAdapter(Context context,List<BucketData> buckList){
    	initDisplayImageOptions();
        mContext = context;
        mBuckList = buckList;
    }
    
    private void initDisplayImageOptions(){
   	 displayImageOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheInMemory(false)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
   }
    
    BitMapCache.Callback callback = new BitMapCache.Callback() {
        @Override
        public void loadBitMap(ImageView imageView, Bitmap bitmap) {
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    };
    @Override
    public int getCount() {
        return mBuckList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.photo_gird_list_view,null);
            holder.imgView = (ImageView)view.findViewById(R.id.photo_grid_list_image);
            holder.countView = (TextView)view.findViewById(R.id.photo_grid_list_imgCount);
            holder.nameView  = (TextView)view.findViewById(R.id.photo_grid_list_imgName);
            holder.checkBox = (CheckBox)view.findViewById(R.id.photo_grid_list_checkview);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        holder.checkBox.setVisibility(View.GONE);
        holder.countView.setText("("+mBuckList.get(i).count+")");
        holder.nameView.setText(mBuckList.get(i).name);
        BitMapCache mapCache = new BitMapCache();
        ImageItem imageItem = mBuckList.get(i).imageList.get(0);
        if (imageItem != null) {
            String thumbnailPath = imageItem.thumbnailPath;
            String imagePath = imageItem.image_path;
            String lastPath = thumbnailPath != null ? thumbnailPath : imagePath;
            if(lastPath != null){
            	lastPath = Scheme.FILE.wrap(lastPath);
            	ImageLoader.getInstance().displayImage(lastPath, holder.imgView, displayImageOptions, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						
					}
				}, new ImageLoadingProgressListener() {
					
					@Override
					public void onProgressUpdate(String imageUri, View view, int current,
							int total) {
						
					}
				});
            }
//            if (imagePath != null) {
//                mapCache.setBitMap(holder.imgView, thumbnailPath, imagePath, callback);
//            }
        }else{
//            holder.imgView.setImageBitmap(null);
        }
        return view;
    }

    class ViewHolder{
        public ImageView imgView;
        public TextView  nameView;
        public TextView countView;
        public CheckBox checkBox;
    }
}
