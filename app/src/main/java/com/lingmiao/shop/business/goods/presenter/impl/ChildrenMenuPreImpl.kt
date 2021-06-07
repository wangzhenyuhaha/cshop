package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.pop.ChildrenMenuPop

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品菜单管理
 */
class ChildrenMenuPreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    private var mCateMenuPop: ChildrenMenuPop? = null

    fun showMenuPop(menuTypes: Int, view: View, listener: ((Int) -> Unit)?) {
        mCateMenuPop = ChildrenMenuPop(context, menuTypes)
        mCateMenuPop!!.setOnClickListener(listener)
        mCateMenuPop?.showPopupWindow(view)
    }

}