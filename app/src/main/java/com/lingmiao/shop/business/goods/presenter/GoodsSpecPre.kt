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

    fun add(id: String, value: String)

    fun loadList(id: String?)

    fun delete(id : String);

    interface PublicView : BaseView {

        fun onLoaded(list: List<GoodsSpecVo>)

        fun onAdded(vo: GoodsSpecVo)

        fun onDeleted(id : String);
    }
}