package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.*

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
interface GoodsInfoPre: BasePresenter {

    fun addInfo(id: String, value: String)

    fun loadInfoList(id: String?)

    fun deleteInfo(id : String);

    interface PublicView : BaseView {

        fun onLoadInfoListSuccess(list: List<GoodsParamVo>)

        fun onAddInfo(vo: GoodsParamVo)

        fun onInfoDeleted(id : String);
    }
}