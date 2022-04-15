package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.sales.presenter.PopularizePre
import kotlinx.coroutines.launch

class PopularizePreImpl(val context: Context, val view: PopularizePre.View) :
    BasePreImpl(view), PopularizePre {

    override fun getMyData() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getMyData().awaitHiResponse()
            view.showDialogLoading()
            if (resp.isSuccess) {
                view.onMyDataSuccess(resp.data)
            }else{
                view.ontMyDataError()
            }
        }
    }
}

