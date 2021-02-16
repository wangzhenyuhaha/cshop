package com.lingmiao.shop.business.me.presenter.impl

import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch


class MyPreImpl(private var view: MyPresenter.View) : BasePreImpl(view), MyPresenter {

    override fun getMyData() {

        		mCoroutine.launch {
        			val resp = MeRepository.apiService.getMyData().awaitHiResponse()
        			if (resp.isSuccess) {
        				view.onMyDataSuccess(resp.data)
        			}else{
        				view.ontMyDataError()
        			}

        		}

    }

}