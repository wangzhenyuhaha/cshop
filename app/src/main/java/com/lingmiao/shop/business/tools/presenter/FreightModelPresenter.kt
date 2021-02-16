package com.lingmiao.shop.business.tools.presenter

import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage

interface FreightModelPresenter : BasePresenter {

    fun loadList(page: IPage, list: MutableList<FreightVoItem>);

    fun onDeleteItem(item: FreightVoItem?, position: Int);

    interface View : BaseView, BaseLoadMoreView<FreightVoItem> {

        fun onModelDeleted(position: Int)
    }
}