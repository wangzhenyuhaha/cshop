package com.lingmiao.shop.business.goods.api.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.goods.adapter.SpecKeyAdapter
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 规格名
 */
class SpecKeyVO: MultiItemEntity, Serializable {

    @SerializedName("disabled")
    var disabled: Int? = 0
    @SerializedName("seller_id")
    var sellerId: String? = null
    @SerializedName("spec_id")
    var specId: String? = null
    @SerializedName("spec_memo")
    var specMemo: String? = null
    @SerializedName("spec_name")
    var specName: String? = null
    @SerializedName("value_list")
    var valueList: MutableList<SpecValueVO>? = null

    // 业务字段
    var isSelected: Boolean = false

    override fun getItemType(): Int {
        return SpecKeyAdapter.TYPE_SPEC
    }
}


class GoodsSpecKeyAddVO : MultiItemEntity {

    override fun getItemType(): Int {
        return SpecKeyAdapter.TYPE_ADD_BTN
    }
}