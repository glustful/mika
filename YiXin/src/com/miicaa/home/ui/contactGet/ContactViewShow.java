package com.miicaa.home.ui.contactGet;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by LM on 14-7-24.
 */
public  class ContactViewShow implements Parcelable{
   public String name;
   public String code;
   public String uid;
   public String uidName;
    View showView;

    public ContactViewShow(){
    	
    }
    
    public ContactViewShow(String name, String code){
        this.name = name;
        this.code = code;
    }

    public String getName(){
        return name;
    }
    public String getCode(){
        return  code;
    }
    public void setViewShow(View showView){
        this.showView = showView;
//        this.showView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewClick();
//            }
//        });
    }

    public View getShowView(){
        return showView;
    }

    public static  final  Creator<ContactViewShow> CREATOR = new Creator<ContactViewShow>() {
        @Override
        public ContactViewShow createFromParcel(Parcel parcel) {
            ContactViewShow data = new ContactViewShow("","");
            data.name = parcel.readString();
            data.code = parcel.readString();
            return data;
        }

        @Override
        public ContactViewShow[] newArray(int i) {
            return new ContactViewShow[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(code);
    }
//    public abstract void viewClick();
}
