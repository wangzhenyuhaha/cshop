package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView


interface GoodsBatchPre : BasePresenter {

    fun clickOnLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOffLine(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnBatchRebate(list: List<GoodsVO>, callback: () -> Unit);

    fun clickOnRebate(string: String?, callback: () -> Unit);

    interface View : BaseView {
        fun onLineSuccess();
        fun offLineSuccess();
        fun onBatchRebateSuccess();
    }
}