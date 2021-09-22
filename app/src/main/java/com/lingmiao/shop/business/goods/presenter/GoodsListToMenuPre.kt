package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO

/**
Create Date : 2021/6/19:56 AM
Auther      : Fox
Desc        :
 **/
interface GoodsListToMenuPre : BasePresenter {

    fun loadListData(cid : String?,cpath : String?, page: IPage, list: List<*>)

    fun bindGoods(ids : List<Int?>?, id : String);

    fun showCategoryPop(cid : String?,target: android.view.View)

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {
        fun onUpdatedCategory(list : List<CategoryVO>?, name : String?)
        fun setGoodsCount(count : Int)
        fun refresh()
    }

}