package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.DeliveryTempVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 配送模板
 */
interface DeliveryTempPre: BasePresenter {

    fun loadTempList(type: String?)

    interface TempView: BaseView, BaseLoadMoreView<DeliveryTempVO> {

    }
}