package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

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

	override fun updateShopManage(bean: ApplyShopInfo) {
		mCoroutine.launch {
			val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
			if (resp.isSuccess) {
				var info = UserManager.getLoginInfo()
				bean.shopLogo?.apply {
					info?.shopLogo = bean.shopLogo;
				}
				bean.shopName?.apply {
					info?.shopName = bean.shopName;
				}
				info?.apply {
					UserManager.setLoginInfo(info!!);
					EventBus.getDefault().post(info);
				}
				view.onUpdateShopSuccess()
			} else {
				view.onUpdateShopError(resp.code)
			}
		}
	}
}