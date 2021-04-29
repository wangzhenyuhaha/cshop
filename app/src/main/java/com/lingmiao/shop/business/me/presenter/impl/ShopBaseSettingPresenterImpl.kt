package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.ShopBaseSettingPresenter
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.lingmiao.shop.business.tuan.presenter.OrderIndexPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ShopBaseSettingPresenterImpl (context: Context,val view : ShopBaseSettingPresenter.View) : BasePreImpl(view) ,ShopBaseSettingPresenter {

    private val shopPresenter: ShopManagePresenter by lazy {
        ShopManagePresenterImpl(context, view);
    }

    override fun updateShopSlogan(bean:ShopManageRequest) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view?.onUpdateSloganSuccess(bean.shopSlogan);
            } else {
                view.onUpdateShopError(resp.code)
            }
        }
    }

    override fun updateShopNotice(bean:ShopManageRequest) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view?.onUpdateNoticeSuccess(bean.shopNotice);
            } else {
                view.onUpdateShopError(resp.code)
            }
        }
    }

    override fun requestShopManageData() {
        shopPresenter?.requestShopManageData();
    }

    override fun updateShopManage(bean: ShopManageRequest) {
        shopPresenter?.updateShopManage(bean);
    }

}