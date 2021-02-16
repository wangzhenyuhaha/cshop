package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * 添加银行卡
 */
interface AddBankCardPresenter : BasePresenter{

    /**
     * 提交银行卡
     */
    fun submitBankCard(data : BankCardVo);

    fun updateBankCard(data : BankCardVo);

    interface View : BaseView {

        /**
         * 提交银行卡成功
         */
        fun submitBankCardSuccess();

    }
}