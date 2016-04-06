package com.yxst.epic.yixin.data.dto.model;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseMsg implements Serializable{

	private static final long serialVersionUID = -6729383626696511814L;

	public static final int BASEMSGTYPE_YOUXIN = 0;
	
	@JsonProperty(value="BaseMsgTypeCode")
	public int BaseMsgTypeCode;
	
	@JsonProperty(value="BaseMsgTypeDesc")
	public String BaseMsgTypeDesc;
	
	public static Msg readValue(String msg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(msg, Msg.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
