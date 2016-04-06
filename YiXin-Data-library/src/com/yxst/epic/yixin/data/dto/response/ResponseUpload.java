package com.yxst.epic.yixin.data.dto.response;

/**
 * 
 * {
 * <p>
 * "fid": "3,4629f0e314",
 * <p>
 * "fileName": "sample04.jpg",
 * <p>
 * "fileUrl": "115.29.107.77:5083/3,4629f0e314",
 * <p>
 * "size": 1722664
 * <p>
 * }
 * 
 * @author liuxue
 *
 */
public class ResponseUpload {
	
	public String fid;
	
	public String fileName;
	
	public String fileUrl;
	
	public long size;
	
	/**
	 * EOF
	 */
	public String error;

	@Override
	public String toString() {
		return "ResponseUpload [fid=" + fid + ", fileName=" + fileName
				+ ", fileUrl=" + fileUrl + ", size=" + size + ", error="
				+ error + "]";
	}
	
}
