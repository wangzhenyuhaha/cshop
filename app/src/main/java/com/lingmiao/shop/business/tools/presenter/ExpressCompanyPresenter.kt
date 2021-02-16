package com.lingmiao.shop.business.tools.presenter

import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface ExpressCompanyPresenter : BasePresenter {

    fun loadList(page: IPage, list: List<ExpressCompanyVo>);

    fun clickItemView(item: ExpressCompanyVo?, position: Int)

    interface View : BaseView, BaseLoadMoreView<ExpressCompanyVo> {

        fun onStatusEdit(flag: Boolean?, position: Int)

    }
}