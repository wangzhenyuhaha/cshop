package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.api.MemberRepository
import com.lingmiao.shop.business.main.presenter.IElectricSignPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/7/2710:43 上午
Auther      : Fox
Desc        :
 **/
class ElectricSignPresenterImpl(context: Context, private var view: IElectricSignPresenter.View) : BasePreImpl(view),
    IElectricSignPresenter {

    override fun getElectricSign() {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MemberRepository.apiService.electricSign("${UserManager.getLoginInfo()?.shopId}").awaitHiResponse()
            if (resp.isSuccess) {
                val electSignStatus = resp.data.electSignStatus;
                if("0".equals(electSignStatus)) {
                    view.onSignSuccess();
                } else if("1".equals(electSignStatus) || "10".equals(electSignStatus)) {
                    if("1".equals(electSignStatus)) {
                        view.showToast("审核失败，请重新签约")
                    }
                    view.setUrl(resp.data.sybsignurl)
                } else if("2".equals(electSignStatus)) {
                    view.onSigning();
                }
            } else{
                view.getSignUrlFailed();
            }
            view.hideDialogLoading();

        }
    }

    override fun getShopStatus() {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MainRepository.apiService.getShopStatus().awaitHiResponse()
            if (resp.isSuccess) {
                if(ShopStatusConstants.isSignSuccess(resp.data.shopStatus)) {
                    view.onSignSuccess();
                }
            }
            view.hideDialogLoading();
        }
    }


}