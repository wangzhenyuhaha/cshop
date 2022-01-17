package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher

interface ElectronicVoucherPresenter : BasePresenter {

    interface View : BaseView, BaseLoadMoreView<ElectronicVoucher> {

    }
}