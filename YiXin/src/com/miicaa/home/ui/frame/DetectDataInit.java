package com.miicaa.home.ui.frame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.miicaa.common.base.DatabaseOption;
import com.miicaa.home.data.business.account.AccountInfo;

/**
 * Created by Administrator on 14-2-11.
 */
public class DetectDataInit implements Runnable {

    DetectAdapter mAdapter;
    Handler mRevedHander;

    public DetectDataInit(DetectAdapter adapter, Handler handler)
    {
        mAdapter = adapter;
        mRevedHander = handler;
    }

    public String getId()
    {
        return mAdapter.getId();
    }

    @Override
    public void run()
    {

        sendResponseMessage();
    }

    private void initUnit()
    {
        String flag = DatabaseOption.getIntance().getValue(AccountInfo.instance().getLastUserInfo().getCode()+"unitInit");

    }


    private void sendResponseMessage()
    {
        Message hadnelMsg = mRevedHander.obtainMessage();
        Bundle bund = new Bundle();
        bund.putSerializable("adpater",mAdapter);
        bund.putSerializable("responseMsg","true");
        hadnelMsg.setData(bund);
        hadnelMsg.sendToTarget();
    }
}
