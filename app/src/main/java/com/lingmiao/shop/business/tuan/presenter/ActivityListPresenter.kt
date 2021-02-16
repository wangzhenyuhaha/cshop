package com.lingmiao.shop.business.tuan.presenter

import com.lingmiao.shop.business.tuan.bean.ActivityVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface ActivityListPresenter : BasePresenter{

    fun loadList(
        page: IPage,
        data: MutableList<ActivityVo>);

    interface View : BaseView, BaseLoadMoreView<ActivityVo> {

    }
}