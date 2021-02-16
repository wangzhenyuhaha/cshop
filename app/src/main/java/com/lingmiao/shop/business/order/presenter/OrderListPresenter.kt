package com.lingmiao.shop.business.order.presenter

import com.lingmiao.shop.business.order.bean.OrderList
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface OrderListPresenter: BasePresenter {
    fun loadListData(
        page: IPage,
        status: String,
        data: MutableList<OrderList>
    )

    fun deleteOrder(tradeSn: String)

    fun verifyOrder(verifyCode: String)

    interface StatusView: BaseView, BaseLoadMoreView<OrderList>{
        fun onDeleteOrderSuccess()

        fun verifySuccess();

        fun verifyFailed();
    }
}