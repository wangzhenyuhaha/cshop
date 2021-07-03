package com.lingmiao.shop.business.goods.api.request

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
    var eventPrice: String? = null //活动价格

    @SerializedName("quantity_count")
    var eventQuantity: String? = null //活动库存

    // 业务字段
    var quantitiyName: String? = null


    companion object {
        fun convert(skuVO: GoodsSkuVOWrapper): QuantityPriceRequest {
            skuVO.addSpecList(skuVO.specList)
            return QuantityPriceRequest().apply {
                quantitiyName = skuVO.skuNameDesc
                eventPrice = skuVO.eventPrice
                eventQuantity = skuVO.eventQuantity
                skuId = skuVO.skuId
            }
        }
    }
}