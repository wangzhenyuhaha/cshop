package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import  com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ShopManagePresenterImpl(context: Context, private var view: ShopManagePresenter.View) :
    BasePreImpl(view), ShopManagePresenter{
	override fun requestShopManageData(){
				mCoroutine.launch {
					val resp = MeRepository.apiService.getShop().awaitHiResponse()
					if (resp.isSuccess) {
						view.onShopManageSuccess(resp.data)
					}else{
						view.onShopManageError(resp.code)
					}

				}
	}

	override fun updateShopManage(bean: ShopManageRequest) {
		mCoroutine.launch {
			val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
			if (resp.isSuccess) {
				view.onUpdateShopSuccess()
			} else {
				view.onUpdateShopError(resp.code)
			}
		}
	}
}