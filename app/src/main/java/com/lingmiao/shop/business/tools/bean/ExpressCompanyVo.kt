package com.lingmiao.shop.business.tools.bean
import com.google.gson.annotations.SerializedName


data class ExpressCompanyVo(
    @SerializedName("logistics_company_do")
    var logisticsCompanyDo: LogisticsCompanyDo? = LogisticsCompanyDo(),
    @SerializedName("open")
    var `open`: Boolean? = false,
    @SerializedName("text_fields")
    var textFields: List<TextField>? = listOf()
)

data class LogisticsCompanyDo(
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("form")
    var form: List<Form>? = listOf(),
    @SerializedName("form_items")
    var formItems: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("is_waybill")
    var isWaybill: Int? = 0,
    @SerializedName("kdcode")
    var kdcode: String? = "",
    @SerializedName("name")
    var name: String? = ""
)

data class TextField(
    @SerializedName("label")
    var label: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("value")
    var value: Any? = Any()
)

data class Form(
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("name")
    var name: String? = ""
)