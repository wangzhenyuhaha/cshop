package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName


/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
data class CategoryVO(

    @SerializedName("category_id")
    var categoryId: String?,
    @SerializedName("category_order")
    var categoryOrder: Int?,
    @SerializedName("category_path")
    var categoryPath: String?,
    @SerializedName("goods_count")
    var goodsCount: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("parent_id")
    var parentId: String?
)