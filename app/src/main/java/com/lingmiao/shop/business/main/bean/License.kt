package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName

data class License(

    //名称
    @SerializedName("name")
    var name: String? = null,

    //统一社会信用代码
    @SerializedName("reg_num")
    var regNum: String? = null,

    //营业期限
    @SerializedName("period")
    var period: String? = null,

    //经营范围
    @SerializedName("business")
    var business: String? = null,



    //确认格式后进行默认选择
    //注册资本
    @SerializedName("capital")
    var capital: String? = null,


    //法定代表人
    @SerializedName("person")
    var person: String? = null,

    //???
    @SerializedName("composing_form")
    var composingForm: String? = null,

    //成立日期
    @SerializedName("set_date")
    var setDate: String? = null,

    //住所
    @SerializedName("address")
    var address: String? = null,

    //类型
    @SerializedName("type")
    var type: String? = null

)
