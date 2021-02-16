package com.lingmiao.shop.business.me.bean

import com.google.gson.annotations.SerializedName

data class ForgetPassword(
    var password:String?=null,
    @SerializedName("sms_code")
    var smsCode:String?=null,
    var mobile:String?=null
){

}