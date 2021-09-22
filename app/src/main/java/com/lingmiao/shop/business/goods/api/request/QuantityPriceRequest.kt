package com.lingmiao.shop.business.goods.api.request

import android.util.Log
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.google.gson.annotations.SerializedName

/**
 * @author elson
 * @date 2020/8/6
 * @Desc 修改库存
 */
class QuantityPriceRequest {

    @SerializedName("sku_id")
    var skuId: String? = null

    @SerializedName("price")
    var price: String? = null //价格

    @SerializedName("quantity_count")
    var quantity: String? = null //库存

    @SerializedName("event_price")
    var eventPrice: String? = null //活动价格

    @SerializedName("event_quantity")
    var eventQuantity: String? = null //活动库存

    // 业务字段
    var quantitiyName: String? = null


    companion object {
        fun convert(skuVO: GoodsSkuVOWrapper): QuantityPriceRequest {
            //处理规格值
            skuVO.addSpecList(skuVO.specList)
            return QuantityPriceRequest().apply {
                quantitiyName = skuVO.skuNameDesc
                price = skuVO.price
                quantity = skuVO.quantity
                eventPrice = skuVO.eventPrice
                eventQuantity = skuVO.eventQuantity
                skuId = skuVO.skuId
            }
        }
    }
}