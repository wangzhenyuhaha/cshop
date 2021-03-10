package com.lingmiao.shop.business.goods.presenter

import android.view.View
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface GoodsManagerPre : BasePresenter {

    fun showCategoryPop(target: android.view.View);

    fun loadListData(page: IPage, list: List<*>, cId: String)

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {
        fun onUpdateCategory(categoryId: String?, categoryName: String?)
    }
}