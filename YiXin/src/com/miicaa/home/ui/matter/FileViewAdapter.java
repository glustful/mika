package com.miicaa.home.ui.matter;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.miicaa.common.base.PicturButton.OnPictureListener;
import com.miicaa.home.R;

public class FileViewAdapter extends BaseAdapter{
	
	static String TAG = "FileViewAdapter";
	
	Context context;
	DelpictureListener listener;
	OnPictureListener onPictureListener;
	HashMap<String, Bitmap> bitMaps;
	ArrayList<String> fids;
	
	ArrayList<MatterImgManager> imageManagerList;
	ArrayList<MatterImgManager> addImageManagerList;
	
	public  FileViewAdapter(Context context) {
		this.context = context;
		bitMaps = new HashMap<String, Bitmap>();
		fids = new ArrayList<String>();
		imageManagerList = new ArrayList<MatterImgManager>();
		addImageManagerList = new ArrayList<MatterImgManager>();
	} 
	
	public void refresh(HashMap<String,Bitmap> bitmaps,ArrayList<String> fids){
		this.bitMaps = bitmaps;
		this.fids = fids;
		this.notifyDataSetChanged();
	}
	
	public void refresh(ArrayList<MatterImgManager> list){
		this.imageManagerList = list;
		this.imageManagerList.addAll(addImageManagerList);
		Log.d(TAG, "imageManagerList.size():"+imageManagerList.size());
		this.notifyDataSetChanged();
	}
	
	public void add(ArrayList<MatterImgManager> list){
		addImageManagerList = list;
		this.imageManagerList.addAll(addImageManagerList);
		notifyDataSetChanged();
	}
	
	public void remove(MatterImgManager iamgeMatter){
		imageManagerList.remove(iamgeMatter);
		this.notifyDataSetChanged();
	}
		

	@Override
	public int getCount() {
//		return fids.size();
		return imageManagerList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = LayoutInflater.from(context).inflate(R.layout.file_view_adapter, null);
		
		convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.an_picture_bg));
		ImageView imageView = com.miicaa.utils.ViewHolder.get(convertView, R.id.bitmap);
		ImageView delBitmap = com.miicaa.utils.ViewHolder.get(convertView, R.id.delBitmap);
		delBitmap.setVisibility(View.VISIBLE);
//		Log.d(TAG, "imageManagerList.get(position):"+imageManagerList.get(position));
		if(position < imageManagerList.size()){
		final MatterImgManager imgManager = imageManagerList.get(position);
		final String fid = imgManager.fid;
		Bitmap bitMap = imgManager.bitmap;
		if(bitMap != null){
		imageView.setImageBitmap(bitMap);
		}
		delBitmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null)
					listener.del(imgManager);
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		}else{
			convertView.setBackgroundDrawable(null);
			delBitmap.setVisibility(View.GONE);
			 imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.add_picture_ico));
			 imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				    if(onPictureListener != null)
				    	onPictureListener.onAddPictureClick();
				}
			});
		}
		return convertView;
	}
	
	
	public interface DelpictureListener{
		void del(String fid);
		void del(MatterImgManager imageManager);
	}
	
	public void setDelPictureListener(DelpictureListener listener){
		this.listener = listener;
		
	}
	
	public void setOnPictureClickListener(OnPictureListener listener){
		this.onPictureListener = listener;
	}

}
