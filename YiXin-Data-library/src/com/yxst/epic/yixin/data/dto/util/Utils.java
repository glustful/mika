package com.yxst.epic.yixin.data.dto.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	public static String writeValueAsString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T readValue(String str, Class<T> t) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return (T) mapper.readValue(str, t);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(fromValue, toValueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
