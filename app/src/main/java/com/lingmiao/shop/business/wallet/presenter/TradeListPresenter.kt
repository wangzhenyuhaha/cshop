package com.lingmiao.shop.business.wallet.presenter

import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

/**
 * 押金列表
 */
interface TradeListPresenter : BasePresenter {

    /**
     * 加载列表
     */
    fun loadList(
        page: IPage,
        accountId : String,
        data: MutableList<DepositVo>)

    interface View : BaseView, BaseLoadMoreView<DepositVo> {

    }
}