package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.order.api.OrderRepository
import  com.lingmiao.shop.business.order.presenter.AfterSalePresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.order.bean.RefoundReqVo
import kotlinx.coroutines.launch

class AfterSalePresenterImpl(context: Context, private var view: AfterSalePresenter.View) :
    BasePreImpl(view), AfterSalePresenter {
    override fun requestAfterSaleData(refundId: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.getAfterSale(refundId).awaitHiResponse()
            if (resp.isSuccess) {
                view.onAfterSaleSuccess(resp.data)
            } else {
                view.onAfterSaleError(resp.code)
            }

        }
    }

    override fun doAfterSaleStock(sn: String) {
        mCoroutine.launch {
            try {
                val resp = OrderRepository.apiService.doAfterSaleStock(sn)
                if (resp.isSuccessful) {
                    view.onAfterSaleStockSuccess()
                } else {
                    view.onAfterSaleStockError(-1)
                }
            }catch (e:Exception){
                e.printStackTrace()
                view.onAfterSaleActionError(-1)
            }


        }
    }

    override fun doAfterSaleAction(
        agree: Int,
        refundId: String,
        refundMoney: String?,
        remark: String
    ) {
        mCoroutine.launch {
            try {
                var item = RefoundReqVo();
                item.agree = agree;
                item.remark = remark;
                item.refundMoney = refundMoney;
                item.sn = refundId;

                val resp = OrderRepository.apiService.doAfterSaleAction(refundId, item)
                if (resp.isSuccessful) {
                    view.onAfterSaleActionSuccess()
                } else {
                    view.onAfterSaleActionError(-1)
                }
            }catch (e:Exception){
                e.printStackTrace()
                view.onAfterSaleActionError(-1)
            }


        }
    }
}