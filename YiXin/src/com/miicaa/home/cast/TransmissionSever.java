package com.miicaa.home.cast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.Consts;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.service.MessageNotify;
import com.yxst.epic.yixin.MyApplication;

/**
 * Created by Administrator on 13-11-21.
 */
//个推广播接收
public class TransmissionSever extends BroadcastReceiver {
    private NotificationManager nm = null;
    
    static String TAG = "TransmissionSever";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
//        Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(Consts.CMD_ACTION)) {
            case Consts.GET_MSG_DATA:
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    MessageNotify.getInstance().handMessage(data, nm, context);
                }
                break;
            case Consts.GET_CLIENTID:
                String cid = bundle.getString("clientid");
                Log.d(TAG, "Consts.GET_CLIENTID :"+cid);
                refreshClient(context, cid);
                Log.d("GexinSdkDemo", "Got ClientID:" + cid);
                break;
            case Consts.BIND_CELL_STATUS:
                String cell = bundle.getString("cell");
                Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
                break;
        }
    }


    private void refreshClient(Context context, final String clientId) {
        //Toast.makeText(HomeApplication.getInstance().getApplicationContext(),clientId,1000).show();
        final MyApplication account = (MyApplication) context.getApplicationContext();
        String oldClientId = account.getAccountInfo().getClientId();
        if (oldClientId == null || oldClientId.length() == 0) {
            oldClientId = clientId;
        }
        Build bd = new Build();
        String deviceInfo = "";
        String deviceType = Build.MANUFACTURER;//
        String deviceVersion = Build.MODEL;
        String osVersion = Build.VERSION.RELEASE;
        String appVersion = getAppVersion(context);
        String sysType = "ANDROID";

        if (account.getAccountInfo().getLastOrgInfo() != null && account.getAccountInfo().getLastUserInfo() != null) {
            new RequestAdpater() {
                @Override
                public void onReponse(ResponseData data) {
                    if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                        account.getAccountInfo().setClientId(clientId);
                    }
                }
                @Override
                public void onProgress(ProgressMessage msg) {
                }
            }.setUrl("/mobile/mobile/devicereg")
                    .addParam("clientCode", oldClientId)
                    .addParam("userCode", account.getAccountInfo().getLastUserInfo().getCode())
                    .addParam("clientId", clientId)
                    .addParam("appVersion", appVersion)
                    .addParam("deviceType", deviceType)
                    .addParam("deviceVersion", deviceVersion)
                    .addParam("orgCode", account.getAccountInfo().getLastOrgInfo().getCode())
                    .addParam("osVersion", osVersion)
                    .addParam("sysType", sysType)
                    .notifyRequest();
        }

    }

    private void sendMessage(String data) {

    }


    public String getAppVersion(Context context) {
        String version = "";

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String name = pi.versionName;
            int code = pi.versionCode;
            version = name;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
}
