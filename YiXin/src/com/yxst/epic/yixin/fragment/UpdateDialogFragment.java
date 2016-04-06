package com.yxst.epic.yixin.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentUpdate;
import com.yxst.epic.yixin.service.UpdateService;

@EFragment
public class UpdateDialogFragment extends DialogFragment {

	@FragmentArg
	Msg msg;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// return super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("有信-更新").setMessage(msg.Content);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mUpdateDialogListener != null) {
					mUpdateDialogListener.onNegativeButtonClick();
				}
			}
		});
		builder.setPositiveButton("下载安装",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mUpdateDialogListener != null) {
							mUpdateDialogListener.onPositiveButtonClick(msg);
						}

//						ObjectContentUpdate oc = Msg.readValue(
//								msg.getObjectContent(),
//								ObjectContentUpdate.class);
						ObjectContentUpdate oc = (ObjectContentUpdate) msg.getObjectContentAsObjectContent();
//						ObjectContentUpdate oc = (ObjectContentUpdate) msg.ObjectContent;
						UpdateService.download(getActivity(), oc.fileName,
								oc.url);
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (mUpdateDialogListener != null) {
					mUpdateDialogListener.onCancel();
				}
			}
		});
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					if (mUpdateDialogListener != null) {
						mUpdateDialogListener.onCancel();
					}
					return true;
				}
				return false;
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	public static interface UpdateDialogListener {
		void onNegativeButtonClick();

		void onPositiveButtonClick(Msg msg);

		void onCancel();
	}

	private UpdateDialogListener mUpdateDialogListener;

	public void setUpdateDialogListener(UpdateDialogListener l) {
		mUpdateDialogListener = l;
	}
}
