package com.lingmiao.shop.business.order.bean
import com.google.gson.annotations.SerializedName


data class OrderNumber(
    @SerializedName("all_num")
    var allNum: Int,
    @SerializedName("cancel_num")
    var cancelNum: Int,
    @SerializedName("complete_num")
    var completeNum: Int,
    @SerializedName("refund_num")
    var refundNum: Int,
    @SerializedName("wait_comment_num")
    var waitCommentNum: Int,
    @SerializedName("wait_pay_num")
    var waitPayNum: Int,
    @SerializedName("wait_rog_num")
    var waitRogNum: Int,
    @SerializedName("wait_ship_num")
    var waitShipNum: Int
)

//all_num	integer($int32)
//所有订单数
//
//cancel_num	integer($int32)
//已取消订单数
//
//complete_num	integer($int32)
//已完成订单数
//
//refund_num	integer($int32)
//售后中订单数
//
//wait_comment_num	integer($int32)
//待评论订单数
//
//wait_pay_num	integer($int32)
//待付款订单数
//
//wait_rog_num	integer($int32)
//待收货订单数
//
//wait_ship_num	integer($int32)
//待发货订单数