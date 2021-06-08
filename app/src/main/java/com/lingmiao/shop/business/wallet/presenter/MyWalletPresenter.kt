package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo

/**
 * 我的钱包
 */
interface MyWalletPresenter : BasePresenter{

    /**
     * 加载钱包数据
     */
    fun loadWalletData()

    interface View : BaseView {

        /**
         * 加载成功
         */
        fun onLoadWalletDataSuccess(data : WalletVo?)

        fun onLoadedAccount(data : WithdrawAccountVo?)

        /**
         * 加载失败
         */
        fun onLoadWalletDataError(code : Int)
    }
}