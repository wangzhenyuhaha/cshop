package com.lingmiao.shop.business.wallet.presenter.impl

import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.presenter.WithdrawAccountPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class WithdrawAccountPresenterImpl (var view : WithdrawAccountPresenter.View) : BasePreImpl(view), WithdrawAccountPresenter{

    override fun getWithdrawAccount() {
        mCoroutine.launch {

            view.showPageLoading();

            val resp = WalletRepository.getWithdrawAccountInfo()

            handleResponse(resp) {
                view.getWithdrawAccountSuccess(resp?.data);
            }
            view.hidePageLoading()
        }
    }
}