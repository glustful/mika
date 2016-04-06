package com.miicaa.home.ui.menu;



import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class SelectPersonInfo implements Parcelable{
    View mView = null;
    public  String mName;
    public String mCode;
    public boolean isSelect = false;
    public boolean checkEnable = true;
    public SelectPersonInfo(String name, String code){
    	 mName = name;
         mCode = code;
    	}
    
    public SelectPersonInfo(){
    	
    }
    
    
    
    public static final Parcelable.Creator<SelectPersonInfo> CREATOR  = new Creator<SelectPersonInfo>() {

		@SuppressWarnings("unchecked")
		@Override
		public SelectPersonInfo createFromParcel(Parcel source) {
			SelectPersonInfo info = new SelectPersonInfo();
			info.mName = source.readString();
			info.mCode = source.readString();
			info.checkEnable = (source.readByte() != 0);
			return info;
		}

		@Override
		public SelectPersonInfo[] newArray(int size) {
			return new SelectPersonInfo[size];
		}
		
	};
    
	@Override
	public String toString() {
//		return super.toString();
		return "name:"+mName+"code:"+mCode
				+"isSelect:"+isSelect+"checkEnable:"+checkEnable;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mCode);
		dest.writeByte((byte) (checkEnable ? 1 : 0));
	}
    
    
}
    
