package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface CategoryEditPre : BasePresenter {

    fun loadLv1GoodsGroup()

    fun loadLv2GoodsGroup(lv1GroupId: String?)

    fun clickMenuView(item: CategoryVO?, position: Int, view: View,type:Int)

    fun showAddDialog(pId : Int);

    fun showEditDialog(item: CategoryVO);

    fun add(pId : Int, name : String);

    fun update(item: CategoryVO);

    fun delete(id : String);

    interface PubView: BaseView, BaseLoadMoreView<CategoryVO> {
        fun onAdded(pId : Int, vo : CategoryVO);
        fun onUpdated(vo : CategoryVO);
        fun onLoadedLv2(
            key: String,
            list: List<CategoryVO>?
        );
        fun onDeleted(id : String);
    }
}