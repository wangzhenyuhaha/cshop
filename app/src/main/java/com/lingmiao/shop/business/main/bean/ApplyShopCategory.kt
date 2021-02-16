
package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName
data class ApplyShopCategory(
    @SerializedName("category_id")
    var categoryId: Int?,
    @SerializedName("category_order")
    var categoryOrder: Int?,
    @SerializedName("category_path")
    var categoryPath: String?,
    @SerializedName("goods_count")
    var goodsCount: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("name")
    var name: String?,//分类名称
    var selected: Boolean = false,
    @SerializedName("parent_id")
    var parentId: Int?
)