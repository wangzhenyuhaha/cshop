package com.lingmiao.shop.business.commonpop.bean

/**
Create Date : 2021/3/811:16 AM
Auther      : Fox
Desc        :
 **/
interface ItemData {
    fun getIValue() : String?;
    fun getIName() : String?;
    fun getIHint() : String?;
    fun isItemChecked() : Boolean?;
    fun shiftChecked(flag : Boolean?);
}