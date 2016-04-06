package com.yxst.epic.yixin.data.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yxst.epic.yixin.data.dto.response.ResponseUpload;

/**
 * @author liuxue
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectContentImage extends ObjectContent {

	private static final long serialVersionUID = -4377658450482863186L;

	public int width;

	public int height;

//	@JsonIgnore(true)
	public String filePath;
	
	public String fileExtention;

	public String fileMimeType;

	public ResponseUpload responseUpload;
}
