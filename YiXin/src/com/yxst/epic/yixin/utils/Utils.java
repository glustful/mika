package com.yxst.epic.yixin.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miicaa.home.R;
import com.miicaa.home.ui.home.FramMainActivity;
import com.yxst.epic.yixin.activity.ChatActivity_;
import com.yxst.epic.yixin.data.dto.model.Member;

public class Utils {

	private static final String TAG = "Utils";

//	public static final String HOST_API = "http://10.180.120.111:8093";
	public static final String HOST_API = "http://www.miicaa.com";
	public static final String HOST_FILE = "http://121.40.30.228:9333";
//	public static final String HOST_API = "http://10.180.120.157:8094";
//	public static final String HOST_FILE = "http://10.180.120.157:9333";
	
	/**
	 * 用户名片URL
	 */
	public static  final String URL_ERWEIMA= HOST_API + "/app/user/erweima";
	private static final String URL_AVATA = HOST_API + "/app/client/device/getUserAvatar";
	
	public static final String URL_FILE_XX = HOST_FILE + "/submit?collection=miicaa";
	public static final String URL_FILE_AVATA = HOST_FILE + "/submit?collection=miicaanavata";
	public static final String URL_FILE_IMAGE = HOST_FILE + "/submit?collection=miicaaimage";
	public static final String URL_FILE_VOICE = HOST_FILE + "/submit?collection=miicaanvoice";
	
	public static final File EXTERNAL_STORAGEDIRECTORY = Environment.getExternalStorageDirectory();
	
	public static final String FILE_PATH_DIR_IM = "/yixin";
	
	public static final String FILE_PATH_SUB_DIR_IMAGE_CAPTURE = "/image/capture";
	public static final String FILE_PATH_SUB_DIR_IMAGE_IN = "/image/in";
	public static final String FILE_PATH_SUB_DIR_IMAGE_OUT = "/image/out";
	public static final String FILE_PATH_SUB_DIR_SOUND_IN = "/voice/in";
	public static final String FILE_PATH_SUB_DIR_SOUND_OUT = "/voice/out";
	
	public static String getAndroidDeviceId(Context context) {
		String deviceId = getDeviceId(context);
		if (!TextUtils.isEmpty(deviceId)) {
			return deviceId;
		}
		String androidId = getAndroidId(context);
		if (!TextUtils.isEmpty(deviceId)) {
			return androidId;
		}
		return "null";
	}
	
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return telephonyManager.getDeviceId();
	}
	
	public static void getBuildInfo() {
		String device_model = Build.MODEL; // 设备型号   
		String version_sdk = Build.VERSION.SDK; // 设备SDK版本   
		String version_release = Build.VERSION.RELEASE; // 设备的系统版本  
	}

	public static String getAndroidId(Context context) {
		@SuppressWarnings("deprecation")
		String androidId = android.provider.Settings.System.getString(
				context.getContentResolver(),
				android.provider.Settings.System.ANDROID_ID);
		return androidId;
	}

	public static Map.Entry<String, List<Member>> mapGet(
			Map<String, List<Member>> map, int index) {
		int i = 0;
		for (Iterator<Map.Entry<String, List<Member>>> it = map.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, List<Member>> entry = it.next();

			if (i == index) {
				// return entry.getValue();
				return entry;
			}

			++i;
		}
		return null;
	}

	/**
	 * 在root中；获取Member(uid)的Parent
	 * 
	 * @param root
	 * @param memberUid
	 * @return
	 */
	public static List<Member> getParents(Member root, String memberUid) {
		List<Member> list = new ArrayList<Member>();

		Member parent = getParent(root, memberUid);
		while (parent != null) {
			list.add(parent);
			parent = getParent(root, parent.Uid);
		}

		return list;
	}

	public static Member getParent(Member root, String memberUid) {
		if (root.MemberList != null) {
			if (listContains(root.MemberList, memberUid)) {
				return root;
			} else {
				for (Iterator<Member> it = root.MemberList.iterator(); it
						.hasNext();) {
					Member m = it.next();
					return getParent(m, memberUid);
				}
			}
		}
		return null;
	}

	/**
	 * list直接包含Member（ uid）
	 * 
	 * @param member
	 * @param memberUid
	 * @return
	 */
	public static boolean listContains(List<Member> list, String memberUid) {
		for (Iterator<Member> it = list.iterator(); it.hasNext();) {
			Member m = it.next();
			if (m.Uid.equals(memberUid)) {
				return true;
			}
		}
		return false;
	}

	public static int listIndexOf(List<Member> list, String memberUid) {
		int retVal = -1;

		for (Iterator<Member> it = list.iterator(); it.hasNext();) {
			++retVal;
			Member m = it.next();
			if (m.Uid.equals(memberUid)) {
				return retVal;
			}
		}
		return -1;
	}

	public static Member listGet(List<Member> list, String memberUid) {
		for (Iterator<Member> it = list.iterator(); it.hasNext();) {
			Member m = it.next();
			if (m.Uid.equals(memberUid)) {
				return m;
			}
		}
		return null;
	}

	public static Member listFindMember(Member root, String memberUid) {
		if (root.Uid.equals(memberUid)) {
			return root;
		} else {
			for (Iterator<Member> it = root.MemberList.iterator(); it.hasNext();) {
				Member m = it.next();
				return listFindMember(m, memberUid);
			}
		}
		return null;
	}

	public static List<Member> listFindPath(Member root, String memberUid) {
		List<Member> path = new ArrayList<Member>();
		if (listFindPath(path, root, memberUid, 0)) {
			return path;
		}

		path.clear();
		if (root != null) {
			path.add(root);
		}

		return path;
	}

	private static boolean listFindPath(List<Member> path, Member root,
			String memberUid, int level) {

		if (path.size() > level) {
			path.set(level, root);
			path.removeAll(new ArrayList<Member>(path.subList(level + 1,
					path.size())));
		} else {
			path.add(root);
		}

		if (root.Uid.equals(memberUid)) {
			return true;
		} else {
			if (root.MemberList != null) {
				for (Iterator<Member> it = root.MemberList.iterator(); it
						.hasNext();) {
					if (listFindPath(path, it.next(), memberUid, level + 1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Show a notification while this service is running.
	 */
	public static void showNotification(Context context, CharSequence title,
			CharSequence content) {
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		// CharSequence text = context.getText(R.string.remote_service_started);

		// Set the icon, scrolling text and timestamp
		// Notification notification = new Notification(R.drawable.ic_launcher,
		// text,
		// System.currentTimeMillis());
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		// Intent intent = new Intent(context, MainActivity.class);
		
		Intent intent = new Intent(context, FramMainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse("custom://"+System.currentTimeMillis()));
		intent.putExtra("fanye", 1);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the info for the views that show in the notification panel.
		// notification.setLatestEventInfo(context,
		// context.getText(R.string.remote_service_label),
		// text, contentIntent);
		notification.setLatestEventInfo(context, title, content, contentIntent);

		notification.defaults = Notification.DEFAULT_ALL;

		// Send the notification.
		// We use a string id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.id.notification, notification);
	}

	public static void cancelNotification(Context context) {
		NotificationManager mNM = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNM.cancel(R.id.notification);
	}

	/**
	 * 返回当前的应用是否处于前台显示状态
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean isTopApp(Context context, String packageName) {
		// _context是一个保存的上下文
		ActivityManager am = (ActivityManager) context.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am
				.getRunningAppProcesses();
		if (list.size() == 0)
			return false;
		for (ActivityManager.RunningAppProcessInfo process : list) {
			Log.d(TAG, Integer.toString(process.importance));
			Log.d(TAG, process.processName);
			if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& process.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTopActivity(Context context, String activityName) {
		ActivityManager am = (ActivityManager) context.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list == null || list.size() == 0)
			return false;
		for (RunningTaskInfo info : list) {
			// Log.d(TAG, "info.topActivity:" + info.topActivity);
			// Log.d(TAG, "info.baseActivity:" + info.baseActivity);
			if (info.topActivity.getClassName().equals(activityName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	public static boolean isLocked(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		// keyguardManager.isKeyguardLocked();
		return keyguardManager.inKeyguardRestrictedInputMode();
	}

	public static boolean isSouldNotification(Context context) {
		Class<?> clazz = FramMainActivity.class;

		Log.d(TAG, "isLocked:" + Utils.isLocked(context));
		Log.d(TAG, "isScreenOn:" + Utils.isScreenOn(context));
		Log.d(TAG,
				"isTopActivity:"
						+ isTopActivity(context, ChatActivity_.class.getName()));
		Log.d(TAG, "isTopActivity:" + isTopActivity(context, clazz.getName()));

		boolean isTopActivity = isTopActivity(context,
				ChatActivity_.class.getName())
				|| isTopActivity(context, clazz.getName());

		return !isScreenOn(context) || isLocked(context) || !isTopActivity;
	}

	public static void playRingtone(Context context) {
		try {
			RingtoneManager mRingtoneManager = new RingtoneManager(context);
			mRingtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION);

			// // The volume keys will control the stream that we are choosing a
			// ringtone for
			// context.setVolumeControlStream(mRingtoneManager.inferStreamType());

			// Stop playing the previous ringtone
			mRingtoneManager.stopPreviousRingtone();

			Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context,
					RingtoneManager.TYPE_NOTIFICATION);
			Ringtone ringtone = RingtoneManager.getRingtone(context, uri);

			if (ringtone != null) {
				ringtone.play();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String format(long timeMillis, String pattern) {
		return format(new Date(timeMillis), pattern);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat f = new SimpleDateFormat(pattern);
			return f.format(date);
		}
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String format(Date date) {
		if (date != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f.format(date);
		}
		return null;
	}

	public static String format(long timeMillis) {
		return format(new Date(timeMillis));
	}

	public static void longLogD(String tag, String msg) {
		Log.d(tag, "longLogD");
		if (msg != null) {
			int start = 0;
			int end = Math.min(1000, msg.length());
			while (start < end) {
				Log.d(tag, msg.substring(start, end));
				start = end;
				end = Math.min(end + 1000, msg.length());
			}
		}
		Log.d(tag, "longLogD");
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File ensureIMDir(Context context) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(Environment.getExternalStorageDirectory(),
					FILE_PATH_DIR_IM);
			if (!savedir.exists()) {
				if (!savedir.mkdirs()) {
					return null;
				}
			}
			return savedir;
		}
		return null;
	}

	public static File ensureIMSubDir(Context context, String name) {
		File dir = ensureIMDir(context);
		if (dir != null) {
			File subDir = new File(dir, name);
			if (!subDir.exists()) {
				if (!subDir.mkdirs()) {
					return null;
				}
			}
			return subDir;
		}
		return null;
	}

	public static boolean createNoMediaFile(File dirFile) {
		File file = new File(dirFile, ".nomedia");
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void showToastShort(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static String getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
        return path.substring(lastDot + 1);
//        return path.substring(lastDot + 1).toUpperCase(Locale.getDefault());
    }
	
	public static String writeValueAsString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T readValue(String str, Class<T> t) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(str, t);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(fromValue, toValueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private static Bitmap Create2DCode(String str) throws WriterException {
//		Map hints = new HashMap();
//		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//
//		BitMatrix matrix = new MultiFormatWriter().encode(str,
//				BarcodeFormat.QR_CODE, 300, 300, hints);
//		int width = matrix.getWidth();
//		int height = matrix.getHeight();
//		int[] pixels = new int[width * height];
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				if (matrix.get(x, y)) {
//					pixels[y * width + x] = 0xff000000;
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
//	
	public static String getAvataUrl(String userName) {
		return getAvataUrl(userName, 0, 0);
	}
	
	public static String getAvataUrl(String userName, int size) {
		return getAvataUrl(userName, size, size);
	}
	
	public static String getAvataUrl(String userName, int width, int height) {
//		if (TextUtils.isEmpty(userName)) {
//			return null;
//		}
		try {
			UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(URL_AVATA);
			ucb.queryParam("userName", userName);
			ucb.queryParam("width", width);
			ucb.queryParam("height", height);
			UriComponents uc = ucb.build();
			return uc.toUriString();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	public static String getImgUrl(String url) {
		return getImgUrl(url, 0);
	}
	
	public static String getImgUrl(String url, int size) {
		return getImgUrl(url, size, size);
	}
	
	public static String getImgUrl(String url, int width, int height) {
//		if (TextUtils.isEmpty(url)) {
//			return null;
//		}
		try {
			UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(url);
			ucb.queryParam("width", width);
			ucb.queryParam("height", height);
			UriComponents uc = ucb.build();
			return uc.toUriString();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	public static String addQueryParams(String url, Map<String, Object> params) {
		try {
			UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(url);
			if (params != null) {
				for (Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					ucb.queryParam(entry.getKey(), entry.getValue());
				}
			}
			UriComponents uc = ucb.build();
			return uc.toUriString();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	
	/*
	 * 从七天前的离线消息开始拉
	 */
	public static long TIME_IN_MILLS_ONE_DAY = 24 * 60 * 60 *1000 *7;
	public static long getMid7Day(){
		return(System.currentTimeMillis() - TIME_IN_MILLS_ONE_DAY) * 1000;
	}
	
//	public static void appmsgsrv(Context context) {
//		context.getResources().getString(R.string.appmsgsrv_protocol);
//		context.getResources().getString(R.string.appmsgsrv_host);
////		context.getResources().getInteger(R.integer.appmsgsrv_port);
//		
////		URL url = new URL(protocol, host, port, file)
//	}
	
	
}
