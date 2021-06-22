package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

/**
Create Date : 2021/6/228:48 AM
Auther      : Fox
Desc        :
 **/
class CateSpecAndValueVo {
    @SerializedName("category_id")
    var categoryId: String? = ""
    /**
     * 参数名称
     */
    @SerializedName("spec_name")
    var specName: String? = null

    /**
     * 参数值
     */
    @SerializedName("value_string")
    var valueString: String? = null
}