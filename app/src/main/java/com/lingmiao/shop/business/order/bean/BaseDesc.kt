package com.lingmiao.shop.business.order.bean

import com.google.gson.annotations.SerializedName

/**
Create Date : 2020/12/308:37 PM
Auther      : Fox
Desc        :
 **/
data class BaseDesc (
    @SerializedName("status")
    var status: Boolean? = false,
    @SerializedName("desc")
    var desc: String? = ""
){

}