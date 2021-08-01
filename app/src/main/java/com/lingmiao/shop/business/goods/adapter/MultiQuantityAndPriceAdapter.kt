package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.business.tools.adapter.addTextChangeListener

/**
 * Author : Elson
 * Date   : 2020/8/16
 * Desc   : 多规格库存设置
 */
class MultiQuantityAndPriceAdapter :
    BaseQuickAdapter<QuantityPriceRequest, BaseViewHolder>(R.layout.goods_adapter_multi_quantity_price) {

    override fun convert(helper: BaseViewHolder, quantityVO: QuantityPriceRequest?) {
        if (quantityVO == null) return

        helper.setText(R.id.quantityNameTv, quantityVO.quantitiyName)
        helper.setGone(R.id.quantityNameTv, quantityVO?.quantitiyName?.length?:0 > 0)
        helper.setText(R.id.priceEdt, quantityVO.eventPrice)
        helper.setText(R.id.quantityEdt, quantityVO.eventQuantity)
        helper.setText(R.id.event_priceEdt, quantityVO.eventPrice)
        helper.setText(R.id.event_quantityEdt, quantityVO.eventQuantity)
        addTextChangeListener(helper.getView(R.id.quantityEdt), quantityVO.quantity) {
            quantityVO.quantity = it
        }
        addTextChangeListener(helper.getView(R.id.priceEdt), quantityVO.price) {
            quantityVO.price = it
        }
        addTextChangeListener(helper.getView(R.id.event_quantityEdt), quantityVO.eventQuantity) {
            quantityVO.eventQuantity = it
        }
        addTextChangeListener(helper.getView(R.id.event_priceEdt), quantityVO.eventPrice) {
            quantityVO.eventPrice = it
        }
    }

    fun getUpdateQuantityList(): List<QuantityPriceRequest> {
        return data.filter { it.quantity.isNotBlank() }
    }

}