package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.presenter.GoodsMenuSelectPre
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品待审核列表
 */
class GoodsMenuSelectPreImpl(val context: Context, val view: GoodsMenuSelectPre.StatusView) :
    BasePreImpl(view), GoodsMenuSelectPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }


    override fun loadListData(page: IPage, oldDatas: List<*>) {
        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = GoodsRepository.loadGoodsList(page.getPageIndex(), GoodsVO.MARKET_STATUS_ENABLE.toString(), "1,2", "", "", null,null,null);
                //GoodsRepository.loadGoodsListByCId(page.getPageIndex(), "")
            if (resp.isSuccess) {
                val goodsList = resp.data.data

                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

}