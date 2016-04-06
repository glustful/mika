package com.miicaa.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.miicaa.base.request.UploadFileItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
/*
 * @author: hacker.e
 * 图片本地化
 * LRU算法实现
 */
public class FileUtils {
	static FileUtils instance;
	static String TAG = "FileUtils";
	static String downLoadFilePath = Environment.getExternalStorageDirectory()+"/miicaa/download/" ;
	static Integer STATIC_IMG_CACHE_SIZE = 10;
	static Integer STATIC_IMG_CACHE_BIG_IMG_SIZE = 50;
	static Integer MB = 1024*1024;
	static String JPEG = "jpeg";
	static Integer HARD_CACHE_CAPACITY = 30;
	static Integer HARD_CACHE_BIG_CAPACITY = 5;
	static Long diffTime = (long) (10 * 3600 * 3600);
	String rootLittleImgPath;
	String rootBigImgPath;
	
	
	public FileUtils(){
		
		String path = Environment.getExternalStorageDirectory().getPath();
		rootLittleImgPath = path + "/miicaa/cache/cacheLitteImg";
		rootBigImgPath = path + "/miicaa/cache/cacheBigImg";
		
	}
	public static FileUtils geInstance(){
		return (instance != null ? instance :(new FileUtils()));
//		return new FileUtils();
	}
	
	public Bitmap getLittleImg(String fileName){
		Bitmap bitmap = mHardBitmapCache.get(fileName);
		
		if(bitmap != null){
			/*
			 * 如果是调用到就重新进行一次插入，将其置于顶部，确保是在最后进入lru算法
			 */
			Log.d(TAG, "hardHasmap bitmap is"+bitmap);
			mHardBitmapCache.remove(fileName);
			mHardBitmapCache.put(fileName, bitmap);
			return bitmap;
		}
		
		SoftReference<Bitmap> bitmapSoftRefrence = mSoftBitmapCache.get(fileName);
		if(bitmapSoftRefrence != null){
			mSoftBitmapCache.remove(fileName);
			mSoftBitmapCache.put(fileName, bitmapSoftRefrence);
			Log.d(TAG, "soft bitmap is"+ bitmapSoftRefrence);
			return bitmapSoftRefrence.get();
		}
		
	   String pathName = rootLittleImgPath+"/"+fileName;
	   try{
	   bitmap = BitmapFactory.decodeFile(pathName);
	   if(bitmap != null){
	   mHardBitmapCache.put(fileName, bitmap);
	   updateDoFileTime(rootLittleImgPath, fileName);
	   /*
	    * 递归取出值
	    */
	   getLittleImg(fileName);
	   }
	   }catch(OutOfMemoryError e){
		   recyleBigHardBitmap();
		   recyleLittleHardBitmap();
		   getLittleImg(fileName);
	   }
	   return bitmap;
		
	}
	
	
	public Bitmap getBigImg(String filName){
		Bitmap bitmap = mHardBigBitmapCache.get(filName);
		if(bitmap != null){
			mHardBigBitmapCache.remove(filName);
			mHardBigBitmapCache.put(filName, bitmap);
			return bitmap;
		}
		SoftReference<Bitmap>  bitmapRefrence= mSoftBigBitmapCache.get(filName);
		if(bitmapRefrence != null){
			mSoftBigBitmapCache.remove(filName);
			mSoftBigBitmapCache.put(filName, bitmapRefrence);
			return bitmapRefrence.get();
		}
		
		String filePath = rootBigImgPath+"/"+filName;
			try {
			  Log.d(TAG, "rootBigImgPath + ;;'++++"+filePath);
		      bitmap = revitionBigImageSize(filePath);
		      if(bitmap != null){
		      mHardBigBitmapCache.put(filName, bitmap);
		      updateDoFileTime(rootBigImgPath, filName);
		      getBigImg(filName);
		      }
			} catch (FileNotFoundException e) {
				Log.d(TAG, "file not found :"+e.getMessage());
				e.printStackTrace();
			}catch(OutOfMemoryError e){
				/*
				 * 如果使用中内存溢出，捕获异常然后手动回收内存
				 */
				    recyleBigHardBitmap();
				   recyleLittleHardBitmap();
				   getLittleImg(filName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Ioexception in getBigImage"+e.getMessage());
			}
		return bitmap;
	}
	
	
	
	/*
	 * 计算sd卡啊上的剩余空间
	 */
	private Integer freeOnSd(){
		StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFree = ((double)sf.getAvailableBlocks()*(double)sf.getBlockSize()) / MB;
		return (int)sdFree;
	}
	
	
	public void saveBigBmp(final Bitmap map,final String imgName){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Log.d(TAG, "save bigmap run!");
				saveToBmp(rootBigImgPath, map, imgName, FileSizeState.Big);
			}
		}).start();
		
	}
	
	public void saveLittleBmp(final Bitmap map,final String imgName){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				saveToBmp(rootLittleImgPath, map, imgName, FileSizeState.Little);
			}
		}).start();
		
	}
	
	/*
	 * 保存图片到本地
	 */
	private void saveToBmp(String rootPath,
			Bitmap map,
			String imgName,
			FileSizeState state){
		if(map != null){
			Log.d(TAG,"Bitmap :not null" );
		}
		
		
		removeExpiredCache(rootPath, imgName);
		howRemoveCache(rootPath,state);
		/*
		 * 如果sd卡上的空间不足缓存大小
		 */
		Log.d(TAG, "The free size on sd is"+freeOnSd());
		if(STATIC_IMG_CACHE_SIZE > freeOnSd()){
			Log.d(TAG, "cache is full-----");
			return;
		}
		String path = makeDir(rootPath);
	     path += "/"+imgName;
		File file = new File(path);
		if(!file.exists()){
			try{
				file.createNewFile();
				FileOutputStream os = new FileOutputStream(path);
				map.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.flush();
				os.close();
			}catch(FileNotFoundException e){
				Log.d(TAG, "file create filed @"+e.getMessage());
				e.printStackTrace();
			}catch(IOException e){
				Log.d(TAG, "file create ioe Error" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void saveBigBmp(final InputStream inputStream,final String fileName){
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				saveToBmp(rootBigImgPath, inputStream, fileName, FileSizeState.Big);
//			}
//		}).start();
		
	}
	
	private void saveToBmp(String rootPath,
			InputStream inputStream,
			String imgName,
			FileSizeState state){
		removeExpiredCache(rootPath, imgName);
		howRemoveCache(rootPath,state);
		/*
		 * 如果sd卡上的空间不足缓存大小
		 */
		Log.d(TAG, "The free size on sd is"+freeOnSd());
		if(STATIC_IMG_CACHE_SIZE > freeOnSd()){
			Log.d(TAG, "cache is full-----");
			return;
		}
		String path = makeDir(rootPath);
	     path += "/"+imgName;
			try{
				FileOutputStream os = new FileOutputStream(path);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);
				byte buffer[] = new byte[256];
				int byteRad = 0;
				Log.d(TAG, "流大小"+bufferedInputStream.available()+"~~~"+path);
				while((byteRad = bufferedInputStream.read(buffer)) > 0){
					bufferedOutputStream.write(buffer, 0, byteRad);
				}
				
				os.flush();
				os.close();
				bufferedInputStream.close();
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				File f = new File(path);
				Log.d(TAG, "文件大小"+f.length()+"~~~"+path);
			}catch(FileNotFoundException e){
				Log.d(TAG, "file create filed @"+e.getMessage());
				e.printStackTrace();
			}catch(IOException e){
				Log.d(TAG, "file create ioe Error" + e.getMessage());
				e.printStackTrace();
			}catch (Exception e) {
				Log.d(TAG, "file exception:"+e.getMessage());
			}
			if(addNewBigImageListener != null){
				addNewBigImageListener.addNewBigImageView();
			}
	}
	
	private String makeDir(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		return path;
	}
	
	public interface AddNewBigImageListener{
		void addNewBigImageView();
	}
	
	AddNewBigImageListener addNewBigImageListener;
	public void setAddNewBigImageListener(AddNewBigImageListener listener){
		addNewBigImageListener = listener;
	}

	private void howRemoveCache(String dirPath,FileSizeState state){
		int staticSize = 0;
		if(state == FileSizeState.Big){
			staticSize = STATIC_IMG_CACHE_BIG_IMG_SIZE;
		}else if(state == FileSizeState.Little){
			staticSize = STATIC_IMG_CACHE_SIZE;
		}
		howRemoveCache(dirPath, staticSize);
	}
	
	/*
	 * 超出缓存大小就删除近期不使用的百分之40的数据
	 */
	private void howRemoveCache(String dirpath,int staticSize){
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
		File file  = new File(dir,fileName);
		Long newModifiedTime  = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	
	class ForLastModiSort implements Comparator<File>{

		@Override
		public int compare(File lhs, File rhs) {
			if(lhs.lastModified() > rhs.lastModified()){
				return 1;
			}else if(lhs.lastModified() == rhs.lastModified()){
				return 0;
			}else{
				return -1;
			}
			
		}
		
	}
	
	
	/*
	 * 删除最近不使用的文件
	 */
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
    
    private final static HashMap<String, Bitmap> mHardBigBitmapCache = new LinkedHashMap<String, Bitmap>
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
    
    private final static  ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBigBitmapCache =new ConcurrentHashMap<String,SoftReference<Bitmap>>
    (HARD_CACHE_CAPACITY / 2); 
    
    
    
    public enum FileSizeState{
    	Big,
    	Little
    }
    
    public void recyleBigHardBitmap(){
    	for(Map.Entry<String, Bitmap> map : mHardBigBitmapCache.entrySet()){
    		Bitmap bitmap = map.getValue();
    		if(bitmap != null && !bitmap.isRecycled()){
    			   // 回收并且置为null
    	        bitmap.recycle(); 
    	        bitmap = null; 
    		}
    	}
//    	System.gc();
    }
    
    public void recyleLittleHardBitmap(){
    	for(Map.Entry<String, Bitmap> map : mHardBitmapCache.entrySet()){
    		Bitmap bitmap = map.getValue();
    		if(bitmap != null && !bitmap.isRecycled()){
    			   // 回收并且置为null
    	        bitmap.recycle(); 
    	        bitmap = null; 
    		}
    	}
//    	System.gc();
    }
    
    
		private Bitmap revitionBigImageSize(String path) throws IOException {
        	File file = new File(path);
            if(!file.exists()){
            	return null;
            }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        options.inJustDecodeBounds = false;
//        while (true) {
//            if (((options.outWidth >> i) * (options.outHeight>>i)) > (1024*1024)){
                in = new BufferedInputStream(new FileInputStream(file));
                
                Log.d(TAG, "inputstram remoc in fileutils"+bitmap+"####"+file.length()+"####"+in.available());
//                options.inSampleSize = (int) Math.pow(2.0D, i);
//                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in);
//                break;
//            }
//            i += 1;
//        }
                Log.d(TAG, "bitmap remoc in fileutils"+bitmap);
        return bitmap;
        }
		
		
		public Boolean hasBigCacheImage(String fileName){
			return hasCacheImage(rootBigImgPath, fileName);
		}
		
		public Boolean hasLittleCacheImage(String fileName){
			return hasCacheImage(rootLittleImgPath, fileName);
		}
		
		private Boolean hasCacheImage(String rootPath,String fileName){
			return new File(rootPath+"/"+fileName).exists();
		}
		
		public String getBigCacheImagePath(String fileName){
			return getCacheImagePath(rootBigImgPath, fileName);
		}
		
		public String getLittleCacheImagePath(String fileName){
			return getCacheImagePath(rootLittleImgPath, fileName);
		}
		
		private String getCacheImagePath(String rootPath,String fileName){
			return rootPath + "/" + fileName;
		}
    
		public String getLittleImagePath(){
			return this.rootLittleImgPath;
		}
		
		public String getDownLoadFileStringPath(){
			return downLoadFilePath;
		}
		
		//从文件路径中得到文件名
	    public static   String getFileName(String path){
	        int start = path.lastIndexOf("/");
	        if(start != -1 ){
	            return path.substring(start+1);
	        }
	        return "";
	    }
	    
		public static  ArrayList<UploadFileItem> getUploadFileItems(List<String> paths,String infoId,String appUn){
			ArrayList<UploadFileItem> fList = new ArrayList<UploadFileItem>();
			HashMap<String, String> uploadParam = new HashMap<String, String>();
			uploadParam.put("appUn", appUn);
			uploadParam.put("infoId", infoId);
			for(int i = 0; i < paths.size() ;i++){
			uploadParam.put("fileName", getFileName(paths.get(i)));
			UploadFileItem item = new UploadFileItem(paths.get(i), uploadParam);
		    fList.add(item);
		}
			return fList;
		}
		
		public static long getFilesLength(List<String> mFilePathList){
			long length = 0;
			for(String path : mFilePathList){
				File f = new File(path);
				if(f.exists())
					length += f.length();
			}
			return length;
			
		}
}
