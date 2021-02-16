package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * 设置支付宝账户
 */
interface AliPayAccountPresenter : BasePresenter, WithdrawAccountPresenter {
    /**
     * 提交账户信息
     */
    fun submitAccountInfo(data: WithdrawAccountBean)
    /**
     * 修改账户信息
     */
    fun updateAccountInfo(data : WithdrawAccountBean)

    interface View : BaseView, WithdrawAccountPresenter.View {
        /**
         * 设置成功
         */
        fun setAccountSuccess();
    }
}