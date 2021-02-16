package com.lingmiao.shop.business.tools.presenter

import com.lingmiao.shop.business.tools.bean.*
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface EditFreightModelPresenter : BasePresenter {

    fun loadItem(id: String);

    fun getFeeSetting(item: FreightVoItem): FeeSettingVo;

    fun getTimeSetting(item: FreightVoItem): TimeSettingVo;

    fun getArea(item: Item) : Map<String, Map<String, Any>>;

    fun getAreas(items : MutableList<Item>?) : TempArea ?;

    fun updateModel(item: FreightVoItem);

    fun addModel(item : FreightVoItem);

    interface View : BaseView {
        fun loadItemSuccess(item: FreightVoItem);

        fun updateModelSuccess(flag : Boolean);
    }
}