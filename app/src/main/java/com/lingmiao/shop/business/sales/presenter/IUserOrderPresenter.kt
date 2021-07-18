package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.main.bean.MemberOrderBean
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.sales.bean.OrderItemVo
import com.lingmiao.shop.business.sales.bean.UserOrderVo
import com.lingmiao.shop.business.sales.bean.UserVo

/**
Create Date : 2021/3/101:10 AM
Auther      : Fox
Desc        :
 **/
interface IUserOrderPresenter : BasePresenter {

    fun loadListData(page: IPage, memberId : String, list: List<*>)

    fun getOrderCount(memberId : String);

    interface PubView : BaseView, BaseLoadMoreView<OrderList>  {

       fun onSetOrderCount(memberOrder : MemberOrderBean);

    }
}