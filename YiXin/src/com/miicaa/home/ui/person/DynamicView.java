package com.miicaa.home.ui.person;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.ui.common.handle.CommonView;

/**
 * 动态view
 * @author youynu
 *
 * Created by youynu on 14-1-7.
 */
public class DynamicView extends CommonView {
    /**
     * 为了显示动态左边的圈或者竖线
     */
    private DynamicShowType type = DynamicShowType.SINGLE_SHOW;
    private TextView dynamicTitle;

    public DynamicView(Context context) {
        this.context = context;
        init();
    }

    public DynamicView(Context context,DynamicShowType type ) {
        this.type = type;
        this.context = context;
        init();
    }

    @Override
    public void initUI() {
        super.initUI();
        if (type == DynamicShowType.SINGLE_SHOW) {
            rootView = inflater.inflate(R.layout.dynamic_record_view, null);
        } else if (type == DynamicShowType.SAME_SHOW) {
            rootView = inflater.inflate(R.layout.dynamic_record_same_view, null);
        }
        if(rootView != null) {
            dynamicTitle = (TextView) rootView.findViewById(R.id.dynamic_item_id_title);
        }

    }

    @Override
    public void initData() {
        super.initData();
        //TODO 准备动态数据

        dynamicTitle.setText(Html.fromHtml("创建了事务：<font color='#1F9272'>完成首页0.2 for Web 的向导验证ddddddddd</font> "));
    }

    public static enum DynamicShowType {
        SINGLE_SHOW,
        SAME_SHOW
    }
}

