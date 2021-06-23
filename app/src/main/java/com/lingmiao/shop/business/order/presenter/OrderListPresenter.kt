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
        data: MutableList<OrderList>
    )

    fun deleteOrder(tradeSn: String)

    fun takeOrder(tradeSn: String)

    fun refuseOrder(tradeSn: String)

    fun shipOrder(sn: String)

    fun signOrder(sn : String)

    fun refuseService(sn : String)

    fun acceptService(sn : String)

    fun verifyOrder(verifyCode: String)

    interface StatusView: BaseView, BaseLoadMoreView<OrderList>{

        fun onTakeSuccess();

        fun onRefuseSuccess();

        fun onDeleteOrderSuccess()

        fun onShipped();

        fun onSigned();

        fun onRefusedService();

        fun onAcceptService();

        fun verifySuccess();

        fun verifyFailed();
    }
}