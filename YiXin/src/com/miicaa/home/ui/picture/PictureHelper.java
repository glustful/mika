package com.miicaa.home.ui.picture;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import com.miicaa.home.R;
import com.miicaa.home.data.business.matter.AccessoryInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.utils.FileUtils;
import com.miicaa.utils.fileselect.MyFileItem;

/**
 * Created by hacker.e on 14-8-19.
 */
public class PictureHelper {
    public final static int LOADING = 0X01;
    public final static int LOADF = 0X02;
    public final  static int LOADED = 0X03;
    static String TAG = "PictureHelper";
    String mId;
    ArrayList<String> fidList;
    PictureLoadCallBack pictureLoadCallBack;
    Context mContext;

    public PictureHelper(String id , Context context){
    	mContext = context;
        mId = id;
        fidList = new ArrayList<String>();
    }

    public void setFileIds(ArrayList<String> fids){
    	this.fidList = fids;
    }
    
    
    
    public void getAttachement() {
      final String url = "/home/phone/thing/getsingleattach";
      final String fileUrl = "/docbase_srv/staticfile/resize/downLoad";
      new RequestAdpater() {
          @Override
          public void onReponse(ResponseData data) {

              if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                  dataCtreat(fileUrl,data.getJsonArray());
              } else {
              }
          }

          @Override
          public void onProgress(ProgressMessage msg) {
          }
      }.setUrl(url)
              .addParam("dataId", mId)
              .notifyRequest();
  }

  private void dataCtreat(String url ,JSONArray jsonArray) {
	  if (jsonArray == null || jsonArray.length() ==0){
          return;
      }
	  Log.d(TAG, "dataCtreat jsonArray:"+jsonArray);
	  ArrayList<MyFileItem> files = new ArrayList<MyFileItem>();
	  ArrayList<String> picturesList = new ArrayList<String>();
      for (int i = 0; i < jsonArray.length(); i++) {
          AccessoryInfo info = new AccessoryInfo();
          JSONObject jsonObject = jsonArray.optJSONObject(i);
          info.setTitle(jsonObject.optString("title"));
          info.setExt(jsonObject.optString("ext"));
          info.setFileId(jsonObject.optString("fileId"));
          if ("png".equalsIgnoreCase(info.getExt()) 
        	   || "jpg".equalsIgnoreCase(info.getExt()) 
        	   || "gif".equalsIgnoreCase(info.getExt()) 
        	   || "bmp".equalsIgnoreCase(info.getExt())
               || "jpeg".equalsIgnoreCase(info.getExt())) {
              String pictureUrl = mContext.getString(R.string.little_file_download, 
            		  UserAccount.mSeverHost,info.getFileId());
              picturesList.add(pictureUrl);
          }else{
        	  MyFileItem item = new MyFileItem();
        	  item.fid = info.getFileId();
        	  item.name = info.getTitle();
        	  item.path = item.name+"."+info.getExt();
        	  files.add(item);
          }
      }
//      if(files.size()>0 && pictureLoadCallBack!=null){
    	  pictureLoadCallBack.loadFileComplete(files,picturesList);
//      }
//      if(onPictureUriIsLoadedListener != null){
//    	  onPictureUriIsLoadedListener.urlLoad(url, fidList);
//      }
//      if(fidList.size() > 0)
//      downViewImg(fidList);
       if(pictureLoadCallBack != null)
    	  pictureLoadCallBack.loadComplete();
  }
  
  
  int count = 0;
  public void downViewImg(ArrayList<String> fids){
	  if(fids != null){
		  for(String fid : fids){
			  getWebImg(fid);
		  }
	  }
  }
    
    public void getWebImg(final String fid){
    	Bitmap bitmap = FileUtils.geInstance().getLittleImg(fid);
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                Message msg = new Message();
                count ++;
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    try {
                        InputStream is = data.getmIs();
                        Bitmap bitmap = getPictureBmp(is);
                        pictureLoadCallBack.loadImg(bitmap,fid);
                        FileUtils.geInstance().saveLittleBmp(bitmap, fid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                	pictureLoadCallBack.loadImg(null, fid);
                    pictureLoadCallBack.errorCall(data.getMsg());
                }
                if(count == fidList.size()){
                	pictureLoadCallBack.loadComplete();
                }

            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl("/docbase_srv/staticfile/resize/downLoad")
                .setRequestMethod(RequestAdpater.RequestMethod.eGet)
                .setRequestType(RequestAdpater.RequestType.eFileStream)
                .addParam("fid", fid)
                .notifyRequest();
    }

    private static Bitmap getPictureBmp(InputStream in){
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i = 0;
        Bitmap bitmap = null;
        while (true){
            if (options.outWidth >> i <=256 &&
                  options.outHeight >> i <= 256  ){
                options.inSampleSize =(int) Math.pow(2.0D,i);
                options.inInputShareable = true;
            	options.inPurgeable = true;
            	options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeStream(in,null,options);
                SoftReference<Bitmap> reference = new SoftReference<Bitmap>(bitmap);
                bitmap = reference.get();
                break;
            }
        }
        return bitmap;
    }

    Boolean noFrist = false;
    public interface PictureLoadCallBack{
        void loadImg(Bitmap bitmap,String fid);
        void errorCall(String msg);
        void loadComplete();
        void loadFileComplete(ArrayList<MyFileItem> files,ArrayList<String> pictureUrls);
    }

    public void setLoadCallBack(PictureLoadCallBack pictureLoadCallBack){
        this.pictureLoadCallBack = pictureLoadCallBack;
    }
    
    
    
    public static void requestFirstPic(String fid,final FirstPictureLoadListener listener){
    	 new RequestAdpater() {
             @Override
             public void onReponse(ResponseData data) {

                 Message msg = new Message();
                 if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                     try {
                         InputStream is = data.getmIs();
                         Bitmap bitmap = getPictureBmp(is);
                         listener.loadPic(bitmap);
                     }catch(Exception e){
                    	 e.printStackTrace();
                     }
                 }else{
                	 listener.loadPic(null);
                	 Log.d(TAG, "data"+data.getMsg()+"---"+data.getCode()+"---"+data.getResultState());
                 }
             }

             @Override
             public void onProgress(ProgressMessage msg) {

             }
         }.setUrl("/docbase_srv/staticfile/resize/downLoad")
                 .setRequestMethod(RequestAdpater.RequestMethod.eGet)
                 .setRequestType(RequestAdpater.RequestType.eFileStream)
                 .addParam("fid", fid)
                 .notifyRequest();
    }
    
    

    public static Integer getPictureCount(ArrayList<AccessoryInfo> exts,final OnFirstPicureListener listener){
          if(exts == null || exts.size() == 0)
        	  return 0;
    	  int count = 0;
    	  ArrayList<String> fids = new ArrayList<String>() ;
          for(AccessoryInfo ext : exts)
          {
        	  String str = ext.getExt();
              if("png".equalsIgnoreCase(str)||"jpg".equalsIgnoreCase(str)||"gif".equalsIgnoreCase(str)||
              		"bmp".equalsIgnoreCase(str)||"jpeg".equalsIgnoreCase(str)||"gif".equalsIgnoreCase(str))
              {
                 fids.add(ext.getFileId());
                 count++;
              }
          }
          if(listener != null && fids.size() > 0)
          {
        	  listener.beginLoadPic(fids);
          }
          return count;
    }
    
    public interface FirstPictureLoadListener{
    	void loadPic(Bitmap map);
    }
    
    public interface OnFirstPicureListener{
    	void beginLoadPic(ArrayList<String> fids);
    }
    
    public interface OnPictureUriIsLoadedListener{
    	void urlLoad(String url,ArrayList<String> fids);
    }
    
    OnPictureUriIsLoadedListener onPictureUriIsLoadedListener;
}
