package com.lingmiao.shop.business.main.bean
import com.blankj.utilcode.util.StringUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ApplyShopInfo(
    //店铺经营类目    ids
    @SerializedName("goods_management_category")
    var goodsManagementCategory: String?=null,
    //店铺经营类目 返回文本
    @SerializedName("category_names")
    var categoryNames: String?=null,
    //手持身份证照片
    @SerializedName("hold_img")
    var holdImg: String?=null,
    //身份证反面照片
    @SerializedName("legal_back_img")
    var legalBackImg: String?=null,
    //身份证正面照片
    @SerializedName("legal_img")
    var legalImg: String?=null,
    //营业执照电子版
    @SerializedName("licence_img")
    var licenceImg: String?=null,
    //联系人姓名
    @SerializedName("link_name")
    var linkName: String?=null,
    //联系人电话
    @SerializedName("link_phone")
    var linkPhone: String?=null,
    //店铺所在省
    @SerializedName("shop_province")
    var shopProvince: String?=null,
    //店铺所在市
    @SerializedName("shop_city")
    var shopCity: String?=null,
    //店铺所在县
    @SerializedName("shop_county")
    var shopCounty: String?=null,
    //店铺所在镇
    @SerializedName("shop_town")
    var shopTown: String?=null,
    //店铺详细地址
    @SerializedName("shop_add")
    var shopAdd: String?=null,
    //店铺纬度
    @SerializedName("shop_lat")
    var shopLat: Double?=null,
    //店铺经度
    @SerializedName("shop_lng")
    var shopLng: Double?=null,
    //店铺logo
    @SerializedName("shop_logo")
    var shopLogo: String?=null,
    @SerializedName("shop_slogan")
    var shopSlogan: String? = null,
    @SerializedName("shop_notice")
    var shopNotice: String? = null,
    //店铺名称
    @SerializedName("shop_name")
    var shopName: String?=null,
    @SerializedName("shop_desc")
    var shopDesc: String? = null,
    @SerializedName("shop_id")
    var shopId: Int? = null,
    // 未支付自动取消订单分钟数
    @SerializedName("unpaid_cancel_orders_time")
    var unpaidCancelOrderTime : Int? = null,
    // 已接单自动取消订单分钟数
    @SerializedName("cancel_order_time")
    var cancelOrderTime : Int? = null,
    // 自动接单
    @SerializedName("auto_accept")
    var autoAccept : Int? = null,
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
//    @SerializedName("shop_banner")
//    var shopBanner: String?,
    //店铺客服qq
    @SerializedName("shop_qq")
    var shopQq: String? = null,
    //店铺类型
//    @SerializedName("shop_tag")
//    var shopTag: String?=null,
    //店铺类型
    @SerializedName("shop_type")
    var shopType: String?=null,

    @SerializedName("order_setting")
    var orderSetting: OrderSetting? = null
) : Serializable {

    fun getShopTypeStr() : String {
        return if(StringUtils.equals("2", shopType)) "单店" else "连锁店";
    }

    fun getFullAddress() : String {
        return String.format("%s%s%s%s", shopProvince, shopCity, shopCounty, shopAdd);
    }
}


data class OrderSetting(
    @SerializedName("auto_accept")
    var autoAccept: Int? = null,
    /**
     * 已接单取消订单数
     */
    @SerializedName("cancel_order_day")
    var cancelOrderDay: Int? = 0,
    /**
     * 评价超时天数
     */
    @SerializedName("comment_order_day")
    var commentOrderDay: Int? = 0,
    /**
     * 订单完成天数
     */
    @SerializedName("complete_order_day")
    var completeOrderDay: Int? = 0,
    /**
     * 自动支付天数（对货到付款的订单有效)
     */
    @SerializedName("complete_order_pay")
    var completeOrderPay: Int? = 0,
    /**
     * 自动确认收货天数
     */
    @SerializedName("rog_order_day")
    var rogOrderDay: Int? = 0,
    /**
     * 售后失效天数
     */
    @SerializedName("service_expired_day")
    var serviceExpiredDay: Int? = 0,
    /**
     * 未支付自动取消订单分钟数
     */
    @SerializedName("unpaid_cancel_orders_min")
    var unpaidCancelOrdersMin: Int? = 0
) : Serializable