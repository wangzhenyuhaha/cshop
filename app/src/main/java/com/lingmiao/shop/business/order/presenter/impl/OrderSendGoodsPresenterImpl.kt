package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.OrderSendGoodsPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class OrderSendGoodsPresenterImpl(context: Context, private var view: OrderSendGoodsPresenter.View) :
    BasePreImpl(view), OrderSendGoodsPresenter{
	override fun requestOrderSendGoodsData(
        orderId: String,
        logisticsCompany: String,
        logisticsNumber: String,
        logisticsId: String
    ) {
				mCoroutine.launch {
					try {
						val resp = OrderRepository.apiService.requestOrderSendGoodsData(orderId,logisticsCompany,logisticsNumber,logisticsId)
						if (resp.isSuccessful) {
							view.onOrderSendGoodsSuccess()
						}else{
							view.onOrderSendGoodsError(-1)
						}
					}catch (e:Exception){
						e.printStackTrace()
						view.onOrderSendGoodsError(-1)
					}


				}
	}

	override fun requestLogisticsCompanyList() {
		mCoroutine.launch {
			val resp = OrderRepository.apiService.requestLogisticsCompanyList().awaitHiResponse()
			if (resp.isSuccess) {
				view.onLogisticsCompanyListSuccess(resp.data)
			}else{
				view.onLogisticsCompanyListError(resp.code)
			}

		}
	}
}