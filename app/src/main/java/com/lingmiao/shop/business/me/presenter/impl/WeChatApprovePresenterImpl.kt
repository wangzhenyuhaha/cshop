package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.WeChatApprovePresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class WeChatApprovePresenterImpl (val view : WeChatApprovePresenter.View) : BasePreImpl(view) ,
    WeChatApprovePresenter {

    override fun approve() {
        mCoroutine.launch {

            val resp = MeRepository.apiService.queryFinalOpenShop().awaitHiResponse();

        }
    }

}