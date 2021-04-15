package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import java.io.Serializable

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
interface GoodsSalesSelectPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>)

    interface StatusView : BaseView, BaseLoadMoreView<GoodsVO> {

    }

}