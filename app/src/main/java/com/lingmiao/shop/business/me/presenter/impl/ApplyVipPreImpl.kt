package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import kotlinx.coroutines.launch


class ApplyVipPreImpl(private var view: ApplyVipPresenter.View) : BasePreImpl(view),
    ApplyVipPresenter {

    fun getMyData() {
//        		mCoroutine.launch {
//        			val resp = MeRepository.apiService.getMyData().awaitHiResponse()
//        			if (resp.isSuccess) {
//        				view.onMyDataSuccess(resp.data)
//        			}else{
//        				view.ontMyDataError()
//        			}

//        		}

    }

}