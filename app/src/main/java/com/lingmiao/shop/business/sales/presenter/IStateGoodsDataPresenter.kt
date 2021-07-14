package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.sales.bean.GoodsSalesRespBean

/**
Create Date : 2021/3/101:10 AM
Auther      : Fox
Desc        :
 **/
interface IStateGoodsDataPresenter : BasePresenter {

    fun getGoodsSales(start: Long?, end: Long?);
    interface PubView : BaseView {
        fun setGoodsSales(item : GoodsSalesRespBean?);
    }
}