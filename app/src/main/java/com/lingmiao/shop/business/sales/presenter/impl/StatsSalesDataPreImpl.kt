package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.sales.presenter.IStateSalesDataPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class StatsSalesDataPreImpl(var context: Context, var view: IStateSalesDataPresenter.PubView) :
    BasePreImpl(view), IStateSalesDataPresenter {

    override fun getSalesCount(type: String, start: Long?, end: Long?) {
        mCoroutine.launch {
            val resp = GoodsRepository.salesCount(type, start?:0, end?:0);
            view?.setSalesCount(resp.data);
        }
    }

}