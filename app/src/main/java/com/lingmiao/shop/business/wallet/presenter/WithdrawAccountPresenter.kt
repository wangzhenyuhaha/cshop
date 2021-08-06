package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.wallet.bean.RateVo

/**
 * 提现
 */
interface WithdrawAccountPresenter : BasePresenter {

    /**
     * 查询提现账户信息
     */
    fun getWithdrawAccount();

    interface View : BaseView {

        /**
         * 获取提现信息成功
         */
        fun getWithdrawAccountSuccess(account : WithdrawAccountVo);

        fun setRate(rate : RateVo?);
    }
}