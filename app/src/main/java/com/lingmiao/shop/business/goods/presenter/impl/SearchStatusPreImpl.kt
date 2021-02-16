package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.lingmiao.shop.business.goods.pop.StatusMenuPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView

/**
Create Date : 2021/1/229:35 AM
Auther      : Fox
Desc        :
 **/
class SearchStatusPreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    private var mGoodsMenuPop: StatusMenuPop? = null

    fun showMenuPop(menuTypes: Int, view: View, listener: ((Int) -> Unit)?) {
        mGoodsMenuPop = StatusMenuPop(context, menuTypes)
        mGoodsMenuPop!!.setOnClickListener(listener)
        mGoodsMenuPop?.showPopupWindow(view)
    }

}