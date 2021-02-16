package com.lingmiao.shop.business.tuan.presenter.impl

import com.lingmiao.shop.business.tuan.presenter.OrderRefusePresenter
import com.james.common.base.BasePreImpl

class OrderRefusePresenterImpl(view : OrderRefusePresenter.View) : BasePreImpl(view) ,
    OrderRefusePresenter {

    override fun onRefuse(id: String, reason: String, desc: String) {

    }

}