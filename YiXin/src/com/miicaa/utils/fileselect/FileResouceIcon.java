package com.miicaa.utils.fileselect;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.miicaa.home.R;

public  class FileResouceIcon {

	
	public FileResouceIcon(){
	}
	
	public  static Drawable getResouceIconId(Context context , String fileName){
		Resources re =  context.getResources();
		Drawable resourcesId;
		assert fileName != null;
		if(fileName.endsWith(".png") || fileName.endsWith(".jpg")||fileName.endsWith(".jpeg")||
				fileName.endsWith(".bmp")){
			resourcesId = re.getDrawable(R.drawable.a);
		}else if(fileName.endsWith(".pdf")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_pdf);
		}else if(fileName.endsWith(".ppt")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_ppt);
		}else if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_word);
		}else if(fileName.endsWith("xls")||fileName.endsWith("xlsx")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_execl);
		}else if(fileName.endsWith("ppt")||fileName.endsWith("pptx")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_ppt);
		}else if(fileName.endsWith(".txt")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_txt);
		}else if(fileName.endsWith(".zip") || fileName.endsWith(".rar")){
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_rar);
		}else{
			resourcesId = re.getDrawable(R.drawable.accessory_file_ico_normal);
		}
		return resourcesId;
	};
	
}
