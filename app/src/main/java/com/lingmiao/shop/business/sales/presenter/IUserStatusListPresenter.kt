package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.UserVo

/**
Create Date : 2021/3/101:10 AM
Auther      : Fox
Desc        :
 **/
interface IUserStatusListPresenter : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    interface PubView : BaseView, BaseLoadMoreView<UserVo>  {

    }
}