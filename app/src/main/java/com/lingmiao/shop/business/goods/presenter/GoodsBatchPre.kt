package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.ShareVo


interface GoodsBatchPre : BasePresenter {

    fun clickOnLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOffLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnBatchRebate(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnRebate(string: String?, callback: () -> Unit);

    fun clickDelete(list: List<GoodsVO>?, callback: (ids: String) -> Unit);

    fun getCheckedCount(list: List<GoodsVO>?) : Int

    fun clickShare(id : GoodsVO?, callback: (ShareVo) -> Unit)

    interface View : BaseView {
        fun onLineSuccess();
        fun offLineSuccess();
        fun onBatchRebateSuccess();
        fun onBatchDeleted(ids : String);
    }
}