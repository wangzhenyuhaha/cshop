package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.*

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
interface GoodsSpecPre: BasePresenter {

    fun add(id: String, key: String, value: String)

    fun addSpecValue(specKeyId: String, valueNames: String)

    fun loadList(id: String?)

    fun delete(id : String);

    fun deleteValue(position : Int , id : String);

    fun showAddPop(cId: String);

    interface PublicView : BaseView {

        fun onLoaded(list: List<SpecKeyVO>)

        fun onAdded(vo: GoodsSpecVo)

        fun onDeleted(id : String);

        fun onDeletedValue(position : Int , id : String);

        fun onAddSpecValueSuccess(specKeyId: String, data: List<SpecValueVO>?)
    }
}