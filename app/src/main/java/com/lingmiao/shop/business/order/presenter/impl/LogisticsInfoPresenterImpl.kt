package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.LogisticsInfoPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class LogisticsInfoPresenterImpl(context: Context, private var view: LogisticsInfoPresenter.View) :
    BasePreImpl(view), LogisticsInfoPresenter{
	override fun requestLogisticsInfoData(shipNo: String, logiId: String) {
				mCoroutine.launch {
					val resp = OrderRepository.apiService.requestLogisticsInfoData(shipNo,logiId).awaitHiResponse()
					if (resp.isSuccess) {
						view.onLogisticsInfoSuccess(resp.data)
					}else{
						view.onLogisticsInfoError(resp.code)
					}

				}
	}
}