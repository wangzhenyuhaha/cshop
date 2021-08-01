package com.lingmiao.shop.business.goods.api.request

import com.google.gson.annotations.SerializedName

class PriceAndQuantity {


    @SerializedName("skuQuantityList")
    var quantityList: MutableList<QuantityPrice>? = null

    //活动
    @SerializedName("skuEventQuantityList")
    var eventQuantityList: MutableList<QuantityPrice>? = null


    class QuantityPrice(
        var sku_id: String? = null,
        var price: String? = null,
        var quantity_count: String? = null
    )

}