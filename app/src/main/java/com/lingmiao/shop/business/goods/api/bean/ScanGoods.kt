package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

data class ScanGoods(
    @SerializedName("centerGoodsSkuDO")
    var centerGoodsSkuDO: CenterGoodsSkuDO?,
    @SerializedName("goodsSkuDOList")
    var goodsSkuDOList: List<GoodsSkuDO>?

)
