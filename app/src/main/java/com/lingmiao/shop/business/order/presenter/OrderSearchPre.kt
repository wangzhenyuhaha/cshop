package com.lingmiao.shop.business.order.presenter

import android.view.View
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.order.bean.OrderList

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品查询
 */
interface OrderSearchPre: BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String?, isGoodsName : Boolean)

    interface StatusView: BaseView, BaseLoadMoreView<OrderList> {

    }

}