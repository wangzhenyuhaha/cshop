package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.sales.presenter.IStateSalesDataPresenter
import com.lingmiao.shop.business.sales.presenter.IStateUserDataPresenter

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class StatsUserDataPreImpl(var context: Context, var view: IStateUserDataPresenter.PubView) :
    BasePreImpl(view), IStateUserDataPresenter {

}