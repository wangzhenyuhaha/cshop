package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MainInfoVo : Serializable {

    // 未接订单
    @SerializedName("wait_accept_num")
    var waitAcceptNum: Int? = 0
    // 已接订单
    @SerializedName("already_accept_num")
    var alreadyAcceptNum: Int? = 0
    // 配送中
    @SerializedName("wait_ship_num")
    var waitShipNum: Int? = 0


    // 今日总量
    @SerializedName("all_num")
    var allNum: Int? = 0
    // 今日数量
    @SerializedName("trade_amount")
    var tradeAmount: Double? = 0.0
    // 今日用户数
    @SerializedName("new_member_num")
    var newMemberNum: Int? = 0

    // 退款
    @SerializedName("refund_num")
    var refundNum: Int? = 0
    // 完成
    @SerializedName("complete_num")
    var completeNum: Int? = 0
    // 失效
    @SerializedName("cancel_num")
    var cancelNum: Int? = 0


    @SerializedName("wait_comment_num")
    var waitCommentNum: Int? = 0
    @SerializedName("wait_pay_num")
    var waitPayNum: Int? = 0
    @SerializedName("wait_rog_num")
    var waitRogNum: Int? = 0

    //今日开始时间
    @SerializedName("start_time")
    var start_time:Long? = null
    //今日结束时间
    @SerializedName("end_time")
    var end_time:Long? = null
}