package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

/**
Create Date : 2021/6/19:56 AM
Auther      : Fox
Desc        :
 **/
interface GoodsListOfMenuPre : BasePresenter {

    fun loadListData(path : String?, page: IPage, list: List<*>)

    fun showGroupPop(isTop : Int);

    fun clickMenuView(item: GoodsVO?, position: Int, target: android.view.View);

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {

        fun onUpdateGroup(groupId : List<ShopGroupVO>?, groupName: String?);

        fun setGoodsCount(count : Int);
    }

}