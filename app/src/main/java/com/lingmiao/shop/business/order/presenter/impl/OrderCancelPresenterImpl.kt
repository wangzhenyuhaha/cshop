package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.OrderCancelPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class OrderCancelPresenterImpl(context: Context, private var view: OrderCancelPresenter.View) :
    BasePreImpl(view), OrderCancelPresenter {
    override fun requestOrderCancelData(
        orderId: String,
        reason: String,
        desc: String
    ) {
        mCoroutine.launch {
            try {
                val resp = OrderRepository.apiService.orderCancel(orderId, reason,reason, desc)
                if (resp.isSuccessful) {
                    view.onOrderCancelSuccess()
                } else {
                    view.onOrderCancelError(-1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                view.onOrderCancelError(-1)
            }
        }
    }
}