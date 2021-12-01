package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

data class ScanGoods(
    @SerializedName("centerGoodsSkuDO")
    var `centerGoodsSkuDO`: CenterGoodsSkuDO?,
    @SerializedName("goodsSkuDOList")
    var `goodsSkuDOList`: List<GoodsSkuDO>?
    //  @SerializedName("data")
    //    var `data`: List<T>?,
)

//data class PageVO<T>(
//    @SerializedName("data")
//    var `data`: List<T>?,
//    @SerializedName("data_total")
//    var dataTotal: Int = 0,
//    @SerializedName("page_no")
//    var pageNo: Int = 0,
//    @SerializedName("page_size")
//    var pageSize: Int = 0
//)