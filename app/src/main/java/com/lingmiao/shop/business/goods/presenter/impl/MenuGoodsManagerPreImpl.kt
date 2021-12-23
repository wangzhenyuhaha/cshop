package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.MenuGoodsManagerPre
import kotlinx.coroutines.launch

class MenuGoodsManagerPreImpl(val context: Context, val view: MenuGoodsManagerPre.View) :
    BasePreImpl(view), MenuGoodsManagerPre {

    override fun loadLv1GoodsGroup(isTop: Int) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = GoodsRepository.loadLv1ShopGroup(isTop)
            if (resp.isSuccess) {
                view.onLoadLv1GoodsGroupSuccess(resp.data, isTop)
            }
        }
    }

    //排序
    override fun sort(isTop: Int, cid: String, sort: Int) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = GoodsRepository.sort(isTop, cid, sort)
            handleResponse(resp) {
                view.onSortSuccess(isTop)
            }
        }

    }

}

