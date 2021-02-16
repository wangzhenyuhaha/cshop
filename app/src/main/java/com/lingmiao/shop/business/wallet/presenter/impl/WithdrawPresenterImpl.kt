package com.lingmiao.shop.business.wallet.presenter.impl

import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.lingmiao.shop.business.wallet.bean.WithdrawApplyVo
import com.lingmiao.shop.business.wallet.presenter.WithdrawPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class WithdrawPresenterImpl(private val view: WithdrawPresenter.View) : BasePreImpl(view),
    WithdrawPresenter {

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

    override fun submitAliPayAccount(data: WithdrawAccountBean): Boolean {
        var flag = false;
        mCoroutine.launch {

            val resp = WalletRepository.submitWithdrawAccount(data);

            handleResponse(resp) {
                flag = resp?.isSuccess;
            }
        }
        return flag;
    }

    override fun submitBankAccount(data: BankCardVo): Boolean {
        var flag = false;

        mCoroutine.launch {

            val resp = WalletRepository.bindBankCard(data);

            handleResponse(resp) {
                flag = resp?.isSuccess;
            }
        }

        return flag;
    }

    override fun applyWithdraw(data: WithdrawApplyVo) {
        mCoroutine.launch {

            view.showDialogLoading();

            val resp = WalletRepository.applyWithdraw(data);

            if(resp?.data?.success ?: false) {
                view.showBottomToast("申请成功");
                view.applySuccess();
            } else {
                view.applyFailed(resp?.data?.message ?: "申请失败")
            }
            view.hideDialogLoading()
        }
    }
}