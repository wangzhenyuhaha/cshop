package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import java.io.Serializable

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
interface GoodsStatusPre : BasePresenter, GoodsBatchPre {

    fun loadListData(page: IPage, groupPath : String?, catePath: String?, isEvent : Int?, list: List<*>)

    fun clickMenuView(item: GoodsVO?, position: Int, view: View)

    fun clickItemView(item: GoodsVO?, position: Int)

    interface StatusView : GoodsBatchPre.View, BaseLoadMoreView<GoodsVO> {
        fun onGoodsEnable(goodsId: String?, position: Int)
        fun onGoodsDisable(goodsId: String?, position: Int)
        fun onGoodsQuantity(quantity: String?, position: Int)
        fun onGoodsDelete(goodsId: String?, position: Int)
        fun onSetTotalCount(count: Int?);
    }

}