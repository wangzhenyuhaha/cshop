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
        start: Long?,
        end: Long?,
        data: MutableList<OrderList>,
        size:Int
    )

    fun deleteOrder(tradeSn: String)

    fun takeOrder(trade: OrderList)

    fun refuseOrder(tradeSn: String)

    fun shipOrder(sn: String)

    fun signOrder(sn : String)

    fun refuseService(sn : String)

    fun acceptService(sn : String)

    fun verifyOrder(verifyCode: String)

    fun prepareOrder(sn: String)

    interface StatusView: BaseView, BaseLoadMoreView<OrderList>{

        fun onTakeSuccess(trade: OrderList);

        fun onRefuseSuccess();

        fun onDeleteOrderSuccess()

        fun onPreparedOrder();

        fun onPrepareOrderFail();

        fun onShipped();

        fun onSigned();

        fun onRefusedService();

        fun onAcceptService();

        fun verifySuccess();

        fun verifyFailed();
    }
}