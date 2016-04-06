package com.miicaa.home.ui.contactList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 13-11-27.
 */
public class ContactData implements Parcelable{
	private void parsePYKey() {
		pingyinKey = "#";
		if (quanPingFirst != null && quanPingFirst.length() > 0) {
			char f0 = Character.toUpperCase(quanPingFirst.charAt(0));
			if ('A' <= f0 && f0 <= 'Z') {
				pingyinKey = String.valueOf(f0);
			}
		}
	}

	private String name;
	private String userCode;
	private String pingyinKey;
	private String dataType;
	private String quanPing;
	private String quanPingFirst;
	private Long uid;
	private Boolean isSelect;
	
	public ContactData(){
		isSelect = false;
	}
	
	public void setSelect(Boolean isSelect){
		this.isSelect = isSelect;
	}
	
	public Boolean isSelect(){
		return isSelect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPingyinKey() {
		return pingyinKey;
	}

	public void setPingyinKey(String pingyinKey) {
		this.pingyinKey = pingyinKey;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getQuanPing() {
		return quanPing;
	}

	public void setQuanPing(String quanPing) {
		this.quanPing = quanPing;
	}

	public String getQuanPingFirst() {
		return quanPingFirst;
	}

	public void setQuanPingFirst(String quanPingFirst) {
		this.quanPingFirst = quanPingFirst;

		parsePYKey();
	}
	
	public void setUid(Long uid){
		this.uid = uid;
	}
	
	public Long getUid(){
		return uid;
	}

    public static  final  Creator<ContactData> CREATOR = new Creator<ContactData>() {
        @Override
        public ContactData createFromParcel(Parcel parcel) {
            ContactData data = new ContactData();
            data.name = parcel.readString();
            data.userCode = parcel.readString();
            return data;
        }

        @Override
        public ContactData[] newArray(int i) {
            return new ContactData[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(userCode);
    }

}
