package com.miicaa.service;

import com.miicaa.home.ui.guidepage.GuidePageActivity;
import com.miicaa.utils.AllUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class VersionBroadCaseReceiver extends BroadcastReceiver{

	AlertDialog.Builder builder ;
	
	private static String TAG = "VersionBroadCaseReceiver";
	 public VersionBroadCaseReceiver() {
		  
	}
	
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		AllUtils.showVersionDialog(context);
	}

}
