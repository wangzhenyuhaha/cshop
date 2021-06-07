package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.SalesVo

/**
Create Date : 2021/3/101:10 AM
Auther      : Fox
Desc        :
 **/
interface ISalesSettingPresenter : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    fun delete(id : String?, position: Int);

    fun endDiscount(id : String?, position: Int);

    interface PubView : BaseView, BaseLoadMoreView<SalesVo> {
        fun onDelete(position: Int);
        fun onEndDiscount(position: Int);
    }
}