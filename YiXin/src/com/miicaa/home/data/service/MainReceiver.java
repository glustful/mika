package com.miicaa.home.data.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.miicaa.common.base.Utils;

/**
 * Created by LM on 14-9-17.
 */
public class MainReceiver extends BroadcastReceiver {
    ReceiverCallBack mRcallBack;
    public MainReceiver(Context context,ReceiverCallBack callBack){
        mRcallBack = callBack;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        int code = bundle.getInt("update");
        switch (code){
            case Utils.RECEIVER_CHANGE:
                mRcallBack.callBack();
                break;
            default:
                break;
        }
    }

    public interface ReceiverCallBack{
        public void callBack();
    }
}
