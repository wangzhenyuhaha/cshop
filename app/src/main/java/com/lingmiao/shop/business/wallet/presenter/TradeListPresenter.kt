package com.lingmiao.shop.business.wallet.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.wallet.bean.DepositVo

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

//骑手交易列表
interface RiderListPresenter : BasePresenter {

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

interface OrderListPresenter : BasePresenter {

    /**
     * 加载列表
     */
    fun loadList(
        page: IPage,
        accountId : String,
        data: MutableList<OrderList>)

    interface View : BaseView, BaseLoadMoreView<OrderList> {

    }
}