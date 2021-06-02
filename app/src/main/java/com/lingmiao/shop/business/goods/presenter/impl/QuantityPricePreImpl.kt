package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.business.goods.pop.GoodsQuantityPricePop
import kotlinx.coroutines.launch

/**
Create Date : 2021/6/111:43 AM
Auther      : Fox
Desc        :
 **/
class QuantityPricePreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    fun clickQuantityGoods(goodsId: String?, callback: (String) -> Unit) {
        exeGoodsSku(goodsId) {
            if (it.size == 1) {
                showQuantityPricePop(goodsId!!, arrayListOf(it[0]), callback)
            } else {
                showQuantityPricePop(goodsId!!, it, callback)
            }
        }
    }

    /**
     * 修改多规格库存
     */
    private fun showQuantityPricePop(goodsId: String, skuList: List<GoodsSkuVOWrapper>, callback: (String) -> Unit) {
        val multiQuantityPop = GoodsQuantityPricePop(context)
        multiQuantityPop.setConfirmListener {
            if (it.isNullOrEmpty()) {
                view.showToast("请输入活动价格及库存数量")
                return@setConfirmListener
            }
            exeQuantityPriceRequest(goodsId, it) {
                multiQuantityPop.dismiss()
                callback.invoke(it)
            }
        }
        multiQuantityPop.apply {
            setSkuList(skuList)
            showPopupWindow()
        }
    }

    /**
     * 更新活动库存
     */
    private fun exeQuantityPriceRequest(goodsId: String, skuList: List<QuantityPriceRequest>, callback: (String) -> Unit) {
        mCoroutine.launch {
//            val resp = GoodsRepository.updateGoodsQuantity(goodsId, skuList)
//            handleResponse(resp) {
//                callback.invoke(it.quantity.check("0"))
//            }
        }
    }


    private fun exeGoodsSku(goodsId: String?, callback: (List<GoodsSkuVOWrapper>) -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val skuResp = GoodsRepository.loadGoodsSKU(goodsId)
            if (!skuResp.isSuccess || skuResp.data.isNullOrEmpty()) {
                handleErrorMsg(skuResp.msg)
                return@launch
            }
            callback.invoke(skuResp.data)
        }
    }


}