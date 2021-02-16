package com.lingmiao.shop.business.order.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.order.bean.LogisticsCompanyList

import android.content.Context
import  com.lingmiao.shop.business.order.presenter.LogisticsCompanyListPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.order.api.OrderRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class LogisticsCompanyListPresenterImpl(context: Context, private var view: LogisticsCompanyListPresenter.View) :
    BasePreImpl(view), LogisticsCompanyListPresenter{
	override fun requestLogisticsCompanyListData(){
		//		mCoroutine.launch {
		//			val resp = OrderRepository.apiService.getLogisticsCompanyList().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onLogisticsCompanyListSuccess(resp.data)
		//			}else{
		//				view.onLogisticsCompanyListError(resp.code)
		//			}
		//
		//		}
	}
}