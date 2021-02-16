package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface UpdatePricePresenter: BasePresenter{
 
	fun requestUpdatePriceData(
        tradeSn: String?,
        orderAmount: String,
        shippingAmount: String
    )
	
	interface View : BaseView {
        fun onUpdatePriceSuccess()
        fun onUpdatePriceError(code: Int)
    }
}