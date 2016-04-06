package com.miicaa.home.ui.person;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.miicaa.home.R;
import com.miicaa.home.ui.common.handle.CommonOnBackClickListener;
import com.miicaa.home.ui.common.handle.ICommonActivityHandler;

/**
 * 动态Activity
 * @author youynu
 *
 * Created by youynu on 14-1-7.
 */
public class DynamicActivity extends Activity implements ICommonActivityHandler {
    LinearLayout dynamicListLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dynamic_list_activity);
        initUI();
        initData();
    }

    @Override
    public void initUI() {
        dynamicListLayout = (LinearLayout) this.findViewById(R.id.dynamic_id_listview_layout);
        Button button = (Button) this.findViewById(R.id.notice_id_back);
        button.setOnClickListener(new CommonOnBackClickListener(this));

    }

    @Override
    public void initData() {
        if (dynamicListLayout != null) {
            for (int i = 0; i < 10; i++) {
                dynamicListLayout.addView(new DynamicView(this).getRootView());
                dynamicListLayout.addView(new DynamicView(this, DynamicView.DynamicShowType.SAME_SHOW).getRootView());
            }
        }
    }

    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public void refresh() {

    }
}
