package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.presenter.TradeListPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import kotlinx.coroutines.launch

class TradeListPresenterImpl(var view: TradeListPresenter.View) : BasePreImpl(view),
    TradeListPresenter {

    override fun loadList(page: IPage, accountId : String, datas: MutableList<DepositVo>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (datas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = WalletRepository.getRecordList(page.getPageIndex(), accountId);
            if (resp?.isSuccess) {
                val list = resp?.data?.data?.records ?: arrayListOf();
                view.onLoadMoreSuccess(list, list?.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

}