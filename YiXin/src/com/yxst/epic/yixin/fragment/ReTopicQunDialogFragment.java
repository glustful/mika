package com.yxst.epic.yixin.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Selection;
import android.text.TextUtils;
import android.widget.EditText;

@EFragment
public class ReTopicQunDialogFragment extends DialogFragment {

	@FragmentArg
	String topic;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// return super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("修改群标题");
		final EditText et = new EditText(getActivity());
		et.setText(topic);
		if (topic != null) {
			Selection.setSelection(et.getEditableText(), topic.length());
		}
		builder.setView(et);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newTopic = et.getText().toString();
				
				//if (!TextUtils.isEmpty(newTopic) && !newTopic.equals(topic)) {
					if (mOnTopicChangeListener != null) {
						mOnTopicChangeListener.onTopicChange(newTopic);
					}
				//}

			}
		});
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
	public static interface OnTopicChangeListener {
		void onTopicChange(String topic);
	}
	
	private OnTopicChangeListener mOnTopicChangeListener;
	
	public void setOnTopicChangeListener(OnTopicChangeListener l) {
		mOnTopicChangeListener = l;
	}
}
