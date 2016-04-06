package com.miicaa.base.request;

import java.io.Serializable;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class UploadFileItem implements Parcelable{

	private static String TAG  = "UploadFileItem";
	
	public String path;
	public HashMap<String, String> param;
	public UploadFileItem (String path,HashMap<String, String> param){
		this.path = path;
		this.param = new HashMap<String, String>();
		this.param.putAll(param);
	}
    
	public UploadFileItem() {
		// TODO Auto-generated constructor stub
	}
	
	public UploadFileItem(String path){
		this.path = path;
		this.param = new HashMap<String, String>();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.d(TAG, "writeToParcel  hashmap:"+param);
		dest.writeString(path);
		dest.writeMap(param);
	}
	
	public static final Parcelable.Creator<UploadFileItem> CREATOR  = new Creator<UploadFileItem>() {

		@SuppressWarnings("unchecked")
		@Override
		public UploadFileItem createFromParcel(Parcel source) {
			UploadFileItem fileItem = new UploadFileItem();
			fileItem.path = source.readString();
			fileItem.param = source.readHashMap(HashMap.class.getClassLoader());
			return fileItem;
		}

		@Override
		public UploadFileItem[] newArray(int size) {
			return new UploadFileItem[size];
		}
		
	};
	@Override
	public String toString() {
		return param+"~~~"+path;
//		return super.toString();
	}
	
	
}
