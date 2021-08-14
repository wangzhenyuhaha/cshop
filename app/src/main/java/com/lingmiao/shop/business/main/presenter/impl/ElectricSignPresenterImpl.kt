package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MemberRepository
import com.lingmiao.shop.business.main.presenter.IElectricSignPresenter
import com.lingmiao.shop.business.main.presenter.IShopAddressPresenter
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
            val resp = MemberRepository.apiService.electricSign("${UserManager.getLoginInfo()?.shopId}").awaitHiResponse()
            if (resp.isSuccess) {
//                view.onApplyShopHintSuccess(resp.data)
            }else{
//                view.onApplyShopHintError(resp.code)
            }

        }
    }

}