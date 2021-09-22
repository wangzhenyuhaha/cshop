package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.wallet.bean.RateVo

interface WalletInfoPresenter : BasePresenter{
    /**
     * 加载账户信息
     */
    fun loadInfo()

    fun getTypeHint() : String;

    fun getOptionHint(): String;

    fun getDetailHint(): String;

    fun getTitleHint() : String;

    fun onOptionClicked();

    fun getRate();

    interface View : BaseView {
        /**
         * 加载账户信息成功
         */
        fun loadInfoSuccess(info: AccountVo ?);
        /**
         * 加载账户信息失败
         */
        fun loadInfoError(code : Int);

        fun setRate(rate: RateVo?);
    }
}