package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsTabNumberPre
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class GoodsTabNumberPreImpl(val view: GoodsTabNumberPre.View) : BasePreImpl(view), GoodsTabNumberPre {

    override fun loadNumbers() {
        mCoroutine.launch {
            val resp = GoodsRepository.getDashboardData();
            if(resp?.isSuccess) {
                view?.loadNumberSuccess(resp?.data);
            }
        }
    }


}