package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.OrderDetail

interface OrderDetailPresenter: BasePresenter{
 
	fun requestOrderDetailData(tradeSn: String)
    fun deleteOrder(tradeSn: String)
    fun verifyOrderCode(id : String)
	
	interface View : BaseView {
        fun onOrderDetailSuccess(bean: OrderDetail)
        fun onOrderDetailError(code: Int)
        fun onDeleteOrderSuccess()

        fun verifySuccess();
        fun verifyFailed();
    }
}