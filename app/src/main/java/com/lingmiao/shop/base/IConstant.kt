package com.lingmiao.shop.base

import com.blankj.utilcode.util.PathUtils

object IConstant {
    const val PAGE_SIZE = 10
    const val SERVICE_PHONE = "17717085667"


    val FILE_SEP = System.getProperty("file.separator")
    val CACHE_PATH = PathUtils.getCachePathExternalFirst() + FILE_SEP

    //1申请开店审核成功;  2买家订单支付成功;  3:其它  ,4店铺审核不通过
    const val MESSAGE_APPLY_SHOP_SUCCESS = "1"
    const val MESSAGE_ORDER_PAY_SUCCESS = "2"
    const val MESSAGE_OTHER = "3"
    const val MESSAGE_APPLY_SHOP_REFUSE = "4"

    const val TAB_WAIT_SEND_GOODS = 0
    const val TAB_WAIT_REFUND = 2

    const val JPUSH_TYPE = "jpush_type"

//    配送方式
    const val  SHIP_TYPE_GLOBAL = "GLOBAL"  //快递公司
    const val  SHIP_TYPE_LOCAL = "LOCAL"    //同城
    const val  SHIP_TYPE_SELF = "SELF"      //自提

    var official = true;

    const val PRO_URL = "https://api.base.c-dian.cn";
    const val TEST_URL = "http://47.117.112.134";

    fun getPrivacyServiceH5(): String {
        return "https://docs.qq.com/doc/DR2lkRUNpWFJobmxE"
    }

    fun getUserServiceH5(): String {
        return "https://docs.qq.com/doc/DR2FpbEJNcmdCSFNn"
    }

    fun getSecurityH5() : String {
        return "https://docs.qq.com/doc/DR3dJUEVyZGlLb1VE"
    }

    fun getSellerUrl(): String {
        if(official) return String.format("%s:7003", "http://api.seller.c-dian.cn");
        return "http://47.116.78.248:7003";
    }

    fun getCommonUrl() : String {
        if(official) return String.format("%s", PRO_URL);
        return "http://47.117.112.134:7000";
    }

    fun getUploadFileUrl(): String {
        return  String.format("%s/uploaders", getCommonUrl());
    }

    fun getCaptchaUrl(): String {
        if(official) return String.format("%s:7003/captcha.html", "http://api.base.c-dian.cn");
        return "http://47.117.112.134:9527/captcha.html";
    }

    fun getGoodsDetailH5(): String {
        var baseUrl = "http://t-mms.fisheagle.cn"
        if (official) baseUrl = "http://sellershop.fisheagle.cn"
        return "$baseUrl/static/editer/editer.html"
    }

    /**
     * 传递的不同类型
     */
    const val BUNDLE_KEY_OF_VIEW_TYPE = "KEY_VIEW_TYPE";

    /**
     * 传递的内容项
     */
    const val BUNDLE_KEY_OF_ITEM = "KEY_ITEM";
    /**
     * 传递的内容项唯一码
     */
    const val BUNDLE_KEY_OF_ITEM_ID = "KEY_ITEM_ID";
}