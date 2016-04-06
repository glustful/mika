package com.miicaa.home.ui;

import android.app.Application;

import com.miicaa.common.base.Debugger;
import com.miicaa.home.data.DataCenter;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.service.CacheCtrlSrv;

/**
 * Created by Administrator on 13-11-22.
 */
public class HomeApplication extends Application {
    private static HomeApplication instance;

    public static HomeApplication getInstance() {
        return instance;
    }

    private AccountInfo accountInfo = AccountInfo.instance();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Debugger.init(this);
        DataCenter.intance();

        CacheCtrlSrv.init(this);
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }
}
