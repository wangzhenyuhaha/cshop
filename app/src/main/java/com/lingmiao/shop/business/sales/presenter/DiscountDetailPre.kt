package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.SalesVo

interface DiscountDetailPre : BasePresenter {

    fun submitDiscount(mItem: Coupon?)

    interface View : BaseView {
        fun onSubmitCoupons()
    }
}