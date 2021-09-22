package com.lingmiao.shop.business.me.bean

import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.wallet.bean.TradeReqBody
import java.io.Serializable

/**
Create Date : 2021/8/111:10 上午
Auther      : Fox
Desc        :
 **/
class HelpDocReq : Serializable {
    @SerializedName("body")
    var body: HelpDocReqBody? = HelpDocReqBody();

    @SerializedName("page_num")
    var pageNum: Int? = 0;
    @SerializedName("page_size")
    var pageSize: Int? = 0;
}

class HelpDocReqBody : Serializable {
    var type: String? = "";
}