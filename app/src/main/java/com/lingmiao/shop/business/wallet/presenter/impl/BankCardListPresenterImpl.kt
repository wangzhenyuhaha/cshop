package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.BankCardAccountBean
import com.lingmiao.shop.business.wallet.presenter.BankCardListPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import kotlinx.coroutines.launch

class BankCardListPresenterImpl(var view: BankCardListPresenter.View) : BasePreImpl(view),
    BankCardListPresenter {

    override fun loadList(page: IPage, datas: MutableList<BankCardAccountBean>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (datas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = WalletRepository.queryBankCardList()
            if (resp?.isSuccess) {
                val list = resp?.data?.data ?: arrayListOf();
                view.onLoadSuccess(list, list.isNotEmpty())
            } else {
                view.onLoadFailed()
            }
            view.hidePageLoading()
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData end")
        }
    }

    override fun unBind(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = WalletRepository.unbindBankCard(id);
            handleResponse(resp) {
                view.unBind();
            }
            view.hideDialogLoading()
        }
    }


}