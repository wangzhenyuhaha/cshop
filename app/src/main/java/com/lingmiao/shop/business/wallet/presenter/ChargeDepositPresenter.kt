package com.lingmiao.shop.business.wallet.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * 押金充值
 */
interface ChargeDepositPresenter : BasePresenter{

    /**
     * 充值
     */
    fun charge()

    interface View : BaseView {
        /**
         * 充值成功
         */
        fun chargeSuccess()
        /**
         * 充值失败
         */
        fun chargeError(code : Int)
    }
}