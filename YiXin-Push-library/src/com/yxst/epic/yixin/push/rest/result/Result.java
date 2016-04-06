package com.yxst.epic.yixin.push.rest.result;

import java.io.Serializable;

public abstract class Result implements Serializable {

	private static final long serialVersionUID = -177012828786642537L;
	
	/**
	 * 0 成功
	 */
	public static final int RET_SUCCESS = 0;
	
	/**
	 * 65534 参数错误
	 */
	public static final int RET_PARAMS = 65534;
	
	/**
	 * 65535 内部错误
	 */
	public static final int RET_INNER = 65535;

	/**
	 * 0 成功;65534 参数错误;65535 内部错误
	 */
	public int ret;

	@Override
	public String toString() {
		return "Result [ret=" + ret + "]";
	}

}
