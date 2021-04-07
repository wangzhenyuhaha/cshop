package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.sales.bean.OrderItemVo
import com.lingmiao.shop.business.sales.bean.UserOrderVo
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserOrderPresenter
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class UserOrderPreImpl(var context: Context, var view: IUserOrderPresenter.PubView) : BasePreImpl(view),IUserOrderPresenter {

    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }

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
            val userList = getItems();
            view.onLoadMoreSuccess(userList, userList.isNotEmpty())
            view.hidePageLoading()
        }
    }


    fun getItems() : MutableList<UserOrderVo> {
        val list : MutableList<UserOrderVo> = mutableListOf();
        list.add(UserOrderVo())
        return list;
    }

}