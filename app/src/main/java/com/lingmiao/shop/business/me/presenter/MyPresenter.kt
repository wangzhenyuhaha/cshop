package com.lingmiao.shop.business.me.presenter

import com.lingmiao.shop.business.me.bean.My
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView


interface MyPresenter : BasePresenter {
    fun getMyData()

    interface View : BaseView {
        fun onMyDataSuccess(bean: My)
        fun ontMyDataError()
    }
}
