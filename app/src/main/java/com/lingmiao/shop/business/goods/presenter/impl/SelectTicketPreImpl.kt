package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.presenter.SelectTicketPre
import com.lingmiao.shop.business.sales.api.PromotionRepository
import kotlinx.coroutines.launch

class SelectTicketPreImpl(val context: Context, val view: SelectTicketPre.View) :
    BasePreImpl(view), SelectTicketPre {

    override fun loadListData(page: IPage, list: List<*>) {
        mCoroutine.launch {
            val resp = PromotionRepository.searchElectronicVoucher(page.getPageIndex())
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

}