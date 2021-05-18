package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.lingmiao.shop.business.goods.GoodsPublishActivity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.GoodsNumberEvent
import com.lingmiao.shop.business.goods.fragment.GoodsFragment
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsBatchPre
import com.lingmiao.shop.business.goods.presenter.GoodsStatusPre
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.GoodsPublishNewActivity
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品待审核列表
 */
class GoodsAuthWaitingPreImpl(override val context: Context,override  val view: GoodsStatusPre.StatusView) :
    GoodsBatchPreImpl(context, view as GoodsBatchPre.View), GoodsStatusPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }


    override fun loadListData(page: IPage, datas: List<*>) {
        mCoroutine.launch {
            if (datas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = GoodsRepository.loadAuthGoodsList(page.getPageIndex(), GoodsVO.AUTH_STATUS_WAITING)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                EventBus.getDefault().post(GoodsNumberEvent(GoodsFragment.GOODS_STATUS_IS_AUTH,resp.data.dataTotal));
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun clickMenuView(goodsVO: GoodsVO?, position: Int, view: View) {
        if (goodsVO == null) {
            return
        }
        menuPopPre.showMenuPop(goodsVO.getMenuType(), view) { menuType ->
            if (menuType == GoodsMenuPop.TYPE_EDIT) {
                menuPopPre.clickEditGoods(context, goodsVO)
            } else if(menuType == GoodsMenuPop.TYPE_REBATE) {
                clickOnRebate(goodsVO?.goodsId) {

                }
            }
        }
    }

    override fun clickItemView(item: GoodsVO?, position: Int) {
        GoodsPublishNewActivity.openActivity(context, item?.goodsId)
    }

}