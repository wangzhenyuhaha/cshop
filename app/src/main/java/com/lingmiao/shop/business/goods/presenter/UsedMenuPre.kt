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

    fun deleteGoodsGroup(shopCatId: MenuVo?, position: Int)

    interface PubView: BaseView, BaseLoadMoreView<MenuVo> {
        fun onDeleteGroupSuccess(position: Int)
    }

}