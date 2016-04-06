package com.yxst.epic.yixin.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.fragment.UpdateDialogFragment;
import com.yxst.epic.yixin.fragment.UpdateDialogFragment_;

@EActivity
public class UpdateEnableActivity extends FragmentActivity {

	@Extra
	Msg msg;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		UpdateDialogFragment f = UpdateDialogFragment_.builder().msg(msg)
				.build();
		f.setUpdateDialogListener(mUpdateDialogListener);
		f.show(getSupportFragmentManager(), "update");
	}
	
	private UpdateDialogFragment.UpdateDialogListener mUpdateDialogListener = new UpdateDialogFragment.UpdateDialogListener() {
		@Override
		public void onNegativeButtonClick() {
			finish();
		}

		@Override
		public void onPositiveButtonClick(Msg msg) {
			finish();
		}

		@Override
		public void onCancel() {
			finish();
		}
	};
}
