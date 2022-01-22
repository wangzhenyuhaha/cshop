package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.presenter.ElectronicVoucherPresenter
import kotlinx.coroutines.launch

class ElectronicVoucherPreImpl(val context: Context, val view: ElectronicVoucherPresenter.View) :
    BasePreImpl(view), ElectronicVoucherPresenter {

    override fun loadListData(
        page: IPage,
        list: List<*>
    ) {
        mCoroutine.launch {
            val resp = PromotionRepository.searchElectronicVoucher(page.getPageIndex())
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

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

