package com.miicaa.home.attachment;

import java.io.Serializable;

public class AttachmentItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2101299422298272229L;
	public String fileId;
	public String fileExt;
	public String fileName;
	
	public AttachmentItem(String fileId,String fileExt,String fileName){
		this.fileId = fileId;
		this.fileExt = fileExt;
		this.fileName = fileName;
	}
}
