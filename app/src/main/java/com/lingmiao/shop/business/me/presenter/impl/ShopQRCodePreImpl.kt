package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

/**
Create Date : 2021/6/43:07 PM
Auther      : Fox
Desc        :
 **/
class ShopQRCodePreImpl(context: Context, private var view: IShopQRCodePre.View) : BasePreImpl(view), IShopQRCodePre {

    override fun getQRCode() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getQRCode();
            if(resp.isSuccessful) {
                resp.body()?.string()?.let { view.onSetQRCode(it) }
            } else {
                view.showToast("获取失败");
                view.onGetQRCodeFail();
            }

        }
    }

    //
}