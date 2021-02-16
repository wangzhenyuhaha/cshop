package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.UpdatePricePresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class UpdatePricePresenterImpl(context: Context, private var view: UpdatePricePresenter.View) :
    BasePreImpl(view), UpdatePricePresenter {
    override fun requestUpdatePriceData(
        tradeSn: String?,
        orderAmount: String,
        shippingAmount: String
    ) {
        mCoroutine.launch {
            try {
                val resp = OrderRepository.apiService.getUpdatePrice(
                    tradeSn!!,
                    orderAmount, shippingAmount
                )
                if (resp.isSuccessful) {
                    view.onUpdatePriceSuccess()
                } else {
                    view.onUpdatePriceError(-1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                view.onUpdatePriceError(-1)
            }


        }
    }
}