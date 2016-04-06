package com.yxst.epic.yixin.data.dto.response;

import java.util.List;

import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;

public class GetAppOperationListResponse extends Response {

	private static final long serialVersionUID = 3281405056415312352L;

	public int operationCount;
	
	public List<Operation> operationList;
}
