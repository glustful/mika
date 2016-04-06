package com.miicaa.home.ui.common.handle;

import android.app.Activity;
import android.view.View;

/**
 * Created by youynu on 14-1-7.
 */
public class CommonOnBackClickListener implements View.OnClickListener {
    private  Activity activity;

    public CommonOnBackClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.finish();
    }
}
