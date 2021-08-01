package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/6/43:07 PM
Auther      : Fox
Desc        :
 **/
interface IShopQRCodePre : BasePresenter {

    fun getQRCode()
    fun getShareInfo(shopId: Int)

    interface View : BaseView {

        fun onSetQRCode(url: String);

        fun onGetQRCodeFail();

    }
}