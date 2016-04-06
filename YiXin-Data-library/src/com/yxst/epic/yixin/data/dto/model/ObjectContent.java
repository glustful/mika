package com.yxst.epic.yixin.data.dto.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.yxst.epic.yixin.data.dto.util.Utils;

//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="msgType")
@JsonSubTypes({
	@JsonSubTypes.Type(name = "2", value = ObjectContentImage.class),
		@JsonSubTypes.Type(name = "101", value = ObjectContentApp101.class),
		@JsonSubTypes.Type(name = "1001", value = ObjectContentUpdate.class) })
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ObjectContent implements Serializable{

	private static final long serialVersionUID = 2206264894456970339L;
	
	public int MsgType;
	
	public static <T extends ObjectContent> T readValue(String str, Class<T> t) {
		return Utils.readValue(str, t);
	}
	
	@Override
	public String toString() {
		return Utils.writeValueAsString(this);
	}
}
