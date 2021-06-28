package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.commonpop.bean.ItemData
import java.io.Serializable

/**
 * Author : liuqi
 * Date   : 2020/8/8
 * Desc   : 规格值
 */
class SpecValueVO : Serializable, ItemData {

    @SerializedName("seller_id")
    var sellerId: String? = null

    @SerializedName("spec_id")
    var specId: String? = null

    @SerializedName("spec_image")
    var specImage: String? = null

    @SerializedName("spec_name")
    var specName: String? = null

    @SerializedName("spec_type")
    var specType: String? = null

    @SerializedName("spec_value")
    var specValue: String? = null

    @SerializedName("spec_value_id")
    var specValueId: String? = null
    override fun getIValue(): String? {
        return specValueId;
    }

    override fun getIName(): String? {
        return specValue;
    }

    override fun getIHint(): String? {
        return specValue;
    }

    /**
     * 是否选中
     */
    var isChecked : Boolean ? = false
    override fun isItemChecked(): Boolean? {
        return isChecked;
    }

    override fun shiftChecked(flag: Boolean?) {
        isChecked = !(flag?:false!!);
    }
}