package com.miicaa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.miicaa.home.ui.photoGrid.Bimp;
import com.miicaa.home.ui.picture.PictureHelper;
import com.miicaa.home.ui.picture.PictureHelper.FirstPictureLoadListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class BitmapCacheUtils {
	static BitmapCacheUtils instance;
	static String TAG = "FileUtils";
	static Integer STATIC_IMG_CACHE_SIZE = 10;
	static Integer MB = 1024;
	static String JPEG = "jpeg";
	static Integer HARD_CACHE_CAPACITY = 30;
	static Long diffTime = (long) (10 * 3600 * 3600);
	String rootPath;
	OnLineBitMapListener onLineBitMapListener;
	public BitmapCacheUtils(){
		rootPath = Environment.getExternalStorageDirectory().getPath();
		rootPath += "/miicaa/cache/cache_litte_img";
		
	}
	public static BitmapCacheUtils geInstance(){
		return (instance != null ? instance :(new BitmapCacheUtils()));
	}
	
	private Bitmap getLittleImg(final String fileName,String path){
		Bitmap bitmap = mHardBitmapCache.get(fileName);
		if(bitmap != null){
			/*
			 * 如果是调用到就重新进行一次插入，将其置于顶部，确保是在最后进入lru算法
			 */
			mHardBitmapCache.remove(fileName);
			mHardBitmapCache.put(fileName, bitmap);
			return bitmap;
		}
		
		SoftReference<Bitmap> bitmapSoftRefrence = mSoftBitmapCache.get(fileName);
		if(bitmapSoftRefrence != null){
			mSoftBitmapCache.remove(fileName);
			mSoftBitmapCache.put(fileName, bitmapSoftRefrence);
			return bitmapSoftRefrence.get();
		}
		
	   String pathName = path+"/"+fileName;
	   try{
		   bitmap = Bimp.revitionImageSize(pathName);
	   }catch(IOException e){
		   Log.e(TAG, "this bitmap is null or other wrong !"+e.getMessage());
	   }
	   if(bitmap != null){
		   mHardBitmapCache.put(fileName, bitmap);
		   updateDoFileTime(rootPath, fileName);
		   return bitmap;
	   }else{
		   PictureHelper.requestFirstPic(fileName, new FirstPictureLoadListener() {
			
			@Override
			public void loadPic(Bitmap map) {
				// TODO Auto-generated method stub
				howRemoveCache(rootPath);
				saveToBmp(map, fileName);
				mHardBitmapCache.put(fileName, map);
				if(onLineBitMapListener != null)
					onLineBitMapListener.onlineBitmap(map);
			}
		});
		  return null;
	   }
		
	}
	
	
	
	/*
	 * 计算sd卡啊上的剩余空间
	 */
	private Integer freeOnSd(){
		StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFree = ((double)sf.getAvailableBlocks()*(double)sf.getBlockSize()) / MB;
		return (int)sdFree;
	}
	
	
	/*
	 * 保存图片到本地
	 */
	private void saveToBmp(Bitmap map,String imgName){
		if(map == null){
			Log.d(TAG,"Bitmap :------- nulllll" );
			return;
		}
		/*
		 * 如果sd卡上的空间不足缓存大小
		 */
		if(STATIC_IMG_CACHE_SIZE > freeOnSd()){
			Log.d(TAG, "cache is full-----");
			return;
		}
		String path = rootPath + imgName;
		File file = new File(path);
		if(!file.exists()){
			try{
				file.createNewFile();
				FileOutputStream os = new FileOutputStream(file);
				map.compress(Bitmap.CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	

	/*
	 * 超出缓存大小就删除近期不使用的百分之40的数据
	 */
	private void howRemoveCache(String dirpath){
		File dir = new File(dirpath);
		/*
		 * 遍历文件夹下所有文件
		 */
		File[] files = dir.listFiles();
		if(files == null){
			Log.d(TAG, "has no file in tmpCache");
			return;
		}
		/*
		 * 文件大小
		 */
		int dirSize = 0;
		for(int i = 0 ; i < files.length; i ++){
			if(files[i].getName().contains(JPEG)){
				dirSize += files[i].length();
			}
		}
	    Integer needSize = STATIC_IMG_CACHE_SIZE*MB - dirSize;
		if(needSize <= 0 || needSize > freeOnSd()){
			/*
			 * 删除百分之四十的文件
			 */
			Integer needDelSize = (int)(0.4*files.length);
			/*
			 * 按使用的最后时间排序
			 */
			Arrays.sort(files,new ForLastModiSort());
			
			for(int i = 0 ; i < needDelSize; i++){
				if(files[i].getName().contains(JPEG)){
					try{
					files[i].delete();
					}catch(Exception e){
						Log.e(TAG, "cannot del this more file or this file isn't exits?======="+e.getMessage());
					}
				}
			}
		}
		
		
		
	}
	
	
	/*
	 *更新文件使用的时间 
	 */
	private void updateDoFileTime(String dir,String fileName){
//		String path = rootPath + fileName;
		File file  = new File(dir,fileName);
		Long newModifiedTime  = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	
	class ForLastModiSort implements Comparator<File>{

		@Override
		public int compare(File lhs, File rhs) {
			// TODO Auto-generated method stub
			if(lhs.lastModified() > rhs.lastModified()){
				return 1;
			}else if(lhs.lastModified() == rhs.lastModified()){
				return 0;
			}else{
				return -1;
			}
			
		}
		
	}
	
	private void removeExpiredCache(String dir,String fileName){
		File file = new File(dir,fileName);
	    if(!file.exists()){
	    	Log.d(TAG, "超时删除文件这里文件不存在！====="+fileName);
	    	return ;
	    }
	    /*
	     * 超时了以后就删除文件
	     */
	    if(System.currentTimeMillis() - file.lastModified() > diffTime){
	    	file.delete();
	    }
	}
	
	
	private final HashMap<String,Bitmap> mHardBitmapCache = new LinkedHashMap<String, Bitmap>
	(HARD_CACHE_CAPACITY/2, 0.75f, true){  
        /**
		 * 
		 */
		private static final long serialVersionUID = -8079148599512163793L;

		@Override  
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {  
            if (size() > HARD_CACHE_CAPACITY) {  
               //当map的size大于30时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率  
               mSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));  
                return true;  
            } else  
                return false;  
        }  
    }; 
    
    private final static  ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache =new ConcurrentHashMap<String,SoftReference<Bitmap>>
    (HARD_CACHE_CAPACITY / 2); 
    
    
    
    public interface OnLineBitMapListener{
    	void onlineBitmap(Bitmap bitmap);
    }
    
    public void setOnLineBitMapListener(OnLineBitMapListener listener){
    	this.onLineBitMapListener = listener;
    }
    
    
}
