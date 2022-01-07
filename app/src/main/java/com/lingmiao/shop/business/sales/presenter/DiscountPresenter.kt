package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.bean.Coupon

interface DiscountPresenter : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    interface View : BaseView, BaseLoadMoreView<Coupon> {

    }
}