package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.tools.bean.FeeSettingVo
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.bean.TimeSettingVo

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface DeliveryInTimePresenter : BasePresenter {
    fun addModel(mItem: FreightVoItem, type: Boolean = false)
    fun updateModel(mItem: FreightVoItem)
    fun getTemplate(str: String);


    fun getFeeSetting(item: FreightVoItem?): FeeSettingVo;

    fun getTimeSetting(item: FreightVoItem?): TimeSettingVo;

    interface View : BaseView {
        fun updateModelSuccess(b: Boolean,type: Boolean)
        fun setModel(item: FreightVoItem?)
    }
}