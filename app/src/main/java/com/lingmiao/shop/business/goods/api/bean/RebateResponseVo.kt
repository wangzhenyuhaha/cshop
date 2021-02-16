package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class RebateResponseVo(
    @SerializedName("goods_id")
    var goodsId: Int? = 0,
    @SerializedName("grade1_rebate")
    var grade1Rebate: String? = "",
    @SerializedName("grade2_rebate")
    var grade2Rebate: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("inviter_rate")
    var inviterRate: String? = ""
) : Serializable