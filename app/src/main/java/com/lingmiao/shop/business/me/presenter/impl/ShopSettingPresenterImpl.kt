package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.lingmiao.shop.business.me.presenter.ShopSettingPresenter
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class ShopSettingPresenterImpl(val context: Context, val view: ShopSettingPresenter.View) :
    BasePreImpl(view), ShopSettingPresenter {

    override fun updateShopSlogan(bean: ApplyShopInfo) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view.onUpdateSloganSuccess(bean.shopSlogan)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }

    override fun updateShopNotice(bean: ApplyShopInfo) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view.onUpdateNoticeSuccess(bean.shopNotice)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }

    override fun requestShopManageData() {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            if (resp.isSuccess) {
                view.onShopManageSuccess(resp.data)
            } else {
                view.onShopManageError(resp.code)
            }
            view.hideDialogLoading()
        }
    }


    override fun updateShopManage(bean: ApplyShopInfo) {
        mCoroutine.launch {
            view.showDialogLoading()
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