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
    // 签约的url
    @SerializedName("sybsignurl")
    var sybsignurl: String? = "",
    @SerializedName("trxstatus")
    var trxstatus: String? = "",
    //签约状态，0：成功 1：失败 2：签约中
    @SerializedName("electsignstatus")
    var electSignStatus: String? = ""
)