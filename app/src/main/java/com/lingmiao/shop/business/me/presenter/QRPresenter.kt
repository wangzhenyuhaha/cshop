package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/8/2912:38 下午
Auther      : Fox
Desc        :
 **/
interface QRPresenter : BasePresenter {

    fun requestQRUrl();

    interface View : BaseView {
        fun getQRUrlFailed();
        fun setQRUrl(url : String);
    }
}