package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.OrderCancel

interface OrderCancelPresenter: BasePresenter{
 
	fun requestOrderCancelData(
        orderId: String,
        reason: String,
        toString: String
    )
	
	interface View : BaseView {
        fun onOrderCancelSuccess()
        fun onOrderCancelError(code: Int)
    }
}