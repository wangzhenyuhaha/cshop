package com.lingmiao.shop.business.sales.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.bean.SalesVo

/**
Create Date : 2021/6/411:12 AM
Auther      : Fox
Desc        :
 **/
interface ISalesGoodPresenter : BasePresenter{

    fun showCategoryPop(cid : String?,target: android.view.View)

    fun loadListData(cid : String?, page: IPage, list: List<*>)

    fun onLoadGoodsSetting();

    fun getSalesGoods(id : String);

    fun updateSalesGoods(item : SalesVo);

    interface View : BaseView, BaseLoadMoreView<GoodsVO> {

        fun onUpdatedCategory(list : List<CategoryVO>?, name : String?)

        fun onSetSalesGoods(item : SalesVo);

        fun onUpdatedGoods(item : SalesVo);
    }
}