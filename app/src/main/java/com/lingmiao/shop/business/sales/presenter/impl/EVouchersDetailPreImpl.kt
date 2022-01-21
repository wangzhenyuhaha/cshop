package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.EVouchersDetailPresenter
import kotlinx.coroutines.launch

class EVouchersDetailPreImpl(val context: Context, val view: EVouchersDetailPresenter.View) :
    BasePreImpl(view), EVouchersDetailPresenter {


    override fun submitDiscount(mItem: ElectronicVoucher?) {
        if (mItem == null) {
            return
        }
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = PromotionRepository.submitElectronicVoucher(mItem)
            handleResponse(resp) {
                view.onSubmitElectronicVoucher()
            }
            if (!resp.isSuccess) {
                view.onSubmitElectronicVoucherFailed()
            }
            view.hideDialogLoading()
        }
    }

}