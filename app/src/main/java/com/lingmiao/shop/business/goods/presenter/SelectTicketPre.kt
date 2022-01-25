package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher

interface SelectTicketPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    interface View : BaseView, BaseLoadMoreView<ElectronicVoucher> {

    }
}