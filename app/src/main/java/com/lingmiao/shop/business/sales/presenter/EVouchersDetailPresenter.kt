package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher

interface EVouchersDetailPresenter : BasePresenter {

    fun submitDiscount(mItem: ElectronicVoucher?)

    interface View : BaseView {
        fun onSubmitElectronicVoucher()

        fun onSubmitElectronicVoucherFailed()
    }
}

