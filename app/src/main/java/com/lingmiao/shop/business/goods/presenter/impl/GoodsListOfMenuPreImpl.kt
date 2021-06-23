package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.pop.ChildrenGoodsMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsListOfMenuPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/6/19:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsListOfMenuPreImpl(val context: Context, val view : GoodsListOfMenuPre.View) : BasePreImpl(), GoodsListOfMenuPre {

    private val mGroupPreImpl: PopUserGroupPreImpl by lazy { PopUserGroupPreImpl(view) }

    private val menuPopPre: ChildrenGoodsMenuPreImpl by lazy { ChildrenGoodsMenuPreImpl(context, view) }

    private val quantityPopPre: QuantityPricePreImpl by lazy { QuantityPricePreImpl(context, view) }

    override fun showGroupPop(isTop : Int) {
        mGroupPreImpl.showGoodsGroupPop(context, isTop) { groups, groupName ->
            view.onUpdateGroup(groups, groupName)
        }
    }


    override fun clickMenuView(item: GoodsVO?, position: Int, target: View) {
        if (item == null || item.goodsId == null) {
            return
        }
        menuPopPre.showMenuPop(ChildrenGoodsMenuPop.TYPE_PRICE, target) { menuType ->
            when (menuType) {
                ChildrenGoodsMenuPop.TYPE_PRICE -> {
                    quantityPopPre.clickQuantityGoods(item?.goodsId) {

                    }
                }
            }
        }
    }

    override fun loadListData(path : String?, page: IPage, list: List<*>) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadMenuGoodsList(page.getPageIndex(), GoodsVO.MARKET_STATUS_ENABLE.toString(), GoodsVO.getEnableAuth(), path)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size?:0 >= 10)
                view.setGoodsCount(resp.data.dataTotal);
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

}