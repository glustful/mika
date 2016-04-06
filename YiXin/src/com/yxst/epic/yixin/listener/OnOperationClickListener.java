package com.yxst.epic.yixin.listener;

import java.util.List;

import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;

public interface OnOperationClickListener {

	void onOperationClick(Operation operation);

	void onOperationsClick(List<Operation> operations);
}
