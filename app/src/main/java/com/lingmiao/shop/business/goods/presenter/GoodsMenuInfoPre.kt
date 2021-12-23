package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO

interface GoodsMenuInfoPre : BasePresenter {

    fun loadListData(path : String?, page: IPage, list: List<*>)

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {

    }
}