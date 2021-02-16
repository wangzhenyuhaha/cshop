package com.lingmiao.shop.business.order.presenter

import com.lingmiao.shop.business.order.bean.OrderList
import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2020/12/3010:10 AM
Auther      : Fox
Desc        :
 **/
interface ScanOrderPresenter : BasePresenter {

    fun getBarcodeFormats() : Collection<BarcodeFormat>;

    fun verify(id : String);

    interface View : BaseView {
        fun verifySuccess(data : OrderList?);
        fun verifyFailed();
    }
}