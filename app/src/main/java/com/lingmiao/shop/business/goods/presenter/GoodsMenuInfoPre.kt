package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO

interface GoodsMenuInfoPre : BasePresenter {

    //isEvent 是否使用活动价格  1 用 0 不用
    fun loadListData(path : String?, page: IPage, list: List<*>,isEvent:Int?)

    fun clickMenuView(isTop : Int, item: GoodsVO?, position: Int, target: android.view.View)

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {
        fun onUpdatedGoodsGroup()
    }
}