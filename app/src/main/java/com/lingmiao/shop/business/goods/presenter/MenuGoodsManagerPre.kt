package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

interface MenuGoodsManagerPre : BasePresenter {

    //加载一级菜单
    fun loadLv1GoodsGroup(isTop: Int,isSecond:Boolean )

    //排序
    fun sort(isTop: Int, cid: String, sort: Int)

    //删除一级菜单
    fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int)

    interface View : BaseView {

        //加载一级菜单成功
        fun onLoadLv1GoodsGroupSuccess(list: List<ShopGroupVO>, isTop: Int,isSecond:Boolean)

        //排序成功
        fun onSortSuccess(isTop: Int)

        //删除一级菜单成功
        fun onDeleteGroupSuccess(position: Int)
    }
}