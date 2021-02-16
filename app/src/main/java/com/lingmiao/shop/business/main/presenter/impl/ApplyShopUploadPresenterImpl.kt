package com.lingmiao.shop.business.main.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopUpload

import android.content.Context
import  com.lingmiao.shop.business.main.presenter.ApplyShopUploadPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ApplyShopUploadPresenterImpl(context: Context, private var view: ApplyShopUploadPresenter.View) :
    BasePreImpl(view), ApplyShopUploadPresenter{
	override fun requestApplyShopUploadData(){
		//		mCoroutine.launch {
		//			val resp = MainRepository.apiService.getApplyShopUpload().awaitHiResponse()
		//			if (resp.isSuccess) {
		//				view.onApplyShopUploadSuccess(resp.data)
		//			}else{
		//				view.onApplyShopUploadError(resp.code)
		//			}
		//
		//		}
	}
}