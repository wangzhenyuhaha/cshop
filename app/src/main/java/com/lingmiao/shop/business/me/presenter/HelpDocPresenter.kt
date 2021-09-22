package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.me.bean.HelpDocItemVo
import com.lingmiao.shop.business.order.bean.OrderList

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface HelpDocPresenter : BasePresenter {

    fun queryContentList(page: IPage, type: String, data: MutableList<HelpDocItemVo>);

    interface View : BaseLoadMoreView<HelpDocItemVo>, BaseView {

    }
}