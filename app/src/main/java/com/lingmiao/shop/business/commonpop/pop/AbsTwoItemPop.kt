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
abstract class AbsTwoItemPop<T : ItemData>(context: Context, override var title : String?)
    : AbsThreeItemPop<T>(context, title) {

    override fun getLevel(): Int {
        return LEVEL_2;
    }

    override fun getData3(data2 : T) : List<T> {
        return mutableListOf();
    }

    override fun getAdapter3(): BaseQuickAdapter<T, BaseViewHolder> {
        return DefaultItemAdapter<T>();
    }
}