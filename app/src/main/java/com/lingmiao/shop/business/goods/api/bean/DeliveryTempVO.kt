package com.lingmiao.shop.business.goods.api.bean

import java.io.Serializable
import com.google.gson.annotations.SerializedName


/**
 * Author : Elson
 * Date   : 2020/8/7
 * Desc   : 配送模板 (有部分字段没有放入数据模型)
 */
class DeliveryTempVO : Serializable {

    @SerializedName("base_ship_price")
    var baseShipPrice: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("local_template")
    var localTemplate: String? = null
    @SerializedName("min_ship_price")
    var minShipPrice: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("seller_id")
    var sellerId: Int? = null
    @SerializedName("ship_range")
    var shipRange: Int = 0
    @SerializedName("template_type")
    var templateType: String? = null
    @SerializedName("time_setting")
    var timeSetting: String? = null

    // ----------- 业务字段 -----------
    var isChecked: Boolean = false
}