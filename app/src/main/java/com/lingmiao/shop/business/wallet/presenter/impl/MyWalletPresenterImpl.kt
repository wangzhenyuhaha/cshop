package com.lingmiao.shop.business.wallet.presenter.impl

import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class MyWalletPresenterImpl(private var view : MyWalletPresenter.View) :
    BasePreImpl(view), MyWalletPresenter {

    override fun loadWalletData() {
        mCoroutine.launch {

            view.showDialogLoading()
//            view.showPageLoading();
            val resp = WalletRepository.getWalletIndexInfo();

            handleResponse(resp) {
                if(resp.isSuccess()) {
                    view.onLoadWalletDataSuccess(resp?.data?.data);
                } else {
                    view.onLoadWalletDataError(resp.code);
                }
//                view.hidePageLoading();
                view.hideDialogLoading()
            }
        }
    }
}