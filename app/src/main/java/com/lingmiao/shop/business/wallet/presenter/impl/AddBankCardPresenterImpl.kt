package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.business.wallet.presenter.AddBankCardPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
 * 银行卡
 */
class AddBankCardPresenterImpl(var view : AddBankCardPresenter.View) : BasePreImpl(view), AddBankCardPresenter {

    /**
     * 提交银行卡
     */
    override fun submitBankCard(data: BankCardVo) {
        mCoroutine.launch {

            view.showDialogLoading();

            val resp = WalletRepository.bindBankCard(data);

            handleResponse(resp) {
                ToastUtils.showShort("提交成功")
                view.submitBankCardSuccess();
            }
            view.hideDialogLoading()
        }
    }

    override fun updateBankCard(data: BankCardVo) {
        mCoroutine.launch {

            view.showDialogLoading();
            // todo 修改的接口
            val resp = WalletRepository.bindBankCard(data);

            handleResponse(resp) {
                ToastUtils.showShort("提交成功")
                view.submitBankCardSuccess();
            }
            view.hideDialogLoading()
        }
    }

}