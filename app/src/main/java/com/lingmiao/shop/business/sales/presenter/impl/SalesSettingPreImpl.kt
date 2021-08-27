package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.sales.api.PromotionRepository
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesSettingPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class SalesSettingPreImpl(var context: Context, var view: ISalesSettingPresenter.PubView) :
    BasePreImpl(view), ISalesSettingPresenter {

    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }
            val resp = PromotionRepository.getDiscounts(page.getPageIndex())
            if (resp.isSuccess) {
                val salesList:List<SalesVo>? = resp.data.data
                if (page.isRefreshing() && salesList.isNullOrEmpty()) {
                    view.showNoData()
                } else {
                    view.hidePageLoading()
                }
                view.onLoadMoreSuccess(salesList, salesList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
            //val salesList = getItems();
            //view.onLoadMoreSuccess(salesList, salesList.isNotEmpty())
            //view.hidePageLoading()
        }
    }

    override fun delete(id: String?, position: Int) {
        if (id == null || id.isEmpty()) {
            return
        }
        mCoroutine.launch {
            view?.showDialogLoading()
            val resp = PromotionRepository.deleteById(id!!)
            handleResponse(resp) {
                view.onDelete(position)
            }
            view?.hideDialogLoading()
        }
    }

    override fun endDiscount(id: String?, position: Int) {
        if (id == null || id.isEmpty()) {
            return
        }
        mCoroutine.launch {
            view?.showDialogLoading()
            val resp = PromotionRepository.endDiscount(id!!)
            handleResponse(resp) {
                view.onEndDiscount(position)
            }
            view?.hideDialogLoading()
        }
    }

    fun getItems(): MutableList<SalesVo> {
        val list: MutableList<SalesVo> = mutableListOf()
        list.add(SalesVo())
        return list
    }

}