package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.GoodsMenuInfoPre
import kotlinx.coroutines.launch

class GoodsMenuInfoPreImpl(val context: Context, val view: GoodsMenuInfoPre.View) : BasePreImpl(),
    GoodsMenuInfoPre {


    override fun loadListData(path: String?, page: IPage, list: List<*>) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadMenuGoodsList(
                page.getPageIndex(),
                GoodsVO.MARKET_STATUS_ENABLE.toString(),
                GoodsVO.getEnableAuth(),
                path
            )
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }


}