package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.tools.api.JsonUtil
import com.tencent.mm.opensdk.modelpay.PayReq


/**
Create Date : 2021/5/1711:07 PM
Auther      : Fox
Desc        :
 **/
class WxPayReqVo {
    @SerializedName("form_items")
    var formItems: Any? = Any()
    @SerializedName("gateway_url")
    var gatewayUrl: String? = ""

    fun toPayData() : WxPayDataVo? {
        return JsonUtil.instance.getGson().fromJson(gatewayUrl, WxPayDataVo::class.java);
    }

    fun getPayData() : PayReq? {
        val data = toPayData();
        val pay = PayReq();
        if(data != null) {
            pay.appId = data.appid;
            pay.partnerId = data.partnerId;
            pay.prepayId = data.prepayId;
            pay.packageValue = data.packageX;
            pay.nonceStr = data.noncestr;
            pay.timeStamp = data.timestamp;
            pay.sign = data.sign;
        }
        return pay;
    }
}