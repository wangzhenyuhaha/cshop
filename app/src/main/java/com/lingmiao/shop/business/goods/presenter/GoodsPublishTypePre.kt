package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface GoodsPublishTypePre : BasePresenter {

    fun loadList();
    interface View : BaseView {
        fun onListSuccess(list : List<CategoryVO>);
        fun onSelfListSuccess(list : List<CategoryVO>);
    }
}