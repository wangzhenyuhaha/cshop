package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Author : liuqi
 * Date   : 2020/8/8
 * Desc   : 规格值
 */
class SpecValueVO : Serializable {

    @SerializedName("seller_id")
    var sellerId: String? = null

    @SerializedName("spec_id")
    var specId: String? = null

    @SerializedName("spec_image")
    var specImage: String? = null

    @SerializedName("spec_name")
    var specName: String? = null

    @SerializedName("spec_type")
    var specType: String? = null

    @SerializedName("spec_value")
    var specValue: String? = null

    @SerializedName("spec_value_id")
    var specValueId: String? = null
}