package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.GoodsNumberEvent
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsStatusPre
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.GoodsPublishNewActivity
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品出售中列表
 */
class GoodsMarketEnablePreImpl(override var context: Context,override var view: GoodsStatusPre.StatusView) :
    GoodsBatchPreImpl(context, view), GoodsStatusPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }

    override fun loadListData(page: IPage, datas: List<*>) {
        mCoroutine.launch {
            if (datas.isEmpty()) {
                view.showPageLoading()
            }
            val resp =
                GoodsRepository.loadGoodsList(page.getPageIndex(), GoodsVO.MARKET_STATUS_ENABLE.toString(), GoodsVO.getEnableAuth())
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                EventBus.getDefault().post(GoodsNumberEvent(GoodsNewFragment.GOODS_STATUS_ENABLE,resp.data.dataTotal));
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }

//            val goodsList = getTempList();
//            view.onLoadMoreSuccess(goodsList, getTempList().isNotEmpty())
            view.hidePageLoading()
        }
    }

    fun getTempList() : List<GoodsVO>? {
        val regionsType = object : TypeToken<PageVO<GoodsVO>>(){}.type;
        var json = Gson().fromJson<PageVO<GoodsVO>>(ResourceUtils.readAssets2String("goods.json"), regionsType);
        return json?.data;
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
                GoodsMenuPop.TYPE_REBATE -> {
                    clickOnRebate(goodsVO?.goodsId) {

                    }
                }
                GoodsMenuPop.TYPE_SHARE -> {
                    menuPopPre.clickShareGoods(context, goodsVO)
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

}