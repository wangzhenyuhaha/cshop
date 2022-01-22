package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher

interface ElectronicVoucherPresenter : BasePresenter {

    //查询电子券列表
    fun loadListData(page: IPage, list: List<*>)

    //删除电子券
    fun deleteElectronicVoucher(id: Int, position: Int)

    //编辑电子券
    fun editElectronicVoucher(disabled: Int, id: Int, position: Int)

    interface View : BaseView, BaseLoadMoreView<ElectronicVoucher> {
        //删除电子券成功
        fun deleteCouponSuccess(position: Int)

        //编辑电子券成功
        fun editCouponSuccess(position: Int)
    }
}