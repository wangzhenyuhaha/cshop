package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.presenter.ElectronicVoucherPresenter
import kotlinx.coroutines.launch

class ElectronicVoucherPreImpl(val context: Context, val view: ElectronicVoucherPresenter.View) :
    BasePreImpl(view), ElectronicVoucherPresenter {



    override fun deleteElectronicVoucher(id: Int, position: Int) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = PromotionRepository.deleteCoupons(id)
            view.hideDialogLoading()
            handleResponse(resp) {
                view.deleteCouponSuccess(position)
            }


        }
    }

    override fun editElectronicVoucher(disabled: Int, id: Int, position: Int) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = PromotionRepository.editCoupon(disabled, id)
            view.hideDialogLoading()
            handleResponse(resp)
            {
                view.editCouponSuccess(position)
            }
        }
    }





}

