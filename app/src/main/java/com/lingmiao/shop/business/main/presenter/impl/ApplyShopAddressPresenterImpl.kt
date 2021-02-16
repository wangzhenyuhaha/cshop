package com.lingmiao.shop.business.main.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopAddress

import android.content.Context
import  com.lingmiao.shop.business.main.presenter.ApplyShopAddressPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ApplyShopAddressPresenterImpl(context: Context, private var view: ApplyShopAddressPresenter.View) :
    BasePreImpl(view), ApplyShopAddressPresenter{
	override fun requestApplyShopAddressData(){
		//		mCoroutine.launch {
		//			val resp = MainRepository.apiService.getApplyShopAddress().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onApplyShopAddressSuccess(resp.data)
		//			}else{
		//				view.onApplyShopAddressError(resp.code)
		//			}
		//
		//		}
	}
}