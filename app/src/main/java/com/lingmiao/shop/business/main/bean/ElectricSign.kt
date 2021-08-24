package com.lingmiao.shop.business.main.bean


import com.google.gson.annotations.SerializedName

data class ElectricSign(
    @SerializedName("appid")
    var appid: String? = "",
    @SerializedName("cusid")
    var cusid: String? = "",
    @SerializedName("mchid")
    var mchid: String? = "",
    @SerializedName("randomstr")
    var randomstr: String? = "",
    @SerializedName("retcode")
    var retcode: String? = "",
    @SerializedName("retmsg")
    var retmsg: String? = "",
    @SerializedName("sign")
    var sign: String? = "",
    @SerializedName("sybsignurl")
    var sybsignurl: String? = "",
    @SerializedName("trxstatus")
    var trxstatus: String? = ""
)