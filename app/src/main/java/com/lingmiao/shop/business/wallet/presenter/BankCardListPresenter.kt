package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.BankCardAccountBean
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.list.BaseListView
import com.james.common.base.loadmore.core.IPage

/**
 * 银行卡列表
 */
interface BankCardListPresenter : BasePresenter{

    /**
     * 加载银行卡列表
     */
    fun loadList(
        page: IPage,
        data: MutableList<BankCardAccountBean>)

    /**
     * 解绑
     */
    fun unBind(id : String)

    interface View : BaseView, BaseListView<BankCardAccountBean> {
        /**
         * 解绑之后
         */
        fun unBind()
    }
}