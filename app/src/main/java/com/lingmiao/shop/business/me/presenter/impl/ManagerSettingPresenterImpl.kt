package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ManagerSettingPresenterImpl (val view : ManagerSettingPresenter.View) : BasePreImpl(view) ,ManagerSettingPresenter {

    override fun loadShopInfo() {
        mCoroutine.launch {
            view.showPageLoading()
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            handleResponse(resp) {
                view.onLoadedShopInfo(resp.data)
            }
            view.hidePageLoading()
        }
    }

}