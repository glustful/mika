package com.miicaa.home.ui.common.handle;

import android.view.View;

/**
 * 常用Activity实现方法，便于初始化调用
 * @author youynu
 * Created by youynu on 14-1-3.
 */
public interface ICommonActivityHandler {
    /**
     * 获取当前view的根view
     * @return  根view对象
     */
    public View getRootView();

    /**
     * 刷新当前view
     */
    public void refresh();

    /**
     * 初始化UI操作
     */
    public void initUI();

    /**
     * 初始化UI数据操作
     */
    public void initData();

}
