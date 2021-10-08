package com.lingmiao.shop.printer;

/**
 * Create Date : 2021/10/56:18 下午
 * Auther      : Fox
 * Desc        :
 **/
public interface PrinterListener {
    void onConnect(boolean flag);
    void onDisConnect(boolean flag);
}
