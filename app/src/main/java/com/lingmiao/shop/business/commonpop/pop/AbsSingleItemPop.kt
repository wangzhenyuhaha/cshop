package com.lingmiao.shop.business.commonpop.pop

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/810:47 AM
Auther      : Fox
Desc        :
 **/
@Deprecated("暂时用不到")
abstract class AbsSingleItemPop<T : ItemData>(context: Context, override var title : String?)
    : AbsThreeItemPop<T>(context, title) {

    override fun getLevel(): Int {
        return LEVEL_1;
    }

    override fun getData2(data1: T): List<T> {
        return mutableListOf();
    }

    override fun getData3(data2 : T) : List<T> {
        return mutableListOf();
    }

    override fun getAdapter2(): BaseQuickAdapter<T, BaseViewHolder> {
        return DefaultItemAdapter<T>();
    }

    override fun getAdapter3(): BaseQuickAdapter<T, BaseViewHolder> {
        return DefaultItemAdapter<T>();
    }

}