package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface GoodsTabNumberPre : BasePresenter {

    fun loadNumbers();

    interface View : BaseView{

        fun loadNumberSuccess(vo : DashboardDataVo);

    }
}