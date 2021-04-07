package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.GoodsItem

/**
Create Date : 2021/4/71:22 PM
Auther      : Fox
Desc        :
 **/
interface GoodsTopPresenter : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    interface PubView : BaseView, BaseLoadMoreView<GoodsItem> {

    }
}