package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

/**
Create Date : 2021/5/188:36 AM
Auther      : Fox
Desc        :
 **/
class WxPayDataVo {
    @SerializedName("appid")
    var appid: String? = ""
    @SerializedName("noncestr")
    var noncestr: String? = ""
    @SerializedName("package")
    var packageX: String? = ""
    @SerializedName("partnerid")
    var partnerId: String? = ""
    @SerializedName("prepayid")
    var prepayId: String? = ""
    @SerializedName("sign")
    var sign: String? = ""
    @SerializedName("timestamp")
    var timestamp: String? = ""
}