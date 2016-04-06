package com.yxst.epic.yixin.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;
import com.yxst.epic.yixin.listener.OnOperationClickListener;

@EFragment
public class OperationsMoreDialogFragment extends DialogFragment implements OnClickListener {

//	@FragmentArg
	List<Operation> operations;
	
	@SuppressWarnings("unchecked")
	@AfterInject
	void afterInject() {
		Bundle args = getArguments();
		if (args != null) {
			this.operations = (List<Operation>) args.getSerializable("operations");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
//		OperationAdapter adapter = new OperationAdapter(this.operations);
//		builder.setAdapter(adapter, this);
		
		CharSequence[] items = getItems();
		builder.setItems(items, this);
		
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
	private CharSequence[] getItems() {
		if (this.operations != null) {
			int N = this.operations.size();
			CharSequence[] items = new CharSequence[N];
			for (int i = 0; i < N; i++) {
				Operation operation = this.operations.get(i);
				items[i] = operation.content;
			}
			return items;
		}
		return null;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mOnOperationClickListener != null) {
			Operation operation = this.operations.get(which);
			mOnOperationClickListener.onOperationClick(operation);
		}
	}

	private OnOperationClickListener mOnOperationClickListener;
	
	public void setOnOperationClickListener(OnOperationClickListener l) {
		mOnOperationClickListener = l;
	}
}
