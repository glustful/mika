package com.miicaa.home.ui.photoGrid;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class Bimp {
    public static int max = 0;
    static int cacheSize = 100;
    public static boolean act_bool = true;
    public static List<Bitmap> bmp = new ArrayList<Bitmap>();
    public static List<String> drr = new ArrayList<String>();
    public static HashMap<String,Bitmap> bmpMap = new HashMap<String, Bitmap>();

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
        	int width = options.outWidth >> i;
        	int height = options.outHeight >> i;
//        	Log.d("Bimp", "压缩图片：i:"+i);
            if (width <= 256) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
    
    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        final float srcAspect = (float) srcWidth / (float) srcHeight;
        final float dstAspect = (float) dstWidth / (float) dstHeight;

        if (srcAspect > dstAspect) {
            return srcWidth / dstWidth;
        } else {
            return srcHeight / dstHeight;
        }
}
    
    public static Bitmap revitionImageSizeNew(String path,int dstWidth,int dstHeight) throws IOException{
    	 BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                 new File(path)));
         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inInputShareable = true;
         options.inPurgeable = true;
         options.inJustDecodeBounds = true;
         BitmapFactory.decodeStream(in, null, options);
         in.close();
         int i = 0;
         Bitmap bitmap = null;
         options.inSampleSize = calculateSampleSize(options.outWidth,
        		 options.outHeight, dstWidth, dstHeight);
         options.inJustDecodeBounds = false;
         bitmap = BitmapFactory.decodeStream(in, null, options);
         
//         while (true) {
//        	if(w)
//         	int width = options.outWidth >> i;
//         	int height = options.outHeight >> i;
//             if (width <= 256) {
//                 in = new BufferedInputStream(
//                         new FileInputStream(new File(path)));
//                 options.inSampleSize = (int) Math.pow(2.0D, i);
//                 options.inJustDecodeBounds = false;
//                 bitmap = BitmapFactory.decodeStream(in, null, options);
//                 break;
//             }
//             i += 1;
//         }
         return bitmap;
    }
    
    
    public static void removeSelectPath(String path){
    }

    public static void clearMap(){
        drr.clear();
        bmpMap.clear();
    }
    
    public static LinkedHashMap<String, Bitmap> LRUtoGridPhotoMap = new LinkedHashMap<String, Bitmap>
    ((int) Math.ceil(cacheSize / 0.75f) + 1, 0.75f, true){
    	/**
				 * 
				 */
				private static final long serialVersionUID = -8366438410804241810L;

	@Override
	protected boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
        
		return size() > cacheSize;
        }
    };
    
    public static Bitmap getimage(String srcPath) {
    	//某些相机的角度问题需要旋转
    	int angle = readPictureDegree(srcPath);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		//旋转照片的角度
		Matrix m = new Matrix();
        m.postRotate(angle); 
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), m, true);
//		out.write(baos.toByteArray());
//		return null;
		return compressImage(bitmap,srcPath);//压缩好比例大小后再进行质量压缩
	}
    
	
    
    public static Bitmap compressImage(Bitmap image,String dst) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>50) {	//循环判断如果压缩后图片是否大于50kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dst);
			out.write(baos.toByteArray());
//			image.compress(Bitmap.CompressFormat.JPEG, options, out);
			out.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
    
    //判断照片角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
