package com.lingmiao.shop.business.goods.presenter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   :
 */
interface SpecKeyPre: BasePresenter {

    fun loadGoodsSpecKey(categoryId: String?, specList: List<SpecKeyVO>?)

    fun submitSpecKey(categoryId: String?, specName: String?)

    interface SpceKeyView : BaseView {
        fun onLoadSuccess(list: List<MultiItemEntity>)
        fun onLoadFailed()

        fun onAddSpecSuccess(spceVo: SpecKeyVO)
    }
}