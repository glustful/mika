package com.yxst.epic.yixin.service;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.miicaa.home.R;

public class UpdateService extends Service {

	private static final String TAG = "UpdateService";

	private static final String EXTRA_FILE_NAME = "fileName";
	private static final String EXTRA_URL = "url";

	public static void download(Context context, String fileName, String url) {
		Intent intent = new Intent(context, UpdateService.class);
		intent.putExtra(EXTRA_FILE_NAME, fileName);
		intent.putExtra(EXTRA_URL, url);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		if (handler != null && !handler.isCancelled()) {
			handler.cancel();
		}

		super.onDestroy();
	}

	HttpHandler<File> handler;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		if (intent != null) {
			if (handler == null || handler.isCancelled()) {
				String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
				String url = intent.getStringExtra(EXTRA_URL);
				Log.d(TAG, "onStartCommand() fileName:" + fileName);
				Log.d(TAG, "onStartCommand() url:" + url);
				
				HttpUtils http = new HttpUtils();
				handler = http.download(url,
						new File(getDir(this), fileName).toString(), false, true,
						new MyRequestCallBack());
			}
		}

		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	// private static String getFileName(String url) {
	// if (url != null) {
	//
	// }
	// return null;
	// }

	private class MyRequestCallBack extends RequestCallBack<File> {
		@Override
		public void onStart() {
			Log.d(TAG, "MyRequestCallBack.onStart()");
			super.onStart();

			showNotification("下载开始");
		}

		@Override
		public void onCancelled() {
			Log.d(TAG, "MyRequestCallBack.onCancelled()");
			super.onCancelled();

			// showNotification("下载取消");
			// cancelNotification();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			Log.d(TAG, "MyRequestCallBack.onLoading()");
//			Log.d(TAG, "MyRequestCallBack.onLoading() current:" + current);
//			Log.d(TAG, "MyRequestCallBack.onLoading() total:" + total);
			super.onLoading(total, current, isUploading);

			showNotification("下载中..." + (int) (100 * current / total) + "%");
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Log.d(TAG, "MyRequestCallBack.onFailure() msg:" + msg);
			showNotification("下载失败");
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			Log.d(TAG, "MyRequestCallBack.onSuccess()");

			PendingIntent contentIntent = PendingIntent.getActivity(
					UpdateService.this, 0,
					installApkIntent(UpdateService.this, responseInfo.result),
					0);
			showNotification("下载完成", contentIntent);

			installApk(UpdateService.this, responseInfo.result);

			stopSelf();
		}
	}

	private NotificationManager mNM;

	private void showNotification(CharSequence text) {
		showNotification(text, null);
	}

	private void showNotification(CharSequence text, PendingIntent contentIntent) {
		int moodId = R.drawable.update_package_download_anim0;

		// // In this sample, we'll use the same text for the ticker and the
		// // expanded notification
		// CharSequence text = getText(textId);

		// Set the icon, scrolling text and timestamp.
		// Note that in this example, we pass null for tickerText. We update the
		// icon enough that
		// it is distracting to show the ticker text every time it changes. We
		// strongly suggest
		// that you do this as well. (Think of of the "New hardware found" or
		// "Network connection
		// changed" messages that always pop up)
		Notification notification = new Notification(moodId, null,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, WelcomeActivity_.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, "更新包下载", text, contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.id.update_notification, notification);
	}

	private void cancelNotification() {
		mNM.cancel(R.id.update_notification);
	}

	public static Intent installApkIntent(Context context, File file) {
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	public static void installApk(Context context, File file) {
		// 下载完成，点击安装
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static String getDir(Context context) {
		if (hasSDCard()) {
			return getSDPath();
		} else {
			context.getCacheDir().toString();
		}
		return null;
	}

	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	public static String getSDPath() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}
		return null;
	}

}
