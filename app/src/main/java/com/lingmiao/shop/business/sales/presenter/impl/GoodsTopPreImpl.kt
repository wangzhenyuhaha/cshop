package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.sales.bean.GoodsItem
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.GoodsTopPresenter
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsTopPreImpl(var context: Context, var view: GoodsTopPresenter.PubView) : BasePreImpl(view),GoodsTopPresenter {

    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
//            if (list.isEmpty()) {
//                view.showPageLoading()
//            }

//            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), goodsName ?:"", "");
//            if (resp.isSuccess) {
//                val userList = resp.data.data
//                if (page.isRefreshing() && userList.isNullOrEmpty()) {
//                    view.showNoData()
//                } else {
//                    view.hidePageLoading()
//                }
//                view.onLoadMoreSuccess(userList, userList.isNotEmpty())
//            } else {
//                view.onLoadMoreFailed()
//                view.hidePageLoading()
//            }
//            val userList = getItems();
//            view.onLoadMoreSuccess(userList, userList.isNotEmpty())
//            view.hidePageLoading()
        }
    }


    fun getItems() : MutableList<GoodsItem> {
        val list : MutableList<GoodsItem> = mutableListOf();

        list.add(GoodsItem())
        list.add(GoodsItem())
        list.add(GoodsItem())
        list.add(GoodsItem())
        list.add(GoodsItem())
        return list;
    }


}