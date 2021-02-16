package com.lingmiao.shop.business.order.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.LogisticsInfo

interface LogisticsInfoPresenter: BasePresenter{
 
	fun requestLogisticsInfoData(shipNo: String, logiId: String)
	
	interface View : BaseView {
        fun onLogisticsInfoSuccess(bean: LogisticsInfo)
        fun onLogisticsInfoError(code: Int)
    }
}