package com.lingmiao.shop.business.login.bean

import com.google.gson.annotations.SerializedName


class LoginInfo {
    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("account_member_id")
    var accountMemberId: String? = null //账户会员id
    @SerializedName("face")
    var face: String? = null //会员头像
    @SerializedName("founder")
    var founder: Int? = null
    @SerializedName("nickname")
    var nickname: String? = null  //真实姓名
    @SerializedName("refresh_token")
    var refreshToken: String? = null //刷新token
    @SerializedName("role_id")
    var roleId: Int? = null
    @SerializedName("goods_management_category")
    var goodsCateId : String? = null
    @SerializedName("shop_status")
    var shopStatus: String? = null  //店铺状态  CLOSED("店铺关闭")  REFUSED("审核拒绝")  APPLY("申请开店")
    @SerializedName("uid")
    var uid: Int? = null //会员ID

    @SerializedName("shop_name")
    var shopName: String? = null //店铺名称
    @SerializedName("shop_logo")
    var shopLogo: String? = null //店铺logo

    var clerkId: Int? = null //店员id
    var mobile: String? = null //手机号码
    @SerializedName(value = "shop_id", alternate = ["sellerId","seller_id"])
    var shopId: Int? = null

    @SerializedName("open_status")
    var openStatus: Boolean? = null

    // 是否显示提现  0 不显示体现    1  显示体现
    @SerializedName("show_button")
    var showButton: Int? = 0

    @SerializedName("status_reason")
    var statusReason: String? = null //状态原因（拒绝/未通过）
}
//  【登录】和【注册】之后按照shop_status进行页面跳转判断
//APPLY("申请开店")	等待审核结果	等待审核提示页面---联系客服
//APPLYING("申请中")	申请信息的填写过程中	申请信息填写页面 ---重新填写
//OPEN("开启中")	正常	首页
//CLOSED("店铺关闭")	禁止运营	店铺关闭提示页面---联系客服
//REFUSED("审核拒绝")	修改注册重新申请	审核拒绝提示页面---重新填写
//UN_APPLY("未开店")	申请开店	申请开店提示页面 ---联系客服