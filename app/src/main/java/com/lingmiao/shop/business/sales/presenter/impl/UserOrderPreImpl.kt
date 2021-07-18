package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.api.MemberRepository
import com.lingmiao.shop.business.order.api.OrderRepository
import com.lingmiao.shop.business.sales.presenter.IUserOrderPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class UserOrderPreImpl(var context: Context, var view: IUserOrderPresenter.PubView) : BasePreImpl(view),IUserOrderPresenter {

    override fun loadListData(page: IPage, memberId : String, list: List<*>) {
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showDialogLoading()
            }
            val resp = OrderRepository.apiService.memberOrderList(page.getPageIndex(), IConstant.PAGE_SIZE, memberId).awaitHiResponse();
            if (resp.isSuccess) {
                val userList = resp.data.data
                view.onLoadMoreSuccess(userList, userList?.isNotEmpty()?:false)
            } else {
                view.onLoadMoreFailed()
            }
            view.hideDialogLoading()
        }
    }

    override fun getOrderCount(memberId: String) {
        mCoroutine.launch {
            val resp = MemberRepository.apiService.getBuyCountOfMember(memberId).awaitHiResponse();
            handleResponse(resp) {
                view.onSetOrderCount(it);
            }
        }
    }

}