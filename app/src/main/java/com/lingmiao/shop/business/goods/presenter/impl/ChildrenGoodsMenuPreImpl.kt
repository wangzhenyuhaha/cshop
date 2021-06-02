package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.pop.CateMenuPop
import com.lingmiao.shop.business.goods.pop.ChildrenGoodsMenuPop
import com.lingmiao.shop.business.goods.pop.ChildrenMenuPop

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品菜单管理
 */
class ChildrenGoodsMenuPreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    private var mCateMenuPop: ChildrenGoodsMenuPop? = null

    fun showMenuPop(menuTypes: Int, view: View, listener: ((Int) -> Unit)?) {
        mCateMenuPop = ChildrenGoodsMenuPop(context, menuTypes)
        mCateMenuPop!!.setOnClickListener(listener)
        mCateMenuPop?.showPopupWindow(view)
    }

}