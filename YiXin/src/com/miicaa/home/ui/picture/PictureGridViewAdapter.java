package com.miicaa.home.ui.picture;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miicaa.home.R;
import com.miicaa.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class PictureGridViewAdapter extends BaseAdapter{
	
	static String TAG = "PictureGridViewAdapter";
	
	DisplayImageOptions displayImageOptions;
	String[] fileUrls;
	Context context;
	
	public PictureGridViewAdapter(Context context){
		this.context = context;
	}
	
	public void refresh(String[] fileUrls,DisplayImageOptions displayImageOptions){
		this.fileUrls = fileUrls;
		this.displayImageOptions = displayImageOptions;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return fileUrls !=null ? fileUrls.length : 0;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.picture_image_view,null);
		}
		Log.d(TAG, "imageloader's url is:"+fileUrls[position]);
		ImageView imageView = ViewHolder.get(convertView, R.id.pictureImageView);
		final ProgressBar progressBar = ViewHolder.get(convertView, R.id.progressBar);
		ImageLoader.getInstance()
		.displayImage(fileUrls[position], imageView,displayImageOptions,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				progressBar.setProgress(0);
				progressBar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason failReason) {
				String message = null;
				progressBar.setVisibility(View.GONE);
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
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
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
				// TODO 
				Log.d(TAG, "imageUri is : ...."+imageUri);
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
			}
		},new ImageLoadingProgressListener() {
			
			@Override
			public void onProgressUpdate(String arg0, View arg1, int current, int total) {
				progressBar.setProgress(Math.round(100.0f * current / total));
			}
		});
		return convertView;
	}
	
	OnPictureLoadingCompleteListener onPictureLoadingCompleteListener;
	public interface OnPictureLoadingCompleteListener{
		void pictureLoadingComplete(String imageUri,Bitmap bitmap);
	}

}
