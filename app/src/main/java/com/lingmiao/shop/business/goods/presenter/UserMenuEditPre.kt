package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品 - 添加/编辑分组
 */
interface UserMenuEditPre : BasePresenter {

    fun getShopId() : String

    fun getShopGroup(id : String);

    fun getShopGroupAndChildren(id : String);

    fun addGroup(str : String, pid: String)

    fun deleteGoodsGroup(shopCatId: ShopGroupVO?, position: Int)

    fun submit(groupName: ShopGroupVO)

    fun modifyGroup(groupVO: ShopGroupVO)

    fun clickMenuView(item: ShopGroupVO?, position: Int, target: View);

    interface GroupEditView : BaseView {
        fun onSetGroup(item : ShopGroupVO);
        fun finish()
        fun onDeleteGroupSuccess(position: Int);
        fun onDeleteFailed();
        fun onGroupAdded(item : ShopGroupVO);
        fun onUpdated(item : ShopGroupVO, position: Int);
        fun onUpdateFail();
    }
}