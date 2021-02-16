package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.LogisticsCompanyList

interface LogisticsCompanyListPresenter: BasePresenter{
 
	fun requestLogisticsCompanyListData()
	
	interface View : BaseView {
        fun onLogisticsCompanyListSuccess(bean: LogisticsCompanyList)
        fun onLogisticsCompanyListError(code: Int)
    }
}