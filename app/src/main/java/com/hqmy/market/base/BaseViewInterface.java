package com.hqmy.market.base;


/**
 * Created by dahai on 2017/1/14.
 */

public interface BaseViewInterface {
    /**
     * 获取资源文件ID
     *
     * @return
     */
    int getLayoutId();

    /**
     * 初始化界面
     */
    void initView();

    /**
     * 初始化数据
     */
    void initData();

}
