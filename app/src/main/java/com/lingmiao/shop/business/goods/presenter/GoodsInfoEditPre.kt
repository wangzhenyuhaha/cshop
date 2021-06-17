package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.*

/**
 * Desc   : 商品信息编辑
 */
interface GoodsInfoEditPre: BasePresenter {

    fun addInfo(id: String, value: String?)

    fun updateInfo(item : GoodsParamVo)

    fun itemClick(id: String?);

    fun loadCateList(id: String?)

    interface PublicView : BaseView {

        fun onAddInfo(vo: GoodsParamVo)

        fun onUpdated(vo : GoodsParamVo)

        fun onSetCategories(list: List<CategoryVO>?)
    }
}