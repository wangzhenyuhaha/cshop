package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.sales.presenter.IStateGoodsDataPresenter
import com.lingmiao.shop.business.sales.presenter.IStateSalesDataPresenter
import com.lingmiao.shop.business.sales.presenter.IStateUserDataPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class StatsGoodsDataPreImpl(var context: Context, var view: IStateGoodsDataPresenter.PubView) :
    BasePreImpl(view), IStateGoodsDataPresenter {
    override fun getGoodsSales(start: Long?, end: Long?) {
        mCoroutine.launch {
            val resp = GoodsRepository.goodsSales(start?:0, end?:0);
            view?.setGoodsSales(resp.data);
        }
    }

}