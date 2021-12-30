package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

interface MenuGoodsManagerPre : BasePresenter {

    fun getShopId(): String

    //加载一级菜单
    //isTop 1 置顶 0 常用   isSecond true 需要加载常用菜单      type
    fun loadLv1GoodsGroup(isTop: Int = 1, isSecond: Boolean = false, type: Int = 1)

    //排序
    fun sort(isTop: Int, cid: String, sort: Int)

    //删除一级菜单 isTop 1 置顶   0 常用
    fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int, isTop: Int)

    //更新二级菜单名称
    fun updateSecondGroup(item: ShopGroupVO, newName: String, position: Int)

    // 添加二级菜单
    fun addGroup(str: String, pid: String)

    //

    interface View : BaseView {

        //加载一级菜单成功
        fun onLoadLv1GoodsGroupSuccess(
            list: List<ShopGroupVO>,
            isTop: Int,
            isSecond: Boolean,
            type: Int
        )

        //排序成功
        fun onSortSuccess(isTop: Int)

        //删除一级菜单成功
        fun onDeleteGroupSuccess(position: Int, isTop: Int)

        //更新二级菜单名称成功
        fun updateSecondGroupSuccess(newName: String, position: Int)

        //增加二级菜单成功
        fun addGroupSuccess()
    }
}