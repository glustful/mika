package com.miicaa.home.cast;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

import com.igexin.slavesdk.MessageManager;
import com.miicaa.common.base.Tools;
import com.miicaa.home.data.storage.LocalPath;

/**
 * 
 */
public class PushMessage {
    public static void initPushMessage(Context context) {
        try {
            File initFile = new File(LocalPath.intance().configBasePath + "init");
            if (!initFile.exists()) {//配置文件不存在，表示新版应用是第一次安装
                String path = Environment.getExternalStorageDirectory().getPath() + "/libs";
                File getui1 = new File(path + "/com.yx.prohome.db");
                File getui2 = new File(path + "/com.yx.prohome1.db");
                if (getui1.exists() || getui2.exists()) {
                    Tools.deleteAllFilesOfDir(new File(path));//删除包名更改之前的个推配置
                }
                FileOutputStream fout = new FileOutputStream(initFile);
                byte[] bytes = "program init v1".getBytes();
                fout.write(bytes);
                fout.close();//写文件，表名已经初始化
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageManager.getInstance().initialize(context);
    }

    public static void stopPushMessage(Context context) {
        MessageManager.getInstance().stopService(context);
    }

    //智游初始化
    public static void initSmartGamePush(Context context) {

    }

    //极光初始化
    public static void initAuroraPush(Context context) {

    }
}
