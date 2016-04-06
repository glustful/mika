package com.miicaa.home.ui.common.handle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 通用view加载
 * @author youynu
 *
 * Created by youynu on 14-1-3.
 */
public class CommonView implements ICommonActivityHandler {
    /**
     * 当前上下文对象（通常为调用的context）
     */
    protected Context context;

    public CommonView() {

    }

    /**
     * 用于渲染view
     */
    protected LayoutInflater inflater;
    /**
     * 顶层view(通常为根布局)
     */
    protected View rootView;

    protected LinearLayout rootLayout;

    public CommonView(Context context) {
        this.context = context;
        init();

    }


    public void init() {
        if(context != null) {
            inflater = LayoutInflater.from(context);
        }

        initUI();
        initData();
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public View getRootView() {
        if (context != null ) {
            return  rootView;
        }
        return null;
    }

    public Context getCurrentContext() {
        return context;
    }


}
