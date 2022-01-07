package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.presenter.DiscountDetailPre
import kotlinx.coroutines.launch

class DiscountDetailPreImpl(val context: Context, val view: DiscountDetailPre.View) :
    BasePreImpl(view), DiscountDetailPre {

    override fun submitDiscount(mItem: Coupon?) {
        if (mItem == null) {
            return
        }
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = PromotionRepository.submitCoupons(mItem)
            handleResponse(resp) {
                view.onSubmitCoupons()
            }
            view.hideDialogLoading()
        }
    }

}


