package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.me.bean.ShareVo


interface GoodsBatchPre : BasePresenter {

    fun clickGroup(callback: (ShopGroupVO?, String?) -> Unit);

    fun clickCategory(callback: (CategoryVO?, String?) -> Unit);

    fun clickOnLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOffLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnBatchRebate(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnRebate(string: String?, callback: () -> Unit);

    fun clickDelete(list: List<GoodsVO>?, callback: (ids: String) -> Unit);

    fun getCheckedCount(list: List<GoodsVO>?) : Int

    fun clickShare(id : GoodsVO?, callback: (ShareVo) -> Unit)

    interface View : BaseView {
//        fun onUpdateGroup(groupId: String?, groupName: String?)
//        fun onUpdateCategory(categoryId: String?, categoryName: String?)
        fun onLineSuccess();
        fun offLineSuccess();
        fun onBatchRebateSuccess();
        fun onBatchDeleted(ids : String);
    }
}