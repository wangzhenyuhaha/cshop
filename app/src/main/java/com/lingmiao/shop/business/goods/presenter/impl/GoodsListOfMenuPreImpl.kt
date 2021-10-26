package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
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

    private fun showGroup(isTop : Int, callback: (List<ShopGroupVO>?, String?) -> Unit) {
        mGroupPreImpl.showGoodsGroupPop(context, isTop, callback);
    }

    // 常用菜单只展示自己及子类
    fun showGroup(isTop : Int, path: String?, callback: (List<ShopGroupVO>?, String?) -> Unit) {
        mGroupPreImpl.showGoodsGroupPop(context, isTop, path, callback);
    }

    override fun showGroupPop(isTop : Int, path : String?) {
        if(isTop == 1) {
            showGroup(isTop) { groups, groupName ->
                view.onUpdateGroup(groups, groupName);
            }
        } else {
            showGroup(isTop, path) { groups, groupName ->
                view.onUpdateGroup(groups, groupName);
            }
        }
    }

    override fun clickMenuView(isTop : Int, item: GoodsVO?, position: Int, target: View) {
        if (item?.goodsId == null) {
            return
        }
        menuPopPre.showMenuPop(ChildrenGoodsMenuPop.TYPE_PRICE or ChildrenGoodsMenuPop.TYPE_EDIT_CATE, target) { menuType ->
            when (menuType) {
                ChildrenGoodsMenuPop.TYPE_PRICE -> {

                    //弹出下拉菜单,输入商品ID
                    quantityPopPre.clickQuantityGoods(item.goodsId) {

                    }

                }
                ChildrenGoodsMenuPop.TYPE_EDIT_CATE -> {
                    showGroup(isTop) { groups, groupName ->
                        if(groups == null || groups.isEmpty() || groups.get(0) == null) {
                            return@showGroup;
                        }
                        val last = groups[groups.size - 1];
                        last.shopCatId.let {
                            bindGoods(listOf(item.goodsId!!.toInt()), it!!) {
                                view.onUpdatedGoodsGroup();
                            }
                        }
                    }
                }
            }
        }
    }

    fun bindGoods(ids: List<Int?>?, id: String, callback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.bindGoods(ids, id);
            handleResponse(resp) {
                callback.invoke();
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