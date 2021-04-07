package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsDetailPre
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesSettingPresenter
import com.lingmiao.shop.business.sales.presenter.IStatsPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class SalesSettingPreImpl(var context: Context, var view: ISalesSettingPresenter.PubView) : BasePreImpl(view),ISalesSettingPresenter {
    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }

//            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), goodsName ?:"", "");
//            if (resp.isSuccess) {
//                val salesList = resp.data.data
//                if (page.isRefreshing() && salesList.isNullOrEmpty()) {
//                    view.showNoData()
//                } else {
//                    view.hidePageLoading()
//                }
//                view.onLoadMoreSuccess(salesList, salesList.isNotEmpty())
//            } else {
//                view.onLoadMoreFailed()
//                view.hidePageLoading()
//            }
            val salesList = getItems();
            view.onLoadMoreSuccess(salesList, salesList.isNotEmpty())
            view.hidePageLoading()
        }
    }

    fun getItems() : MutableList<SalesVo> {
        val list : MutableList<SalesVo> = mutableListOf();
        list.add(SalesVo())
        return list;
    }

}