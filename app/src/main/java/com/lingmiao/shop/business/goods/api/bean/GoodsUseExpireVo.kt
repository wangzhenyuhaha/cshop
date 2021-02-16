package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/1/101:01 PM
Auther      : Fox
Desc        :
 **/
data class GoodsUseExpireVo(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("label")
    var label: String? = "",
    @SerializedName("type")
    var type: String? = "",
    @SerializedName("update_time")
    var updateTime: String? = "",
    @SerializedName("value")
    var value: String? = "",
    var checked : Boolean ?= false
)