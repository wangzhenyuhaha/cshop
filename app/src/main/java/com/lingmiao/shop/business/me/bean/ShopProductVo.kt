package com.lingmiao.shop.business.me.bean
import com.google.gson.annotations.SerializedName


/**
Create Date : 2021/5/2010:54 AM
Auther      : Fox
Desc        :
 **/
data class ShopProductVo(
    @SerializedName("depositMoney")
    var depositMoney: Double? = 0.0,
    @SerializedName("shopProductList")
    var shopProductList: List<VipType>? = listOf()
)
