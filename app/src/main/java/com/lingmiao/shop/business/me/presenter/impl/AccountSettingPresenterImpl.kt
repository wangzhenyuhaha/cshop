package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.AccountSetting

import android.content.Context
import com.lingmiao.shop.BuildConfig
import  com.lingmiao.shop.business.me.presenter.AccountSettingPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class AccountSettingPresenterImpl(
    context: Context,
    private var view: AccountSettingPresenter.View
) :
    BasePreImpl(view), AccountSettingPresenter {
    override fun requestAccountSettingData() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getUpgrade(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).awaitHiResponse()
            if (resp.isSuccess) {
                view.onAccountSettingSuccess(resp.data)
            } else {
                view.onAccountSettingError(resp.code)
            }

        }
    }
}