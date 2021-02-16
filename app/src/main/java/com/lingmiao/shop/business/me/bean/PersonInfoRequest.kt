package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


data class PersonInfoRequest(
    var face:String?=null,
    var uname:String?=null,
    var id:String?=null,
    var password:String?=null,
    var oldPassword:String?=null,
    var smsCode:String?=null,
    var mobile:String?=null
)