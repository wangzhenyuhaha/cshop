package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

data class GoodsSkuBarcodeLog(
    @SerializedName("bar_code")
    var bar_code: String? = null,
    @SerializedName("img_url")
    var img_url: String? = null,
    //商品ID
    @SerializedName("goods_id")
    var goods_id: Int? = null
)