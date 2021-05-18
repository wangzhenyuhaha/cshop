package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.lingmiao.shop.business.goods.GoodsPublishActivity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.business.goods.pop.StatusMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsSearchPre
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.GoodsPublishNewActivity
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品出售中列表
 */
class GoodsSearchPreImpl(var context: Context, var view: GoodsSearchPre.StatusView) : BasePreImpl(view), GoodsSearchPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }

    private val statusPreImpl : SearchStatusPreImpl by lazy {
        SearchStatusPreImpl(context, view);
    }

    override fun loadListData(page: IPage, oldDatas: List<*>, searchText: String?, isGoodsName : Boolean) {
        if (searchText.isNullOrBlank()) {
            view.showToast("请输入搜索文字")
            return
        }

        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), if(isGoodsName) searchText else null, if(isGoodsName) null else searchText)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun clickMenuView(goodsVO: GoodsVO?, position: Int, target: View) {
        if (goodsVO == null) {
            return
        }
        menuPopPre.showMenuPop(goodsVO.getMenuType(), target) { menuType ->
            when (menuType) {
                GoodsMenuPop.TYPE_EDIT -> {
                    menuPopPre.clickEditGoods(context, goodsVO)
                }
                GoodsMenuPop.TYPE_ENABLE -> {
                    menuPopPre.clickEnableGoods(goodsVO.goodsId) {
                        view.onGoodsEnable(goodsVO.goodsId, position)
                    }
                }
                GoodsMenuPop.TYPE_DISABLE -> {
                    menuPopPre.clickDisableGoods(goodsVO.goodsId) {
                        view.onGoodsDisable(goodsVO.goodsId, position)
                    }
                }
                GoodsMenuPop.TYPE_QUANTITY -> {
                    menuPopPre.clickQuantityGoods(goodsVO.goodsId) {
                        view.onGoodsQuantity(it, position)
                    }
                }
                GoodsMenuPop.TYPE_DELETE -> {
                    menuPopPre.clickDeleteGoods(goodsVO.goodsId) {
                        view.onGoodsDelete(goodsVO.goodsId, position)
                    }
                }
            }
        }
    }

    override fun clickItemView(item: GoodsVO?, position: Int) {
        GoodsPublishNewActivity.openActivity(context, item?.goodsId)
    }

    override fun clickSearchMenuView(target: View) {
        statusPreImpl?.showMenuPop(
            StatusMenuPop.TYPE_GOODS_OWNER or StatusMenuPop.TYPE_GOODS_NAME,
            target) { menuType ->
            when (menuType) {
                StatusMenuPop.TYPE_GOODS_OWNER->{
                    view?.onShiftGoodsOwner()
                }
                StatusMenuPop.TYPE_GOODS_NAME->{
                    view?.onShiftGoodsName()
                }
            }
        };
    }

}