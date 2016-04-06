package com.miicaa.home.ui.photoGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.yxst.epic.yixin.utils.ImageUtils;

/**
 * Created by LM on 14-8-12.
 */
public class ContentDataHelper {
	final static long MB = 1024*1024;
    final String TAG = getClass().getSimpleName();
    Context context;
    ContentResolver cr;
    
//    public static ContentDataHelper instance;

    HashMap<String,String> thumbnailList;
    ArrayList<HashMap<String,String>> albumsList;
    HashMap<String,BucketData> bucketList;

    Boolean hasImageBucketList = false;

    public ContentDataHelper(){

    }

//    public static ContentDataHelper getInstance(){
//        if (instance == null){
//            instance = new ContentDataHelper();
//        }
//        return instance;
//    }

    public void init(Context context){
        if (this.context == null){
            this.context = context;
            cr = this.context.getContentResolver();
            thumbnailList = new HashMap<String, String>();
            albumsList = new ArrayList<HashMap<String, String>>();
            bucketList = new HashMap<String, BucketData>();
        }

    }

    public void getThumbnail(){
        String[] projection = {BaseColumns._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,projection,
                null,null,null);
        getThumbnailData(cursor);
    }

    public void getThumbnailData(Cursor cursor){//获取缩略图
        if (cursor.moveToFirst()){
            int _id;
            int image_id;
            String image_path;
            int _idColum = cursor.getColumnIndex(BaseColumns._ID);
            int image_idColum = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColum = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            while (cursor.moveToNext()){
                _id = cursor.getInt(_idColum);
                image_id = cursor.getInt(image_idColum);
                image_path = cursor.getString(dataColum);
                thumbnailList.put(""+image_id,image_path);
            }
        }
    }

    public void getAlbums(){
        String[] projection = {BaseColumns._ID, AlbumColumns.ALBUM,
                AlbumColumns.ALBUM_ART, AlbumColumns.ALBUM_KEY,
                AlbumColumns.ARTIST, AlbumColumns.NUMBER_OF_SONGS};
        Cursor cursor = cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,projection,
                null,null,null);
        getAlbumsThumbnailData(cursor);
    }

    public void getAlbumsThumbnailData(Cursor cursor){
        if (cursor.moveToFirst()){
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artList;
            int numOfSongs;
            int _idColum = cursor.getColumnIndex(BaseColumns._ID);
            int albumColum = cursor.getColumnIndex(AlbumColumns.ALBUM);
            int albumArtColum = cursor.getColumnIndex(AlbumColumns.ALBUM_ART);
            int albumKeyColum = cursor.getColumnIndex(AlbumColumns.ALBUM_KEY);
            int artListColums = cursor.getColumnIndex(AlbumColumns.ARTIST);
            int numOfSongsColums = cursor.getColumnIndex(AlbumColumns.NUMBER_OF_SONGS);
            while (cursor.moveToNext()){
                _id = cursor.getInt(_idColum);
                album = cursor.getString(albumColum);
                albumArt = cursor.getString(albumArtColum);
                albumKey = cursor.getString(albumKeyColum);
                artList = cursor.getString(artListColums);
                numOfSongs = cursor.getShort(numOfSongsColums);
                HashMap<String,String> hash = new HashMap<String, String>();
                hash.put("id",_id+"");
                hash.put("album",album);
                hash.put("albumArt",albumArt);
                hash.put("albumKey",albumKey);
                hash.put("artList",artList);
                hash.put("numOfSongs",numOfSongs+"");
                albumsList.add(hash);
            }
        }
    }

    public List<BucketData> buildImageBucketList(){//建立缩略图与图片之间的关系
        long startTime = System.currentTimeMillis();
        ArrayList<BucketData> bucketDataList = new ArrayList<BucketData>();
        //
        //获取缩略图
        getThumbnail();

        //
        String columns[] = new String[] { BaseColumns._ID, ImageColumns.BUCKET_ID,
                ImageColumns.PICASA_ID, MediaColumns.DATA, MediaColumns.DISPLAY_NAME, MediaColumns.TITLE,
                MediaColumns.SIZE, ImageColumns.BUCKET_DISPLAY_NAME,
                MediaColumns.DATE_MODIFIED };
         
        String storOder = MediaColumns.DATE_MODIFIED+" desc";
        // 条件 
        String selection = MediaColumns.MIME_TYPE + "=? OR " + MediaColumns.MIME_TYPE + "=?"; 
        String[] selectionArgs = { "image/jpeg","image/png" }; 
        // 查询图库
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs,
                storOder);
        if (cur.moveToFirst()) {
            // 查询图片
            int photoIDIndex = cur.getColumnIndexOrThrow(BaseColumns._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaColumns.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(MediaColumns.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(MediaColumns.SIZE);
            int bucketDisplayNameIndex = cur
                    .getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(ImageColumns.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(ImageColumns.PICASA_ID);
            int upadteTimeIdex = cur.getColumnIndexOrThrow(MediaColumns.DATE_MODIFIED);
            //
            int totalNum = cur.getCount();

            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);
                Long updateTime = cur.getLong(upadteTimeIdex);
                if(size != null && size.length() >0 ){
                int nSize = (int) (Long.parseLong(size)/MB);
                /*8MB以上图片不予显示*/
//                if(nSize  > 8){
//                	continue;
//                }
                }else{
                	Log.d(TAG,"size is"+size);
                  	continue;
                }
                Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
                        + picasaId + " name:" + name + " path:" + path
                        + " title: " + title + " size: " + size + " bucket: "
                        + bucketName + "---");

                BucketData bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new BucketData();
                    bucketDataList.add(bucket);
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.name = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.iamge_id = _id;
                imageItem.image_path = path;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                imageItem.updateTime = updateTime;
                if(ImageUtils.testImage(path))
                bucket.imageList.add(imageItem);
            } while (cur.moveToNext());
            Iterator<Map.Entry<String,BucketData>> iterator = bucketList.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, BucketData> entry = iterator
                        .next();
                BucketData bucket = entry.getValue();
                Log.d(TAG, entry.getKey() + ", " + bucket.name + ", "
                        + bucket.count + " ---------- ");
                for (int i = 0; i < bucket.imageList.size(); ++i) {
                    ImageItem image = bucket.imageList.get(i);
                    Log.d(TAG, "----- " + image.iamge_id + ", " + image.image_path
                            + ", " + image.thumbnailPath);
                }
            }
            hasImageBucketList  = true;
            return bucketDataList;
        }
        return null;

    }

    public void getBucketImageList(Boolean refesh){
//        if (refesh || (!refesh && !hasImageBucketList)){
//            buildImageBucketList();
//            }
//
//        List<BucketData> tmpList = new ArrayList<BucketData>();
//        for (Map.Entry<String,BucketData> entry : bucketList.entrySet()){
//            tmpList.add(entry.getValue());
//        }
//        return tmpList;
    	new MyPictureTask().execute();
    }
    
    class MyPictureTask extends AsyncTask<Integer, Integer, List<BucketData>>{

		@Override
		protected List<BucketData> doInBackground(Integer... params) {
			List<BucketData> tmpList = buildImageBucketList();
//			 List<BucketData> tmpList = new ArrayList<BucketData>();
//		        for (Map.Entry<String,BucketData> entry : bucketList.entrySet()){
//		            tmpList.add(entry.getValue());
//		        }
		        return tmpList;
		}

		@Override
		protected void onPostExecute(List<BucketData> result) {
			if(result != null && onLoadOverImageListener != null){
				onLoadOverImageListener.loadComplete(result);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
    	
    }
    
    
    
    public interface OnLoadOverImageListener{
    	void loadComplete(List<BucketData> bucketDatas);
    }

    OnLoadOverImageListener onLoadOverImageListener;
    public void setOnLoadOverImageListener(OnLoadOverImageListener listener){
    	onLoadOverImageListener  = listener;
    }

}
