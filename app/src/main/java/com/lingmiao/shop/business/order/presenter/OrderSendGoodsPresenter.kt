package com.lingmiao.shop.business.order.presenter

import com.lingmiao.shop.business.order.bean.LogisticsCompanyItem
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface OrderSendGoodsPresenter : BasePresenter {

    fun requestOrderSendGoodsData(
        orderId: String,
        logisticsCompany: String,
        logisticsNumber: String,
        logisticsId: String
    )

    fun requestLogisticsCompanyList()

    interface View : BaseView {
        fun onOrderSendGoodsSuccess()
        fun onOrderSendGoodsError(code: Int)
        fun onLogisticsCompanyListSuccess(data: List<LogisticsCompanyItem>)
        fun onLogisticsCompanyListError(code: Int)

    }
}