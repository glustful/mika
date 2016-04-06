package com.miicaa.home.cast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-11-19.
 */
public class Remind {
    public void sendRemind(Context context, int icon, CharSequence title, CharSequence text, Class<?> cls) {
        // Notification中包含一个RemoteView控件，实际就是通知栏默认显示的View。通过设置RemoteVIew可以自定义布局
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.remind_content_view);
        contentView.setImageViewResource(R.id.imageRemindIcon, icon);
        contentView.setTextViewText(R.id.textRemindText, text);
//        // 设置按钮点击事件，这里要放一个PendingIntent，
//        // 注意只有在在sdk3.0以上的系统中，通知栏中的按钮点击事件才能响应，这里最好加个条件，sdk3.0以下，不显示按钮
//        contentView.setOnClickPendingIntent(R.id.playpause, pauseMusicIntent());
//        if (android.os.Build.VERSION.SDK_INT >= 11) {
//            contentView.setViewVisibility(R.id.layoutRemindToolbar, View.VISIBLE);
//        } else {
            contentView.setViewVisibility(R.id.layoutRemindToolbar, View.GONE);
//        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification(icon, title, System.currentTimeMillis());
//        new Notification.Builder(context)
//                .setSmallIcon(icon)
//                .setContentTitle(title)
//                .setContentText(text)
//                .setSubText(subText)
//                .setWhen(System.currentTimeMillis())
//                .setOngoing(true)
//                .build(); //创建通知栏实例
        // 设置通知常用标志
        notification.flags = Notification.FLAG_AUTO_CANCEL;     // 点击后自动清除
        notification.defaults |=Notification.DEFAULT_SOUND;     // 声音
        notification.defaults |= Notification.DEFAULT_VIBRATE;  // 振动
//        notification.defaults |= Notification.DEFAULT_LIGHTS;   // LED灯
//        notification.ledARGB = 0xff00ff00;
//        notification.ledOnMS = 300; //亮的时间
//        notification.ledOffMS = 1000; //灭的时间
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        // 指定个性化视图
        notification.contentView = contentView;
        // 指定内容意图
        notification.contentIntent = contentIntent;

        NotificationManager barmanager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        barmanager.notify(0, notification);
    }

    public void shownotification(Context context, int icon, CharSequence title, CharSequence text, CharSequence subText)
    {
        NotificationManager barmanager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notice = new Notification(icon,title,System.currentTimeMillis());
        notice.flags=Notification.FLAG_AUTO_CANCEL;
        Intent appIntent = new Intent(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        appIntent.setComponent(new ComponentName(this.getPackageName(), this.getPackageName() + "." + this.getLocalClassName()));
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent =PendingIntent.getActivity(context, 0,appIntent,0);
        notice.setLatestEventInfo(context,text,subText, contentIntent);
        barmanager.notify(0,notice);
    }

}
