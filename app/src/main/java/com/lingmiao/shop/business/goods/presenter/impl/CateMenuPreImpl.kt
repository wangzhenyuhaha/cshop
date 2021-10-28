package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.pop.CateMenuPop

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品菜单管理
 */
class CateMenuPreImpl(var context: Context, var view: BaseView,var type:Int = 0) : BasePreImpl(view) {

    private var mCateMenuPop: CateMenuPop? = null

    fun showMenuPop(menuTypes: Int, view: View, listener: ((Int) -> Unit)?) {
        mCateMenuPop = CateMenuPop(context, menuTypes,type)
        mCateMenuPop!!.setOnClickListener(listener)
        mCateMenuPop?.showPopupWindow(view)
    }

}