package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName


data class CenterGoodsSkuDO(
    @SerializedName("bar_code")
    var `bar_code`: String? = null,
    @SerializedName("category_id")
    var `category_id`: Int? = null,
    @SerializedName("cost")
    var `cost`: Any? = null,
    @SerializedName("enable_quantity")
    var `enable_quantity`: Int? = null,
    @SerializedName("goods_id")
    var `goods_id`: Int? = null,
    @SerializedName("goods_name")
    var goodsName: String? = null,
    @SerializedName("hash_code")
    var `hash_code`: Int? = null,
    @SerializedName("local_template_id")
    var `local_template_id`: Any? = null,
    @SerializedName("mktprice")
    var `mktprice`: Any? = null,
    @SerializedName("price")
    var `price`: Int? = null,
    @SerializedName("quantity")
    var `quantity`: Int? = null,
    @SerializedName("sku_id")
    var `sku_id`: Int? = null,
    @SerializedName("sn")
    var `sn`: String? = null,
    @SerializedName("template_id")
    var `template_id`: Any? = null,
    @SerializedName("thumbnail")
    var `thumbnail`: String? = null,
    @SerializedName("up_sku_id")
    var `up_sku_id`: Any? = null,
    @SerializedName("weight")
    var `weight`: Any? = null
)