package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class IDCard(

    // 姓名（人像面）
    @SerializedName("name")
    var Name: String? = null,

    // 性别（人像面）
    @SerializedName("sex")
    var Sex: String? = null,

    // 民族（人像面）
    @SerializedName("nation")
    var Nation: String? = null,

    // 出生日期（人像面）
    @SerializedName("birth")
    var Birth: String? = null,

    // 地址（人像面）
    @SerializedName("address")
    var Address: String? = null,

    // 身份证号（人像面）
    @SerializedName("id_num")
    var IdNum: String? = null,

    // 发证机关（国徽面）
    @SerializedName("authority")
    var Authority: String? = null,

    // 证件有效期（国徽面）
    @SerializedName("valid_date")
    var ValidDate: String? = null,

    // 扩展信息
    @SerializedName("advanced_info")
    var AdvancedInfo: String? = null


)
