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
interface DeliveryOfRiderPresenter : BasePresenter {
    fun addModel(mItem: FreightVoItem)
    fun updateModel(id : String, isToSeller : Int, toRiderTime : Int)
    fun getShopTemplate();
    fun getTemplate();
    fun getShopDeliveryStatus();

    fun getFeeSetting(item: FreightVoItem?): FeeSettingVo;

    fun getTimeSetting(item: FreightVoItem?): TimeSettingVo;

    interface View : BaseView {
        fun updateModelSuccess(b: Boolean)
        fun onSetShopTemplate(item : FreightVoItem?)
        fun setModel(item : FreightVoItem?)
        fun onSetShopDeliveryStatus(size : Int, item: FreightVoItem?);
    }
}