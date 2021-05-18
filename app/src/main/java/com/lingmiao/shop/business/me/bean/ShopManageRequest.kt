package com.lingmiao.shop.business.me.bean

import com.google.gson.annotations.SerializedName


data class ShopManageRequest(
    @SerializedName("link_phone")
    var linkPhone: String? = null,
    @SerializedName("link_name")
    var linkName: String? = null,
    // 未支付自动取消订单分钟数
    @SerializedName("unpaid_cancel_orders_time")
    var unpaidCancelOrderTime : Int? = null,
    // 已接单自动取消订单分钟数
    @SerializedName("cancel_order_time")
    var cancelOrderTime : Int? = null,
    // 自动接单
    @SerializedName("auto_accept")
    var autoAccept : Boolean? = null,
    // 客服电话
    @SerializedName("company_phone")
    var companyPhone: String? = null,
    // 营业时间
    @SerializedName("open_end_time")
    var openEndTime: String? = null,
    @SerializedName("open_start_time")
    var openStartTime: String?= null,
    @SerializedName("open_time_type")
    var openTimeType: Int? = null,
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
    @SerializedName("shop_slogan")
    var shopSlogan: String? = null,
    @SerializedName("shop_notice")
    var shopNotice: String? = null,
    @SerializedName("licence_img")
    var licenceImg: String? = null,//店铺资质
    @SerializedName("shop_qq")
    var shopQq: String? = null//店铺客服qq



)