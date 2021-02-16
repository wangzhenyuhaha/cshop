package com.lingmiao.shop.business.tuan.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface OrderRefusePresenter : BasePresenter {

    fun onRefuse(
        id: String,
        reason: String,
        desc: String
    )

    interface View : BaseView {
        fun onOrderRefuseSuccess()
        fun onOrderRefuseError(code: Int)

    }
}