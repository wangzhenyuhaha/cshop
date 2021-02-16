package com.lingmiao.shop.business.main.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopHint

import android.content.Context
import  com.lingmiao.shop.business.main.presenter.ApplyShopHintPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ApplyShopHintPresenterImpl(context: Context, private var view: ApplyShopHintPresenter.View) :
    BasePreImpl(view), ApplyShopHintPresenter{
	override fun requestApplyShopHintData(){
		//		mCoroutine.launch {
		//			val resp = MainRepository.apiService.getApplyShopHint().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onApplyShopHintSuccess(resp.data)
		//			}else{
		//				view.onApplyShopHintError(resp.code)
		//			}
		//
		//		}
	}
}