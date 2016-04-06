package com.yxst.epic.yixin.upload;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 上午1:04
 */
public class UploadService extends Service {

    private static final String TAG = "UploadService";
    
	private static UploadManager UPLOAD_MANAGER;

    public static UploadManager getUploadManager(Context appContext) {
//    	Log.d(TAG, "getUploadManager() UploadService.isServiceRunning(appContext):" + UploadService.isServiceRunning(appContext));
//        if (!UploadService.isServiceRunning(appContext)) {
//            Intent downloadSvr = new Intent("upload.service.action");
//            appContext.startService(downloadSvr);
//        }
//        Log.d(TAG, "getUploadManager() UploadService.isServiceRunning(appContext):" + UploadService.isServiceRunning(appContext));
        if (UploadService.UPLOAD_MANAGER == null) {
            UploadService.UPLOAD_MANAGER = new UploadManager(appContext);
        }
        return UPLOAD_MANAGER;
    }

    public UploadService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        if (UPLOAD_MANAGER != null) {
            try {
                UPLOAD_MANAGER.stopAllUpload();
                UPLOAD_MANAGER.backupUploadInfoList();
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        super.onDestroy();
    }

    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(UploadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
