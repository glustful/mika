package com.miicaa.home.ui.matter;

import android.graphics.Bitmap;

public class MatterImgManager {

	public Bitmap bitmap;
	public String fid;
	public String path;
	
	public MatterImgManager(String fid,Bitmap bitmap,String path){
		this.fid = fid;
		this.bitmap = bitmap;
		this.path = path;
	}
	
	
}
