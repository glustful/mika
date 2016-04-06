package com.miicaa.home.ui.photoGrid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by LM on 14-8-13.
 */
public class BitMapCache {
	
	static String TAG = "BitMapCache";

    //让bitmap所占内存在空闲时就自动回收
    HashMap<String,SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    Boolean isTumbnailPath = false;
    Handler handler = new Handler();
    Bitmap bitmap = null;
    
    
    ThreadPoolExecutor threadPool;
    
    public BitMapCache(){
    	BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingDeque<Runnable>();
    	/*创建一个并发六十条线程的线程池。阻塞线程*/
    	threadPool = new ThreadPoolExecutor(30, 60, 30, TimeUnit.SECONDS, mBlockingQueue);
    	mBitmapDecodeSyncTask = new BitmapDecodeSyncTask();
    }

    private  void setBitMap(final ImageView imageView,final String thumbnailPath,
                          final String imgPath,final Callback callback){
        
    	
//    	/*加入线程池*/
        threadPool.execute(new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String path = null;
            	if (TextUtils.isEmpty(thumbnailPath)&&
                        TextUtils.isEmpty(imgPath)){
                    return;
                }
                if (thumbnailPath != null && !TextUtils.isEmpty(thumbnailPath)){
                    path = thumbnailPath;
//                    Log.d(TAG, "add thumbnailPath:"+thumbnailPath);
                    isTumbnailPath = true;
                }else if (!TextUtils.isEmpty(imgPath)){
                    path = imgPath;
//                    Log.d(TAG, "add imagepath:"+imgPath);
                    isTumbnailPath = false;
                }
                final  String fPath = path;
                try {
                    if (isTumbnailPath) {
                        bitmap = BitmapFactory.decodeFile(thumbnailPath);
                        if (bitmap == null) {
                            bitmap = getResetBitmap(imgPath);
                        }

                    } else {
//                    	Options options = new Options();
////        				options.inPreferQualityOverSpeed = true;
//        				options.inPreferredConfig = Bitmap.Config.RGB_565;
//        				//软引用机制,注意：下面两个属性必须同时使用
//        				options.inPurgeable = true;
//        				options.inInputShareable = true;
//        			    bitmap = BitmapFactory.decodeFile(path,options);
                        bitmap = getResetBitmap(imgPath);
                    }
                    bitmap = centerSquareScaleBitmap(bitmap, 80);
                    
                }catch (IOException e){
                    e.printStackTrace();
                }
                put(fPath,bitmap);
//                imageView.setImageBitmap(bitmap);
                if (callback != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.loadBitMap(imageView,bitmap);
                        }
                    });
                }
			}
		})
        );
        threadPool.shutdown();
    }

    public void setBitMap(final ImageView imageView,
            final String imgPath,final Callback callback){
    	setBitMap(imageView, null, imgPath, callback);
//    	new BitmapDecodeSyncTask(imageView).execute(imgPath);
    }
    
    public  void setBitMap(ImageView imageView,String imagePath,OnBitmapCacheLoadListener listener){
    	new BitmapDecodeSyncTask(imageView, listener).execute(imagePath);
    }
    
    public void stopTask(){
    	stop = true;
    }
    
    public Bitmap getResetBitmap(String path) throws IOException{
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bufferedInputStream, null, options);
        bufferedInputStream.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true){
            if (options.outWidth >> i <=256 &&
                    options.outHeight >> i <= 256){//将图片的高度和宽度锁定在256以内
                bufferedInputStream = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(bufferedInputStream,null,options);
                break;
            }
            i++;
        }
        options.outWidth = 256;
        options.outHeight = 256;
        bufferedInputStream = new BufferedInputStream(
              new FileInputStream(new File(path)));
      options.inSampleSize = (int) Math.pow(2.0D, i);
      options.inJustDecodeBounds = false;
      bitmap = BitmapFactory.decodeStream(bufferedInputStream,null,options);
        
        return bitmap;
    }
    
    private Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
    	   if(null == bitmap || edgeLength <= 0)
    	   {
    	    return  null;
    	   }
    	                                                                                 
    	   Bitmap result = bitmap;
    	   int widthOrg = bitmap.getWidth();
    	   int heightOrg = bitmap.getHeight();
    	                                                                                 
    	   if(widthOrg > edgeLength && heightOrg > edgeLength)
    	   {
    	    //压缩到一个最小长度是edgeLength的bitmap
    	    int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg);
    	    int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
    	    int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
    	    Bitmap scaledBitmap;
    	                                                                                  
    	          try{
    	           scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
    	          }
    	          catch(Exception e){
    	           return null;
    	          }
    	                                                                                       
    	       //从图中截取正中间的正方形部分。
    	       int xTopLeft = (scaledWidth - edgeLength) / 2;
    	       int yTopLeft = (scaledHeight - edgeLength) / 2;
    	                                                                                     
    	       try{
    	        result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
    	        scaledBitmap.recycle();
    	       }
    	       catch(Exception e){
    	        return null;
    	       }       
    	   }
    	                                                                                      
    	   return result;
    	  }


    public void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    public interface  Callback{
        public void loadBitMap(ImageView imageView,Bitmap bitmap);
    }
    
    Boolean stop = false;
    
    BitmapDecodeSyncTask mBitmapDecodeSyncTask;
    class BitmapDecodeSyncTask extends AsyncTask<String, Integer, Bitmap>{
    	ImageView mImageView;
    	OnBitmapCacheLoadListener onBitmapCacheLoadListener;
    	Object tag;
    	public BitmapDecodeSyncTask(ImageView imageView,OnBitmapCacheLoadListener listener){
    		this.mImageView = imageView;
    		this.onBitmapCacheLoadListener = listener;
    	}
    	
    	public BitmapDecodeSyncTask() {
		}

		public void setData(OnBitmapCacheLoadListener listener){
    		this.onBitmapCacheLoadListener = listener;
    	}
    	
    	@Override
		protected void onPreExecute() {
    		if(onBitmapCacheLoadListener!=null){
    			onBitmapCacheLoadListener.beforeLoad();
    		}
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			if(stop){
				cancel(true);
			}
			String path = params[0];
			Log.d(TAG, "BitmapDecodeSyncTask path:"+path);
			if(Bimp.LRUtoGridPhotoMap.containsKey(path)){
				return Bimp.LRUtoGridPhotoMap.get(path);
			}
//			Options options = new Options();
//			options.inPreferQualityOverSpeed = true;
//			options.inPreferredConfig = Bitmap.Config.RGB_565;
			//软引用机制,注意：下面两个属性必须同时使用
//			options.inPurgeable = true;
//			options.inInputShareable = true;
//		    Bitmap bitmap = BitmapFactory.decodeFile(path,options);
			try {
				Bitmap bitmap = getResetBitmap(path);
				centerSquareScaleBitmap(bitmap, 80);
			    Log.d(TAG, "centerSquareScaleBitmap bitmap:"+bitmap);
			    Bimp.LRUtoGridPhotoMap.put(path, bitmap);
			    return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		    
		}

		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(onBitmapCacheLoadListener != null){
				onBitmapCacheLoadListener.loaded(mImageView,result);
			}
//			mImageView.setImageBitmap(result);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
    	
    }

    public interface OnBitmapCacheLoadListener{
    	void beforeLoad();
    	void loading(Integer loadingCount);
    	void loaded(ImageView imageView,Bitmap bitmap);
    }
    
   

}
