package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.lingmiao.shop.business.goods.api.bean.MenuVo

/**
 * Desc   : 商品菜单管理
 */
interface UsedMenuPre: BasePresenter {

    fun loadLv1GoodsGroup()

    fun loadLv2GoodsGroup(lv1GroupId: String?)

    fun deleteGoodsGroup(shopCatId: ShopGroupVO?, position: Int)

    fun addGroup(str : String, level: Int)

    interface PubView: BaseView, BaseLoadMoreView<ShopGroupVO> {

        fun onDeleteGroupSuccess(position: Int)

        fun onGroupAdded(item : ShopGroupVO);
    }

}