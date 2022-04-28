package com.lingmiao.shop.business.goods.api.request

import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.google.gson.annotations.SerializedName

/**
 * @author elson
 * @date 2020/8/6
 * @Desc 修改库存
 */
class QuantityRequest {

    @SerializedName("sku_id")
    var skuId: String? = null

    @SerializedName("quantity_count")
    var quantityCount: String? = null

    // 业务字段
    var quantitiyName: String? = null


    companion object {
        fun convert(skuVO: GoodsSkuVOWrapper): QuantityRequest {
            skuVO.addSpecList(skuVO.specList)
            return QuantityRequest().apply {
                quantitiyName = skuVO.skuNameDesc
                quantityCount = skuVO.enableQuantity
                skuId = skuVO.skuId
            }
        }
    }
}