package com.miicaa.home.ui.photoGrid;

import java.io.Serializable;

/**
 * Created by LM on 14-8-12.
 */
public class ImageItem implements Serializable {
	private static final long serialVersionUID = 8429828402152512795L;
	public String iamge_id;
    public String image_path;
    public String thumbnailPath;
    public Long updateTime;
    Boolean isSelected = false;

    @Override
    public String toString(){
    	return "id="+iamge_id+" path="+image_path+" thumpath="+thumbnailPath;
    }
}
