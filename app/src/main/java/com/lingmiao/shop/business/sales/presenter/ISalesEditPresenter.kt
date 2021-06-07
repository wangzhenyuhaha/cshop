package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.sales.bean.SalesVo

/**
Create Date : 2021/3/101:10 AM
Auther      : Fox
Desc        :
 **/
interface ISalesEditPresenter : BasePresenter {

    fun submitDiscount(mItem: SalesVo?)

    interface PubView : BaseView {

        fun onSubmitDiscount();

    }
}