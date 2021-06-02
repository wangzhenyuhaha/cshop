package com.lingmiao.shop.business.goods.api.request

import com.google.gson.annotations.SerializedName

/**
Create Date : 2021/5/306:23 PM
Auther      : Fox
Desc        :
 **/
class RiderSettingVo {
    @SerializedName("is_rider_to_seller")
    var toSeller : Int?= null
    @SerializedName("rider_to_seller_time")
    var toSellerTime : Int?= null
}