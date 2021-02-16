package com.lingmiao.shop.business.tuan.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface GoodsSelectPresenter : BasePresenter {

    fun loadListData(page: IPage, list: List<*>, goodsName: String?)

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {

    }
}