package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName


data class ShopStatus(
    @SerializedName("shop_staus")
    var shopStatus: String?,
    @SerializedName("status_reason")
    var statusReason: String?,
    @SerializedName("open_status")
    var openStatus: Int?,
    // 返回店铺配置的模板ID, 0表示没有配置模板
    @SerializedName("template_id")
    var templateId: Int? = 0,
    // 店铺是否配置分类, true表示已经配置了分类
    @SerializedName("have_category")
    var haveCategory: Boolean? = false,
    // 是否显示提现
    @SerializedName("show_button")
    var showButton: Int? = 0,
    // 审核失败原因
    @SerializedName("remark")
    var remark: String = ""

)
