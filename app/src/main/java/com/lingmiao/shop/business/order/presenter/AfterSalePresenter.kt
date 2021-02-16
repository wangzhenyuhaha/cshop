package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.AfterSale

interface AfterSalePresenter: BasePresenter{
 
	fun requestAfterSaleData(refundId: String)
	fun doAfterSaleStock(sn: String)
    fun doAfterSaleAction(agree: Int, refundId: String, refundMoney: String?, remark: String)

    interface View : BaseView {
        fun onAfterSaleSuccess(bean: AfterSale)
        fun onAfterSaleError(code: Int)
        fun onAfterSaleActionSuccess()
        fun onAfterSaleActionError(code: Int)
        fun onAfterSaleStockSuccess()
        fun onAfterSaleStockError(code: Int)
    }
}