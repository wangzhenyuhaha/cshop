package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface CategoryEditPre : BasePresenter {

    fun loadLv1GoodsGroup()

    fun loadLv2GoodsGroup(lv1GroupId: String?)

    fun deleteGoodsGroup(shopCatId: CategoryVO?, position: Int)

    fun add(pId : Int, name : String);

    interface PubView: BaseView, BaseLoadMoreView<CategoryVO> {
        fun onDeleteGroupSuccess(position: Int)
        fun addSuccess(pId : Int, vo : CategoryVO);
        fun onLoadedLv2(
            key: String,
            list: List<CategoryVO>?
        );
    }
}