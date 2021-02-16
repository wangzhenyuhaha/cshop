package com.lingmiao.shop.business.me.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.ShopQualification

import android.content.Context
import  com.lingmiao.shop.business.me.presenter.ShopQualificationPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ShopQualificationPresenterImpl(context: Context, private var view: ShopQualificationPresenter.View) :
    BasePreImpl(view), ShopQualificationPresenter{
	override fun requestShopQualificationData(){
		//		mCoroutine.launch {
		//			val resp = MeRepository.apiService.getShopQualification().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onShopQualificationSuccess(resp.data)
		//			}else{
		//				view.onShopQualificationError(resp.code)
		//			}
		//
		//		}
	}
}