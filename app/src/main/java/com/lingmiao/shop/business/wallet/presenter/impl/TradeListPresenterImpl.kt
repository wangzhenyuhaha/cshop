package com.lingmiao.shop.business.wallet.presenter.impl

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.presenter.OrderListPresenter
import com.lingmiao.shop.business.wallet.presenter.RiderListPresenter
import com.lingmiao.shop.business.wallet.presenter.TradeListPresenter
import kotlinx.coroutines.launch

class OrderListPresenterImpl(var view: OrderListPresenter.View) : BasePreImpl(view),
    OrderListPresenter {

    override fun loadList(page: IPage, accountId: String, data: MutableList<OrderList>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (data.isEmpty()) {
                view.showPageLoading()
            }
            val resp = WalletRepository.getOrderList(page.getPageIndex())
            if (resp.isSuccess) {

                val list: List<OrderList> = resp.data?.data ?: arrayListOf()
                view.onLoadMoreSuccess(list, list.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

}

class TradeListPresenterImpl(var view: TradeListPresenter.View) : BasePreImpl(view),
    TradeListPresenter {

    override fun loadList(page: IPage, accountId: String, data: MutableList<DepositVo>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (data.isEmpty()) {
                view.showPageLoading()
            }
            val resp = WalletRepository.getRecordList(page.getPageIndex(), accountId)

            val temp =
                if (resp.isSuccess) {
                    val list = resp.data?.data?.records ?: arrayListOf()
                    view.onLoadMoreSuccess(list, list.isNotEmpty())
                } else {
                    view.onLoadMoreFailed()
                }
            view.hidePageLoading()
        }
    }

}

class RiderListPresenterImpl(var view: RiderListPresenter.View) : BasePreImpl(view),
    RiderListPresenter {

    override fun loadList(page: IPage, accountId: String, data: MutableList<DepositVo>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (data.isEmpty()) {
                view.showPageLoading()
            }
            val resp = WalletRepository.getRiderRecordList(page.getPageIndex(), accountId)
            if (resp.isSuccess) {
                val list = resp.data?.data?.records
                view.onLoadMoreSuccess(list, list.isNotEmpty())

            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

}