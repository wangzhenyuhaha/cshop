package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName


/**
 * Author : liuqi
 * Date   : 2020/8/24
 * Desc   : 用于拉取商品绑定的sku
 */
data class GoodsSkuCacheVO(
    @SerializedName("sku_list")
    var skuList: List<GoodsSkuVOWrapper>?,
    @SerializedName("spec_info")
    var specInfo: List<SpecKeyVO>?
)
