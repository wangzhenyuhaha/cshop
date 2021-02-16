package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsTypeVO

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品类型列表
 */
class GoodsTypeAdapter :
    BaseQuickAdapter<GoodsTypeVO, BaseViewHolder>(R.layout.goods_adapter_goods_type) {

    override fun convert(helper: BaseViewHolder, item: GoodsTypeVO?) {
        item?.apply {
            helper.setText(R.id.titleTv, typeName)
            helper.setText(R.id.subTitleTv, typeDesc)
        }
    }
}