package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.api.MemberRepository
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.fragment.UserStatusFragment
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class UserListOfNewPreImpl(var context: Context, var view: IUserStatusListPresenter.PubView) : BasePreImpl(view),IUserStatusListPresenter {

    val type : Int = UserStatusFragment.TYPE_NEW;

    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }
            var resp = MemberRepository.apiService.getMemberListOfNew(page.getPageIndex(), IConstant.PAGE_SIZE).awaitHiResponse();
            if (resp?.isSuccess) {
                val userList = resp.data.data
                if (page.isRefreshing() && userList.isNullOrEmpty()) {
                    view.showNoData()
                } else {
                    view.hidePageLoading()
                }
                view.setUserListCount(resp.data.dataTotal)
                view.onLoadMoreSuccess(userList, userList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
                view.hidePageLoading()
            }
//            val userList = getItems();
//            view.onLoadMoreSuccess(userList, userList.isNotEmpty())
//            view.hidePageLoading()
        }
    }


    fun getItems() : MutableList<UserVo> {
        val list : MutableList<UserVo> = mutableListOf();
        list.add(UserVo())
        return list;
    }


}