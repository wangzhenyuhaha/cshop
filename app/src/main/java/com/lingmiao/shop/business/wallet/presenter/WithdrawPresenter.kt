package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.lingmiao.shop.business.wallet.bean.WithdrawApplyVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * 提现
 */
interface WithdrawPresenter : BasePresenter, WithdrawAccountPresenter {

    /**
     * 提交支付宝账户
     */
    fun submitAliPayAccount(data : WithdrawAccountBean) : Boolean;

    /**
     * 提交银行卡账户
     */
    fun submitBankAccount(data: BankCardVo) : Boolean;

    /**
     * 申请提现
     */
    fun applyWithdraw(data: WithdrawApplyVo);

    interface View : BaseView, WithdrawAccountPresenter.View {

        /**
         * 申请成功
         */
        fun applySuccess();

        /**
         * 申请失败
         */
        fun applyFailed(msg: String);

    }
}