package com.yxst.epic.yixin.data.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = 5629569037982738876L;

//	OK               = 0
//	NotFoundServer   = 1001
//	NotFound         = 65531
//	TooLong          = 65532
//	AuthErr          = 65533
//	ParamErr         = 65534
//	InternalErr      = 65535
	
	public static final int RET_SUCCESS = 0;
	public static final int RET_ERROR_TOKEN = 65533;
	public static final int RET_ERROR_PARAM = 65534;
	public static final int RET_ERROR = 65535;
	public static final int RET_ERROR_LOGIN = 65539;
	
	/**
	 * 返回码：0成功
	 */
	@JsonProperty(value="ret")
	public Integer Ret;

	/**
	 * （错误）信息
	 */
	@JsonProperty(value="errMsg")
	public String ErrMsg;

}
