package com.lingmiao.shop.business.tuan.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.tuan.presenter.GoodsSelectPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import kotlinx.coroutines.launch

class GoodsSelectPresenterImpl(val view : GoodsSelectPresenter.View) : BasePreImpl(view) ,
    GoodsSelectPresenter {
    override fun loadListData(page: IPage, list: List<*>, goodsName: String?) {
//        if (goodsName.isNullOrBlank()) {
//            view.showToast("请输入搜索文字")
//            return
//        }

        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }

            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), goodsName ?:"", "");
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                if (page.isRefreshing() && goodsList.isNullOrEmpty()) {
                    view.showNoData()
                } else {
                    view.hidePageLoading()
                }
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
                view.hidePageLoading()
            }

        }
    }

}