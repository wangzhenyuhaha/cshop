package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class UserStatusListPreImpl(var context: Context, var view: IUserStatusListPresenter.PubView) : BasePreImpl(view),IUserStatusListPresenter {

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


    fun getItems() : MutableList<UserVo> {
        val list : MutableList<UserVo> = mutableListOf();
        list.add(UserVo())
        return list;
    }


}