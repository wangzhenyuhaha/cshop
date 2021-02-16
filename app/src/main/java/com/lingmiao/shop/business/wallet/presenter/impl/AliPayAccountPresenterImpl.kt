package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.lingmiao.shop.business.wallet.presenter.AliPayAccountPresenter
import com.lingmiao.shop.business.wallet.presenter.WithdrawAccountPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class AliPayAccountPresenterImpl(var view : AliPayAccountPresenter.View) : BasePreImpl(view), AliPayAccountPresenter,
    WithdrawAccountPresenter {

    private val withdrawAccount: WithdrawAccountPresenter by lazy { WithdrawAccountPresenterImpl(view) }

    override fun submitAccountInfo(data : WithdrawAccountBean) {
        mCoroutine.launch {

            view.showPageLoading();

            val resp = WalletRepository.submitWithdrawAccount(data);

            handleResponse(resp) {
                ToastUtils.showShort(resp?.data?.message ?: "设置成功")
                view.setAccountSuccess();
            }
            view.hidePageLoading()
        }
    }

    override fun updateAccountInfo(data: WithdrawAccountBean) {
        mCoroutine.launch {

            view.showDialogLoading();

            val resp = WalletRepository.updateWithdrawAccount(data);

            if(resp.isSuccess && resp?.data?.success ?: false) {
                ToastUtils.showShort(resp?.data?.message ?: "设置成功")
                view.setAccountSuccess();
            }
//            handleResponse(resp) {
//                ToastUtils.showShort(resp?.data?.message ?: "设置成功")
//                view.setAccountSuccess();
//            }
            view.hideDialogLoading()
        }
    }

    override fun getWithdrawAccount() {
        withdrawAccount.getWithdrawAccount();
    }
}