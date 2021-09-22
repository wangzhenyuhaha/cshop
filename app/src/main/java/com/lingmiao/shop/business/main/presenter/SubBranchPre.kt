package com.lingmiao.shop.business.main.presenter

import android.view.View
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.main.bean.BankDetail

interface SubBranchPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String? )

    fun loadSubListData(page: IPage, list: List<*>,bank:String, searchText: String?)

//    fun clickMenuView(item: BankDetail?, position: Int, view: View)
//
//    fun clickItemView(item: BankDetail?, position: Int)
//
//    fun clickSearchMenuView(target: View);

    interface StatusView: BaseView, BaseLoadMoreView<BankDetail> {
        fun onGoodsEnable(goodsId: String?, position: Int)
        fun onGoodsDisable(goodsId: String?, position: Int)
        fun onGoodsQuantity(quantity: String?, position: Int)
        fun onGoodsDelete(goodsId: String?, position: Int)
        fun onShiftGoodsOwner()
        fun onShiftGoodsName()
    }
}