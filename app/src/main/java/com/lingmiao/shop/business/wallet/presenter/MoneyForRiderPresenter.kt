package com.lingmiao.shop.business.wallet.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.wallet.bean.AccountVo

interface MoneyForRiderPresenter : BasePresenter {

    fun loadInfo()

    //充值
    fun riderMoneyApply(mWallet: AccountVo?, value: Double)

    //提现
    fun riderMoneyWithdrawal(value: Double)

    interface View : BaseView {
        /**
         * 加载账户信息成功
         */
        fun loadInfoSuccess(info: AccountVo?)

        /**
         * 加载账户信息失败
         */
        fun loadInfoError(code: Int)

        fun onWithdrawSuccess()

        fun onApplied(value: WxPayReqVo?)
    }

}