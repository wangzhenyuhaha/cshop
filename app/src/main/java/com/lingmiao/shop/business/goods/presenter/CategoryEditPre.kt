package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.me.bean.ShopManage
import com.lingmiao.shop.business.me.bean.ShopManageRequest

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface CategoryEditPre : BasePresenter {

    fun loadLv1GoodsGroup()

    fun loadLv2GoodsGroup(lv1GroupId: String?)

    fun deleteGoodsGroup(shopCatId: CategoryVO?, position: Int)

    fun add(vo : CategoryVO);

    interface PubView: BaseView, BaseLoadMoreView<CategoryVO> {
        fun onDeleteGroupSuccess(position: Int)
        fun addSuccess(vo : CategoryVO);
    }
}