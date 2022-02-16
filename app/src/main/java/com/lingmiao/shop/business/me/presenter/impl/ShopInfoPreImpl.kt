package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ShopInfoPresenter
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class ShopInfoPreImpl(val context: Context, val view: ShopInfoPresenter.View) :
    BasePreImpl(view), ShopInfoPresenter {


    //加载店铺数据
    override fun loadShopInfo() {
        mCoroutine.launch {
            view.showPageLoading()
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            handleResponse(resp) {
                view.onLoadedShopInfo(resp.data)
            }
            view.hidePageLoading()
        }
    }

    override fun updateShopManage(bean: ApplyShopInfo) {
        mCoroutine.launch {
            // view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                val info = UserManager.getLoginInfo()
                bean.shopLogo?.apply {
                    info?.shopLogo = bean.shopLogo
                }
                bean.shopName?.apply {
                    info?.shopName = bean.shopName
                }
                info?.apply {
                    UserManager.setLoginInfo(info)
                    EventBus.getDefault().post(info)
                }
                view.showToast("修改成功")
                view.onUpdateShopSuccess(bean)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }

}