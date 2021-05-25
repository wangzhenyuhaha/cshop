package com.lingmiao.shop.business.me.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.DeliveryOfRiderPresenter
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
class DeliveryInOfRiderPresenterImpl (val view : DeliveryOfRiderPresenter.View) : BasePreImpl(view) ,DeliveryOfRiderPresenter {

    private var json : Gson;

    init {

        json = Gson();
    }

    override fun addModel(item: FreightVoItem) {
        mCoroutine.launch {
            view.showDialogLoading();
            LogUtils.dTag("add model" + item.name);
            val resp = ToolsRepository.addShipTemplate(item);
            handleResponse(resp) {
                view.updateModelSuccess(true);
            }
            view.hideDialogLoading();
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
        return json?.getAdapter(TypeToken.get(FeeSettingVo::class.java)).fromJson(item?.feeSetting);
    }

    override fun getTimeSetting(item: FreightVoItem?): TimeSettingVo {
        return json?.getAdapter(TypeToken.get(TimeSettingVo::class.java)).fromJson(item?.timeSetting);
    }

    override fun getTemplate(str : String) {
        //shops/ship-templates
        mCoroutine.launch {
            //view.showDialogLoading();
            val resp = ToolsRepository.shipTemplates(str);
            handleResponse(resp) {
                if(resp.data.size > 0) {
                    view.setModel(resp?.data?.get(0));
                }
            }
            //view.hideDialogLoading();
        }
    }

    override fun getShopDeliveryStatus() {
        mCoroutine.launch {
            //view.showDialogLoading();
            val resp = ToolsRepository.shipTemplates(FreightVoItem.TYPE_LOCAL);
            handleResponse(resp) {
                view?.onSetShopDeliveryStatus(resp.data.size, resp?.data?.get(0))
            }
            //view.hideDialogLoading();
        }
    }

}