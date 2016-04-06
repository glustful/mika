package com.miicaa.service;

import com.miicaa.home.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class ProgressNotifyCation {

//	private  static int noftifId = 0x1;
	
	private static String TAG = "ProgressNotifyCation";
	
	Context mContext;
	RemoteViews remoteViews;
	NotificationManager mNoticationManager;
	NotificationCompat mNotifcationCompat;
	NotificationCompat.Builder mNotifcationBuilder;
	
	public ProgressNotifyCation(Context context){
		this.mContext = context;
		mNoticationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifcationCompat = new NotificationCompat();
		mNotifcationBuilder = new NotificationCompat.Builder(context);
		remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notify_cation_view);
	}
	
	
	Intent mNoticationIntent;
	public void setIntentClass(Class<?> claz){
		mNoticationIntent = new Intent(mContext, claz);
	}
	
	public int noticationFlags;
	public void setNoticationFlags(int flags){
		 noticationFlags = flags;
	}
	
	public PendingIntent getDefaultIntent(int flags){
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, mNoticationIntent, flags);
		return pendingIntent;

	}
	
	public void notifyShow(int noftifId){
		mNotifcationBuilder.setContent(remoteViews)
		.setWhen(System.currentTimeMillis())
		.setTicker("miicaa")
		.setOngoing(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(getDefaultIntent(PendingIntent.FLAG_UPDATE_CURRENT));//如果有最新的intent，则放弃前面的用最新的
		
		@SuppressWarnings("deprecation")
		Notification notification = mNotifcationBuilder.getNotification();
//		notification.flags = noticationFlags;
		mNoticationManager.notify(noftifId, notification);
	}
	
	public void setContent(CharSequence text){
		remoteViews.setTextViewText(R.id.contentView, text);
//		mNoticationManager.notify(noftifId,mNotifcationBuilder.getNotification());
	}
	
	public void setProgress(int noftifId,int progress){
		Log.d(TAG, "setProgress progress:"+progress);
		remoteViews.setProgressBar(R.id.notifyProgressBar, 100, progress, false);
		remoteViews.setTextViewText(R.id.notifyProgressText, progress+"%");
		mNoticationManager.notify(noftifId,mNotifcationBuilder.getNotification());
	}
	
	public void setProgressContent(CharSequence text){
		remoteViews.setTextViewText(R.id.notifyContentText, text);
		remoteViews.setTextColor(R.id.notifyContentText, Color.BLACK);
	}
	
	public void completeProgress(int notifId,CharSequence text){
		remoteViews.setViewVisibility(R.id.notifyContentText, View.VISIBLE);
		remoteViews.setTextViewText(R.id.notifyContentText, text);
		remoteViews.setTextColor(R.id.notifyContentText, Color.BLACK);
		remoteViews.setViewVisibility(R.id.notifyProgressLayout, View.GONE);
		mNotifcationBuilder.setOngoing(false);
		mNotifcationBuilder.setAutoCancel(true);
//		.setAutoCancel(true);
		mNoticationManager.notify(notifId, mNotifcationBuilder.getNotification());
	}
	
	public void cancelAllNotify(){
		mNoticationManager.cancelAll();
	}
	

}
