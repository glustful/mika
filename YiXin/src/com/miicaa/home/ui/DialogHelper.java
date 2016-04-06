package com.miicaa.home.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by LM on 14-9-4.
 */
public class DialogHelper  {
    public static  void showArrDiaLog(Context context,final ArrDiaLogCallBack diaLogCallBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("取消公开，将只有任务的发起人、办理人、抄送人能看到该任务")
                .setNegativeButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    	diaLogCallBack.callBack(Activity.RESULT_OK);
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        diaLogCallBack.callBack(Activity.RESULT_CANCELED);
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    public interface ArrDiaLogCallBack{
    	/*
    	 * param
    	 * Activity.RESULT_OK
    	 * Activity.RESULT_CANCELED
    	 */
        public void callBack(int result);
    }


}
