package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.presenter.IUserManagerPresenter
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class UserStatusListPreImpl(var context: Context, var view: IUserStatusListPresenter.PubView) : BasePreImpl(view),IUserStatusListPresenter {

    override fun loadListData(page: IPage, list: List<*>) {

    }

}