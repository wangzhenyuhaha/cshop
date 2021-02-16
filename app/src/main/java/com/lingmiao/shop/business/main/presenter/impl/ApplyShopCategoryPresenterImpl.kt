package com.lingmiao.shop.business.main.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopCategory

import android.content.Context
import  com.lingmiao.shop.business.main.presenter.ApplyShopCategoryPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ApplyShopCategoryPresenterImpl(context: Context, private var view: ApplyShopCategoryPresenter.View) :
    BasePreImpl(view), ApplyShopCategoryPresenter{
	override fun requestApplyShopCategoryData(){
				mCoroutine.launch {
					val resp = MainRepository.apiService.getApplyShopCategory().awaitHiResponse()
					if (resp.isSuccess) {
						view.onApplyShopCategorySuccess(resp.data)
					}else{
						view.onApplyShopCategoryError(resp.code)
					}

				}
	}
}