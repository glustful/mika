package com.yxst.epic.yixin.data.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yxst.epic.yixin.data.dto.response.ResponseUpload;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectContentVoice extends ObjectContent {

	private static final long serialVersionUID = -1347663935996419372L;
	
//	@JsonIgnore(true)
	public String filePath;
	
	public String fileExtention;
	public String fileMimeType;
	
	public long voiceLength;
	
//	@JsonIgnore(true)
	public boolean isListened;
	
	public ResponseUpload responseUpload;
}
