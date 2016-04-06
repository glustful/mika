package com.yxst.epic.yixin.push.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.yxst.epic.yixin.push.cli.Listener;
import com.yxst.epic.yixin.push.cli.PushCli;
import com.yxst.epic.yixin.push.cli.PushMessage;
import com.yxst.epic.yixin.push.util.Utils;

public abstract class PushCliService extends Service {

	private static final String TAG = "PushCliService";

	// /**
	// * 推送服务Service 的 Action Name
	// */
	// public static final String ACTION_NAME =
	// "com.yxtech.example.service.GoPushCliService";

	private String mTagLocalOld;

	private PushCli mPushCli;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");

		// AlarmReceiver.alarm(this);
		startServiceRepeatly(this);

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand() " + this);

		Log.d(TAG, "onStartCommand() isPushCliRunning():"
				+ isGoPushCliRunning());

		// startForeground(0, null);

		String tagLocalNew = getKey();
		String tagLocalOld = mTagLocalOld;
		mTagLocalOld = tagLocalNew;

		boolean isNetworkAvailable = Utils.isNetworkAvailable(this);

		Log.d(TAG, "onStartCommand() tagLocalNew:" + tagLocalNew);
		Log.d(TAG, "onStartCommand() tagLocalOld:" + tagLocalOld);
		Log.d(TAG, "onStartCommand() isNetworkAvailable:" + isNetworkAvailable);

		if (TextUtils.isEmpty(tagLocalNew) || !tagLocalNew.equals(tagLocalOld)
				|| !isNetworkAvailable) {
			destroyGoPushCli();
		}

		if (!TextUtils.isEmpty(tagLocalNew) && !isGoPushCliRunning()
				&& isNetworkAvailable) {
			destroyGoPushCli();
			gopushInit(tagLocalNew);
			testSync();
		}

		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");

		// AlarmReceiver.alarm(this);

		// stopForeground(true);
		destroyGoPushCli();

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * start the service through alarm repeatly
	 */
	private void startServiceRepeatly(Context context) {
		// Intent intent = new Intent(getApplicationContext(),
		// GoPushCliService.class);
		// Intent intent = new Intent(ACTION_NAME);
		// Intent intent = new Intent(context, getGoPushCliService(context));
		Intent intent = getGoPushCliServiceIntent(context);
		AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent mPendingIntent = PendingIntent.getService(this, 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 15*1000,
				mPendingIntent);
	}

	/**
	 * 推送服务是否正常运行
	 * 
	 * @return
	 */
	private boolean isGoPushCliRunning() {
		PushCli cli = mPushCli;
		return cli != null && cli.isGetNode() && cli.isHandshake();
	}

	/**
	 * 销毁推送服务
	 */
	private void destroyGoPushCli() {
		Log.d(TAG, "destroyPushCli()");
		PushCli cli = mPushCli;
		if (cli != null) {
			cli.destory();
		}
		mPushCli = null;
	}

	/**
	 * 初始化推送服务
	 * 
	 * @param key
	 */
	private void gopushInit(String key) {
		// mGoPushCli = new GoPushCli("115.29.107.77", 8090, key, 30, 0, 0,
		// mGoPushCli = new GoPushCli("10.180.128.161", 8090, key, 30, 0, 0,
//		mGoPushCli = new GoPushCli(getHost(), getPort(), key, 30, 0, 0,
		mPushCli = new PushCli(getHost(), getPort(), key, 30/*180*/, getMid(), 0,
				new Listener() {
					@Override
					public void onOpen() {
						Log.w(TAG, "PushCli onOpen()");
					}

					@Override
					public void onOnlineMessage(PushMessage message) {
						Log.w(TAG, "PushCli onOnlineMessage() message:" + message);

						try {
							PushCliService.this.onOnlineMessage(message);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onOfflineMessage(ArrayList<PushMessage> messages) {
						Log.w(TAG, "PushCli onOfflineMessage() messages:" + messages);

						try {
							PushCliService.this.onOfflineMessage(messages);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Throwable e, String message) {
						Log.w(TAG, "PushCli onError() message:" + message);
						e.printStackTrace();
						destroyGoPushCli();
					}

					@Override
					public void onClose() {
						Log.w(TAG, "PushCli onClose()");
						destroyGoPushCli();
					}
				});
	}

	/**
	 * 启动推送服务 - 异步订阅
	 */
	protected void testNoSync() {
		PushCli cli = mPushCli;
		cli.start(false);
	}

	/**
	 * 启动推送服务 - 同步订阅
	 */
	private void testSync() {
		final PushCli cli = mPushCli;
		new Thread() {
			public void run() {
				cli.start(true);
			}
		}.start();
	}

	/**
	 * 开启推送服务Service
	 * 
	 * @param context
	 */
	public static void startService(Context context) {
		// Intent intent = new Intent(context, GoPushCliService.class);
		// Intent intent = new Intent(ACTION_NAME);
		// Intent intent = new Intent(context, getGoPushCliService(context));
		// context.startService(intent);

		context.startService(getGoPushCliServiceIntent(context));
	}

	/**
	 * 停止
	 * 
	 * @param context
	 */
	public static void stopService(Context context) {
		// Intent intent = new Intent(context, GoPushCliService.class);
		// Intent intent = new Intent(ACTION_NAME);
		// Intent intent = new Intent(context, getGoPushCliService(context));
		// context.stopService(intent);

		context.stopService(getGoPushCliServiceIntent(context));
	}

	private static Intent getGoPushCliServiceIntent(Context context) {
		Intent intent = new Intent(PushCliService.class.getName());
		intent.setPackage(context.getPackageName());
		
		List<ResolveInfo> resolveInfos = context.getPackageManager()
				.queryIntentServices(intent, 0);
		
		Log.d(TAG, "List<ResolveInfo>:" + resolveInfos);
		
		if (resolveInfos != null) {
			for (ResolveInfo info : resolveInfos) {
				Intent i = new Intent();
				i.setClassName(info.serviceInfo.packageName, info.serviceInfo.name);
				return i;
			}
		}
		return null;
	}

	// private static Class<?> getGoPushCliService(Context context) {
	//
	// try {
	// ApplicationInfo info = context.getPackageManager().getApplicationInfo(
	// context.getPackageName(), PackageManager.GET_META_DATA);
	// String className = info.metaData.getString("GoPushCliService");
	// return Class.forName(className);
	// } catch (NameNotFoundException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * 实现该方法，设置推送服务的Host
	 * 
	 * @return
	 */
	protected abstract String getHost();

	/**
	 * 实现该方法，设置推送服务的Port
	 * 
	 * @return
	 */
	protected abstract int getPort();

	/**
	 * 实现该方法，设置推送服务的KEY(TAG)
	 * 
	 * @return
	 */
	protected abstract String getKey();

	/**
	 * 实现该方法，设置上次接收私信推送以来最大的mid
	 * 
	 * @return
	 */
	protected abstract long getMid();
	
	/**
	 * 推送服务 在线消息回调
	 * 
	 * @param message
	 */
//	protected abstract void onOnlineMessage(PushMessage message);
	protected void onOnlineMessage(PushMessage message) {
		executorService.submit(new ListenerNotification(message));
	}

	/**
	 * 推送服务 离线消息回调
	 * 
	 * @param messages
	 */
//	protected abstract void onOfflineMessage(ArrayList<PushMessage> messages);
	protected void onOfflineMessage(ArrayList<PushMessage> messages) {
		executorService.submit(new ListenerNotification(messages));
	}

	protected final Map<PushMessageListener, ListenerWrapper> recvListeners = new ConcurrentHashMap<PushMessageListener, ListenerWrapper>();

	public void addPushMessageListener(PushMessageListener msgListener,
			PushMessageFilter msgFilter) {
		if (msgListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
		ListenerWrapper wrapper = new ListenerWrapper(msgListener, msgFilter);
		recvListeners.put(msgListener, wrapper);
	}
	
	protected static class ListenerWrapper {

        private PushMessageListener packetListener;
        private PushMessageFilter packetFilter;

        public ListenerWrapper(PushMessageListener packetListener, PushMessageFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public void notifyListenerOnlineMessage(PushMessage message) {
            if (packetFilter == null || packetFilter.acceptOnlineMessage(message)) {
                packetListener.processOnlineMessage(message);
            }
        }
        
        public void notifyListenerOfflineMessage(ArrayList<PushMessage> messages) {
        	if (packetFilter == null || packetFilter.acceptOfflineMessage(messages)) {
//        		packetListener.processOfflineMessage(messages);
        		packetListener.processOfflineMessage(packetFilter.getAcceptOfflineMessage(messages));
        	}
        }
    }
	
	private class ListenerNotification implements Runnable {

        private PushMessage message;
        private ArrayList<PushMessage> messages;

        public ListenerNotification(PushMessage message) {
            this.message = message;
        }
        
        public ListenerNotification(ArrayList<PushMessage> messages) {
        	this.messages = messages;
        }

        public void run() {
            for (ListenerWrapper listenerWrapper : recvListeners.values()) {
            	
            	if (message != null) {
            		try {
            			listenerWrapper.notifyListenerOnlineMessage(message);
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
            	}
            	
            	if (messages != null) {
            		try {
            			listenerWrapper.notifyListenerOfflineMessage(messages);
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
            	}
            }
        }
    }
	
	private final static AtomicInteger connectionCounter = new AtomicInteger(0);
	
	private final int connectionCounterValue = connectionCounter.getAndIncrement();
	
	private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new SmackExecutorThreadFactory(connectionCounterValue));
	
	private static final class SmackExecutorThreadFactory implements ThreadFactory {
        private final int connectionCounterValue;
        private int count = 0;

        private SmackExecutorThreadFactory(int connectionCounterValue) {
            this.connectionCounterValue = connectionCounterValue;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Smack Executor Service " + count++ + " ("
                            + connectionCounterValue + ")");
            thread.setDaemon(true);
            return thread;
        }
    }
}
