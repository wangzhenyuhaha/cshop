package com.lingmiao.shop.business.goods.api.bean

import com.james.common.utils.exts.isNotBlank

/**
 * Author : Elson
 * Date   : 2020/8/12
 * Desc   : 规格列表数据模型
 */
class GoodsSkuVOWrapper : GoodsSkuVO() {

    // 业务字段
    var skuNameDesc: String? = null
    // id拼接，格式如下：id1,id2,id3
    var skuIds: String? = null
    var isChecked: Boolean = false
    var isEditable : Boolean = true;

    fun addSpecList(list: List<SpecValueVO>?) {
        specList = list
        generateSpecNameAndId()
    }

    fun generateSpecNameAndId() {
        specList?.forEachIndexed { index, it ->
            if (index == 0) {
                skuNameDesc = it.specValue
                skuIds = it.specValueId
            } else {
                skuNameDesc = "${skuNameDesc}，${it.specValue}"
                skuIds = "${skuIds},${it.specValueId}"
            }
        }
    }

    fun convert(params: SpecSettingParams) {
        params.let {
            if (it.price.isNotBlank()) {
                price = it.price
            }
            if (it.marketPrice.isNotBlank()) {
                mktprice = it.marketPrice
            }
            if (it.quantity.isNotBlank()) {
                quantity = it.quantity
            }
            if (it.costPrice.isNotBlank()) {
                cost = it.costPrice
            }
            if (it.goodsNo.isNotBlank()) {
                sn = it.goodsNo
            }
            if (it.weight.isNotBlank()) {
                weight = it.weight
            }
            if (it.goodsSKU.isNotBlank()) {
                upSkuId = it.goodsSKU
            }
        }
    }

}