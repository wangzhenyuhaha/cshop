package com.lingmiao.shop.business.order.bean
import com.google.gson.annotations.SerializedName

data class LogisticsCompanyItem(
    @SerializedName("logistics_company_do")
    var logisticsCompanyDo: LogisticsCompanyDo?,
    @SerializedName("open")
    var `open`: Boolean?
//    @SerializedName("text_fields")
//    var textFields: List<Any>?
)

data class LogisticsCompanyDo(
    @SerializedName("code")
    var code: String?,
//    @SerializedName("form")
//    var form: List<Any>?,
//    @SerializedName("form_items")
//    var formItems: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_waybill")
    var isWaybill: Int?,
    @SerializedName("kdcode")
    var kdcode: String?,
    @SerializedName("name")
    var name: String?
)