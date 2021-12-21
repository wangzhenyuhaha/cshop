package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

interface MenuGoodsManagerPre : BasePresenter {

    //加载一级菜单
    fun loadLv1GoodsGroup(isTop: Int)

    //排序
    fun sort(isTop : Int, cid: String, sort : Int)

    interface View : BaseView {

        //加载一级菜单成功
        fun onLoadLv1GoodsGroupSuccess(list: List<ShopGroupVO>, isTop: Int)

        //排序成功
        fun onSortSuccess()

    }
}