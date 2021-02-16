package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理
 */
interface GroupManagerPre: BasePresenter {

    fun loadLv1GoodsGroup()

    fun loadLv2GoodsGroup(lv1GroupId: String?)

    fun deleteGoodsGroup(shopCatId: ShopGroupVO?, position: Int)

    interface GroupManagerView: BaseView, BaseLoadMoreView<ShopGroupVO> {
        fun onDeleteGroupSuccess(position: Int)
    }
}