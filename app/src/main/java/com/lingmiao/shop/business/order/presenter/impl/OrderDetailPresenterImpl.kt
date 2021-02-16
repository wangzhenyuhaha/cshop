package com.lingmiao.shop.business.order.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.OrderDetailPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class OrderDetailPresenterImpl(private var view: OrderDetailPresenter.View) :
    BasePreImpl(view), OrderDetailPresenter {

    override fun requestOrderDetailData(orderId: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.getOrderDetail(orderId).awaitHiResponse()
            if (resp.isSuccess) {
                view.onOrderDetailSuccess(resp.data)
            } else {
                view.onOrderDetailError(resp.code)
            }

        }
    }


    override fun deleteOrder(tradeSn: String) {
        mCoroutine.launch {
            try {
                val resp = OrderRepository.apiService.deleteOrder(tradeSn)
                view.hideDialogLoading()
                LogUtils.d("lqx","deleteOrder:"+ resp.isSuccessful)
                if (resp.isSuccessful) {
                    view.onDeleteOrderSuccess()
                } else {
                }
            }catch (e:Exception){
                e.printStackTrace()
                view.hideDialogLoading()
            }

        }
    }

    override fun verifyOrderCode(id: String) {
        mCoroutine.launch {
            view?.showDialogLoading();
            val resp = OrderRepository.apiService.verifyOrderShip(id).awaitHiResponse()
            if (resp.isSuccess && resp?.data?.status == true) {
                view?.verifySuccess()
                view?.showToast(resp?.data?.desc)
            } else {
                view?.verifyFailed()
            }
            view?.hideDialogLoading();
        }
    }
}