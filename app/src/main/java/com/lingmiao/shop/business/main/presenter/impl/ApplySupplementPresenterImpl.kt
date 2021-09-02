package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.api.MemberRepository
import com.lingmiao.shop.business.main.presenter.IApplySupplementPresenter
import com.lingmiao.shop.business.main.presenter.IElectricSignPresenter
import com.lingmiao.shop.business.main.presenter.IShopAddressPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/7/2710:43 上午
Auther      : Fox
Desc        :
 **/
class ApplySupplementPresenterImpl(context: Context, private var view: IApplySupplementPresenter.View) : BasePreImpl(view),
    IApplySupplementPresenter {

    override fun getSupplementSign() {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MemberRepository.apiService.applySupplementUrl("${UserManager.getLoginInfo()?.shopId}").awaitHiResponse()
            if (resp.isSuccess) {
                view.setUrl(resp.data.url)
            } else{
                view.getSupplementUrlFailed();
            }
            view.hideDialogLoading();

        }
    }

}