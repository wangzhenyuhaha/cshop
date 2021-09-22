package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.QRPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/8/2912:40 下午
Auther      : Fox
Desc        :
 **/
class QRCodePreImpl (val context: Context, private var view: QRPresenter.View) : BasePreImpl(view), QRPresenter {

    override fun requestQRUrl() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getQRCode();
            if(resp.isSuccessful) {
                resp.body()?.string()?.let { view.setQRUrl(it) }
            } else {
                view.showToast("获取失败");
                view.getQRUrlFailed();
            }
        }
    }

}