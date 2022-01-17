package com.lingmiao.shop.business.me.presenter.impl

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.LinkInSettingPresenter
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.FeeSettingVo
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.bean.TimeSettingVo
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class DeliveryInTimePresenterImpl (val view : DeliveryInTimePresenter.View) : BasePreImpl(view) ,DeliveryInTimePresenter {

    private var json : Gson;

    init {

        json = Gson();
    }

    override fun addModel(item: FreightVoItem) {
        mCoroutine.launch {
            view.showDialogLoading()
            LogUtils.dTag("add model" + item.name)
            val resp = ToolsRepository.addShipTemplate(item)
            handleResponse(resp) {
                view.updateModelSuccess(true)
            }
            view.hideDialogLoading()
        }
    }

    override fun updateModel(item: FreightVoItem) {
        mCoroutine.launch {
            view.showPageLoading();
            if(item?.isLocalTemplateType()) {
                val resp = ToolsRepository.updateShipTemplates(item?.id!!, item);
                handleResponse(resp) {
                    view.updateModelSuccess(resp.data);
                }
            } else {
                val resp = ToolsRepository.updateShipTemplates(item);
                handleResponse(resp) {
                    view.updateModelSuccess(true);
                }
            }
            view.hidePageLoading();
        }
    }

    override fun getFeeSetting(item: FreightVoItem?): FeeSettingVo {
        return json.getAdapter(TypeToken.get(FeeSettingVo::class.java)).fromJson(item?.feeSetting?:"")
    }

    override fun getTimeSetting(item: FreightVoItem?): TimeSettingVo {
        return json.getAdapter(TypeToken.get(TimeSettingVo::class.java)).fromJson(item?.timeSetting?:"")
    }

    override fun getTemplate(str : String) {
        mCoroutine.launch {
            val resp = ToolsRepository.shipTemplates(str)
            if(resp.isSuccess) {
                if(resp.data.isNotEmpty()) {
                    view.setModel(resp.data?.get(0))
                } else {
                    view.setModel(null)
                }
            } else {
                view.setModel(null)
            }
        }
    }

}