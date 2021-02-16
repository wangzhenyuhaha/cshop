package com.lingmiao.shop.business.tuan.presenter

import com.lingmiao.shop.business.tuan.bean.OrderVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface OrderListPresenter : BasePresenter {
    fun loadList(
        page: IPage,
        data: MutableList<OrderVo>);

    interface View : BaseView, BaseLoadMoreView<OrderVo> {

    }
}