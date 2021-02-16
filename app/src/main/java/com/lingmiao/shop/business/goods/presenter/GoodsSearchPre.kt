package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品查询
 */
interface GoodsSearchPre: BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String?, isGoodsName : Boolean)

    fun clickMenuView(item: GoodsVO?, position: Int, view: View)

    fun clickItemView(item: GoodsVO?, position: Int)

    fun clickSearchMenuView(target: View);

    interface StatusView: BaseView, BaseLoadMoreView<GoodsVO> {
        fun onGoodsEnable(goodsId: String?, position: Int)
        fun onGoodsDisable(goodsId: String?, position: Int)
        fun onGoodsQuantity(quantity: String?, position: Int)
        fun onGoodsDelete(goodsId: String?, position: Int)
        fun onShiftGoodsOwner()
        fun onShiftGoodsName()
    }

}