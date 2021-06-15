package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesEditPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityEditPreImpl(var context: Context, var view: ISalesEditPresenter.PubView) : BasePreImpl(view),ISalesEditPresenter {

    override fun submitDiscount(mItem: SalesVo?) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val resp = PromotionRepository.submitDiscount(mItem);
            handleResponse(resp) {
                view?.onSubmitDiscount();
            }
            view?.hideDialogLoading();
        }
    }

    override fun update(mItem: SalesVo?) {
        if(mItem?.id?.isNullOrEmpty() == true) {
            return;
        }
        mCoroutine.launch {
            view?.showDialogLoading()
            val resp = PromotionRepository.update(mItem!!);
            handleResponse(resp) {
                view?.onUpdate();
            }
            view?.hideDialogLoading();
        }
    }

}