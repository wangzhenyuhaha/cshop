package com.lingmiao.shop.business.sales.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ElectronicVoucher: Serializable {

    //电子券ID
    @SerializedName("coupon_id")
    var couponID: Int? = null

    //电子券名称
    @SerializedName("title")
    var title: String? = null

    //电子券领取时间开始
    @SerializedName("start_time")
    var couponStartTime: Long? = null

    //电子券领取时间结束
    @SerializedName("end_time")
    var couponEndTime: Long? = null

    //电子券使用时间模式  FIX，固定时间   PERIOD，领取后生效
    @SerializedName("use_time_type")
    var useTimeType: String? = null

    //电子券使用起始
    @SerializedName("use_start_time")
    var useStartTime: Long? = null

    //电子券使用结束
    @SerializedName("use_end_time")
    var useEndTime: Long? = null

    //电子券有效天数
    @SerializedName("use_period")
    var usePeriod: Int? = null

    //库存
    @SerializedName("create_num")
    var createNum: Int? = null

    //优惠规则   满
    @SerializedName("coupon_threshold_price")
    var manPrice: Double? = null

    //优惠规则  减
    @SerializedName("coupon_price")
    var jianPrice: Double? = null

    //每人限领
    @SerializedName("limit_num")
    var limitNum: Int? = null

    //是否禁用 -1：否，0：是
    @SerializedName("disabled")
    var disabled: Int? = null

    //电子券绑定的商品ID
    @SerializedName("goods_id")
    var goodsID: Int? = null

    //电子券绑定的商品名称
    @SerializedName("goods_name")
    var goodsName: String? = null

}