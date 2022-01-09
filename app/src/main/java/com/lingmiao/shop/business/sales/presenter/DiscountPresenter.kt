package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.bean.Coupon

interface DiscountPresenter : BasePresenter {

    //查询优惠券列表
    fun loadListData(page: IPage, list: List<*>)

    //删除优惠券
    fun deleteCoupon(id: Int,position:Int)

    interface View : BaseView, BaseLoadMoreView<Coupon> {

        //删除优惠券成功
        fun deleteCouponSuccess(position: Int)
    }
}