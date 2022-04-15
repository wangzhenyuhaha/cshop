package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.My

interface PopularizePre : BasePresenter {

    fun getMyData()

    interface View : BaseView {
        fun onMyDataSuccess(bean: My)
        fun ontMyDataError()
    }

}