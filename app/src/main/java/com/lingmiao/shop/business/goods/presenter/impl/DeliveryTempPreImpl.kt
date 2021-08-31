package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.DeliveryTempPre
import com.james.common.base.BasePreImpl
import com.james.common.utils.exts.isNotEmpty
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/8/7
 * Desc   : 配送模板
 */
class DeliveryTempPreImpl(var view: DeliveryTempPre.TempView) : BasePreImpl(view), DeliveryTempPre {


    override fun loadTempList(tempType: String?) {
        if (tempType.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadDeliveryTempList(tempType)
            handleResponse(resp) {
                view.onLoadMoreSuccess(resp.data, resp.data.isNotEmpty())
            }
        }
    }
}