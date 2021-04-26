package com.lingmiao.shop.business.me.bean

import com.google.gson.annotations.SerializedName


data class ShopManageRequest(
    @SerializedName("link_phone")
    var linkPhone: String? = null,
    @SerializedName("link_name")
    var linkName: String? = null,
//    @SerializedName("open_end_time")
//    var openEndTime: String?,
//    @SerializedName("open_start_time")
//    var openStartTime: String?,
//    @SerializedName("open_time_type")
//    var openTimeType: Int?,
//    @SerializedName("ship_type")
//    var shipType: String?,
    @SerializedName("shop_add")
    var shopAdd: String? = null,
//    @SerializedName("shop_banner")
//    var shopBanner: String?,
    @SerializedName("shop_desc")
    var shopDesc: String? = null,
    @SerializedName("shop_id")
    var shopId: Int? = null,
    @SerializedName("shop_lat")
    var shopLat: Double? = null,
    @SerializedName("shop_lng")
    var shopLng: Double? = null,
    @SerializedName("shop_logo")
    var shopLogo: String? = null,
    @SerializedName("shop_name")
    var shopName: String? = null,
    @SerializedName("licence_img")
    var licenceImg: String? = null,//店铺资质
    @SerializedName("shop_qq")
    var shopQq: String? = null//店铺客服qq


)