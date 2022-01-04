package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.pop.ChildrenGoodsMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsMenuInfoPre
import kotlinx.coroutines.launch

class GoodsMenuInfoPreImpl(val context: Context, val view: GoodsMenuInfoPre.View) : BasePreImpl(),
    GoodsMenuInfoPre {

    private val mGroupPreImpl: PopUserGroupPreImpl by lazy { PopUserGroupPreImpl(view) }

    private val menuPopPre: ChildrenGoodsMenuPreImpl by lazy {
        ChildrenGoodsMenuPreImpl(
            context,
            view
        )
    }

    private val quantityPopPre: QuantityPricePreImpl by lazy { QuantityPricePreImpl(context, view) }

    override fun loadListData(path: String?, page: IPage, list: List<*>, isEvent: Int?) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadMenuGoodsList(
                page.getPageIndex(),
                GoodsVO.MARKET_STATUS_ENABLE.toString(),
                GoodsVO.getEnableAuth(),
                path
            )
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                if (isEvent == 1) {
                    //使用活动价
                    if (goodsList != null) {
                        for (i in goodsList) {
                            i.isEvent = true
                        }
                    }
                }
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

    private fun showGroup(isTop: Int, callback: (List<ShopGroupVO>?, String?) -> Unit) {
        mGroupPreImpl.showAllGoodsGroupPop(context, callback)
    }

    override fun clickMenuView(isTop: Int, item: GoodsVO?, position: Int, target: View) {
        if (item?.goodsId == null) {
            return
        }
        menuPopPre.showMenuPop(
            ChildrenGoodsMenuPop.TYPE_PRICE or ChildrenGoodsMenuPop.TYPE_EDIT_CATE,
            target
        ) { menuType ->
            when (menuType) {
                ChildrenGoodsMenuPop.TYPE_PRICE -> {
                    //弹出下拉菜单,输入商品ID
                    quantityPopPre.clickQuantityGoodsNew(item.goodsId) {
                    }
                }
                ChildrenGoodsMenuPop.TYPE_EDIT_CATE -> {
                    showGroup(isTop) { groups, _ ->
                        if (groups == null || groups.isEmpty()) {
                            return@showGroup
                        }
                        val last = groups[groups.size - 1]
                        bindGoods(listOf(item.goodsId!!.toInt()), last.shopCatId!!) {
                            view.onUpdatedGoodsGroup()
                        }
                    }
                }
            }
        }
    }


    private fun bindGoods(ids: List<Int?>?, id: String, callback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.bindGoods(ids, id)
            handleResponse(resp) {
                callback.invoke()
            }
        }
    }

}