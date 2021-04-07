package com.lingmiao.shop.business.sales.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
Create Date : 2021/4/79:30 AM
Auther      : Fox
Desc        :
 **/
class PayItem : MultiItemEntity {

    var type: Int = 0;

    companion object {
        val TYPE_TITLE = 0;
        val TYPE_IN = 1;
        val TYPE_IN_ITEM = 2;
        val TYPE_OUT = 3;
        val TYPE_OUT_ITEM = 4;
    }

    override fun getItemType(): Int {
        return type;
    }
}