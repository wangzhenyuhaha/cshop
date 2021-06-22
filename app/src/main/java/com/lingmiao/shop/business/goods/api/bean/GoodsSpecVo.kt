package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.goods.view.FlowViewVo


/**
Create Date : 2021/5/167:47 AM
Auther      : Fox
Desc        :
 **/
class GoodsSpecVo : FlowViewVo {
    @SerializedName("disabled")
    var disabled: Int? = 0;
    @SerializedName("seller_id")
    var sellerId: String? = "";
    @SerializedName("spec_id")
    var specId: Int? = 0;
    @SerializedName("spec_memo")
    var specMemo: String? = "";
    @SerializedName("spec_name")
    var specName: String? = "";
    @SerializedName("value_list")
    var valueList: List<SpecKeyVO>? = listOf()
    override fun getFName(): String? {
        return specName;
    }

    override fun getFId(): String? {
        return specId.toString();
    }
}