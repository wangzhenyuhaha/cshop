package com.lingmiao.shop.business.tuan.presenter

import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface GoodsSubmitPresenter : BasePresenter {

    interface View : BaseView, BaseLoadMoreView<GoodsVo> {

    }
}